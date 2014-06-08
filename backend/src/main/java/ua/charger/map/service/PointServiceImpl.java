package ua.charger.map.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.ChargerPointDTO;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PointServiceImpl implements PointService {
    private static final Logger LOGGER = Logger.getLogger(PointServiceImpl.class);
    //todo: must be optimized for production usage
    private Collection<ChargerPoint> chargerPoints = new ConcurrentLinkedQueue<>();
    private AtomicInteger count = new AtomicInteger();

    @Override
    public void save(ChargerPoint chargerPoint) {
        chargerPoints.add(chargerPoint);
        count.incrementAndGet();
        LOGGER.info("Saved: {" + chargerPoint + "}");
    }

    @Override
    /**
     todo: must be optimized for abroad (Africa, America, Australia) users
     */
    public List<ChargerPointDTO> list(double latStart, double lngStart,
                                      double latEnd, double lngEnd) {
        List<ChargerPointDTO> chargerPoints = new LinkedList<>();
        if (count.intValue() == 0) {
            return chargerPoints;
        }
        ChargerPointDTO nearestPoint = new ChargerPointDTO(this.chargerPoints.iterator().next());
        double nearestPointDistance = 272;
        double tmpPointDistance;
        double centerLat = (latStart + latEnd) / 2;
        double centerLng = (lngStart + lngEnd) / 2;
        for (ChargerPoint chargerPoint : this.chargerPoints) {
            if ((tmpPointDistance = calculateDifferent(chargerPoint, centerLat, centerLng)) < nearestPointDistance) {
                nearestPoint = new ChargerPointDTO(chargerPoint);
                nearestPointDistance = tmpPointDistance;
            }
            if (chargerPoint.getLat() > latStart || chargerPoint.getLat() < latEnd)
                continue;
            if (chargerPoint.getLng() < lngStart || chargerPoint.getLng() > lngEnd)
                continue;
            chargerPoints.add(new ChargerPointDTO(chargerPoint));
        }
        if (chargerPoints.size() == 0) {
            chargerPoints.add(nearestPoint);
        }
        LOGGER.info("Returned list {" + chargerPoints + '}');
        return chargerPoints;
    }

    @Override
    public int found(int id) {
        for (ChargerPoint chargerPoint : chargerPoints) {
            if (chargerPoint.getId() == id) {
                return chargerPoint.getFounds().incrementAndGet();
            }
        }
        throw new IllegalStateException("No such id");
    }

    @Override
    public int notFound(int id) {
        for (ChargerPoint chargerPoint : chargerPoints) {
            if (chargerPoint.getId() == id) {
                return chargerPoint.getNotFounds().incrementAndGet();
            }
        }
        throw new IllegalStateException("No such id");
    }

    private static double calculateDifferent(ChargerPoint chargerPoint, double centerLat, double centerLng) {
        return Math.abs(centerLat - chargerPoint.getLat()) + Math.abs(centerLng - chargerPoint.getLng());
    }

}
