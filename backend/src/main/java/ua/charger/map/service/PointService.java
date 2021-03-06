package ua.charger.map.service;

import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.ChargerPointDTO;

import java.util.List;

public interface PointService {
    void save(ChargerPoint chargerPoint);
    List<ChargerPointDTO> list(double latStart, double lngStart,
                               double latEnd, double lngEnd);
    int found(int id);
    int notFound(int id);
}
