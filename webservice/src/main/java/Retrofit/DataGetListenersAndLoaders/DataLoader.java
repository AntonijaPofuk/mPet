package Retrofit.DataGetListenersAndLoaders;

import java.util.ArrayList;

import Retrofit.Model.Ljubimac;

public abstract class DataLoader {

    public ArrayList<Ljubimac> listaLjubimaca;


    protected DataLoadedListener mDataLoadedListener;

    public void loadData(DataLoadedListener dataLoadedListener){
        this.mDataLoadedListener = dataLoadedListener;
    }

    public boolean dataLoaded(){
        if(listaLjubimaca == null){
            return false;
        }
        else{
            return true;
        }
    }

}
