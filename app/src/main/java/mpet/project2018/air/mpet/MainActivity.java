package mpet.project2018.air.mpet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.raizlabs.android.dbflow.sql.language.Delete;

import mpet.project2018.air.core.PetDataInterface;
import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Skeniranje;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import mpet.project2018.air.database.entities.Skeniranje_Table;
import mpet.project2018.air.mpet.fragments.AboutApp;
import mpet.project2018.air.mpet.fragments.AboutUs;
import mpet.project2018.air.mpet.fragments.CheckNFCOptions;
import mpet.project2018.air.mpet.fragments.HomeLoggedIn;
import mpet.project2018.air.mpet.fragments.HomeLoggedOut;
import mpet.project2018.air.mpet.fragments.Login;
import mpet.project2018.air.mpet.fragments.PetDataFragment;
import mpet.project2018.air.mpet.fragments.UpdateKorisnik;
import mpet.project2018.air.mpet.fragments.MojiLjubimci;
import mpet.project2018.air.mpet.fragments.NoviLjubimac;
import mpet.project2018.air.mpet.fragments.About;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.mpet.fragments.PrikazObavijestiDetaljno;
import mpet.project2018.air.mpet.fragments.PrikazSvihObavijesti;
import mpet.project2018.air.mpet.fragments.Registracija;

import static mpet.project2018.air.mpet.Config.ID_SHARED_PREF;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

import mpet.project2018.air.mpet.fragments.UklanjanjeKartice;
import mpet.project2018.air.mpet.fragments.UpdateLjubimac;
import mpet.project2018.air.mpet.obavijesti.NotificationService;
import mpet.project2018.air.nfc.NFCManager;


