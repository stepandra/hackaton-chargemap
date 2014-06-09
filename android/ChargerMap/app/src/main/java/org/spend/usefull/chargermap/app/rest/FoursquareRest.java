package org.spend.usefull.chargermap.app.rest;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareAnswer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

@Rest(rootUrl = "https://api.foursquare.com/v2/venues", converters = {MappingJackson2HttpMessageConverter.class})
public interface FoursquareRest {
    @Get(value = "/explore" +
            "?client_id=XBMMXDSCBNDQ2CRJCPUDDZHSN4WS4DSO0ZHKQDEKMSP4RAAY" +
            "?client_secret=SNM2TSDCSJN1JJPJ3PTXIALM55VUU1K2YKQUR43CZKJJYOV1" +
            "?v='20140605'" +
            "?limit=50" +
            "?query='розетка'" +
            "?ll={centerLat},{centerLng}")
    public FoursquareAnswer explore(double centerLat, double centerLng);
}
