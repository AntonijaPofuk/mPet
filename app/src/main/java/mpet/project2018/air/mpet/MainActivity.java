package mpet.project2018.air.mpet;

import android.content.Intent;
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
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.mpet.fragments.Pocetna;
import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.fragments.PrikazObavijestiDetaljno;
import mpet.project2018.air.mpet.fragments.PrikazSvihObavijesti;
import mpet.project2018.air.mpet.fragments.Registracija;
import mpet.project2018.air.mpet.fragments.SkeniranjeNFCKartice;
import mpet.project2018.air.mpet.obavijesti.NotificationService;
import mpet.project2018.air.mpet.prijava.Login;
import mpet.project2018.air.mpet.prijava.LoginActivity;


public class MainActivity extends AppCompatActivity
        //Listeneri za klikove, OnFragmentInteractionListener je za sve fragmente
        implements
        Pocetna.OnFragmentInteractionListener,
        Pocetna_neulogirani.OnFragmentInteractionListener,
        Registracija.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        SkeniranjeNFCKartice.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        PrikazObavijestiDetaljno.OnFragmentInteractionListener
        //TODO: dodaj novi fragment ovdje uvijek a na početku fragmenta implementiraj mlistenere

{

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
        navigationView.setCheckedItem(R.id.nav_frag1);         //Provjera prvog elementa u draweru
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();             //otvaranje fragmenta
        ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
        ft.commit();



        //---------------------------------------------------------------
        MainDatabase.initializeDatabase(this);
       //----------------------------------------------------------------




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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
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

            fragment = new Pocetna();
        } else if (id == R.id.nav_frag2) {

        }else if (id == R.id.nav_frag3) {

        }
        else if (id == R.id.nav_frag4) {
            startActivity(new Intent(mpet.project2018.air.mpet.MainActivity.this, LoginActivity.class));
        }
        else if (id == R.id.nav_frag5) {
            fragment = new Pocetna_neulogirani();
        }       //Promjena fragmenta
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }        //zatvaranje izbornika
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
        Korisnik korisnik=new Korisnik(198,"Matija","Hasija","mhasija","1234","mail","adresa","0100330","32131","url");
        korisnik.save();
    }

    public void popuniLjubimca(){
            Korisnik k=new Korisnik();
            k.setId_korisnika(198);
        Ljubimac ljubimac=new Ljubimac(1,"Rex",21,50,"pas","m","opis","Url",k);
        Kartica a=new Kartica("6542fer74f");
        ljubimac.setKartica(a);
        ljubimac.save();
    }

    public void popuniKarticu(){
        Kartica k=new Kartica("6542fer74f");

        k.save();
    }


}
