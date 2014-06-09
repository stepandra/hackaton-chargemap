package org.spend.usefull.chargermap.app;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationAdapter extends Location {
    public LocationAdapter(LatLng position) {
        super(LocationAdapter.class.getName());
        setLatitude(position.latitude);
        setLongitude(position.longitude);
    }
}
