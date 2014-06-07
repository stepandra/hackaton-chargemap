jQuery(function($) {
    var map, userPosition, addPoint,
        $body = $('body');

    function initialize() {
        var i,
            mapOptions = {
                zoom: 11,
                center: new google.maps.LatLng(50.45015, 30.52651),
                zoomControl: true,
                disableDefaultUI: true,
                zoomControlOptions: {
                    style: google.maps.ZoomControlStyle.SMALL,
                    position: google.maps.ControlPosition.RIGHT_TOP
                }
            };

        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        
        google.maps.event.addListener(map, 'bounds_changed', $.debounce(function() {
            var sw = map.getBounds().getSouthWest(),
                ne = map.getBounds().getNorthEast();

            $.ajax({
                type: "GET",
                url: 'points/list',
                data: {
                    latEnd: sw.lat(),
                    lngEnd: ne.lng(),
                    latStart: ne.lat(),
                    lngStart: sw.lng()
                },
                dataType: "json",
                success: function(response) {
                    for (i = 0; i < response.length; i++) {
                        new google.maps.Marker({
                            position: new google.maps.LatLng(response[i].lat, response[i].lng),
                            map: map
                        }).setMap(map);
                    }
                }
            });
        }, 100));

        geolocate();
        $body.addClass('page_init_yes');
    }

    function geolocate() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                userPosition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(userPosition);
                map.setZoom(17);
            });
        }
    }

    addPoint = {
        active: false,
        marker: null,
        infowindow: null,

        changeState: function() {
            this.active ? this.hide() : this.show();
        },

        show: function() {
            var that = this;

            if (this.active) {
                return;
            }

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
                                // todo: говорить пользователю спасибо
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
            if (!this.active) {
                return;
            }

            this.active = false;
            this.marker.setMap(null);
            $('.add-button').removeClass('button_active_yes');
        }
    };
    
    $('.add-button').click(function() {
        addPoint.changeState();
    });

    google.maps.event.addDomListener(window, 'load', initialize);
});