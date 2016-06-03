package com.example.marc.map2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Localisation
        Location myLocation;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation != null) {
            Log.i("Location", "Long :" + String.valueOf(myLocation.getLongitude()) + ", Lat :" + String.valueOf(myLocation.getLatitude()));
        } else {
            Log.i("Location", "Try again");
            Toast.makeText(getApplicationContext(),"Vos données de localisation ou internet ne sont pas activées.\nVeuillez les activer pour que le service fonctionne.",Toast.LENGTH_LONG).show();
        }

        //Récupère les données
        String stringAdressPosition = "";
        stringAdressPosition = getIntent().getStringExtra("stringAdressPosition");
        Log.i("MapsActivity, Position", stringAdressPosition);

        String stringAdressDestination = "";
        stringAdressDestination = getIntent().getStringExtra("stringAdressDestination");
        Log.i("MapsActivity,Destinati", stringAdressDestination);

        String demande = "";
        demande = getIntent().getStringExtra("Demande");
        Log.i("MapsActivity, Demande", String.valueOf(demande));


        //Set up

        setUpMapIfNeeded();

        //Choix action

        if (demande.equals("Trajet")) {
            LatLng location;
            LatLng destination;

            if (stringAdressPosition!=null & stringAdressDestination!=null) {
                try {
                    if (myLocation != null & stringAdressPosition.equals("Ma position")) {
                        location = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    } else {
                        location = findLatLng(stringAdressPosition);
                    }

                    //Transformer des adresses en coordonnées
                    destination = findLatLng(stringAdressDestination);
                    Log.i("Location :", String.valueOf(location.latitude) + String.valueOf(location.longitude));
                    Log.i("Destination :", String.valueOf(destination.latitude) + String.valueOf(destination.longitude));

                    onSearch(location, destination);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    setUpMapIfNeeded();
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas correctement remplis les champs pour que nous puissions calculer un trajet.\nVeuillez réessayer.",Toast.LENGTH_LONG).show();
                }
            }
            else {
                setUpMapIfNeeded();
                Toast.makeText(getApplicationContext(),"Vous n'avez pas correctement remplis les champs pour que nous puissions calculer un trajet.\nVeuillez réessayer.",Toast.LENGTH_LONG).show();
            }
        }
        else if (demande.equals("Location")) {
            onLocalisation(myLocation);
        }
        else {
            setUpMapIfNeeded();
            Toast.makeText(getApplicationContext(),"Oh dear, we are in trouble ...",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * @param myLocation
     */

    public void onLocalisation(Location myLocation) {
        LatLng myLatLng= new LatLng(44.3333,1.2167);
        try {
            myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            Log.i("Location Latitude", String.valueOf(myLocation.getLatitude()));
            Log.i("Location Longitude", String.valueOf(myLocation.getLongitude()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Nous n'arrivons pas à vous localiser.\nVeuillez vérifier vos options de localisation et réessayer.", Toast.LENGTH_LONG).show();
        }

        mMap.addMarker(new MarkerOptions().position(myLatLng).title("Votre position"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));

    }
    public void onSearch(LatLng location, LatLng destination) {


        Log.i("Location Latitude", String.valueOf(location.latitude));
        Log.i("Location Longitude", String.valueOf(location.longitude));

        mMap.addMarker(new MarkerOptions().position(location).title("Votre position").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_menu_myplaces)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));


        Log.i("Destination Latitude", String.valueOf(destination.latitude));
        Log.i("Destination Longitude", String.valueOf(destination.longitude));

        mMap.addMarker(new MarkerOptions().position(destination).title("Votre destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


        mMap.addPolyline(new PolylineOptions().add(location, destination).width(7).color(R.color.lightOrange));
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (mMap != null) {
                setUpMap("");
            }
        }
    }

    private void setUpMap(String stringAdress) {
        String location = stringAdress;

        List<Address> addressList = null;

        if (location == "") {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }


        else if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = null;
            LatLng myAdressLatLng = null;

            if (addressList != null) {
                address = addressList.get(0);
                myAdressLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            }

            Log.i("Position Latitude",String.valueOf(address.getLatitude()));
            Log.i("Position Longitude",String.valueOf(address.getLongitude()));

            mMap.addMarker(new MarkerOptions().title("Vous").position(myAdressLatLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myAdressLatLng,5));
        }

    }

    public LatLng findLatLng(String stringAdress) {
        List<Address> addressList = null;
        if (stringAdress != null || !stringAdress.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(stringAdress, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = null;
            LatLng latLng = null;

            if (addressList != null) {
                address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
            }
            return latLng;
        }
        else {
            return new LatLng(0,0);
        }
    }
    }

