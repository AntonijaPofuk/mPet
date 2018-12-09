package mpet.project2018.air.mpet.CodeHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.core.ModuleCommonMethods;
import mpet.project2018.air.core.ModuleImplementationMethods;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.fragments.PrikazPodatakaOSkeniranomeLjubimcu;
import mpet.project2018.air.nfc.SkeniranjeNFCFragment;

public class CodeHandlerHelper extends Fragment implements Serializable, ModuleCommonMethods
{


    // Metoda koja provjerava da li je uneseni ili skenirani kod u odgovarajućem formatu
    public boolean validateCodeFormat(String code )
    {
        String tagContent = code;

        if(tagContent.length()==10 && tagContent.matches(("[A-Za-z0-9]+")))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Metoda koja vraća instancu Main aktivnosti
    public Class getContainerActivity()
    {
        return MainActivity.class;
    }

    // Metoda za pokretanje zajedničkog fragmenta za prikaz podataka
    public void showPetDataFragment(FragmentActivity nowActivity, Ljubimac gotLjubimac)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("downloadPet",gotLjubimac);
        PrikazPodatakaOSkeniranomeLjubimcu mDiscountListFragment = new PrikazPodatakaOSkeniranomeLjubimcu();
        mDiscountListFragment.setArguments(bundle);
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, mDiscountListFragment);
        mFragmentTransaction.commit();
    }
}
