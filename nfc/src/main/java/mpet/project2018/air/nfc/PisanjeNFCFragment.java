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
import android.content.SharedPreferences;
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

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.security.SecureRandom;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.DataPost.KarticaMethod;
import Retrofit.DataPost.LjubimacMethod;
import Retrofit.Model.Ljubimac;
import Retrofit.RemotePost.KarticaOnDataPostedListener;
import Retrofit.RemotePost.LjubimacOnDataPostedListener;
import mpet.project2018.air.core.ModuleCommonMethods;
import mpet.project2018.air.core.ModuleImplementationMethods;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Kartica_Table;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac_Table;

public class PisanjeNFCFragment extends  Fragment implements KarticaOnDataPostedListener, LjubimacOnDataPostedListener {


    private String switchingPet;

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
        private String ljubimacID="1"; // id ljubimca koji se želi staviti na karticu, Prima se preko bundlea kod create fragmenta
        private String upisanaKartica; // kartica
        private int logedUserID=213; // logirani user, dohvaća se preko shared prefsa
        private TextView nfcOutput;
        private ProgressBar nfcProgress;
        private Activity runningActivity;
        private boolean switchFlag=false; // flag za odabir odgovarajućeg nastavka izvođenja koda nakon zapisa na server
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

            // Odkomentirati kad se spojimo s loginom
            //Bundle bundle = this.getArguments();
            //ljubimacID = bundle.getInt("petToPutOnTag");


            //SharedPreferences mSettings = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            //logedUserID=mSettings.getInt("ulogiraniKorisnikId",0);

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
            //Metode za testiranje bez uređaja
            //testing();
            //actionsIfFormatOKAndLocked("6542fer74z");
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
                scannedFlag=true;
                if (nfcInstance.isNFCIntent(intent)) {
                    if (nfcInstance.validateTag(intent)) {
                        String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));
                        upisanaKartica=tagCode;

                        Toast.makeText(runningActivity, tagCode, Toast.LENGTH_SHORT).show();

