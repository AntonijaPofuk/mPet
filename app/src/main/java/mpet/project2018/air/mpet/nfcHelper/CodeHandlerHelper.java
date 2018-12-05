package mpet.project2018.air.mpet.nfcHelper;

import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;

public class CodeHandlerHelper implements LjubimacDataLoadedListener
{

    private List<Ljubimac> listaLjubimaca;

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
        else return true;
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
}
