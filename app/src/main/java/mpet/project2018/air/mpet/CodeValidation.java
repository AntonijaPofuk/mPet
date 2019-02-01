package mpet.project2018.air.mpet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.core.ValidationOutput;
import mpet.project2018.air.mpet.fragments.PetDataFragment;

public class CodeValidation implements LjubimacDataLoadedListener {

    private Context context=null;
    private OnFragmentInteractionListener mListener;

    public CodeValidation(Context c, OnFragmentInteractionListener l){
        context=c;
        mListener=l;
    }

    /**
     * Provjerava da li je dohvaćeni kod u predviđenom formatu
     * @param code kod koji se provjerava
     * @return status formata koda
     */
    public void validateCodeFormat(String code)
    {
        if(code.length()==10 && code.matches(("[A-Za-z0-9]+"))){
            loadPet(code);
        }
        else {
            ValidationOutput.alertingMessage(context);
        }
    }

    private void loadPet(String code){
        LjubimacDataLoader ljubimacDataLoader=new LjubimacDataLoader(this);
        ljubimacDataLoader.loadDataByTag(code);
    }

    @Override
    public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {
        if (listaLjubimaca.isEmpty()) ValidationOutput.alertingMessage(context);
        else {
            mListener.petCodeLoaded(listaLjubimaca.get(0));
        }

    }



}
