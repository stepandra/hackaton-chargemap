package org.spend.usefull.chargermap.app.dto.com.veikus.rozetka;

import org.spend.usefull.chargermap.app.VenueLatLngAdapter;
import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareItem;

public class ChargerPointDTO {
    private Integer id;
    private Integer foursquareId;
    private float lat, lng;
    private String description;
    private int founds;
    private int notFounds;

    public ChargerPointDTO(){}

    public ChargerPointDTO(FoursquareItem item) {
        this.foursquareId = item.getId();
        this.lat = (float) item.getVenue().getLocation().getLat();
        this.lng = (float) item.getVenue().getLocation().getLng();
        this.description = item.getMessage();
        this.founds = 1;
    }


    public Integer getId() {
        return id;
    }

    public Integer getFoursquareId() {
        return foursquareId;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
    }

    public int getFounds() {
        return founds;
    }

    public int getNotFounds() {
        return notFounds;
    }

    @Override
    public String toString() {
        return "ChargerPointDTO{" +
                "id=" + id +
                ", foursquareId=" + foursquareId +
                ", lat=" + lat +
                ", lng=" + lng +
                ", description='" + description + '\'' +
                ", founds=" + founds +
                ", notFounds=" + notFounds +
                '}';
    }
}
