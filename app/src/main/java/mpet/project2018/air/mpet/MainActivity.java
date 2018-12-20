package mpet.project2018.air.mpet;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import android.widget.Toast;


import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.mpet.fragments.KorisnikUredivanje;
import mpet.project2018.air.mpet.fragments.Pocetna_ulogirani;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.mpet.fragments.Pocetna_ulogirani;

import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.fragments.PrikazObavijestiDetaljno;
import mpet.project2018.air.mpet.fragments.PrikazSvihObavijesti;
import mpet.project2018.air.mpet.fragments.Registracija;
import mpet.project2018.air.mpet.fragments.SkeniranjeNFCKartice;

import mpet.project2018.air.mpet.fragments.Login;

import static mpet.project2018.air.mpet.Config.ID_SHARED_PREF;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;
import mpet.project2018.air.mpet.obavijesti.NotificationService;



public class MainActivity extends AppCompatActivity
        //Listeneri za klikove, OnFragmentInteractionListener je za sve fragmente
        implements
        Pocetna_ulogirani.OnFragmentInteractionListener,
        Pocetna_neulogirani.OnFragmentInteractionListener,
        Registracija.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        SkeniranjeNFCKartice.OnFragmentInteractionListener,
        KorisnikUredivanje.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        PrikazObavijestiDetaljno.OnFragmentInteractionListener,
        PrikazSvihObavijesti.OnFragmentInteractionListener
        //TODO: dodaj novi fragment ovdje uvijek a na početku fragmenta implementiraj mlistenere

        //TODO: dodaj novi fragment ovdje uvijek a na početku fragmenta implementiraj mlistenere
{
    //------------------------------------------------------------------------
    /*private boolean loggedIn = false;


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(MainActivity.this, Pocetna_ulogirani.class);
            startActivity(intent);

             findViewById(R.id.visible_login_button).setVisibility(View.VISIBLE);

        }
    } */

TextView textView;
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

       /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();    //otvaranje prvog fragmenta kod pokretanja app
        ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
        ft.commit();*/


        MainDatabase.initializeDatabase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String id1 = sharedPreferences.getString(ID_SHARED_PREF, "").toString();
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        if (sharedPreferences.getString(ID_SHARED_PREF, "ulogiraniKorisnikId").toString().equals("ulogiraniKorisnikId")) { //getString
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.mainFrame, new Pocetna_neulogirani());
            ft1.commit();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_neulogirani);
            }

        else {
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.mainFrame, new Pocetna_ulogirani());
            ft2.commit();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            }

//promjena imena korisnika u izborniku
        View linearLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView textView = linearLayout.findViewById(R.id.korImeIzbornik);
        textView.setText("Prijavljeni korisnik: " + id1);



       /* Boolean loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        if(loggedIn) {
            navigationView.getMenu().findItem(R.id.nav_frag3).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_frag2).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.nav_frag3).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_frag2).setVisible(false);

        }*/


    }


    public void openUserData(View v){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new KorisnikUredivanje());
        ft.commit();
       //----------------------------------------------------------------

        popuniKorisnika();
        popuniLjubimca();
        popuniKarticu();


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PrikazSvihObavijesti prikazSvihObavijesti = new PrikazSvihObavijesti();
                fragmentTransaction.replace(R.id.mainFrame, prikazSvihObavijesti);
                fragmentTransaction.commit();


    }

   /*private void reg(){
        SkeniranjeNFCKartice mDiscountListFragment = new SkeniranjeNFCKartice();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, mDiscountListFragment);
        mFragmentTransaction.commit();
    }*/



        @Override
        public void onBackPressed() {
            //----------------------------------------------------------------------
         /*   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            android.app.FragmentManager fm = getFragmentManager();

            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
>>>>>>> origin/Matija_Branch
            } else {
                super.onBackPressed();
            }
            */
         android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
         if(fm.getBackStackEntryCount()>0){
             fm.popBackStack();

         }
            else{super.onBackPressed();}


        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // back btn definiraj prek PARENT-a u manifestu
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_frag1) {

            //fragment = new Pocetna_ulogirani();  //Promjena fragmenta iz aktivnosit
        } else if (id == R.id.nav_frag2) {

        }else if (id == R.id.nav_frag3) {

        }
        else if (id == R.id.nav_frag4) {

        }
        else if (id == R.id.nav_frag5) {

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

//za login
    private String getLoginEmailAddress(){
        String storedEmail = "";
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        if(mBundle != null){
            storedEmail = mBundle.getString("EMAIL");
        }
        return storedEmail;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getObavijestiIntent();//dohvaćanje intenta za detaljne obavijesti
    }

    //dohvaćanje intenta od obavijesti
    private void getObavijestiIntent(){
            String idSken="";
            Intent mIntent=getIntent();
            if(mIntent.hasExtra("idSkeniranja")) {
                idSken = mIntent.getStringExtra("idSkeniranja");
                //otvoriti fragment za detalje skeniranja s narednim id-em
                if (idSken != "") {
                    //otvaranje fragmenta
                    Bundle bundle=new Bundle();
                    bundle.putString("idSkena",idSken);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    PrikazObavijestiDetaljno fragmentObavijestiDetaljno = new PrikazObavijestiDetaljno();
                    fragmentObavijestiDetaljno.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mainFrame, fragmentObavijestiDetaljno);
                    fragmentTransaction.commit();
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

    public void popuniKorisnika(){
        Korisnik korisnik=new Korisnik(177,"Matija","Hasija","mhasija","1234","mail","adresa","0100330","32131","url");
        korisnik.save();
    }

    public void popuniLjubimca(){
            Korisnik k=new Korisnik();
            k.setId_korisnika(177);
        Ljubimac ljubimac=new Ljubimac(1,"Rex",21,50,"pas","m","opis","Url",k);
        Kartica a=new Kartica("6542fer74f");
        ljubimac.setKartica(a);
        ljubimac.save();
    }

    public void popuniKarticu(){
        Kartica k=new Kartica("6542fer74f");
        Korisnik kor=new Korisnik();
        kor.setId_korisnika(177);
        k.setKorisnik(kor);
        k.save();
    }

}
