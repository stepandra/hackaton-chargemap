jQuery(function($) {
    var map, mapOptions, userPosition, addPoint,
        $body = $('body');

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
        
        $body.addClass('page_init_yes');
    }

    function geolocate() {

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                userPosition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

                console.log("fd", position.coords.longitude);
                map.setCenter(userPosition);
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

    addPoint = {
        active: false,
        marker: null,
        infowindow: null,

        changeState: function() {
            if (this.active) {
                this.hide();
            } else {
                this.show();
            }
        },

        show: function() {
            var that = this;
            this.active = true;

            this.marker = new google.maps.Marker({
                map: map,
                position: userPosition,
                draggable: true
            });

            if (!this.infowindow) {
                this.infowindow = new google.maps.InfoWindow({
                    content: $('#createPopup').html()
                });

                google.maps.event.addListener(this.infowindow, 'domready', function () {
                    google.maps.event.clearListeners(that.infowindow, 'domready');

                    $('.add-popup > form').submit(function(e) {
                        e.preventDefault();

                        var $this = $(this),
                            position = that.marker.getPosition(),
                            val = $this.find('textarea').val();

                        $this.find('.add-popup__error').hide();
                        $this.find('input').attr('disabled', 'disabled');

                        $.ajax({
                            url: 'points/add',
                            type: 'POST',
                            data: {
                                lat: position.lat(),
                                lng: position.lng(),
                                description: val
                            },
                            success: function (data) {
                                that.hide();
                            },
                            error: function (data) {
                                $this.find('input').removeAttr('disabled');
                                $this.find('.add-popup__error').show().text('Произошла ошибка. Попробуйте позже');
                            }
                        });

                        return false;
                    });
                });
            }

            this.infowindow.open(map, this.marker);

            google.maps.event.addListener(this.infowindow, 'closeclick', function(){
                that.hide();
            });

            $('.add-button').addClass('button_active_yes');
        },

        hide: function() {
            this.active = false;
            this.marker.setMap(null);
            $('.add-button').removeClass('button_active_yes');
        }
    };

    $('.add-button').click(function() {
        addPoint.changeState();
    });
});