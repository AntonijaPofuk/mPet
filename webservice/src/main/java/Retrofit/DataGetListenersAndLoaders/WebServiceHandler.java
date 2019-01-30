package Retrofit.DataGetListenersAndLoaders;

/**
 * Sučelje koje služi kao listener odgovora na GET zahtjev upućen web servisu
 */
public interface WebServiceHandler {

    /**
     * Metoda koja se izvodi nakon primljenog odgovora web servisa
     * @param result tijelo odgovora
     * @param ok indikator pristiglog odgovora
     * @param prijava indikator da je riječ o odgovor na zahtjev vezan uz prijavu u aplikaciju
     */
    void onDataArrived(Object result, boolean ok, boolean prijava);

}
