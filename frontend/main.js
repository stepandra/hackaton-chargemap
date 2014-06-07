jQuery(function($) {
    var map,
        mapOptions;

    function initialize(lat, lng) {
        mapOptions = {
            zoom: 17,
            zoomControl: true,
            disableDefaultUI: true,
            disableDefaultUI: true,
            zoomControlOptions: {
                style: google.maps.ZoomControlStyle.SMALL,
                position: google.maps.ControlPosition.RIGHT_TOP
            }
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
            mapOptions);

        geolocate();

        pointViewer();

    }

    function geolocate() {

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                console.log("fd", position.coords.longitude);
                map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
                console.log(map.getBounds().getSouthWest(), map.getBounds().getNorthEast());

                // initialize(position.coords.latitude, position.coords.longitude);

            });
        }
    }

    function pointViewer() {
        var markers = [];
        var marker;
        setTimeout(console.log(map.getBounds()), 1000);

        $.getJSON('response.json', function(response) {
            for (var i = 0; i < response.position.length; i++) {


                marker = new google.maps.Marker({
                    position: new google.maps.LatLng(response.position[i].lat, response.position[i].lng),
                    map: map
                });
                markers.push(marker);
            };





        });
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }


    google.maps.event.addDomListener(window, 'load', initialize);

})