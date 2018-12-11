package mpet.project2018.air.mpet;

import android.content.Intent;
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

import org.w3c.dom.Text;

import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.mpet.fragments.Logout;
import mpet.project2018.air.mpet.fragments.Pocetna_ulogirani;
import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.fragments.Registracija;
import mpet.project2018.air.mpet.fragments.SkeniranjeNFCKartice;
import mpet.project2018.air.mpet.fragments.Login;


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
    String dohvaceniId;

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


        //--------------------------------------------------------
        View nav = navigationView.getHeaderView(0);
        TextView KorIme = (TextView) nav.findViewById(R.id.korImeIzbornik);
        KorIme.setText(dohvaceniId);
        //--------------------------------------------------------

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();    //otvaranje prvog fragmenta kod pokretanja app
        ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
        ft.commit();

        MainDatabase.initializeDatabase(this);
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

            //fragment = new Pocetna_ulogirani();
        } else if (id == R.id.nav_frag2) {

        }else if (id == R.id.nav_frag3) {

        }
        else if (id == R.id.nav_frag4) {

        }
        else if (id == R.id.nav_frag5) {
            //fragment = new Pocetna_neulogirani();   //Promjena fragmenta iz aktivnosit
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
