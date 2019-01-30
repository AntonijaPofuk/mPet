package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.ArrayList;
import java.util.List;

import Retrofit.Model.Ljubimac;

/**
 * Sučelje namijenjeno za realizaciju unutar fragmenta koji šalje GET zahtjev web servisu
 */
public interface LjubimacDataLoadedListener {

    void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca);

}
