package mpet.project2018.air.mpet;

import android.content.Context;
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


import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.mpet.fragments.KorisnikUredivanje;
import mpet.project2018.air.mpet.fragments.MojiLjubimci;
import mpet.project2018.air.mpet.fragments.NoviLjubimac;
import mpet.project2018.air.mpet.fragments.ONama;
import mpet.project2018.air.mpet.fragments.PocetnaUlogirani;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.mpet.fragments.PocetnaNeulogirani;
import mpet.project2018.air.mpet.fragments.Prijava;
import mpet.project2018.air.mpet.fragments.PrikazObavijestiDetaljno;
import mpet.project2018.air.mpet.fragments.PrikazSvihObavijesti;
import mpet.project2018.air.mpet.fragments.Registracija;

import static mpet.project2018.air.mpet.Config.ID_SHARED_PREF;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;
import mpet.project2018.air.mpet.obavijesti.NotificationService;
import mpet.project2018.air.mpet.fragments.ModulNavigationFragment;


public class MainActivity extends AppCompatActivity
        //Listeneri za klikove, OnFragmentInteractionListener je za sve fragmente
        implements
        PocetnaUlogirani.OnFragmentInteractionListener,
        PocetnaNeulogirani.OnFragmentInteractionListener,
        Registracija.OnFragmentInteractionListener,
        Prijava.OnFragmentInteractionListener,
        KorisnikUredivanje.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        PrikazObavijestiDetaljno.OnFragmentInteractionListener,
        PrikazSvihObavijesti.OnFragmentInteractionListener,
        ModulNavigationFragment.OnFragmentInteractionListener,
        MojiLjubimci.OnFragmentInteractionListener,
        NoviLjubimac.OnFragmentInteractionListener,
        ONama.OnFragmentInteractionListener
        //TODO: dodaj novi fragment ovdje uvijek a na početku fragmenta implementiraj mlistenere

{

    private DrawerLayout dl;
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

        Navigation();

        MainDatabase.initializeDatabase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String id1 = sharedPreferences.getString(ID_SHARED_PREF, "").toString();
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        if (sharedPreferences.getString(ID_SHARED_PREF, "ulogiraniKorisnikId").toString().equals("ulogiraniKorisnikId")) { //getString
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.mainFrame, new PocetnaNeulogirani());
            ft1.commit();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_neulogirani);
            }

        else {
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.mainFrame, new PocetnaUlogirani());
            ft2.commit();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            }

//promjena imena korisnika u izborniku
        View linearLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView textView = linearLayout.findViewById(R.id.korImeIzbornik);
        textView.setText("Prijavljeni korisnik: " + id1);


          }


    public void openUserData(View v){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new KorisnikUredivanje());
        ft.commit();
       //----------------------------------------------------------------

        /*
        popuniKorisnika();
        popuniLjubimca();
        popuniKarticu();


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PrikazSvihObavijesti prikazSvihObavijesti = new PrikazSvihObavijesti();
                fragmentTransaction.replace(R.id.mainFrame, prikazSvihObavijesti);
                fragmentTransaction.commit();
*/

    }

   /*private void reg(){
        SkeniranjeNFCKartice mDiscountListFragment = new SkeniranjeNFCKartice();
=======
        navigationView.setCheckedItem(R.id.nav_frag1);         //Provjera prvog elementa u draweru
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();             //otvaranje fragmenta
        //ft.replace(R.id.mainFrame, new PocetnaNeulogirani());
        //ft.commit();


        reg2();


        //---------------------------------------------------------------
        MainDatabase.initializeDatabase(this);
        //--------------------------------------------------------------
        //mockData();
    }

/*
    private void mockData()
    {
        //Korisnik logiraniKorisnik=   SQLite.select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(213)).querySingle();
        /*Kartica novaKartica=new Kartica();
        novaKartica.setId_kartice("6542fer74f");
        novaKartica.setKorisnik(logiraniKorisnik);
        novaKartica.save();
        Kartica kartica=   SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is("6542fer74f")).querySingle();*/
        /*Ljubimac ljubimac =new Ljubimac();
        ljubimac.setId_ljubimca(55);
        ljubimac.setKartica(null);
        ljubimac.setKorisnik(logiraniKorisnik);
        ljubimac.save();
        Ljubimac ljubimac1=SQLite.select().from(Ljubimac.class).where(Ljubimac_Table.id_ljubimca.is(1)).querySingle();
        ljubimac1.setKartica(null);
        ljubimac1.save();
        if(ljubimac1==null) Toast.makeText(this, "nul je", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, ljubimac1.getKartica().getId_kartice() , Toast.LENGTH_SHORT).show();

    }
*/
        /*
    private void reg2(){
        CodeHandlerHelper novaInstancaProba=new CodeHandlerHelper();
        PisanjeNFCFragment mDiscountListFragment = new PisanjeNFCFragment(novaInstancaProba,this);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, mDiscountListFragment);
        mFragmentTransaction.commit();
    }
*/

        @Override
        public void onBackPressed() {

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

            //fragment = new PocetnaUlogirani();  //Promjena fragmenta iz aktivnosit
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

    /*upravljanje izbornikom*/

    public void swap(Fragment newFragment){
        //android.app.FragmentTransaction t = getFragmentManager().beginTransaction();
        FragmentTransaction t =  this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.mainFrame, newFragment);
        t.addToBackStack(null);
        t.commit();
    }

    public void swapLogout(Fragment newFragment){
        FragmentTransaction t =  this.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.mainFrame, newFragment);
        t.commit();
    }

    public void Navigation(){
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

                        switch (menuItem.getItemId()){
                            case R.id.nav_frag1:
                                PocetnaNeulogirani pocetnaNeulogirani = new PocetnaNeulogirani();
                                swap(pocetnaNeulogirani);
                                break;
                            /**/
                            case R.id.nav_frag01:
                                PocetnaUlogirani pocetnaUlogirani = new PocetnaUlogirani();
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
                            /**/
                            /*
                            case R.id.nav_frag3:
                                 skeniranja
                                break;
                                */
                            /**/
                            case R.id.nav_frag4:
                                 ONama onama=new ONama();
                                 swap(onama);
                                break;
                            case R.id.nav_frag5:
                                //odjava
                                SharedPreferences preferences = getSharedPreferences
                                        (Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                                editor.remove("ulogiraniKorisnikId");

                                editor.commit();
                                /**/
                                clearBackStack();
                                PocetnaNeulogirani pocetna=new PocetnaNeulogirani();
                                swapLogout(pocetna);
                                /*zamjena izbornika*/
                                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                                navigationView.getMenu().clear();
                                navigationView.inflateMenu(R.menu.activity_main_drawer_neulogirani);
                                break;
                            /**/
                            case R.id.nav_frag6:
                                Prijava prijava = new Prijava();
                                swap(prijava);
                                break;
                            /**/
                        }

                        return true;
                    }
                });
    }

    /************/
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
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

