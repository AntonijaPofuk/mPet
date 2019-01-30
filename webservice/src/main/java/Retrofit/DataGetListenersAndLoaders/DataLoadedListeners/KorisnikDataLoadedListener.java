package Retrofit.DataGetListenersAndLoaders.DataLoadedListeners;

import java.util.List;

import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;

/**
 * Sučelje namijenjeno za realizaciju unutar fragmenta koji šalje GET zahtjev web servisu
 */
public interface KorisnikDataLoadedListener {

    void KorisnikOnDataLoaded(List<Korisnik> listaKorisnika);
}
