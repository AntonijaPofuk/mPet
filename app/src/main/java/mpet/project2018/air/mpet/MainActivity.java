package mpet.project2018.air.mpet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.mpet.fragments.Logout;
import mpet.project2018.air.mpet.fragments.Pocetna_ulogirani;
import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.fragments.Registracija;
import mpet.project2018.air.mpet.fragments.SkeniranjeNFCKartice;
import mpet.project2018.air.mpet.fragments.Login;

import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;


public class MainActivity extends AppCompatActivity
        //Listeneri za klikove, OnFragmentInteractionListener je za sve fragmente
        implements
        Pocetna_ulogirani.OnFragmentInteractionListener,
        Pocetna_neulogirani.OnFragmentInteractionListener,
        Registracija.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        Logout.OnFragmentInteractionListener,
        SkeniranjeNFCKartice.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener
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
        }
    } */


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


       /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();    //otvaranje prvog fragmenta kod pokretanja app
        ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
        ft.commit();*/

        MainDatabase.initializeDatabase(this);




        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
        String id1 = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "").toString(); //getString
        Toast.makeText(this,"Vas id je"+id1, Toast.LENGTH_SHORT).show();

        if (sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "").toString().equals("")) { //getString
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.mainFrame, new Pocetna_neulogirani());
            ft2.commit();
        }
        else{
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.mainFrame, new Pocetna_neulogirani());
            ft1.commit();

        }

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


}
