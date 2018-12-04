package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.List;

import Retrofit.Model.Kartica;
import Retrofit.Model.Ljubimac;

public interface KarticaDataLoadedListener {

    void onDataLoaded(List<Kartica> listaKartica);
}
