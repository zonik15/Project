package com.app.heyphil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
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

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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

public class MapActivity<ListData> extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, OnMarkerClickListener {

    private TextToSpeech tts;
    String text;
    // Google Map
    private GoogleMap googleMap;
    private Spinner current;
    private Button go;
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
    JSONArray contacts = null;
    private String loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.lat1.clear();
        Data.lon1.clear();
        Data.name1.clear();
        Data.tel.clear();
        Data.address.clear();
        Data.pcode.clear();
        /*if(Data.isDoctor == true){

        }*/
        setContentView(R.layout.activity_main);
        new GetProviders().execute();
        current=(Spinner)findViewById(R.id.current);
        go=(Button)findViewById(R.id.button1);
        et_provider=(EditText)findViewById(R.id.provider);
        lv_provider=(ListView)findViewById(R.id.lv_provider);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Boogaloo-Regular.ttf");
        et_provider.setTypeface(tf);
        go.setTypeface(tf);
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
                lv_provider.setVisibility(View.VISIBLE);
                current.setVisibility(View.GONE);
                go.setVisibility(View.GONE);
                //chatBubbleActivity.TTS(text);
                text="Select provider to locate in google map.";
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
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
                text=pro+" is located at "+add+". Tap google marker to view information.";
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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
        go.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(), Data.lat1.toString(), Toast.LENGTH_LONG).show();
            }
        });
        ArrayAdapter<String> location = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Data.mylist){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v= super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Boogaloo-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView)v).setTextColor(Color.rgb(110,104,104));
                return  v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v= super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Boogaloo-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
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
                new onLoginAsync().execute();
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
                    double latitude =Data.doctorLat;
                    double longitude =Data.doctorLon;
                    Toast.makeText(getApplicationContext(), ""+Data.doctorLat.toString(), Toast.LENGTH_LONG).show();

                    // Adding a marker
                    MarkerOptions marker = new MarkerOptions().position(
                            new LatLng(latitude,longitude))
                            .title("Hello Maps");

                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Data.doctorLat,
                                    Data.doctorLon)).zoom(12).build();
                    googleMap.addMarker(marker);
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
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
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Boogaloo-Regular.ttf");
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
        btn_no.setTypeface(tf);
        btn_yes.setTypeface(tf);
        tv_provider.setTypeface(tf);
        tv_add.setTypeface(tf);
        tv_address.setTypeface(tf);
        tv_con.setTypeface(tf);
        tv_contact.setTypeface(tf);
        tv_create.setTypeface(tf);
        tv_provider.setText(Data.doctorName);
        tv_address.setText(Data.doctorAddress);
        tv_contact.setText("09123456789");
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
        return true;
    }
    private void getXMLMemberinfo()
    {
        String mem_link = "https://apps.philcare.com.ph/PCareWebServices/Providers.svc/HospitalsPerMember/?Location="+loc+"&area=&Certno="+Data.cert;
        list_xml_link.clear();
        System.out.println("==========url"+convertToUrl(mem_link));
        list_xml_link.add(convertToUrl(mem_link));
    }
    public static String convertToUrl(String urlStr)
    {
        try
        {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            return String.valueOf(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    private class onLoginAsync extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            getXMLMemberinfo();
        }


        protected String doInBackground(String... params)
        {

            String status = null;

            try
            {
                status =getXML(list_xml_link.get(0));

                if (status.equals("error"))
                {
                    status = "error";
                }
                else
                {
                    status = getXMLValue(status);
                    total++;
                }
            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return status;
        }


        protected void onPostExecute(String result)
        {
            if (result.equals("False"))
            {
            }
            else if (result.equals("True"))
            {
                //getXMLLinks();
                new onGetAllXMLData().execute();
            }
            else if (result.equals("error"))
            {
                successful_flag = "False";

            }
        }
    }
    /**
     * Get XML data.
     *
     * @param //sevice
     *              - URL on string format
     * @return String - return XML value
     *
     * @response String - returned if error occur
     */
    public static String getXML(String service)
    {

        String result = null;
        HttpGet request = new HttpGet(service);

        HttpParams httpParams = new BasicHttpParams();
        int some_reasonable_timeout = (int) (50 * 1000);
        HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);
        HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout);
        HttpClient client = new DefaultHttpClient(httpParams);

        try
        {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK)
            {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                result = responseHandler.handleResponse(response);
            }
            else
            {
                Log.e("trace", "Error1");
                return "error";
            }
        }
        catch (ClientProtocolException e)
        {
            Log.e("trace", "Error2", e);
            return "error";
        }
        catch (IOException e)
        {
            Log.e("trace", "Error3", e);
            return "error";
        }
        finally
        {
            Log.e("trace", "finally");
            client.getConnectionManager().shutdown();
        }

        return result;
    }
    // get All XML Data
    private class onGetAllXMLData extends AsyncTask<String, String, String>
    {
        int list_xml_link_total;


        protected void onPreExecute()
        {
            maxBarValue = list_xml_link.size();
        }


        protected String doInBackground(String... params)
        {
            list_xml_link_total = list_xml_link.size();
            String s;
            for (int i = currentDownload; i < list_xml_link_total; i++)
            {
                s = getXML(list_xml_link.get(i));
                if (s.equals("error"))
                {
                    currentDownload = i;
                    return "false";
                }
                else
                {
                    list_xml_data.add(s);
                    total++;

                }

            }

            return "true";
        }


        protected void onPostExecute(String result)
        {
            if (result.equals("true"))
            {
            }
            else
            {
                successful_flag = "False";

            }

        }
    }
    private String getXMLValue(String xml) throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(xml));
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xpp.getName().equals("ProviderName") || xpp.getName().equals("a:ProviderName"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) name = xpp.getText();
                    Data.name1.add(name);
                }
                else if (xpp.getName().equals("Latitude") || xpp.getName().equals("a:Latitude"))
                {
                    if (xpp.next() == XmlPullParser.TEXT)latitude =xpp.getText();
                    if(latitude.equals("Null")){
                        Data.lat1.add(""+0);
                    }
                    else{
                        Data.lat1.add(""+latitude);
                    }
                }
                else if (xpp.getName().equals("Longitude") || xpp.getName().equals("a:Longitude"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) longitude =xpp.getText();
                    if(longitude.equals("Null")){
                        longitude="0";
                        Data.lon1.add(longitude);
                    }
                    else{
                        Data.lon1.add(""+longitude);
                    }

                }
                else if (xpp.getName().equals("ProviderCode") || xpp.getName().equals("a:ProviderCode"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) pcode =xpp.getText();
                    Data.pcode.add(pcode);

                }
                else if (xpp.getName().equals("Address") || xpp.getName().equals("a:Address"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) address =xpp.getText();
                    Data.address.add(address);

                }
                else if (xpp.getName().equals("Telephone") || xpp.getName().equals("a:Telephone"))
                {
                    if (xpp.next() == XmlPullParser.TEXT) tel =xpp.getText();
                    if(tel.isEmpty()){
                        Data.tel.add("None");
                    }
                    else{
                        Data.tel.add(""+tel);

                    }
                }

            }

            eventType = xpp.next();
        }

        return successful_flag;


    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetProviders extends AsyncTask<Void, Void, Void> {

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
            String Providerurl="https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchHospitals/?CertNo="+Data.cert+"&Type=OP&City=&Hospital=";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Providerurl, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_PROVIDERS);
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

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
        Intent i=new Intent(this, ChatBubbleActivity.class);
        startActivity(i);
        super.onBackPressed();
    }


}