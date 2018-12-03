package Retrofit.DataGetListenersAndLoaders;

import java.util.ArrayList;
import java.util.List;

import Retrofit.Model.Ljubimac;

public interface DataLoadedListener {

    void onDataLoaded(List<Ljubimac> listaLjubimaca);

}
