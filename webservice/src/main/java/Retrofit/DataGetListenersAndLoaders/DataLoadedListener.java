package Retrofit.DataGetListenersAndLoaders;

import java.util.ArrayList;

import Retrofit.Model.Ljubimac;

public interface DataLoadedListener {

    void onDataLoaded(ArrayList<Ljubimac> listaLjubimaca);

}
