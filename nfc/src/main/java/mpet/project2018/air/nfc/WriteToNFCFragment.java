package mpet.project2018.air.nfc;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.security.SecureRandom;
import Retrofit.DataPost.KarticaMethod;
import Retrofit.DataPost.LjubimacMethod;
import Retrofit.RemotePost.KarticaOnDataPostedListener;
import Retrofit.RemotePost.LjubimacOnDataPostedListener;
import mpet.project2018.air.core.CodeValidation;
import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Kartica_Table;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac_Table;

public class WriteToNFCFragment extends  Fragment implements KarticaOnDataPostedListener, LjubimacOnDataPostedListener {

        // ljubimac koji se remova s taga
        private String switchingPet;
        // instanca za rad snfc operacijama
        private NFCManager nfcInstance;
        // id ljubimca koji se želi staviti na karticu, Prima se preko bundlea kod create fragmenta
        private Integer ljubimacID;
        // nfc tag na koji se pokušava staviti ljubimac
        private String upisanaKartica;
        // logirani korisnik
        private String logedUserID="";
        // view elementi
        private TextView nfcOutput;
        private ProgressBar nfcProgress;
        // zastavica koja opisuje radnje stavljanja na karticu, put ili switch
        private boolean switchFlag=false;
        // zastavica koja onemogućuje višestruko skeniranje
        private boolean scannedFlag=false;
        // trenutna aktivnost
        private OnFragmentInteractionListener listenerActivity;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            listenerActivity.onFragmentInteraction("Dodavanje na NFC tag");

            View view=inflater.inflate(R.layout.writting_to_nfc_tag,container,false);
            nfcInstance=new NFCManager(getContext());

            nfcOutput=view.findViewById(R.id.NFCStatusOutputWritting);
            nfcProgress=view.findViewById(R.id.progressBarNFCWritting);

            IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
            getActivity().registerReceiver(mReceiver, filter);

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                ljubimacID =  bundle.getInt("Pet");
            }

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", 0);
            logedUserID = sharedPreferences.getString("ulogiraniKorisnikId", "DEFAULT").toString();

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
            Intent intent1 = new Intent(getActivity(), getActivity().getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent1, 0);
            IntentFilter[] intentFilter = new IntentFilter[] { };

            if(nfcInstance.checkNFCAvailability()) nfcInstance.getNfcAdapterInstance().enableForegroundDispatch(getActivity(), pendingIntent, intentFilter, null);

            nfcStatusOutput(nfcInstance.checkNFCAvailability());

            Intent receivedIntent=getActivity().getIntent();

            if(!(receivedIntent.hasExtra("old"))) {
                if (nfcInstance.isNFCIntent(receivedIntent)) {
                    receivedIntent.putExtra("old",1);

                    if(InternetConnectionHandler.isOnline(getActivity())) performActionsAfterTagReading(receivedIntent);
                    else
                    {
                        Toast.makeText(getContext(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
                    }
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
     * Ispis statusa nfc adaptear
     * @param status status nfc adaptera
     */
        private void nfcStatusOutput(Boolean status)
        {
            if(!status) nfcOutput.setText(getString(R.string.no_nfc));
            else nfcOutput.setText(getString(R.string.yes_nfc));
        }

    /**
     * Akcije nakon pristiglog inetnta
     * @param intent pristigli intent
     */
    private void performActionsAfterTagReading(Intent intent) {

            if (!scannedFlag) {
                scannedFlag=true;
                if (nfcInstance.isNFCIntent(intent)) {
                    if (nfcInstance.validateTag(intent)) {
                        String tagCode = nfcInstance.getCodeFromNdefRecord(nfcInstance.getFirstNdefRecord(nfcInstance.getNdefMessageFromIntent(intent)));
                        upisanaKartica=tagCode;

                        if (CodeValidation.validateCodeFormat(tagCode)) {
                            if(checkLockedStatus(intent)) writeToNFC(intent);
                            else actionsIfFormatOKAndLocked(tagCode);
                        }
                        else
                        {
                            if(checkLockedStatus(intent)) writeToNFC(intent);
                            else outputValidationStatus(false);
                        }
                    }
                    else
                        {
                        if (checkLockedStatus(intent)) writeToNFC(intent);
                        else outputValidationStatus(false);
                    }
                }
            }
        }

    /**
     * Provjera statusa zaključanosti kartice
     * @param intent pristigli intent
     * @return status zaključanosti
     */
    private boolean checkLockedStatus(Intent intent)
        {
            if(!nfcInstance.isLocked(nfcInstance.getTag(intent))) return false;
            else return true;
        }

    /**
     * Pisanje na karticu
     * @param intent pristigli intent
     */
    private void writeToNFC(Intent intent)
        {

            try {
                    String tagKey=randomTagKeyGenerator();
                    NdefRecord ndefRecord = nfcInstance.createTextRecord(tagKey);
                    NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {ndefRecord });
                    Tag tag = nfcInstance.getTag(intent);
                    boolean writeResult = nfcInstance.writeNdefMessage(tag, ndefMessage);
                    if (writeResult) {
                        switchFlag=false;
                        KarticaMethod methodPost=new KarticaMethod(this);
                        methodPost.Upload(tagKey,String.valueOf(logedUserID));

                    } else {
                        outputValidationStatus(false);

                    }
            } catch (Exception e) {
                Log.e("onNewIntent", e.getMessage());
            }
        }

    /**
     * Ispis stanja validacije
     * @param validationStatus status validacije
     */
    private void outputValidationStatus(boolean validationStatus) {
        nfcProgress.setVisibility(View.INVISIBLE);

            if (validationStatus)
                alertingMessage(getResources().getString(R.string.write_ok), mpet.project2018.air.core.R.drawable.success_message, validationStatus);
            else
                alertingMessage(getResources().getString(R.string.wrtite_not_ok), mpet.project2018.air.core.R.drawable.fail_message, validationStatus);
        }


    /**
     * Dialog za ispis statusa validacije
     * @param message poruka koja se ispisuje
     * @param imageIcon popratna ikona
     * @param status status validacije
     */
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
                            if(status) listenerActivity.petPutOnTag(logedUserID);
                        }
                    })
                    .setIcon(imageIcon)
                    .show();
        }

    /**
     * Praćenje stanja nfc adaptera
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
            }};

    /**
     * Generiranje jedinstvenog ključa kartice kod pisanja na istu
     * @return jedinstvani kod
     */
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

    /**
     * Okida se kada se kartica zapiše u remote bazu
      * @param idKartice id kartice koju se sprema
     */
    @Override
    public void onDataPosted(String idKartice) {

            try{
                if(!idKartice.equals("greska") && !idKartice.equals("duplikat"))
                {
                    upisanaKartica=idKartice;
                    Korisnik logiraniKorisnik= SQLite.select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(logedUserID))).querySingle();
                    Kartica novaKartica=new Kartica(idKartice);
                    novaKartica.setKorisnik(logiraniKorisnik);
                    novaKartica.save();
                    LjubimacMethod ljubimacSwitch=new LjubimacMethod(this);
                    ljubimacSwitch.Upload("",ljubimacID.toString(),idKartice,"pridruzivanje");
                }
                else outputValidationStatus(false);
            }
            catch (Exception e){}
    }

    /**
     * Okida se kada je ljubimcu pridružena kartica u remore bazi podataka
     * @param idLjubimca id ljubimca kojeg se sprema
     */
    @Override
    public void onDataPostedLjubimac(String idLjubimca) {

            try{
                if(!idLjubimca.equals("greska")) {
                    writePetToDataBase(idLjubimca);
                    outputValidationStatus(true);
                }
                else outputValidationStatus(false);
            }
            catch (Exception e){}
    }

    /**
     * Zapsivanje promjena u lokalnu bazu podataka
     * @param petID id ljubimca kojeg se mijenjalo
     */
    private void writePetToDataBase(String petID)
    {
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

            }
            else {
                mpet.project2018.air.database.entities.Ljubimac switchLjubimac = SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
                ).where(Ljubimac_Table.id_ljubimca.is(Integer.parseInt(petID))).querySingle();
                Kartica kartica = SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is(upisanaKartica)).querySingle();

                switchLjubimac.setKartica(kartica);
                switchLjubimac.save();
            }

        }
        else;
    }

    /**
     * Provjera nalazi li se kartica u lokalnoj bazi podataka
     * @param tagCode od kartice koja se traži
     * @return status pretrage
     */
    private boolean checkTagInLocalDB(String tagCode)
    {
        Kartica kartica=   SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is(tagCode)).querySingle();
        if(kartica==null) return  false;
        else
        {
            if(kartica.getKorisnik().getId_korisnika()==Integer.parseInt(logedUserID)) return true;
            else return false;
        }
    }

    /**
     * Provjera nalazi li se ljubimac u lokalnoj bazi podataka i da li mu je pridružena kartica
     * @param tagCode id kartice pod kojom bi se ljubimac trebao nalaziti
     * @return
     */
    private boolean checkPetInLocalDB(String tagCode)
    {
        mpet.project2018.air.database.entities.Ljubimac ljubimac=SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
        ).where(Ljubimac_Table.kartica_id_kartice.is(tagCode)).querySingle();

        if(ljubimac==null) return false;
        else return  true;
    }

    /**
     * Dohvaćanje ljubimca iz lokalne baze podataka prema kartici
     * @param tagCode kartica koja bi trebala biti pridružena ljubimcu
     * @return ljubimac pridružen kartici
     */
    private String returnPetFromLocalDB(String tagCode)
    {
        mpet.project2018.air.database.entities.Ljubimac ljubimac=SQLite.select().from(mpet.project2018.air.database.entities.Ljubimac.class
        ).where(Ljubimac_Table.kartica_id_kartice.is(tagCode)).querySingle();

        switchingPet=String.valueOf(ljubimac.getId_ljubimca());
        return String.valueOf(ljubimac.getId_ljubimca());
    }

    /**
     * Akcije koje se pokreću ukoliko se ljubimac sprema na novu karticu
     * @param tagCode generirani kod kartice
     */
    private void actionsIfFormatOKAndLocked(String tagCode)
    {
            if(checkTagInLocalDB(tagCode)){
                if(checkPetInLocalDB(tagCode)){
                    switchFlag=true;
                    LjubimacMethod ljubimacSwitch=new LjubimacMethod(this);
                    ljubimacSwitch.Upload(returnPetFromLocalDB(tagCode),ljubimacID.toString(),"","zamjena");

                }
                else{
                    switchFlag=false;
                    LjubimacMethod ljubimacSwitch=new LjubimacMethod(this);
                    ljubimacSwitch.Upload("",ljubimacID.toString(),tagCode,"pridruzivanje");

                }
            }
            else outputValidationStatus(false);
        }

    /**
     * Dohvat aktivnosti
      * @param context kontekst u kojem se dohvaća aktivnost
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


