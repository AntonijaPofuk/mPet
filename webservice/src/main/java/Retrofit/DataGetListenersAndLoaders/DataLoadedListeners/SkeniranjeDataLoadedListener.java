package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.List;

import Retrofit.Model.Ljubimac;
import Retrofit.Model.Skeniranje;

/**
 * Sučelje namijenjeno za realizaciju unutar fragmenta koji šalje GET zahtjev web servisu
 */
public interface SkeniranjeDataLoadedListener {

    void SkeniranjeOnDataLoaded(List<Skeniranje> listaSkeniranja);

}
