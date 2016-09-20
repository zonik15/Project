package com.app.heyphil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity<ListData> extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, OnMarkerClickListener {

    private TextToSpeech tts;
    String text;
    // Google Map
    private GoogleMap googleMap;
    private Spinner current;
    Double lat;
    Double lon;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private String           tel;
    private String           address;
    private String           name;
    private String           latitude;
    private String          longitude;
    private String           City;
    private String           pcode;
    List<String>            list_xml_link           = new ArrayList<String>();
    List<String>            list_xml_data           = new ArrayList<String>();
    private String          successful_flag         = "";
    private int             maxBarValue;
    private int             total;
    private int             currentDownload         = 1;
    // JSON Node names (provider)
    private static final String TAG_PROVIDERS = "SearchHospitalsResult";
    private static final String TAG_CITY = "City";
    private static final String TAG_LAT = "Latitude";
    private static final String TAG_LONG = "Longitude";
    private static final String TAG_PCODE = "ProviderCode";
    private static final String TAG_PNAME = "ProviderName";
    private static final String TAG_TEL= "Telephone";
    private EditText et_provider;
    private ListView lv_provider;
    ListAdapter Provideradapter;
    // contacts JSONArray
    JSONArray jsonArray= null;
    private String loc;
    static ProgressDialog pDialog;
    private String directionUrl;
    String duration;
    String distance;
    ArrayList<LatLng> coordList = new ArrayList<LatLng>();
    boolean stat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        setContentView(R.layout.activity_main);
        Data.lat1.clear();
        Data.lon1.clear();
        Data.name1.clear();
        Data.tel.clear();
        Data.address.clear();
        Data.pcode.clear();
        new GetCity().execute();
        current=(Spinner)findViewById(R.id.current);
        et_provider=(EditText)findViewById(R.id.provider);
        lv_provider=(ListView)findViewById(R.id.lv_provider);
        Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
        et_provider.setTypeface(tf);
        initilizeMap();
        tts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });
        et_provider.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Data.getPro=true;
                if(Data.City=="Select Location"){
                    Toast.makeText(getBaseContext(),"Please select location!",Toast.LENGTH_SHORT).show();
                    //text="Please select location!";
                    //updateResult(text);
                    current.setVisibility(View.VISIBLE);
                }
                else {
                    new GetProviders().execute();
                }
                com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
                text="Select provider to locate in google map.";
                //updateResult(text);
                new GetCity().execute();
            }
        });
        if(Data.sprovider!=null&&!Data.sprovider.isEmpty())
        {
            et_provider.setText(Data.sprovider);
        }
        et_provider.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                ((Filterable) Provideradapter).getFilter().filter(s);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        lv_provider.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Data.lat1.clear();
                Data.lon1.clear();
                Data.name1.clear();
                Data.tel.clear();
                Data.address.clear();
                Data.pcode.clear();
                String add=(((TextView) view.findViewById(R.id.address)).getText().toString());
                String pro=(((TextView) view.findViewById(R.id.name)).getText().toString());
                String tel=(((TextView) view.findViewById(R.id.mobile)).getText().toString());
                String code=(((TextView) view.findViewById(R.id.pcode)).getText().toString());
                String lat=(((TextView) view.findViewById(R.id.lat)).getText().toString());
                String lon=(((TextView) view.findViewById(R.id.lon)).getText().toString());
                Data.sprovider=pro;
                text=pro+" is located at "+add+". Tap google marker to view information.";
                updateResult(text);
                Data.name1.add(pro);
                Data.address.add(add);
                Data.tel.add(tel);
                Data.pcode.add(code);
                Data.lat1.add(lat);
                Data.lon1.add(lon);
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        ArrayAdapter<String> location = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Data.mylist){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v= super.getView(position, convertView, parent);
                Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
                ((TextView) v).setTypeface(tf);
                ((TextView)v).setTextColor(Color.rgb(110,104,104));
                return  v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v= super.getDropDownView(position, convertView, parent);
                Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
                ((TextView) v).setTypeface(tf);
                ((TextView)v).setTextColor(Color.rgb(110,104,104));
                return  v;
            }
        };
        current.setAdapter(location);
        int initialposition=current.getSelectedItemPosition();
        current.setSelection(initialposition, false);
        current.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Data.lat1.clear();
                Data.lon1.clear();
                Data.name1.clear();
                Data.tel.clear();
                Data.address.clear();
                Data.pcode.clear();
                loc=current.getItemAtPosition(position).toString();
                stat=true;
                Data.getCity=true;
                Data.City=loc;
                updateResult("Here are the providers in"+loc+". Tap or click the google marker to view information an to proceed to create or request LOA. Thank you!");
                new GetProvidersCity().execute();
                new GetCity().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        buildGoogleApiClient();

        if(mGoogleApiClient!= null){
            mGoogleApiClient.connect();
        }
        else{
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
/*
	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}*/

    /**
     * function to load map If map is not created it will create it for you
     * */
    public URI getHost(String url){
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateResult(final String result)
    {
        //String username = "674e4f93-d26f-4ba6-bacc-086167126d90";
        //String password = "BBtH4uf1stvO";

        String username = Data.ttsUsername;
        String password = Data.ttsPassword;


        String serviceURL = "https://stream.watsonplatform.net/text-to-speech/api";

        com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().initWithContext(this.getHost(serviceURL));
        com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().setCredentials(username, password);
        com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().setVoice("en-US_MichaelVoice");

        MapActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().synthesize(result);
                //mAdapter.notifyDataSetChanged();

            }
        });
    }
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            else{
                try {
                    // Changing map type
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

                    // Showing / hiding your current location
                    //googleMap.setMyLocationEnabled(true);

                    // Enable / Disable zooming controls
                    googleMap.getUiSettings().setZoomControlsEnabled(false);

                    // Enable / Disable my location button
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                    // Enable / Disable Compass icon
                    googleMap.getUiSettings().setCompassEnabled(true);

                    // Enable / Disable Rotate gesture
                    googleMap.getUiSettings().setRotateGesturesEnabled(true);

                    // Enable / Disable zooming functionality
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);
                    googleMap.setOnMarkerClickListener(this);
                    double latitude = Data.doctorLat;
                    double longitude = Data.doctorLon;
                    //Toast.makeText(getApplicationContext(), ""+Data.doctorLat.toString(), Toast.LENGTH_LONG).show();

                    // Adding a marker
                    IconGenerator iconFactory = new IconGenerator(getBaseContext());
                    //addIcon(iconFactory, "Default", new LatLng(-33.8696, 151.2094));

                    iconFactory.setColor(Color.WHITE);


                    addIcon(iconFactory, Data.doctorProvider, new LatLng(latitude,longitude),Data.doctorPcode);
                  /*  MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(latitude,longitude))
                            .title("Hello Maps");

                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                     googleMap.addMarker(marker);*/
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Data.doctorLat,
                                    Data.doctorLon)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }
    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position, String i) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                snippet(i).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        googleMap.addMarker(markerOptions);
    }
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            lat=mLastLocation.getLatitude();
            lon=mLastLocation.getLongitude();
            // Loading map
            initilizeMap();
        }
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onMarkerClick(final Marker arg0) {
        // TODO Auto-generated method stub
        directionUrl="https://maps.googleapis.com/maps/api/directions/json?origin="+ Data.lat+",+"+ Data.lon+"&destination="+ Data.doctorLat+",+"+ Data.doctorLon+"&sensor=false&mode=driving&alternatives=true&key=AIzaSyAh2tjcjLNp2FS4bmxMi0h-FXFvRUeXRho";
        new getDirection().execute();
        return true;
    }
    private class getDirection extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(directionUrl, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                JSONObject jsonObject = null;
                List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
                try {
                    jsonObject = new JSONObject(jsonStr);
                    JSONArray routesArray = jsonObject.getJSONArray("routes");
// Grab the first route
                    JSONObject route = routesArray.getJSONObject(0);
// Take all legs from the route
                    JSONArray legs = route.getJSONArray("legs");
// Grab first leg
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject durationObject = leg.getJSONObject("duration");
                    JSONObject distanceObject = leg.getJSONObject("distance");
                    duration = durationObject.getString("text");
                    distance = distanceObject.getString("text");
                    List path = new ArrayList<HashMap<String, String>>();
                    for(int i=0;i<legs.length();i++){
                        JSONArray jSteps = ( (JSONObject)legs.get(i)).getJSONArray("steps");
                        for(int k=0;k<jSteps.length();k++){
                            String instruction = "";
                            instruction = (String) ((JSONObject)jSteps.get(k)).get("html_instructions");
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for(int l=0;l<list.size();l++){
                                coordList.add(new LatLng(((LatLng)list.get(l)).latitude, ((LatLng)list.get(l)).longitude));
                                System.out.println("PATH======"+coordList.toString());
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            PolylineOptions polylineOptions = new PolylineOptions();

// Create polyline options with existing LatLng ArrayList
            polylineOptions.addAll(coordList);
            polylineOptions
                    .width(5)
                    .color(Color.BLUE);

// Adding multiple points in map using polyline and arraylist
            googleMap.addPolyline(polylineOptions);
            showProvider();
            coordList.clear();
        }

    }
    private void showProvider(){
        Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.map_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_provider=(TextView)dialog.findViewById(R.id.textView);
        TextView tv_add=(TextView)dialog.findViewById(R.id.textView2);
        TextView tv_address=(TextView)dialog.findViewById(R.id.textView3);
        TextView tv_con=(TextView)dialog.findViewById(R.id.textView4);
        TextView tv_contact=(TextView)dialog.findViewById(R.id.textView5);
        TextView tv_create=(TextView)dialog.findViewById(R.id.textView6);
        Button btn_no=(Button)dialog.findViewById(R.id.button);
        Button btn_yes=(Button)dialog.findViewById(R.id.button2) ;
        ImageView exit=(ImageView)dialog.findViewById(R.id.exit);
        TextView tv_distance=(TextView)dialog.findViewById(R.id.tv_distance);
        TextView distances=(TextView)dialog.findViewById(R.id.distance);
        TextView tv_time=(TextView)dialog.findViewById(R.id.tv_time);
        TextView time=(TextView)dialog.findViewById(R.id.time);
        btn_no.setTypeface(tf);
        btn_yes.setTypeface(tf);
        tv_provider.setTypeface(tf);
        tv_add.setTypeface(tf);
        tv_address.setTypeface(tf);
        tv_con.setTypeface(tf);
        tv_contact.setTypeface(tf);
        tv_create.setTypeface(tf);
        tv_distance.setTypeface(tf);
        tv_time.setTypeface(tf);
        distances.setTypeface(tf);
        time.setTypeface(tf);
        tv_provider.setText(Data.doctorName);
        tv_address.setText(Data.doctorAddress);
        tv_contact.setText("09123456789");
        distances.setText(""+distance);
        time.setText(""+duration);
        text=""+tv_provider.getText().toString()+" is "+distance+" away from your current location. It takes "+duration+" to travel to get there!";
        updateResult(text);
        dialog.show();
        btn_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),CreateLOA.class);
                i.putExtra("Provider_Name", Data.doctorProvider);
                i.putExtra("Provider_Address", Data.doctorAddress);
                i.putExtra("Provider_Contact", "######");
                i.putExtra("Pcode", Data.doctorPcode);
                i.putExtra("Dname", Data.doctorName);
                startActivity(i);
            }
        });
        btn_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetProvidersCity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+loc.replace(" ","+")+"&area=&Certno="+ Data.cert, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("SearchHospitalsPerMemberResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String access = c.getString("Access");
                        String address = c.getString("Address");
                        String latitude = c.getString("Latitude");
                        String longitude = c.getString("Longitude");
                        String providerCode = c.getString("ProviderCode");
                        String providerName1= c.getString("ProviderName");
                        String telephone = c.getString("Telephone");
                        if(!access.equals("NO ACCESS")){
                            Data.address.add(address);
                            Data.lat1.add(latitude);
                            Data.lon1.add(longitude);
                            Data.pcode.add(providerCode);
                            Data.name1.add(providerName1);
                            Data.tel.add("" + telephone);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            Intent n = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(n);
            finish();
        }

    }
    /* Async task class to get json by making HTTP call
    * */
    private class GetProviders extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Data.providerList.clear();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+ Data.City.replace(" ","+")+"&area=&Certno="+ Data.cert, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("SearchHospitalsPerMemberResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        HashMap<String, String> provider = new HashMap<String, String>();
                        String access = c.getString("Access");
                        String address = c.getString("Address");
                        String latitude = c.getString("Latitude");
                        String longitude = c.getString("Longitude");
                        String providerCode = c.getString("ProviderCode");
                        String providerName1= c.getString("ProviderName");
                        String telephone = c.getString("Telephone");
                        // adding each child node to HashMap key => value
                        provider.put(TAG_CITY, address);
                        provider.put(TAG_LAT, latitude);
                        provider.put(TAG_LONG, longitude);
                        provider.put(TAG_PCODE, providerCode);
                        provider.put(TAG_PNAME, providerName1);
                        provider.put(TAG_TEL, telephone);
                        if(!access.equals("NO ACCESS")){
                            Data.providerList.add(provider);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            Provideradapter = new SimpleAdapter(
                    MapActivity.this, Data.providerList,
                    R.layout.provider_list, new String[] { TAG_PNAME, TAG_CITY,
                    TAG_TEL,TAG_PCODE, TAG_LAT, TAG_LONG }, new int[] { R.id.name,
                    R.id.address, R.id.mobile, R.id.pcode, R.id.lat, R.id.lon });
            lv_provider.setAdapter(Provideradapter);
            lv_provider.setVisibility(View.VISIBLE);
            current.setVisibility(View.GONE);

        }
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetProviderss extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
		/*	pDialog = new ProgressDialog(CreateLOA.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
	*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String Providerurl="https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchHospitals/?CertNo="+ Data.cert+"&Type=OP&City=&Hospital=";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Providerurl, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray(TAG_PROVIDERS);
                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String city = c.getString(TAG_CITY);
                        String lat = c.getString(TAG_LAT);
                        String lon = c.getString(TAG_LONG);
                        String pcode = c.getString(TAG_PCODE);
                        String pname = c.getString(TAG_PNAME);
                        String tel = c.getString(TAG_TEL);

                        // tmp hashmap for single contact
                        HashMap<String, String> provider = new HashMap<String, String>();

                        // adding each child node to HashMap key => value

                        provider.put(TAG_CITY, city);
                        provider.put(TAG_LAT, lat);
                        provider.put(TAG_LONG, lon);
                        provider.put(TAG_PCODE, pcode);
                        provider.put(TAG_PNAME, pname);
                        provider.put(TAG_TEL, tel);
                        Data.providerList.add(provider);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
		/*	if (pDialog.isShowing())
				pDialog.dismiss();*/
            /**
             * Updating parsed JSON data into ListView
             * */
            Provideradapter = new SimpleAdapter(
                    MapActivity.this, Data.providerList,
                    R.layout.provider_list, new String[] { TAG_PNAME, TAG_CITY,
                    TAG_TEL,TAG_PCODE, TAG_LAT, TAG_LONG }, new int[] { R.id.name,
                    R.id.address, R.id.mobile, R.id.pcode, R.id.lat, R.id.lon });
            lv_provider.setAdapter(Provideradapter);
        }

    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Data.lat1.clear();
        Data.lon1.clear();
        Data.name1.clear();
        Data.tel.clear();
        Data.address.clear();
        Data.pcode.clear();
        finish();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetCity extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            Data.mylist.clear();
            Data.mylist.add(Data.City);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchProviderCity/", ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("SearchProviderCityResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String city = c.getString("City");
                        Data.mylist.add(city);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
        }
    }

}