package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.List;

import Retrofit.Model.Ljubimac;
import Retrofit.Model.Skeniranje;

public interface SkeniranjeDataLoadedListener {

    void onDataLoaded(List<Skeniranje> listaSkeniranja);

}
