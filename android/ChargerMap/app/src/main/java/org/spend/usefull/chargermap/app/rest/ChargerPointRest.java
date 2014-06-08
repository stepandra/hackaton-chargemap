package org.spend.usefull.chargermap.app.rest;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.spend.usefull.chargermap.app.dto.com.veikus.rozetka.Answer;
import org.spend.usefull.chargermap.app.dto.com.veikus.rozetka.ChargerPointDTO;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = "http://rozetka.veikus.com:8080/points", converters = {MappingJackson2HttpMessageConverter.class})
public interface ChargerPointRest {

    @Post("/add")
    void add(ChargerPointDTO chargerPoint);

    @Get("/list/{latStart}/{lngStart}/{latEnd}/{lngEnd}")
    Answer<List<ChargerPointDTO>> list(double latStart, double lngStart, double latEnd, double lngEnd);

    @Post("/{id}/found")
    Answer<Integer> found(int id);

    @Post("/{id}/notFound")
    Answer<Integer> notFound(int id);
}
