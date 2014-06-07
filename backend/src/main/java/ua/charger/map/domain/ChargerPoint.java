package ua.charger.map.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ChargerPoint {
    private static final AtomicInteger ID_COUNT = new AtomicInteger();
    private int id;
    private Integer foursquareId;
    private float lag, lng;
    private String description;
    private AtomicInteger founds = new AtomicInteger();
    private AtomicInteger notFounds = new AtomicInteger();

    public ChargerPoint(float lat, float lng, String description) {
        this.id = ID_COUNT.incrementAndGet();
        this.lag = lat;
        this.lng = lng;
        this.description = description;
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
        return lag;
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
                ", lag=" + lag +
                ", lng=" + lng +
                ", description='" + description + '\'' +
                ", founds=" + founds +
                ", notFounds=" + notFounds +
                '}';
    }
}
