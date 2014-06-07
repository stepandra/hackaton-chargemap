jQuery(function($) {
    var map, mapOptions, userPosition, addPoint,
        $body = $('body');

    function initialize(lat, lng) {
        mapOptions = {
            zoom: 8,
            center: new google.maps.LatLng(-34, 150)
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
            mapOptions);

        geolocate();
        $body.addClass('page_init_yes');
    }

    function geolocate() {

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                console.log("fd", position.coords.longitude);

                // initialize(position.coords.latitude, position.coords.longitude);
                userPosition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(userPosition);
            });
        }
    }

    function showAddWindow() {

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