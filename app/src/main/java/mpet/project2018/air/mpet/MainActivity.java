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
import android.widget.Toast;

import mpet.project2018.air.database.MainDatabase;
import mpet.project2018.air.mpet.fragments.Pocetna;
import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.prijava.LoginActivity;


public class MainActivity extends AppCompatActivity
        //Listeneri za klikove, OnFragmentInteractionListener je za sve fragmente
        implements
        Pocetna.OnFragmentInteractionListener,
        Pocetna_neulogirani.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();             //otvaranje fragmenta
        ft.replace(R.id.mainFrame, new Pocetna());
        ft.commit();

        //---------------------------------------------------------------
        MainDatabase.initializeDatabase(this);
        //--------------------------------------------------------------

    }
        /*
        private void reg(){
            DiscountListFragment mDiscountListFragment = new DiscountListFragment();
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container, mDiscountListFragment);
            mFragmentTransaction.commit();
        }
        */
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



}
