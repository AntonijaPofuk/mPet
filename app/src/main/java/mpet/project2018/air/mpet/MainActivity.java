package mpet.project2018.air.mpet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import mpet.project2018.air.database.MainDatabase;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainDatabase.initializeDatabase(this);


        /*
        private void reg(){
            DiscountListFragment mDiscountListFragment = new DiscountListFragment();
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container, mDiscountListFragment);
            mFragmentTransaction.commit();
        }
        */

    }


}
