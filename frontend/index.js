jQuery(function($) {
    var map, userPosition, CreatePoint, Markers,
        $body = $('body');

    function initialize() {
        var i,
            mapOptions = {
                zoom: 11,
                center: userPosition,
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
                ne = map.getBounds().getNorthEast(),
                center = map.getCenter();

            // Получаем данные с нашего сервера
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
                    var item;

                    for (i = 0; i < response.length; i++) {
                        item = response[i];
                        item.src = "db";

                        Markers.add(item);
                    }
                }
            });

            // Получаем данные от foursquare
            $.ajax({
                type: 'GET',
                url: 'https://api.foursquare.com/v2/venues/explore',
                data: {
                    ll: center.lat() + ',' + center.lng(),
                    limit: 50,
                    query: 'розетка',
                    client_id: 'XBMMXDSCBNDQ2CRJCPUDDZHSN4WS4DSO0ZHKQDEKMSP4RAAY',
                    client_secret: 'SNM2TSDCSJN1JJPJ3PTXIALM55VUU1K2YKQUR43CZKJJYOV1',
                    v: 20140605
                },
                dataType: "jsonp",
                success: function(response) {
                    var items;

                    if (response && response.response && response.response.groups && response.response.groups[0] &&
                        response.response.groups[0].items) {

                        items = response.response.groups[0].items;
                        items.forEach(function(item) {
                            if (item.venue && item.venue.name && item.venue.location && item.venue.location.lat
                                && item.venue.location.lng && item.tips && item.tips[0]) {

                                Markers.add({
                                    src: 'foursquare',
                                    id: item.venue.id,
                                    description: item.venue.name + "<br>\n" + item.tips[0].text,
                                    lat: item.venue.location.lat,
                                    lng: item.venue.location.lng
                                });
                            }

                        });
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

    Markers = {
        cache: {},
        infoWindow: null,

        add: function(data) {
            var marker,
                that = this,
                cacheKey = data.src + '_' + data.id;

            if (!this.cache.hasOwnProperty(cacheKey)) {
                marker = new google.maps.Marker({
                    position: new google.maps.LatLng(data.lat, data.lng),
                    map: map,
                    animation: google.maps.Animation.DROP
                });

                google.maps.event.addListener(marker, 'click', function() {
                    that.showHint(cacheKey);
                });

                data.marker = marker;
                this.cache[cacheKey] = data;
            }
        },

        showHint: function(cacheKey) {
            var data;

            if (this.cache.hasOwnProperty(cacheKey)) {
                data = this.cache[cacheKey];

                this.getInfoWindow().setContent(data.description);
                this.getInfoWindow().open(map, data.marker);
            }
        },

        getInfoWindow: function() {
            return this.infoWindow || (this.infoWindow = new google.maps.InfoWindow({ content: 'test' }));
        }
    };

    CreatePoint = {
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
                                Markers.add({
                                    lat: position.lat(),
                                    lng: position.lng(),
                                    description: val
                                });
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
        CreatePoint.changeState();
    });

    userPosition = new google.maps.LatLng(50.45015, 30.52651); // Стандартные координаты
    google.maps.event.addDomListener(window, 'load', initialize);
});