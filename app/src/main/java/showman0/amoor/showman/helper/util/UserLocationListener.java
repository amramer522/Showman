package showman0.amoor.showman.helper.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class UserLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {

        LogUtil.verbose("onLocationChanged: current location" + location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
