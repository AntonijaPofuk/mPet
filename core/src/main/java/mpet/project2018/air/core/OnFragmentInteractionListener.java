package mpet.project2018.air.core;

import android.support.v4.app.Fragment;

import Retrofit.Model.Ljubimac;


/*
This would be used for calling the name of fragment and fragment transaction
*/
public interface OnFragmentInteractionListener {

    void onFragmentInteraction(String title);
    void swapFragment(boolean addToBackstack, Fragment fragToShow);
    void petCodeLoaded(Ljubimac pet);
    void petPutOnTag(String userId);
}