public class MainActivity extends AppCompatActivity implements PetDataInterface, OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout dl;


    TextView textView;

    //TODO: swapFragment u MainActivity?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();//Pokretanje servisa za obavijesti
        getObavijestiIntent();//dohvaćanje intenta za detaljne obavijesti

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_frag1); //Provjera i odabir prvog elementa u draweru

        Navigation();

        MainDatabase.initializeDatabase(this);


        Toast.makeText(this, "NFC je "+ Config.DEFAULT_INPUT_METHOD, Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String id1 = sharedPreferences.getString(ID_SHARED_PREF, "").toString();
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        if (sharedPreferences.getString(ID_SHARED_PREF, "ulogiraniKorisnikId").toString().equals("ulogiraniKorisnikId")) { //getString
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

        }


        checkUnreadNotificationsNumber();//obavijesti

        setDefaultCodeInputMethod();

    }

    public void openUserData(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
        UpdateKorisnik updateKorisnik = UpdateKorisnik.newInstance(idPrijavljeni);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new UpdateKorisnik());
        ft.addToBackStack(null);
        ft.commit();

    }


    public void changeHeaderData(){ //promjena headera kod pokretanja app
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String id1 = sharedPreferences.getString(ID_SHARED_PREF, "").toString();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.getHeaderView(0);
        View header =navigationView.getHeaderView(0);

        mpet.project2018.air.database.entities.Korisnik k=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class)
                .where(Korisnik_Table.id_korisnika.is(Integer.parseInt(id1))).querySingle();
        String korime=k.getKorisnicko_ime();
        Bitmap slika = k.getSlika();

        TextView textView = header.findViewById(R.id.korImeIzbornik);
        textView.setText(korime);

        ImageView imageView = header.findViewById(R.id.imageViewKorSlika);
        imageView.setImageBitmap(slika);

    };
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_frag1) {

            //fragment = new HomeLoggedIn();  //Promjena fragmenta iz aktivnosit
        } else if (id == R.id.nav_frag2) {

        } else if (id == R.id.nav_frag3) {

        } else if (id == R.id.nav_frag4) {

        } else if (id == R.id.nav_frag5) {

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
        //zatvaranje izbornika
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //globaliziraj ju ili ovak
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*upravljanje izbornikom*/
    public void swap(Fragment newFragment) {
        //android.app.FragmentTransaction t = getFragmentManager().beginTransaction();
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.mainFrame, newFragment);
        t.addToBackStack(null);
        t.commit();
    }

    public void swapLogout(Fragment newFragment) {
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.mainFrame, newFragment);
        t.commit();
    }

    public void Navigation() {
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        //navigationView.setCheckedItem(R.id.);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
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
                                String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
                                MojiLjubimci mojiLjubimci = MojiLjubimci.newInstance(idPrijavljeni);
                                swap(mojiLjubimci);
                                break;

                            case R.id.nav_frag3:
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                PrikazSvihObavijesti prikazSvihObavijesti = new PrikazSvihObavijesti();
                                fragmentTransaction.replace(R.id.mainFrame, prikazSvihObavijesti);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;

                            case R.id.nav_frag4:
                                About onama = new About();
                                swap(onama);
                                break;
                            case R.id.nav_frag5:
                                //odjava
                                SharedPreferences preferences = getSharedPreferences
                                        (Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                                editor.remove("ulogiraniKorisnikId");
                                editor.commit();
                                /**/
                                clearBackStack();
                                HomeLoggedOut pocetna = new HomeLoggedOut();
                                swapLogout(pocetna);
                                /*zamjena izbornika*/
                                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//bez head
                                navigationView.getMenu().clear();
                                navigationView.inflateMenu(R.menu.activity_main_drawer_logged_out); //opcije
                                navigationView.getHeaderView(0);
                                navigationView.removeHeaderView(navigationView.getHeaderView(0));
                                navigationView.inflateHeaderView(R.layout.nav_header_logged_out); //head
                                deleteDatabase();
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
                        }
                        return true;
                    }
                });
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void deleteDatabase() {
        Delete.table(Korisnik.class);
        Delete.table(Skeniranje.class);
        Delete.table(Ljubimac.class);
        Delete.table(Kartica.class);
    }

    @Override
    public void onFragmentInteraction(String title) {  // Preimenovanje stranice
        getSupportActionBar().setTitle(title);
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

    //dohvaćanje intenta od obavijesti
    private void getObavijestiIntent() {
        String idSken = "";
        Intent mIntent = getIntent();
        if (mIntent.hasExtra("idSkeniranja")) {
            idSken = mIntent.getStringExtra("idSkeniranja");
            //otvoriti fragment za detalje skeniranja s narednim id-em
            if (idSken != "") {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PrikazSvihObavijesti prikazSvihObavijesti = new PrikazSvihObavijesti();
                fragmentTransaction.replace(R.id.mainFrame, prikazSvihObavijesti);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //otvaranje fragmenta
                Bundle bundle = new Bundle();
                bundle.putString("idSkena", idSken);

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                PrikazObavijestiDetaljno fragmentObavijestiDetaljno = new PrikazObavijestiDetaljno();
                fragmentObavijestiDetaljno.setArguments(bundle);
                fragmentTransaction1.replace(R.id.mainFrame, fragmentObavijestiDetaljno);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
            }

        }

    }

    //Pokretanje servisa za obavijesti
    public void startService() {
        String input = "";
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    //zaustavljanje servisa za obavijesti
    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    public void popuniKorisnika() {
        Korisnik korisnik = new Korisnik(712, "Jabuka", "Juric", "jabucica", "admin123", "mail", "adresa", "0100330", "32131", "url");
        korisnik.save();
    }

    public void popuniLjubimca() {
        Korisnik k = new Korisnik();
        k.setId_korisnika(177);
        Ljubimac ljubimac = new Ljubimac(1, "Rex", 21, 50, "pas", "m", "opis", "Url", k);
        Kartica a = new Kartica("6542fer74f");
        ljubimac.setKartica(a);
        ljubimac.save();
    }

    public void popuniKarticu() {
        Kartica k = new Kartica("6542fer74f");
        Korisnik kor = new Korisnik();
        kor.setId_korisnika(177);
        k.setKorisnik(kor);
        k.save();
    }

    public void bellClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PrikazSvihObavijesti prikazSvihObavijesti = new PrikazSvihObavijesti();
        fragmentTransaction.replace(R.id.mainFrame, prikazSvihObavijesti);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
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
        mFragmentTransaction.commit();
    }

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
                            .putString(Config.DEFAULT_INPUT_METHOD, "nfc")
                            .apply();
                } else {
                    this.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(Config.DEFAULT_INPUT_METHOD, "manual")
                            .apply();
                }
            }
        }
    }
}

