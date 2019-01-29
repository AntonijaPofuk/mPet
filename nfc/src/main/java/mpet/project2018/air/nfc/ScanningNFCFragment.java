package mpet.project2018.air.nfc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.core.CodeValidation;
import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.core.LocationAvailabilityHandler;
import mpet.project2018.air.core.OnFragmentInteractionListener;



public class ScanningNFCFragment extends Fragment implements LjubimacDataLoadedListener {

    // running aktivnost
    private OnFragmentInteractionListener listenerActivity ;
    // instanca NFCManagera koja služi za nfc operacije
    private NFCManager nfcInstance;
    // Dohvaćeni ljubimac
    private Ljubimac loadedPet;
    // view elementi
    private TextView nfcOutput;
    private ProgressBar nfcProgress;
    // zastavica koja onemogućuje višestruko skeniranje
    private boolean scannedFlag=false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listenerActivity.onFragmentInteraction("NFC skeniranje");

        View view=inflater.inflate(R.layout.skeniranje_nfc_screen,container,false);
        nfcInstance=new NFCManager(getContext());

        nfcOutput=view.findViewById(R.id.NFCStatusOutput);
        nfcProgress=view.findViewById(R.id.progressBarNFC);

        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        getActivity().registerReceiver(mReceiver, filter);

        LocationAvailabilityHandler.locationCheck(getActivity());

        return  view;
    }

    /**
     * override kako bi se uklonio nfc listener
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nfcStatusOutput(nfcInstance.checkNFCAvailability());
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent1 = new Intent(getActivity(), getActivity().getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent1, 0);
        IntentFilter[] intentFilter = new IntentFilter[] { };

        if(nfcInstance.checkNFCAvailability()) nfcInstance.getNfcAdapterInstance().enableForegroundDispatch(getActivity(), pendingIntent, intentFilter, null);

        nfcStatusOutput(nfcInstance.checkNFCAvailability());

        Intent receivedIntent=getActivity().getIntent();

        if(!(receivedIntent.hasExtra("old")))
        {
            if (nfcInstance.isNFCIntent(receivedIntent))
            {
                receivedIntent.putExtra("old",1);
                performActionsAfterTagReading(receivedIntent);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(nfcInstance.checkNFCAvailability())
            nfcInstance.getNfcAdapterInstance().disableForegroundDispatch(getActivity());
    }

    /**
     * Ispis statusa nfc značajke
     * @param status status nfc značajke
     */
    private void nfcStatusOutput(Boolean status)
    {
        if(!status) nfcOutput.setText(getString(R.string.no_nfc));
        else nfcOutput.setText(getString(R.string.yes_nfc));

    }

    /**
     * Okida se nakon što je pristigao novi intent
     * @param intent pristigli intent
     */
    private void performActionsAfterTagReading(Intent intent) {
        if(!scannedFlag)
        {
            scannedFlag=true;
            if (nfcInstance.isNFCIntent(intent))
            {
                if (nfcInstance.validateTag(intent))
                {
                    String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));
                    validateCode(tagCode);
                }
                else outputValidationStatus(false);
            }
        }
    }

    /**
     * Validacija formata koda
     * @param code kod koji se validira
     */
    private void validateCode(String code) {

        if(CodeValidation.validateCodeFormat(code))
        {
            if(InternetConnectionHandler.isOnline(getActivity())) {
                LjubimacDataLoader petLoader = new LjubimacDataLoader(this);
                petLoader.loadDataByTag(code);
            }
            else {
                Toast.makeText(getContext(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
                scannedFlag = false;
            }
        }
        else
        {
            outputValidationStatus(false);
        }
    }

    /**
     * Ispis statusa validacije
     * @param validationStatus status validacije
     */
    private void outputValidationStatus(boolean validationStatus)
    {
            nfcProgress.setVisibility(View.INVISIBLE);
            if (validationStatus)
            {listenerActivity.petCodeLoaded(loadedPet);
                scannedFlag=false;
            }
            else alertingMessage(getResources().getString(mpet.project2018.air.core.R.string.codeStatusNotOK), mpet.project2018.air.core.R.drawable.fail_message);
    }

    /**
     * Okida se nakon dohvata ljubimca pomoću web servisa
     * @param listaLjubimaca lista dohvaćenih ljubimaca, prazna ili s jednim elementom
     */
    @Override
    public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {
        if(listaLjubimaca.isEmpty()) outputValidationStatus(false);
        else
        {
            loadedPet=listaLjubimaca.get(0);
            outputValidationStatus(true);
        }
    }

    /**
     * Ispis prikladne poruke kod skeniranja
     * @param message poruka za ispis
     * @param imageIcon odgovarajuća popratna ikona
     */
    private void alertingMessage(String message, int imageIcon)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Rezultat Provjere koda")
                .setMessage(message)
                .setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        scannedFlag=false;
                        dialog.dismiss();
                        nfcProgress.setVisibility(View.VISIBLE);

                    }
                })
                .setIcon(imageIcon)
                .show();
    }

    /**
     * Broadcast reciever za praćenje stanja NFC adaptera
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                        NfcAdapter.STATE_OFF);
                switch (state) {
                    case NfcAdapter.STATE_OFF:
                        if(nfcInstance.checkNFCAvailability()) nfcInstance.getNfcAdapterInstance().disableForegroundDispatch(getActivity());
                        nfcStatusOutput(nfcInstance.checkNFCAvailability());
                        break;
                    case NfcAdapter.STATE_TURNING_OFF:
                        break;
                    case NfcAdapter.STATE_ON:
                        Intent intent1 = new Intent(getActivity(), getActivity().getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent1, 0);
                        IntentFilter[] intentFilter = new IntentFilter[] { };
                        if(nfcInstance.checkNFCAvailability()) nfcInstance.getNfcAdapterInstance().enableForegroundDispatch(getActivity(), pendingIntent, intentFilter, null);
                        nfcStatusOutput(nfcInstance.checkNFCAvailability());
                        break;
                    case NfcAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    /**
     * Dohvat instance aktivnosti
     * @param context kontekst aplikacije
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listenerActivity = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

}
