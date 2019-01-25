package mpet.project2018.air.mpet;

import android.support.v4.app.Fragment;


/*
This would be used for calling the name of fragment and fragment transaction
*/
public interface OnFragmentInteractionListener {
    public void onFragmentInteraction(String title);

    public void swapFragment(boolean addToBackstack, Fragment fragToShow);


}
