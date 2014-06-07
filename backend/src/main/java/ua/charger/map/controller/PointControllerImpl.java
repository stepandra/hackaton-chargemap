package ua.charger.map.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.ChargerPointDTO;
import ua.charger.map.service.PointService;

import java.util.List;

@Controller
@RequestMapping("/points")
public class PointControllerImpl implements PointController {
    @Autowired
    private PointService pointService;

    @Override
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestParam("lat") float lat,
                    @RequestParam("lng") float lng,
                    @RequestParam("description") String description,
                    @RequestParam(value = "notFound", required = false, defaultValue = "false") boolean notFound) {
        ChargerPoint chargerPoint = new ChargerPoint(lat, lng, description);
        if (notFound) {
            chargerPoint.getNotFounds().getAndIncrement();
        } else {
            chargerPoint.getFounds().getAndIncrement();
        }
        pointService.save(chargerPoint);
    }

    @Override
    @RequestMapping(value = "/found", method = RequestMethod.POST)
    public
    @ResponseBody
    int found(@RequestParam("chargerPointId") int id) {
        return pointService.found(id);
    }

    @Override
    @RequestMapping(value = "/notFound", method = RequestMethod.POST)
    public
    @ResponseBody
    int notFound(@RequestParam("chargerPointId") int id) {
        return pointService.notFound(id);
    }

    @Override
    @RequestMapping("/list")
    public
    @ResponseBody
    List<ChargerPointDTO> list(
            @RequestParam("latStart") float latStart,
            @RequestParam("lngStart") float lngStart,
            @RequestParam("latEnd") float latEnd,
            @RequestParam("lngEnd") float lngEnd) {
        return pointService.list(latStart, lngStart, latEnd, lngEnd);
    }
}
