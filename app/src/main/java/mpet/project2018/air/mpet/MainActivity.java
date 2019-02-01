package mpet.project2018.air.mpet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.raizlabs.android.dbflow.sql.language.Delete;

import mpet.project2018.air.core.ModuleInterface;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Skeniranje;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mpet.project2018.air.database.entities.Skeniranje_Table;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.fragments.CheckNFCOptions;
import mpet.project2018.air.mpet.fragments.HomeLoggedIn;
import mpet.project2018.air.mpet.fragments.HomeLoggedOut;
import mpet.project2018.air.mpet.fragments.Login;
import mpet.project2018.air.mpet.fragments.MyPets;
import mpet.project2018.air.mpet.fragments.PetDataFragment;
import mpet.project2018.air.mpet.fragments.UpdateUser;
import mpet.project2018.air.mpet.fragments.About;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.mpet.fragments.NotificationDetails;
import mpet.project2018.air.mpet.fragments.NotificationAll;

import static mpet.project2018.air.mpet.Config.ID_SHARED_PREF;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

import mpet.project2018.air.mpet.notifications.NotificationService;
import mpet.project2018.air.nfc.NFCManager;
import mpet.project2018.air.nfc.ScanningNFCFragment;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private DrawerLayout dl;
    TextView textView;
    public List<ModuleInterface> listaModula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getObavijestiIntent();//dohvaćanje intenta za detaljne obavijesti

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_frag1);

        Navigation();

        MainDatabase.initializeDatabase(this);

        /*
         * Provjera ulogiranog korisnika
         */
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        if (Objects.requireNonNull(sharedPreferences.getString(ID_SHARED_PREF, "ulogiraniKorisnikId")).equals("ulogiraniKorisnikId")) { //getString
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.mainFrame, new HomeLoggedOut());
            ft1.commit();

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_logged_out);
            navigationView.inflateHeaderView(R.layout.nav_header_logged_out);
        } else {
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.mainFrame, new HomeLoggedIn());
            ft2.commit();

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            navigationView.inflateHeaderView(R.layout.nav_header);
            changeHeaderData();
            startService();
        }
        moduleSetup();
        checkUnreadNotificationsNumber();//obavijesti
        setDefaultCodeInputMethod();

    }

    private void moduleSetup()
    {

        listaModula=new ArrayList<ModuleInterface>();
        ModuleInterface nfcModule=new ScanningNFCFragment();
        listaModula.add(nfcModule);
        ModuleInterface manualModule=new ManualInputFragment();
        listaModula.add(manualModule);

    }

    /**
     * Promjena headera u navigation draweru
     */
    public void changeHeaderData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String id1 = sharedPreferences.getString(ID_SHARED_PREF, "");
        final NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.getHeaderView(0);
        View header =navigationView.getHeaderView(0);

        Korisnik k= null;
        if ( id1 != null ) {
            k = new SQLite().select().from(Korisnik.class)
                    .where(Korisnik_Table.id_korisnika.is(Integer.parseInt(id1))).querySingle();
        }
        if(k!=null) {
            String korime = k.getKorisnicko_ime();
            Bitmap slika = k.getSlika();

            TextView textView = header.findViewById(R.id.korImeIzbornik);
            textView.setText(korime);

            ImageView imageView = header.findViewById(R.id.imageViewKorSlika);
            imageView.setImageBitmap(slika);

            textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
                                                String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "");
                                                UpdateUser updateKorisnik = UpdateUser.newInstance(idPrijavljeni);
                                                swap(updateKorisnik);
                                                dl.closeDrawer(Gravity.START, false);

                                            }
                                        }
            );
        }

    }
    @Override
    public void onBackPressed() {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    /*upravljanje izbornikom*/
    public void swap(Fragment newFragment) {
        //android.app.FragmentTransaction t = getFragmentManager().beginTransaction();
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.mainFrame, newFragment);
        t.addToBackStack(null);
        t.commit();
    }
    public void Navigation() {
        dl = findViewById(R.id.drawer_layout);
        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        dl.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_frag1:
                                HomeLoggedOut pocetnaNeulogirani = new HomeLoggedOut();
                                swap(pocetnaNeulogirani);
                                break;
                            /**/
                            case R.id.nav_frag01:
                                HomeLoggedIn pocetnaUlogirani = new HomeLoggedIn();
                                swap(pocetnaUlogirani);
                                break;
                            /**/
                            case R.id.nav_frag2:
                                //getSupportActionBar().setTitle(R.string.nav_home);
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
                                String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "");
                                MyPets mojiLjubimci = MyPets.newInstance(idPrijavljeni);
                                swap(mojiLjubimci);
                                break;

                            case R.id.nav_frag3:
                                NotificationAll notificationAll = new NotificationAll();
                                swap(notificationAll);
                                break;

                            case R.id.nav_frag4:
                                About onama = new About();
                                swap(onama);
                                break;
                            case R.id.nav_frag5:
                                logout();
                                break;
                            /**/
                            case R.id.nav_frag6:
                                Login prijava = new Login();
                                swap(prijava);
                                break;
                            /**/
                            case R.id.nav_fragOptions:
                                CheckNFCOptions checkNFCOptions = new CheckNFCOptions();
                                swap(checkNFCOptions);

                                break;
                            case R.id.nav_user_data:
                                SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREF_NAME, 0);
                                String idPrijavljeni1 = sharedPreferences1.getString(Config.ID_SHARED_PREF, "");
                                UpdateUser updateKorisnik = UpdateUser.newInstance(idPrijavljeni1);
                                swap(updateKorisnik);
                                dl.closeDrawer(Gravity.START, false);
                                break;
                            }
                        return true;
                    }
                });
    }


    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sigurno se želite odjaviti?");
        alertDialogBuilder.setPositiveButton("Da",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences preferences = getSharedPreferences
                                (Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.remove("ulogiraniKorisnikId");

                        editor.apply();

                        /*zamjena izbornika*/
                        NavigationView navigationView = findViewById(R.id.nav_view);
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.activity_main_drawer_logged_out);
                        navigationView.getHeaderView(0);
                        navigationView.removeHeaderView(navigationView.getHeaderView(0));
                        navigationView.inflateHeaderView(R.layout.nav_header_logged_out);

                        deleteDatabase();
                        stopService();

                        HomeLoggedOut frag;
                        frag = new HomeLoggedOut();
                        swapFragment(false, frag);
                    }
                });

        alertDialogBuilder.setNegativeButton("Ne",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * brisanje lokalnih korisničkih podataka
     */
    private void deleteDatabase() {
        Delete.table(Korisnik.class);
        Delete.table(Skeniranje.class);
        Delete.table(Ljubimac.class);
        Delete.table(Kartica.class);
    }

    @Override
    public void onFragmentInteraction(String title) {  // Preimenovanje stranice
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    // Dohvaćanje pristiglih Intenta
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getObavijestiIntent();//dohvaćanje intenta za detaljne obavijesti
    }

    /**
     * Dohvaćanje intenta koji dolazi na glavnu aktivnost
     * nakon klika na obavijest na početnom zaslonu mobilnog
     * uređaja
     */
    private void getObavijestiIntent() {
        String idSken = "";
        Intent mIntent = getIntent();
        if (mIntent.hasExtra("idSkeniranja")) {
            idSken = mIntent.getStringExtra("idSkeniranja");
            //otvoriti fragment za detalje skeniranja s narednim id-em
            if (idSken != "") {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NotificationAll notificationAll = new NotificationAll();
                fragmentTransaction.replace(R.id.mainFrame, notificationAll);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //otvaranje fragmenta
                Bundle bundle = new Bundle();
                bundle.putString("idSkena", idSken);

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                NotificationDetails fragmentObavijestiDetaljno = new NotificationDetails();
                fragmentObavijestiDetaljno.setArguments(bundle);
                fragmentTransaction1.replace(R.id.mainFrame, fragmentObavijestiDetaljno);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }

        }

    }

    /**
     * Pokretanje pozadinskog servisa za obavijesti
     */
    public void startService() {
        String input = "inputService";
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    /**
     * Zaustavljanje pozadinskog servisa za obavijesti
     */
    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    /**
     * Hvatanje klika na ikonu za obavijesti u glavoj aktivnosti
     * @param v
     */
    public void bellClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NotificationAll notificationAll = new NotificationAll();
        fragmentTransaction.replace(R.id.mainFrame, notificationAll);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Osvježavanje broja novih obavijesti pokraj ikone za obavijesti
     * ako nema novih obavijesti, broj je nevidljiv
     * služi i za sakrivanje ikone za obavijesti nakon odjave korisnika
     */
    public void checkUnreadNotificationsNumber() {
        // List <mpet.project2018.air.database.entities.Skeniranje> skeniranjeList1=new SQLite().select().from(mpet.project2018.air.database.entities.Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(skeniranje.id_skeniranja))).queryList();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
                String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();

                List<mpet.project2018.air.database.entities.Skeniranje> skeniranjeList1 = new SQLite().select().from(mpet.project2018.air.database.entities.Skeniranje.class).where(Skeniranje_Table.procitano.is("0")).queryList();

                Integer counter = 0;

                for (mpet.project2018.air.database.entities.Skeniranje s : skeniranjeList1) {
                    counter++;
                }

                if (counter > 0 && !(idPrijavljeni == "" || idPrijavljeni.isEmpty() || idPrijavljeni == null)) {
                    TextView textViewBell = (TextView) findViewById(R.id.notificationTextView);
                    textViewBell.setTextColor(Color.RED);
                    textViewBell.setVisibility(View.VISIBLE);
                    textViewBell.setText(counter.toString());
                } else {
                    TextView textViewBell = (TextView) findViewById(R.id.notificationTextView);
                    textViewBell.setVisibility(View.INVISIBLE);
                }

                if (idPrijavljeni == "" || idPrijavljeni.isEmpty() || idPrijavljeni == null) {

                    findViewById(R.id.notificationBell).setVisibility(View.INVISIBLE);

                } else {
                    findViewById(R.id.notificationBell).setVisibility(View.VISIBLE);
                }

                handler.postDelayed(this, 1000);
            }
        }, 1000);  //delay za obavijesti u milisekundama, promijeniti oboje, oboje moraju biti isti
    }

    public void swapFragment(boolean addToBackstack, Fragment fragToShow){
        if(this == null)
            return;
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, (Fragment) fragToShow);
        if(addToBackstack)
            ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void petCodeLoaded(Retrofit.Model.Ljubimac pet) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("downloadPet", pet);
        PetDataFragment petFrag = new PetDataFragment();
        petFrag.setArguments(bundle);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, petFrag);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }


    @Override
    public void petPutOnTag(String userId) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("ID_KORISNIKA", userId);
        MyPets fragment = new MyPets();
        fragment.setArguments(bundle);
        ft.replace(R.id.mainFrame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openModuleFragment(String module) {
        swapFragment(true,(Fragment) listaModula.get(Integer.parseInt(module)));
    }

    @Override
    public void onCodeArrived(String code) {
        CodeValidation codeValidation=new CodeValidation(this,this);
        codeValidation.validateCodeFormat(code);
    }

    /**
     * Metoda koja automatski postavlja default način unosa koda
     */
    private void setDefaultCodeInputMethod()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        if(sharedPreferences!=null) {

            String defaultMethod = sharedPreferences.getString(Config.DEFAULT_INPUT_METHOD, "");

            if (defaultMethod.equals("")) {
                NFCManager managerInstance = new NFCManager(this);
                if (managerInstance.checkNFCExistence()) {
                    this.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(Config.DEFAULT_INPUT_METHOD, "0")
                            .apply();
                } else {
                    this.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(Config.DEFAULT_INPUT_METHOD, "1")
                            .apply();
                }
            }
        }
    }
}

