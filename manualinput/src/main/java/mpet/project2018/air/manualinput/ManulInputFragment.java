package mpet.project2018.air.manualinput.;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.core.ModuleCommonMethods;
import mpet.project2018.air.core.ModuleImplementationMethods;

@SuppressLint("ValidFragment")
public class ManualInputFragment extends Fragment implements ModuleImplementationMethods, LjubimacDataLoadedListener {


    @SuppressLint("ValidFragment")
    public ManualInputFragment(ModuleCommonMethods supportClass)
    {
        commonMethodsInstance=supportClass;
    }

    private ModuleCommonMethods commonMethodsInstance;
    private Ljubimac loadedPet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.skeniranje_nfc_screen,container,false);
        nfcInstance=new NFCManager(getContext());

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nfcStatusOutput(nfcInstance.checkNFCAvailability());
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent1 = new Intent(getActivity(), commonMethodsInstance.getContainerActivity()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent1, 0);
        IntentFilter[] intentFilter = new IntentFilter[] { };

        if(nfcInstance.checkNFCAvailability()) nfcInstance.getNfcAdapterInstance().enableForegroundDispatch(getActivity(), pendingIntent, intentFilter, null);

        nfcStatusOutput(nfcInstance.checkNFCAvailability());

        Intent receivedIntent=getActivity().getIntent();

        if(!(receivedIntent.hasExtra("old"))) {
            if (nfcInstance.isNFCIntent(receivedIntent)) {
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

    private void nfcStatusOutput(Boolean status)
    {
        if(!status) Toast.makeText(getContext(), "Turn on NFC", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getContext(), "NFC turned on", Toast.LENGTH_SHORT).show();

    }

    private void performActionsAfterTagReading(Intent intent) {
        if (nfcInstance.isNFCIntent(intent)) {
            if (nfcInstance.validateTag(intent)) {
                String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));
                //Toast.makeText(getContext(), tagCode, Toast.LENGTH_SHORT).show();
                validateCode(tagCode);
            }
            else outputValidationStatus(false);
        }
    }

    @Override
    public Fragment getModuleFragment() {
        return this;
    }

    @Override
    public void validateCode(String code) {

        if(commonMethodsInstance.validateCodeFormat(code))
        {
            LjubimacDataLoader petLoader=new LjubimacDataLoader(this);
            petLoader.loadDataByTag(code);
        }
        else
        {
            outputValidationStatus(false);
        }

    }

    @Override
    public void outputValidationStatus(boolean validationStatus) {

        if(validationStatus)alertingMessage( getResources().getString(R.string.codeStatusOK),R.drawable.success_message,validationStatus);
        else alertingMessage(getResources().getString(R.string.codeStatusNotOK),R.drawable.fail_message,validationStatus);
    }

    @Override
    public void showDataInFragment(FragmentActivity nowActivity, Ljubimac nowPet) {
        commonMethodsInstance.showPetDataFragment(nowActivity,nowPet);
    }

    @Override
    public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {

        if(listaLjubimaca.isEmpty()) outputValidationStatus(false);
        else
        {
            loadedPet=listaLjubimaca.get(0);
            outputValidationStatus(true);
        }

    }

    private void alertingMessage(String message, int imageIcon, final boolean status)
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
                        dialog.dismiss();
                        //if(!status) loadBar.setVisibility(View.VISIBLE);
                        if(status) showDataInFragment(getActivity(),loadedPet);
                    }
                })
                .setIcon(imageIcon)
                .show();
    }
}
