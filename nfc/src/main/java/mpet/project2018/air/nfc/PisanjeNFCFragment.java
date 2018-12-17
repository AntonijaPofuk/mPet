package mpet.project2018.air.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import mpet.project2018.air.core.ModuleCommonMethods;
import mpet.project2018.air.core.ModuleImplementationMethods;

public class PisanjeNFCFragment extends  Fragment {


        public PisanjeNFCFragment() {
        }

        @SuppressLint("ValidFragment")
        public PisanjeNFCFragment(ModuleCommonMethods supportClass, Activity rA)
        {
            commonMethodsInstance=supportClass;
            runningActivity=rA;
        }

        private ModuleCommonMethods commonMethodsInstance;
        private NFCManager nfcInstance;
        private Ljubimac loadedPet;
        private TextView nfcOutput;
        private ProgressBar nfcProgress;
        private Activity runningActivity;

        private boolean scannedFlag=false;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view=inflater.inflate(R.layout.writting_to_nfc_tag,container,false);
            nfcInstance=new NFCManager(getContext());

            nfcOutput=view.findViewById(R.id.NFCStatusOutputWritting);
            nfcProgress=view.findViewById(R.id.progressBarNFCWritting);

            IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
            getActivity().registerReceiver(mReceiver, filter);

            return  view;
        }

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
            if(!status) nfcOutput.setText(getString(R.string.no_nfc));
            else nfcOutput.setText(getString(R.string.yes_nfc));

        }

        private void performActionsAfterTagReading(Intent intent) {

            if (!scannedFlag) {
                //scannedFlag=true;
                if (nfcInstance.isNFCIntent(intent)) {
                    if (nfcInstance.validateTag(intent)) {
                        String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));

                        Toast.makeText(runningActivity, tagCode, Toast.LENGTH_SHORT).show();

                        if (nfcInstance.checkFormat(tagCode)) {
                            checkLockedStatus(intent);
                        }
                        else outputValidationStatus(false);
                    }
                    checkLockedStatus(intent);
                }
            }
        }

        private void checkLockedStatus(Intent intent)
        {
            if(!nfcInstance.isLocked(nfcInstance.getTag(intent)))
            {
                outputValidationStatus(false);
            }

            else{
                writeToNFC(intent);
            }
        }

        private void writeToNFC(Intent intent)
        {
            try {
                    NdefRecord ndefRecord = nfcInstance.createTextRecord("tojetomala");
                    NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {ndefRecord });
                    Tag tag = nfcInstance.getTag(intent);
                    boolean writeResult = nfcInstance.writeNdefMessage(tag, ndefMessage);
                    if (writeResult) {
                        Toast.makeText(getActivity(), "Tag written!", Toast.LENGTH_SHORT).show();
                        scannedFlag=false;
                    } else {
                        Toast.makeText(getActivity(), "Tag write failed!", Toast.LENGTH_SHORT).show();
                        scannedFlag=false;
                    }
            } catch (Exception e) {
                Log.e("onNewIntent", e.getMessage());
            }
        }

        /*@Override
        public Fragment getModuleFragment() {
            return this;
        }

        @Override
        public void validateCode(String code) {

            if(commonMethodsInstance.validateCodeFormat(code))
            {
                if(!scannedFlag) {
                    LjubimacDataLoader petLoader = new LjubimacDataLoader(this);
                    petLoader.loadDataByTag(code);
                }
            }
            else
            {
                outputValidationStatus(false);
            }

        }


        @Override
        public void showDataInFragment(FragmentActivity nowActivity, Ljubimac nowPet) {
            commonMethodsInstance.showPetDataFragment(nowActivity,nowPet);
        }

        @Override
        public String getModuleName() {
            return runningActivity.getResources().getString(R.string.module_name);
        }

        @Override
        public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {

            if(listaLjubimaca.isEmpty()) outputValidationStatus(false);
            else
            {
                loadedPet=listaLjubimaca.get(0);
                outputValidationStatus(true);
            }

        }*/

        private void outputValidationStatus(boolean validationStatus) {
        nfcProgress.setVisibility(View.INVISIBLE);

            if (validationStatus)
                alertingMessage(getResources().getString(R.string.write_ok), mpet.project2018.air.core.R.drawable.success_message, validationStatus);
            else
                alertingMessage(getResources().getString(R.string.wrtite_not_ok), mpet.project2018.air.core.R.drawable.fail_message, validationStatus);
        }


        private void alertingMessage(String message, int imageIcon, final boolean status)
        {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getActivity());
            }
            builder.setTitle("Rezultat Pisanja na NFC karticu")
                    .setMessage(message)
                    .setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scannedFlag=false;
                            dialog.dismiss();
                            if(!status) nfcProgress.setVisibility(View.VISIBLE);
                            //if(status) showDataInFragment(getActivity(),loadedPet);
                        }
                    })
                    .setIcon(imageIcon)
                    .show();
        }

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
                            Intent intent1 = new Intent(getActivity(), commonMethodsInstance.getContainerActivity()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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


    }


