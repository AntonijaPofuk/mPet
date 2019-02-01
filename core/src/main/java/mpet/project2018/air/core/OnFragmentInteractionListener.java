package mpet.project2018.air.core;

import android.support.v4.app.Fragment;
import Retrofit.Model.Ljubimac;


/**
 * Sučelje za zamjenu fragmenata i texta koji prati zamjenu fragmenata
 */
public interface OnFragmentInteractionListener {

    /**
     * Ispis naziva trenutnog fragmenta
     * @param title naziv fragmenta
     */
    void onFragmentInteraction(String title);

    /**
     * Osnovna metoda za zamjenu fragmenata
     * @param addToBackstack ovisno o parametru fragment se stavlja na backstack ili ne
     * @param fragToShow fragment kojeg je potrebno otvoriti
     */
    void swapFragment(boolean addToBackstack, Fragment fragToShow);

    /**
     * Otvaranje fragmenta za prikaz podataka o ljubimcu
     * @param pet dohvaćeni ljubimac o kojem se prikazuju podaci
     */
    void petCodeLoaded(Ljubimac pet);

    /**
     * otvaranje fragmenta popisa ljubimaca nakon dodavanja na NFC tag
     * @param userId id korisnika čiji je ljubimac stavljen na NFC tag
     */
    void petPutOnTag(String userId);

    void openModuleFragment(String module);

    void onCodeArrived(String code);
}
