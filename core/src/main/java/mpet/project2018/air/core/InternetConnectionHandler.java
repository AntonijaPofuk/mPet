package mpet.project2018.air.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Klasa za provjeru dostupnosti internetske veze
 */
public class InternetConnectionHandler {

    /**
     * Provjerava konekciju na internet
     * @param contect kontext u kojem se provjerava konekcija
     * @return ststus konekcije, ima, nema
     */
    public static boolean isOnline(Context contect)
    {
        ConnectivityManager cm = (ConnectivityManager)contect.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
