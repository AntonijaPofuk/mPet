package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.List;

import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;

public interface KorisnikDataLoadedListener {

    void KorisnikOnDataLoaded(List<Korisnik> listaKorisnika);
}
