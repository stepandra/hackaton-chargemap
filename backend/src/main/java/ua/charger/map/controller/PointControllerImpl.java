package ua.charger.map.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.charger.map.domain.ChargerPoint;
import ua.charger.map.dto.Answer;
import ua.charger.map.dto.ChargerPointDTO;
import ua.charger.map.service.PointService;

import java.util.List;

@Controller
@RequestMapping(value = "/points")
public class PointControllerImpl implements PointController {
    private static final Logger LOGGER = Logger.getLogger(PointControllerImpl.class);

    @Autowired
    private PointService pointService;

    @Override
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/add")
    public void add(@RequestParam("chargerPoint") ChargerPointDTO chargerPointDTO) {
        ChargerPoint chargerPoint = new ChargerPoint(chargerPointDTO);
        pointService.save(chargerPoint);
    }

    @Override
    @RequestMapping(value = "/{id}/found")
    public
    @ResponseBody
    Answer<Integer> found(@PathVariable("id") int id) {
        Answer<Integer> answer = new Answer<>();
        try {
            answer.setElements(pointService.found(id));
        } catch (RuntimeException runtimeException) {
            answer.setError(runtimeException.getMessage());
        }
        return answer;
    }

    @Override
    @RequestMapping(value = "/{id}/notFound")
    public
    @ResponseBody
    Answer<Integer> notFound(@PathVariable("id") int id) {
        Answer<Integer> answer = new Answer<>();
        try {
            answer.setElements(pointService.notFound(id));
        } catch (RuntimeException runtimeException) {
            answer.setError(runtimeException.getMessage());
        }
        return answer;
    }

    @Override
    @RequestMapping(value = "/list/{latStart}/{lngStart}/{latEnd}/{lngEnd}")
    public
    @ResponseBody
    Answer<List<ChargerPointDTO>> list(
            @PathVariable("latStart") double latStart,
            @PathVariable("lngStart") double lngStart,
            @PathVariable("latEnd") double latEnd,
            @PathVariable("lngEnd") double lngEnd) {
        LOGGER.info("Request {" +
                "latStart:" + latStart + ", " +
                "lngStart:" + lngStart + ", " +
                "latEnd:" + latEnd + ", " +
                "lngEnd:" + lngEnd + "}");
        Answer<List<ChargerPointDTO>> list = new Answer<>();
        list.setElements(pointService.list(latStart, lngStart, latEnd, lngEnd));
        return list;
    }
}
