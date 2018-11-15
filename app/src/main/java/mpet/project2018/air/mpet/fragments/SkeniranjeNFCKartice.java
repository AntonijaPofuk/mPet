package mpet.project2018.air.mpet.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.nfc.NFCManager;


public class SkeniranjeNFCKartice extends Fragment
{


    private NFCManager nfcInstance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        nfcInstance= new NFCManager(getActivity());

       return inflater.inflate(R.layout.skeniranje_kartice,container,false);
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

        if(!status) Toast.makeText(getActivity(), "NFC nije okej", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), "NFC okej", Toast.LENGTH_SHORT).show();

    }

    private void performActionsAfterTagReading(Intent intent) {
        if (nfcInstance.isNFCIntent(intent)) {
            if (nfcInstance.validateTag(intent)) {
                String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));

                //Ljubimac petOnTag= MainDatabase.getPetByCode(tagCode);

                /*if(petOnTag==null)
                {
                    Toast.makeText(this, "Ne postoji ljubimac s danim kodom", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, petOnTag.getIme()+petOnTag.getKorisnik().getKorisnicko_ime()+petOnTag.getKartica().getId_kartice()+"", Toast.LENGTH_LONG).show();
                }*/

                Toast.makeText(getActivity(), tagCode, Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(getActivity(), "Greška kartice", Toast.LENGTH_SHORT).show();
        }
    }
}
