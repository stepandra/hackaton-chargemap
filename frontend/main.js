jQuery(function($) {
    var map,
        mapOptions;

    function initialize(lat, lng) {
        mapOptions = {
            zoom: 8,
            center: new google.maps.LatLng(-34, 150)
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
            mapOptions);

        geolocate();
    }

    function geolocate() {

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                console.log("fd", position.coords.longitude);

                // initialize(position.coords.latitude, position.coords.longitude);
                map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
            });
        }
    }



    google.maps.event.addDomListener(window, 'load', initialize);

})