package mpet.project2018.air.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

public class LocationAvailabilityHandler {

    /**
     * Metoda za provjeru dostupnosti lokkacije uređaja i ispis odgovorajućeg Toasa
     * @param context kontekst u kojem se izvodi aktivnost
     */
    static public void locationCheck(Context context) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(context, mpet.project2018.air.core.R.string.turnOnLocation, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