                        if (nfcInstance.checkFormat(tagCode)) {
                            if(checkLockedStatus(intent)) writeToNFC(intent);
                            else actionsIfFormatOKAndLocked(tagCode);
                        }
                        else
                        {
                            if(checkLockedStatus(intent)) writeToNFC(intent);
                            else outputValidationStatus(false);
                        }
                    }
                    else {
                        if (checkLockedStatus(intent)) writeToNFC(intent);
                        else outputValidationStatus(false);
                    }
                }
            }
        }

        private boolean checkLockedStatus(Intent intent)
        {
            if(!nfcInstance.isLocked(nfcInstance.getTag(intent)))
            {
                Toast.makeText(runningActivity, "Locked", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
                {
                    Toast.makeText(runningActivity, "Not Locked", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        private void writeToNFC(Intent intent)
        {
            try {
                    String tagKey=randomTagKeyGenerator();
                    NdefRecord ndefRecord = nfcInstance.createTextRecord(tagKey);
                    NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {ndefRecord });
                    Tag tag = nfcInstance.getTag(intent);
                    boolean writeResult = nfcInstance.writeNdefMessage(tag, ndefMessage);
                    if (writeResult) {
                        outputValidationStatus(true);
                        switchFlag=false;
                        KarticaMethod methodPost=new KarticaMethod(this);
                        methodPost.Upload(tagKey,String.valueOf(logedUserID));
                        Toast.makeText(getActivity(), "Tag written!", Toast.LENGTH_SHORT).show();

                    } else {
                        outputValidationStatus(false);
                        Toast.makeText(getActivity(), "Tag write failed!", Toast.LENGTH_SHORT).show();

                    }
            } catch (Exception e) {
                Log.e("onNewIntent", e.getMessage());
            }
        }

        private void testing()
        {
            String tagKey=randomTagKeyGenerator();
            KarticaMethod methodPost=new KarticaMethod(this);
            methodPost.Upload(tagKey,String.valueOf(logedUserID));
        }


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
                            nfcProgress.setVisibility(View.VISIBLE);
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
            }};

        private String randomTagKeyGenerator()
        {
            String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
            SecureRandom rnd = new SecureRandom();
            StringBuilder sb = new StringBuilder( 10 );
            for( int i = 0; i < 10; i++ ) {
                sb.append(AB.charAt(rnd.nextInt(AB.length())));
            }
            return sb.toString();
        }


    @Override
    public void onDataPosted(String idKartice) {

            try{

                //Toast.makeText(runningActivity, idKartice, Toast.LENGTH_SHORT).show();
                if(idKartice!="greska" && idKartice!="duplikat")
                {

                    upisanaKartica=idKartice;
                    Korisnik logiraniKorisnik= SQLite.select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(logedUserID)).querySingle();
                    Kartica novaKartica=new Kartica(idKartice);
                    novaKartica.setKorisnik(logiraniKorisnik);
                    novaKartica.save();

                    LjubimacMethod ljubimacSwitch=new LjubimacMethod(this);
                    ljubimacSwitch.Upload("",ljubimacID,idKartice,"pridruzivanje");

                    //Kartica kartica=   SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is(idKartice)).querySingle();
                    //List<Kartica> lista=SQLite.select().from(Kartica.class).queryList();
                    //Toast.makeText(runningActivity, String.valueOf(lista.size()), Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e){}
    }

    @Override
    public void onDataPostedLjubimac(String idLjubimca) {

            try{
                writePetToDataBase(idLjubimca);
            }
            catch (Exception e){}
    }

    private void writePetToDataBase(String petID)
    {
        Toast.makeText(runningActivity, petID, Toast.LENGTH_SHORT).show();
        if(petID!="0")
        {
            if(switchFlag){

                mpet.project2018.air.database.entities.Ljubimac switchLjubimac = SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
                ).where(Ljubimac_Table.id_ljubimca.is(Integer.parseInt(petID))).querySingle();

                Kartica kartica = SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is(upisanaKartica)).querySingle();

                switchLjubimac.setKartica(kartica);
                switchLjubimac.save();

                mpet.project2018.air.database.entities.Ljubimac switchLjubimac2 = SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
                ).where(Ljubimac_Table.id_ljubimca.is(Integer.parseInt(switchingPet))).querySingle();

                switchLjubimac2.setKartica(null);
                switchLjubimac2.save();

                //Toast.makeText(runningActivity, switchLjubimac2.getKartica().getId_kartice(), Toast.LENGTH_SHORT).show();

            }
            else {
                mpet.project2018.air.database.entities.Ljubimac switchLjubimac = SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
                ).where(Ljubimac_Table.id_ljubimca.is(Integer.parseInt(petID))).querySingle();

                Kartica kartica = SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is(upisanaKartica)).querySingle();

                switchLjubimac.setKartica(kartica);
                switchLjubimac.save();

                mpet.project2018.air.database.entities.Ljubimac switchLjubimac2 = SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
                ).where(Ljubimac_Table.id_ljubimca.is(Integer.parseInt(petID))).querySingle();

                Toast.makeText(runningActivity, switchLjubimac2.getKartica().getId_kartice(), Toast.LENGTH_SHORT).show();
            }

        }
        else;
    }

    private boolean checkTagInLocalDB(String tagCode)
    {
        Kartica kartica=   SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is(tagCode)).querySingle();
        if(kartica==null) return  false;
        else
        {
            if(kartica.getKorisnik().getId_korisnika()==Integer.parseInt("213")) return true;
            else return false;
        }
    }

    private boolean checkPetInLocalDB(String tagCode)
    {
        mpet.project2018.air.database.entities.Ljubimac ljubimac=SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
        ).where(Ljubimac_Table.kartica_id_kartice.is(tagCode)).querySingle();

        if(ljubimac==null) return false;
        else return  true;
    }

    private String returnPetFromLocalDB(String tagCode)
    {
        mpet.project2018.air.database.entities.Ljubimac ljubimac=SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
        ).where(Ljubimac_Table.kartica_id_kartice.is(tagCode)).querySingle();

        switchingPet=String.valueOf(ljubimac.getId_ljubimca());
        return String.valueOf(ljubimac.getId_ljubimca());
    }

    private void actionsIfFormatOKAndLocked(String tagCode)
    {
            if(checkTagInLocalDB(tagCode)){
                if(checkPetInLocalDB(tagCode)){
                    switchFlag=true;
                    LjubimacMethod ljubimacSwitch=new LjubimacMethod(this);
                    ljubimacSwitch.Upload(returnPetFromLocalDB(tagCode),ljubimacID,"","zamjena");
                }
                else{
                    switchFlag=false;
                    LjubimacMethod ljubimacSwitch=new LjubimacMethod(this);
                    ljubimacSwitch.Upload("",ljubimacID,tagCode,"pridruzivanje");
                }
            }
            else outputValidationStatus(false);
        }
}


