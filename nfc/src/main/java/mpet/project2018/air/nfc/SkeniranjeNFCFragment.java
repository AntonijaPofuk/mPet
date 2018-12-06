package mpet.project2018.air.nfc;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mpet.project2018.air.core.ModuleCommonMethods;
import mpet.project2018.air.core.ModuleImplementationMethods;

public class SkeniranjeNFCFragment extends Fragment implements ModuleImplementationMethods {

    public SkeniranjeNFCFragment() {
    }

    @SuppressLint("ValidFragment")
    public SkeniranjeNFCFragment(ModuleCommonMethods supportClass)
    {
        commonMethodsInstance=supportClass;
    }

    private ModuleCommonMethods commonMethodsInstance;
    private NFCManager nfcInstance;
    private String Kod;

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
                Kod=tagCode;
                //if(codeHandlerHelperInstance.checkFormat(tagCode))
                Toast.makeText(getContext(), tagCode, Toast.LENGTH_SHORT).show();
                validateCode();
                //{

                    //nfcReadingStatusOutput(codeHandlerHelperInstance.checkForCode(tagCode));
                //}

            }
            else Toast.makeText(getContext(), "Greška u čitanju kartice", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public Fragment getModuleFragment() {
        return null;
    }

    @Override
    public void validateCode() {
        commonMethodsInstance.validateCode(Kod);
    }

    @Override
    public void outputValidationStatus(boolean validationStatus) {

        if(!validationStatus) Toast.makeText(getContext(), "Krivi format", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getContext(), "Dobar format", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDataInFragment() {

    }
}
