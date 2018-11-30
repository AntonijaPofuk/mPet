package mpet.project2018.air.mpet.fragments;

import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.nfcHelper.NFCHelper;
import mpet.project2018.air.nfc.NFCManager;


public class SkeniranjeNFCKartice extends Fragment implements View.OnClickListener
{

    private SkeniranjeNFCKartice.OnFragmentInteractionListener mListener;

    public SkeniranjeNFCKartice() {}

    private NFCManager nfcInstance;

    private TextView ispisPoruka;
    private ProgressBar loadBar;
    private Button potvrdiUnosKoda;
    private EditText unosKodaField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.skeniranje_kartice,container,false);
        nfcInstance= new NFCManager(getActivity());

        if (mListener != null) {
            mListener.onFragmentInteraction("Skeniranje");
        }
        ispisPoruka= view.findViewById(R.id.uputaSkeniranje);
        loadBar= view.findViewById(R.id.skeniranjeProgres);
        potvrdiUnosKoda=view.findViewById(R.id.unosKodaGumb);
        unosKodaField=view.findViewById(R.id.unosKodaField);
        potvrdiUnosKoda.setOnClickListener(this);

        return  view;
    }

    @Override
    public void onClick(View v) {
        String uneseniKod= unosKodaField.getText().toString();
        nfcReadingStatusOutput(NFCHelper.checkFormat(uneseniKod));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nfcStatusOutput(nfcInstance.checkNFCAvailability());

    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent1 = new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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
        if(!status) ispisPoruka.setText(R.string.nfc_not_ok);
        else ispisPoruka.setText(R.string.skeniranje_tekst);

    }

    private void nfcReadingStatusOutput(boolean status)
    {
        loadBar.setVisibility(View.INVISIBLE);
        if(!status) alertingMessage(getResources().getString(R.string.nfc_read_failed),R.drawable.fail_message,status);
        else alertingMessage( getResources().getString(R.string.nfc_read_ok),R.drawable.success_message,status);

    }

    private void performActionsAfterTagReading(Intent intent) {
        if (nfcInstance.isNFCIntent(intent)) {
            if (nfcInstance.validateTag(intent)) {
                String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));
                nfcReadingStatusOutput(NFCHelper.checkFormat(tagCode));
            }
            else nfcReadingStatusOutput(false);
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

                         if(!status) loadBar.setVisibility(View.VISIBLE);
                        // else
                    }
                })
                .setIcon(imageIcon)
                .show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Pocetna_neulogirani.OnFragmentInteractionListener) {
            mListener = (SkeniranjeNFCKartice.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }
}
