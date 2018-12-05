package mpet.project2018.air.mpet.CodeHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.core.ModuleCommonMethods;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.fragments.PrikazPodatakaOSkeniranomeLjubimcu;

public class CodeHandlerHelper extends Fragment implements LjubimacDataLoadedListener, Serializable, ModuleCommonMethods
{

    private Ljubimac dohvaceniLjubimac;

    // Metoda koja provjerava da li je uneseni ili skenirani kod u odgovvarajućem formatu
    public boolean checkFormat(String code )
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

    // Metoda koja provjerava postoji li u bazi podataka ljubimac s danim kodom
    public boolean checkForCode(List<Ljubimac> listaLjubimaca)
    {
    if(listaLjubimaca.isEmpty()) return false;
        else
    {
        dohvaceniLjubimac=listaLjubimaca.get(0);
        return true;
    }
    }

    // Metoda koja provjerava ispravnost koda
    public void validateCode(String codeToValidate)
    {
        if(checkFormat(codeToValidate))
        {
            LjubimacDataLoader dataLoader=new LjubimacDataLoader(this);
            dataLoader.loadDataByTag(codeToValidate);
        }

        else
        {
            // pozovi metodu za ispis poruke statusa s false
        }
    }

    // Metoda koja se izvodi kod dohvata podataka pomoću web servisa
    @Override
    public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {

        if(checkForCode(listaLjubimaca))
        {
            // pozovi metodu za ispis poruke statusa s true
        }

        else
        {
            // pozovi metodu za ispis poruke statusa s false
        }

    }

    // Metoda koja vraća instancu Main aktivnosti
    public Class getContainerActivity()
    {
        return MainActivity.class;
    }

    // Metoda za pokretanje zajedničkog fragmenta za prikaz podataka
    public void showwPetDataFragment(String code)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("downloadPet",dohvaceniLjubimac);
        PrikazPodatakaOSkeniranomeLjubimcu mDiscountListFragment = new PrikazPodatakaOSkeniranomeLjubimcu();
        mDiscountListFragment.setArguments(bundle);
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, mDiscountListFragment);
        mFragmentTransaction.commit();
    }
}
