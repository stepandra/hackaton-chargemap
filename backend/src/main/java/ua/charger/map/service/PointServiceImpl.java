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
    private Collection<ChargerPoint> chargerPoints = new ConcurrentLinkedQueue<>();
    private AtomicInteger count = new AtomicInteger();

    @Override
    public void save(ChargerPoint chargerPoint) {
        chargerPoints.add(chargerPoint);
        count.incrementAndGet();
        LOGGER.info("Saved: {" + chargerPoint + "}");
    }

    @Override
    public List<ChargerPointDTO> list(float latStart, float lngStart,
                                   float latEnd, float lngEnd) {
        List<ChargerPointDTO> chargerPoints = new LinkedList<>();
        for (ChargerPoint chargerPoint : this.chargerPoints) {
            if (chargerPoint.getLat() < latStart || chargerPoint.getLat() > latEnd)
                continue;
            if (chargerPoint.getLng() < lngStart || chargerPoint.getLng() > lngEnd)
                continue;
            chargerPoints.add(new ChargerPointDTO(chargerPoint));
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


}
