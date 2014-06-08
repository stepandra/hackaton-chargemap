package org.spend.usefull.chargermap.app.service;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

@EBean(scope = EBean.Scope.Singleton)
public class LocationManager implements LocationListener {
    private Location location;

    @SystemService
    protected android.location.LocationManager locationManager;

    @AfterInject
    public void afterInject() {
        locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 400, 1000, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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

    public boolean isLocationDetected() {
        return location != null;
    }

    public Location getLocation() {
        return location;
    }
}
