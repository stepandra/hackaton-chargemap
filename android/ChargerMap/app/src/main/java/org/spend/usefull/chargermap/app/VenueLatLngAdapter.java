package org.spend.usefull.chargermap.app;

import com.google.android.gms.maps.model.LatLng;

import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareVenue;

/**
 * Created by anatoly-home-air on 08/06/2014.
 */
public class VenueLatLngAdapter {
    public static LatLng adapt(FoursquareVenue foursquareVenue) {
        double lat = foursquareVenue.getLocation().getLat();
        double lng = foursquareVenue.getLocation().getLng();
        return new LatLng(lat, lng);
    }
}
