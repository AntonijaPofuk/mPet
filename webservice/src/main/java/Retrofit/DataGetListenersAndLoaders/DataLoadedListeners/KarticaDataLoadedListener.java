package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.List;

import Retrofit.Model.Kartica;
import Retrofit.Model.Ljubimac;

/**
 * Sučelje namijenjeno za realizaciju unutar fragmenta koji šalje GET zahtjev web servisu
 */
public interface KarticaDataLoadedListener {

    void KarticaOnDataLoaded(List<Kartica> listaKartica);
}
