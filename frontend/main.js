jQuery(function($) {
    var map, mapOptions, userPosition, addPoint,
        $body = $('body');

    function initialize(lat, lng) {
        mapOptions = {
            zoom: 17,
            center: new google.maps.LatLng(50.45015, 30.52651),
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

        $body.addClass('page_init_yes');
    }

    function geolocate() {




        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {

                map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
                console.log(map.getBounds().getSouthWest(), map.getBounds().getNorthEast());


                // initialize(position.coords.latitude, position.coords.longitude);

            });
        }
    }

    function pointViewer() {
        var markers = [];
        var marker;


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

    var myEfficientFn = $.debounce(function() {

        map.getBounds();

    }, 500);
    setTimeout(myEfficientFn(), 1000);

    google.maps.event.addDomListener(window, 'load', initialize);
    //google.maps.event.addListener(map, 'bounds_changed', myEfficientFn);

    addPoint = {
        active: false,
        marker: null,

        changeState: function() {
            if (this.active) {
                this.hide();
            } else {
                this.show();
            }
        },

        show: function() {
            this.active = true;

            this.marker = new google.maps.Marker({
                map: map,
                position: userPosition,
                draggable: true
            });
        },

        hide: function() {
            this.active = false;
            this.marker.setMap(null);
        }
    };

    $('.add-button').click(function() {
        var $this = $(this);

        if ($this.hasClass('button_active_yes')) {
            $this.removeClass('button_active_yes');
        } else {
            $this.addClass('button_active_yes');
        }

        addPoint.changeState();
    });
});