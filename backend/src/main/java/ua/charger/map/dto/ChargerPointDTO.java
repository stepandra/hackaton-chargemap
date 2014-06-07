package ua.charger.map.dto;

import ua.charger.map.domain.ChargerPoint;

public class ChargerPointDTO {
    private final int id;
    private final Integer foursquareId;
    private final float lat, lng;
    private final String description;
    private final int founds;
    private final int notFounds;

    public ChargerPointDTO(ChargerPoint chargerPoint) {
        this.id = chargerPoint.getId();
        this.foursquareId = chargerPoint.getFoursquareId();
        this.lat = chargerPoint.getLat();
        this.lng = chargerPoint.getLng();
        this.description = chargerPoint.getDescription();
        this.founds = chargerPoint.getFounds().get();
        this.notFounds = chargerPoint.getNotFounds().get();
    }

    public int getId() {
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
