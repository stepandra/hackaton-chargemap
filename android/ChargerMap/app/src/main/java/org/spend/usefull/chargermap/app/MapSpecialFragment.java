package org.spend.usefull.chargermap.app;

import android.app.Fragment;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareAnswer;
import org.spend.usefull.chargermap.app.dto.com.foursquare.api.FoursquareItem;
import org.spend.usefull.chargermap.app.dto.com.veikus.rozetka.Answer;
import org.spend.usefull.chargermap.app.dto.com.veikus.rozetka.ChargerPointDTO;
import org.spend.usefull.chargermap.app.rest.ChargerPointRest;
import org.spend.usefull.chargermap.app.rest.FoursquareRest;
import org.spend.usefull.chargermap.app.service.LocationManager;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EFragment(R.layout.fragment_map)
public class MapSpecialFragment extends Fragment {
    private GoogleMap map;
    protected List<Marker> markers = new LinkedList<Marker>();

    @Bean
    protected LocationManager locationManager;

    @RestService
    protected ChargerPointRest chargerPointRest;

    @RestService
    protected FoursquareRest foursquareRest;

    @ViewById
    protected View content;

    private void setCallbacks() {
        map.setMyLocationEnabled(true);
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                initMapUISettings();
                if (locationManager.isLocationDetected())
                    moveCameraToUser();
            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (!locationManager.isLocationDetected())
                    return;
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                cameraChanged(bounds, cameraPosition.target);
            }
        });
    }

    @Background
    void cameraChanged(LatLngBounds bounds, LatLng center) {
        Answer<List<ChargerPointDTO>> answer;
        try {
            answer = chargerPointRest.list(
                    bounds.northeast.latitude,
                    bounds.southwest.longitude,
                    bounds.southwest.latitude,
                    bounds.northeast.longitude
            );
        } catch (RestClientException exception) {
            showError(exception.getLocalizedMessage());
            answer = new Answer<List<ChargerPointDTO>>();
        }
        FoursquareAnswer foursquareAnswer;
        try {
            foursquareAnswer = foursquareRest.explore(center.latitude, center.longitude);
        } catch (RestClientException exception) {
            showError(exception.getMessage());
            foursquareAnswer = new FoursquareAnswer();
            foursquareAnswer.setGroups(Collections.EMPTY_LIST);
        }
        List<FoursquareItem> foursquareItems = foursquareAnswer.getAllItems();
        merge(answer.getElements(), foursquareItems);
        List<ChargerPointDTO> chargerPointDTOs = merge(answer.getElements(), foursquareItems);
        pickPointsOnMap(chargerPointDTOs);
        if (answer.getError() != null) {
            showError(answer.getError());
        }
    }

    private List<ChargerPointDTO> merge(List<ChargerPointDTO> chargerPoints, List<FoursquareItem> foursquareItems) {
        if (chargerPoints == null) {
            chargerPoints = new LinkedList<ChargerPointDTO>();
        }
        foursquareFor:
        for (Iterator<FoursquareItem> foursquareItem = foursquareItems.iterator(); foursquareItem.hasNext(); ) {
            FoursquareItem item = null;
            for (ChargerPointDTO chargerPoint : chargerPoints) {
                item = foursquareItem.next();
                if (chargerPoint.getFoursquareId() == item.getId()) {
                    foursquareItem.remove();
                    continue foursquareFor;
                }
            }
            chargerPoints.add(new ChargerPointDTO(item));
        }
        return chargerPoints;
    }

    @UiThread
    void showError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void pickPointsOnMap(List<ChargerPointDTO> chargerPoints) {
        for (Marker marker : markers) {
            if (!marker.isVisible()) {
                marker.remove();
            }
        }

        chargersLabel:
        for (ChargerPointDTO chargerPoint : chargerPoints) {
            for (Marker marker : markers) {
                if (marker.getPosition().latitude == chargerPoint.getLat() &&
                        marker.getPosition().longitude == chargerPoint.getLng()) {
                    continue chargersLabel;
                }
            }
            markers.add(map.addMarker(new MarkerOptions()
                    .position(new LatLng(chargerPoint.getLat(), chargerPoint.getLng()))
                    .title(chargerPoint.getDescription())
                    .anchor(0.0f, 1.0f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))));
        }
        moveCameraToSeePosition();
    }

    @UiThread
    void moveCameraToSeePosition() {
        if (markers.size() == 0) {
            return;
        }
        Marker nearestPoint = markers.get(0);
        double nearestPointDistance = 272;
        double tmpPointDistance;
        for (Marker marker : markers) {
            if ((tmpPointDistance = locationManager.getLocation().distanceTo(new LocationAdapter(marker.getPosition()))) < nearestPointDistance) {
                nearestPoint = marker;
                nearestPointDistance = tmpPointDistance;
            }
        }
        while (!see(nearestPoint)) {
            try {
                map.animateCamera(CameraUpdateFactory.zoomOut());
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean see(Marker nearestPoint) {
        LatLngBounds latLngBounds = map.getProjection().getVisibleRegion().latLngBounds;
        if (nearestPoint.getPosition().latitude > latLngBounds.northeast.latitude || nearestPoint.getPosition().latitude < latLngBounds.southwest.latitude)
            return false;
        if (nearestPoint.getPosition().longitude > latLngBounds.northeast.longitude || nearestPoint.getPosition().longitude < latLngBounds.southwest.longitude)
            return false;
        return true;
    }

    private static double calculateDifferent(double markerLatitude, double markerLongitude,
                                             double centerLat, double centerLng) {
        return Math.abs(centerLat - markerLatitude) + Math.abs(centerLng - markerLongitude);
    }

    private void moveCameraToUser() {
        Location location = locationManager.getLocation();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
    }

    @AfterViews
    public void initMap() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        setCallbacks();
    }

    @Click(R.id.addNewPointButton)
    void addNewPoint() {
        map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_new))
                        .position(map.getCameraPosition().target)
                        .draggable(true)
        ).showInfoWindow();
    }

    private void initMapUISettings() {
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        // Setting a custom info window adapter for the google map
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View popup = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                final LatLng latLng = marker.getPosition();

                // Getting reference to the TextView to set latitude
                TextView textViewName = (TextView) popup.findViewById(R.id.textViewName);
                textViewName.setText("Зарядка");

                // Getting reference to the TextView to set longitude
                TextView textViewDescription = (TextView) popup.findViewById(R.id.textViewDescription);
                textViewDescription.setText(marker.getTitle());

                if (marker.getTitle() == null) {
                    textViewName.setVisibility(View.INVISIBLE);
                    textViewDescription.setVisibility(View.INVISIBLE);

                    final EditText editTextName = (EditText) popup.findViewById(R.id.editTextName);
                    editTextName.setVisibility(View.VISIBLE);

                    final EditText editTextDescription = (EditText) popup.findViewById(R.id.editTextDescription);
                    editTextDescription.setVisibility(View.VISIBLE);

                    ImageButton imageButtonUp = (ImageButton) popup.findViewById(R.id.imageButtonUp);
                    imageButtonUp.setEnabled(false);

                    ImageButton imageButtonDown = (ImageButton) popup.findViewById(R.id.imageButtonDown);
                    imageButtonDown.setEnabled(false);

                    Button buttonAdd = (Button) popup.findViewById(R.id.buttonAdd);
                    buttonAdd.setVisibility(View.VISIBLE);

                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            if (editTextName.getText().toString().equals("") || editTextDescription.getText().toString().equals("")) {
                                return;
                            }
                            ChargerPointDTO chargerPointDTO = new ChargerPointDTO();
                            chargerPointDTO.setDescription(editTextDescription.getText().toString());
                            chargerPointDTO.setLat((float) latLng.latitude);
                            chargerPointDTO.setLng((float) latLng.longitude);
                            try {
                                chargerPointRest.add(chargerPointDTO);
                            } catch (RestClientException exception) {
                                showError(exception.getLocalizedMessage());
                            }
                        }
                    });
                }

                return popup;

            }
        });
    }
}
