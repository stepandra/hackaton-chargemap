jQuery(function($) {
    var map, mapOptions, userPosition, addPoint,
        $body = $('body');

    function initialize(lat, lng) {
        mapOptions = {
            zoom: 17,
            center: new google.maps.LatLng(50.45015, 30.52651),
            zoomControl: true,
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
        google.maps.event.addListener(map, 'bounds_changed', $.debounce(function() {
            $.ajax({
                type: "GET",
                url: 'http://rozetka.veikus.com:8080/points/list',
                data: {
                    latEnd: map.getBounds().getSouthWest().k,
                    lngEnd: map.getBounds().getNorthEast().A,
                    latStart: map.getBounds().getNorthEast().k,
                    lngStart: map.getBounds().getSouthWest().A
                },
                dataType: "json",
                success: function(response) {
                    for (var i = 0; i < response.length; i++) {
                        marker = new google.maps.Marker({
                            position: new google.maps.LatLng(response[i].lat, response[i].lng),
                            map: map
                        });
                        markers.push(marker);
                    };
                    for (var i = 0; i < markers.length; i++) {
                        markers[i].setMap(map);
                    }
                }




            });
            console.log(map.getBounds().getNorthEast().A, map.getBounds().getSouthWest().k);
        }, 100));
        $body.addClass('page_init_yes');
    }

    function geolocate() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));

            });
        }
    }

    function pointViewer(respone) {
        var markers = [];
        var marker;


    }
    google.maps.event.addDomListener(window, 'load', initialize);
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