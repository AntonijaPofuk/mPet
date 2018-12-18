package mpet.project2018.air.mpet;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Kartica_Table;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Ljubimac_Table;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.CodeHelper.CodeHandlerHelper;
import mpet.project2018.air.mpet.fragments.Pocetna;
import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.fragments.Registracija;
import mpet.project2018.air.mpet.fragments.ModulNavigationFragment;
import mpet.project2018.air.mpet.prijava.Login;
import mpet.project2018.air.mpet.prijava.LoginActivity;
import mpet.project2018.air.nfc.PisanjeNFCFragment;
import mpet.project2018.air.nfc.SkeniranjeNFCFragment;


public class MainActivity extends AppCompatActivity
        //Listeneri za klikove, OnFragmentInteractionListener je za sve fragmente
        implements
        Pocetna.OnFragmentInteractionListener,
        Pocetna_neulogirani.OnFragmentInteractionListener,
        Registracija.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        ModulNavigationFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener
        //TODO: dodaj novi fragment ovdje uvijek a na početku fragmenta implementiraj mlistenere

{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_frag1);         //Provjera prvog elementa u draweru
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();             //otvaranje fragmenta
        //ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
        //ft.commit();


        reg2();


        //---------------------------------------------------------------
        MainDatabase.initializeDatabase(this);
        //--------------------------------------------------------------
        //mockData();
    }


    private void mockData()
    {
        Korisnik logiraniKorisnik=   SQLite.select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(213)).querySingle();
        /*Kartica novaKartica=new Kartica();
        novaKartica.setId_kartice("6542fer74f");
        novaKartica.setKorisnik(logiraniKorisnik);
        novaKartica.save();
        Kartica kartica=   SQLite.select().from(Kartica.class).where(Kartica_Table.id_kartice.is("6542fer74f")).querySingle();*/
        Ljubimac ljubimac =new Ljubimac();
        ljubimac.setId_ljubimca(55);
        ljubimac.setKartica(null);
        ljubimac.setKorisnik(logiraniKorisnik);
        ljubimac.save();
        Ljubimac ljubimac1=SQLite.select().from(Ljubimac.class).where(Ljubimac_Table.id_ljubimca.is(55)).querySingle();
        //ljubimac1.setKartica(kartica);
        //ljubimac1.save();
        if(ljubimac1==null) Toast.makeText(this, "nul je", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, ljubimac1.getKorisnik().getIme() , Toast.LENGTH_SHORT).show();

    }

    private void reg2(){
        CodeHandlerHelper novaInstancaProba=new CodeHandlerHelper();
        PisanjeNFCFragment mDiscountListFragment = new PisanjeNFCFragment(novaInstancaProba,this);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, mDiscountListFragment);
        mFragmentTransaction.commit();
    }
    

        @Override
        public void onBackPressed() {
                android.app.FragmentManager fm = getFragmentManager();
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



    }


}
