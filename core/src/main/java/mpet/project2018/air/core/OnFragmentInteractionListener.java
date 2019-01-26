package mpet.project2018.air.core;

import android.support.v4.app.Fragment;
import Retrofit.Model.Ljubimac;


/**
 * Sučelje za zamjenu fragmenata i texta koji prati zamjenu fragmenata
 */
public interface OnFragmentInteractionListener {
    void onFragmentInteraction(String title);
    void swapFragment(boolean addToBackstack, Fragment fragToShow);
    void petCodeLoaded(Ljubimac pet);
    void petPutOnTag(String userId);
}
