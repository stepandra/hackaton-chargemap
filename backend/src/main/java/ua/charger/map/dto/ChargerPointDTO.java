package ua.charger.map.dto;

import ua.charger.map.domain.ChargerPoint;

public class ChargerPointDTO {
    private int id;
    private Integer foursquareId;
    private float lat, lng;
    private String description;
    private int founds;
    private int notFounds;

    public ChargerPointDTO() {}

    public ChargerPointDTO(int id, Integer foursquareId, float lat, float lng, String description, int founds, int notFounds) {
        this.id = id;
        this.foursquareId = foursquareId;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.founds = founds;
        this.notFounds = notFounds;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setFoursquareId(Integer foursquareId) {
        this.foursquareId = foursquareId;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFounds(int founds) {
        this.founds = founds;
    }

    public void setNotFounds(int notFounds) {
        this.notFounds = notFounds;
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
