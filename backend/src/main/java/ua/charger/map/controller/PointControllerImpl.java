package ua.charger.map.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
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
    @RequestMapping(value = "/{id}/found", method = RequestMethod.POST)
    public
    @ResponseBody
    int found(@PathVariable("id") int id) {
        return pointService.found(id);
    }

    @Override
    @RequestMapping(value = "/{id}/notFound", method = RequestMethod.POST)
    public
    @ResponseBody
    int notFound(@PathVariable("id") int id) {
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
