package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.ArrayList;
import java.util.List;

import Retrofit.Model.Ljubimac;

public interface LjubimacDataLoadedListener {

    void onDataLoaded(List<Ljubimac> listaLjubimaca);

}
