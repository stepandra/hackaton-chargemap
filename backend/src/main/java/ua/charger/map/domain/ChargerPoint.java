package ua.charger.map.domain;

import ua.charger.map.dto.ChargerPointDTO;

import java.util.concurrent.atomic.AtomicInteger;

public class ChargerPoint {
    private static final AtomicInteger ID_COUNT = new AtomicInteger();
    private int id;
    private Integer foursquareId;
    private float lat, lng;
    private String description;
    private AtomicInteger founds = new AtomicInteger();
    private AtomicInteger notFounds = new AtomicInteger();

    public ChargerPoint(ChargerPointDTO chargerPointDTO) {
        this.id = ID_COUNT.incrementAndGet();
        this.foursquareId = chargerPointDTO.getFoursquareId();
        this.lat = chargerPointDTO.getLat();
        this.lng = chargerPointDTO.getLng();
        this.description = chargerPointDTO.getDescription();
        this.founds = new AtomicInteger(chargerPointDTO.getFounds());
        this.notFounds = new AtomicInteger(chargerPointDTO.getNotFounds());
    }

    public AtomicInteger getFounds() {
        return founds;
    }

    public AtomicInteger getNotFounds() {
        return notFounds;
    }

    public int getId() {
        return id;
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

    public Integer getFoursquareId() {
        return foursquareId;
    }

    public void setFoursquareId(Integer foursquareId) {
        this.foursquareId = foursquareId;
    }

    @Override
    public String toString() {
        return "ChargerPoint{" +
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
