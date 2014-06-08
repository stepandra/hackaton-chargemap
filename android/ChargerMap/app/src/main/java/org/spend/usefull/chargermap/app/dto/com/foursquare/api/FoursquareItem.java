package org.spend.usefull.chargermap.app.dto.com.foursquare.api;

public class FoursquareItem {
    private int id;
    private String message;
    private FoursquareVenue venue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FoursquareVenue getVenue() {
        return venue;
    }

    public void setVenue(FoursquareVenue venue) {
        this.venue = venue;
    }
}
