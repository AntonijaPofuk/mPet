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
import mpet.project2018.air.core.ModuleImplementationMethods;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.fragments.PrikazPodatakaOSkeniranomeLjubimcu;
import mpet.project2018.air.nfc.SkeniranjeNFCFragment;

public class CodeHandlerHelper extends Fragment implements LjubimacDataLoadedListener, Serializable, ModuleCommonMethods
{

    private Ljubimac dohvaceniLjubimac;
    private ModuleImplementationMethods modul;

    public void setFragment(ModuleImplementationMethods modul)
    {
        this.modul=modul;
    }

    // Metoda koja provjerava da li je uneseni ili skenirani kod u odgovarajućem formatu
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
            //LjubimacDataLoader dataLoader=new LjubimacDataLoader(this);
            //dataLoader.loadDataByTag(codeToValidate);

            modul.outputValidationStatus(true);
        }

        else
        {
            modul.outputValidationStatus(false);
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
    public void showPetDataFragment()
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
