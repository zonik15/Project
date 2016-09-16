package com.app.heyphil;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class MainActivity<ListData> extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, OnMarkerClickListener {
	private TextToSpeech tts;
	String text;
	// Google Map
	private GoogleMap googleMap;
	private Spinner current;
	Double lat;
	Double lon;
	GoogleApiClient mGoogleApiClient;
	Location mLastLocation;
	private String directionUrl;
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
	private String loc;
	// JSON Node names (provider)
	private static final String TAG_PROVIDERS = "SearchHospitalsResult";
	private static final String TAG_ACCESS = "Access";
	private static final String TAG_CITY = "City";
	private static final String TAG_LAT = "Latitude";
	private static final String TAG_LONG = "Longitude";
	private static final String TAG_PCODE = "ProviderCode";
	private static final String TAG_PNAME = "ProviderName";
	private static final String TAG_TEL= "Telephone";
	private EditText et_provider;
	private ListView lv_provider;
	private Button btn_request;
	ListAdapter Provideradapter;
	private String pname;
	private String prcode;
	private String paddress;
	private String pcontact;
	private String dpname;
	 // contacts JSONArray
 	JSONArray jsonArray = null;
	private int index;
	private String code;
	String duration;
	String distance;
	ArrayList<LatLng> coordList = new ArrayList<LatLng>();
	static ProgressDialog pDialog;
	boolean stat;
	String Providerurl;
	Context context;
	private StreamPlayer player = new StreamPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		current=(Spinner)findViewById(R.id.current);
		et_provider=(EditText)findViewById(R.id.provider);
		lv_provider=(ListView)findViewById(R.id.lv_provider);
		btn_request=(Button)findViewById(R.id.requestloa);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		et_provider.setTypeface(tf);
		btn_request.setTypeface(tf);
		initilizeMap();
		new GetCity().execute();
		if(Data.getCity) {
			player.stopPlayer();
			Data.getCity=false;
			text = "Here are the providers in " + Data.City + ". Tap or click the google marker to view information an to proceed to create or request LOA. Thank you!";
			show(text);
			//updateResult(text);
		}
		else if(Data.getPro){
			player.stopPlayer();
			Data.getPro=false;
			text= Data.sprovider+" is located at "+ Data.sproviderad+". Tap google marker to view information.";
			show(text);
			//updateResult(text);
		}
		tts=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if(status!=TextToSpeech.ERROR){
					tts.setLanguage(Locale.getDefault());
				}
			}
		});
		lv_provider.setVisibility(View.GONE);
		btn_request.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!et_provider.getText().toString().isEmpty()&&et_provider.getText().toString()!=null){
					Intent i=new Intent(getApplicationContext(),CreateLOA.class);
					i.putExtra("Provider_Name", Data.name1.get(0));
					i.putExtra("Provider_Address", Data.address.get(0));
					i.putExtra("Provider_Contact", Data.tel.get(0).replace("NULL", "0"));
					i.putExtra("Pcode", Data.pcode.get(0));
					i.putExtra("Dname", " ");
					startActivity(i);
				}
				else{
					Toast.makeText(getBaseContext(),"Please select provider or tap google marker to proceed and request an LOA.",Toast.LENGTH_SHORT).show();
				}
			}
		});
		et_provider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				player.stopPlayer();
				btn_request.setVisibility(View.GONE);
				text="Select provider to locate in google map.";
				show(text);
				Data.getPro=true;
				//updateResult(text);
				new GetProviders().execute();
				//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				//new GetCity().execute();

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
				btn_request.setVisibility(View.VISIBLE);
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
				Data.sproviderad=add;
				Data.code=code;
				Log.e("Parameters",pro+tel+code);
				//text=pro+" is located at "+add+". Tap google marker to view information.";
				//show(text);
				//updateResult(text);
				Data.name1.add(pro);
				Data.address.add(add);
				Data.tel.add(tel);
				Data.pcode.add(code);
				Data.lat1.add(lat);
				Data.lon1.add(lon);
				Data.providerList.clear();
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
					Data.sprovider=null;
					loc=current.getItemAtPosition(position).toString();
					stat=true;
					Data.getCity=true;
					Data.City=loc;
					//text="Here are the providers in"+loc+". Tap or click the google marker to view information an to proceed to create or request LOA. Thank you!";
					//show(text);
					//updateResult("Here are the providers in"+loc+". Tap or click the google marker to view information an to proceed to create or request LOA. Thank you!");
					new GetProvidersCity().execute();
					new GetCity().execute();
					Data.providerList.clear();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					//updateResult("Select location to locate providers in google map.");
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
					googleMap.setMyLocationEnabled(true);

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
					double[] Lat = new double[Data.lat1.size()];
					double[] Lon = new double[Data.lon1.size()];
					//MarkerOptions marker = new MarkerOptions().position(
					//		new LatLng(Data.lat, Data.lon))
					//		.title("I'm Here!");
					//marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
					//googleMap.addMarker(marker);
					//Toast.makeText(getApplicationContext(), ""+Data.lat1.toString(), Toast.LENGTH_LONG).show();
					for (int i = 0; i < Data.lat1.size(); i++)
					{ //iterate over the elements of the list
					    Lat[i] = Double.parseDouble(Data.lat1.get(i).replace("NULL", "0"));
					    Lon[i] = Double.parseDouble(Data.lon1.get(i).replace("NULL", "0"));//store each element as a double in the array
						// Adding a marker

						IconGenerator iconFactory = new IconGenerator(getBaseContext());
						//addIcon(iconFactory, "Default", new LatLng(-33.8696, 151.2094));

						iconFactory.setColor(Color.WHITE);


						addIcon(iconFactory, Data.name1.get(i), new LatLng(Lat[i],Lon[i]),""+i);

						/*
						MarkerOptions marker1 = new MarkerOptions().position(
								new LatLng(Lat[i],Lon[i]))
								.title(""+i)
								.snippet(Data.pcode.get(i));
						marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.gpin));

						googleMap.addMarker(marker1);
						*/
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(Lat[0],
										Lon[0])).zoom(14).build();

						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
					}
					System.out.println("Size====="+ Data.lat1.size());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position,String i) {
		MarkerOptions markerOptions = new MarkerOptions().
				icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
				position(position).
				snippet(i).
				anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

		googleMap.addMarker(markerOptions);
	}

	private CharSequence makeCharSequence() {
		String prefix = "Mixing ";
		String suffix = "different fonts";
		String sequence = prefix + suffix;
		SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
		ssb.setSpan(new StyleSpan(ITALIC), 0, prefix.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new StyleSpan(BOLD), prefix.length(), sequence.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}



	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
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
		//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
		player.stopPlayer();
		index=Integer.parseInt(arg0.getSnippet());
		code=arg0.getSnippet();
		directionUrl="https://maps.googleapis.com/maps/api/directions/json?origin="+Data.lat+",+"+Data.lon+"&destination="+Data.lat1.get(index)+",+"+Data.lon1.get(index)+"&sensor=false&mode=driving&alternatives=true&key=AIzaSyAh2tjcjLNp2FS4bmxMi0h-FXFvRUeXRho";
		new getDirection().execute();
		return true;
	}
	private void showProvider(){
		player.stopPlayer();
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.map_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
		TextView tv_provider=(TextView)dialog.findViewById(R.id.textView);
		TextView tv_add=(TextView)dialog.findViewById(R.id.textView2);
		TextView tv_address=(TextView)dialog.findViewById(R.id.textView3);
		TextView tv_con=(TextView)dialog.findViewById(R.id.textView4);
		TextView tv_contact=(TextView)dialog.findViewById(R.id.textView5);
		TextView tv_create=(TextView)dialog.findViewById(R.id.textView6);
		TextView tv_distance=(TextView)dialog.findViewById(R.id.tv_distance);
		TextView distances=(TextView)dialog.findViewById(R.id.distance);
		TextView tv_time=(TextView)dialog.findViewById(R.id.tv_time);
		TextView time=(TextView)dialog.findViewById(R.id.time);
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
		tv_distance.setTypeface(tf);
		tv_time.setTypeface(tf);
		distances.setTypeface(tf);
		time.setTypeface(tf);
		tv_provider.setText(Data.name1.get(index));
		tv_address.setText(Data.address.get(index));
		tv_contact.setText(Data.tel.get(index));
		distances.setText(""+distance);
		time.setText(""+duration);
		text=""+tv_provider.getText().toString()+" is "+distance+" away from your current location. It takes "+duration+" to travel to get there!";
		//updateResult(text);
		pname= Data.name1.get(index);
		paddress= Data.address.get(index);
		pcontact= Data.tel.get(index).replace("NULL", "0");
		prcode=code;
		dpname="";
		//et_provider.setText(""+Data.name1.get(index).toString());
		dialog.show();
		btn_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new SynthesisTask().cancel(true);
				player.stopPlayer();
				//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				Intent i=new Intent(getApplicationContext(),CreateLOA.class);
				i.putExtra("Provider_Name", Data.name1.get(index));
				i.putExtra("Provider_Address", Data.address.get(index));
				i.putExtra("Provider_Contact", Data.tel.get(index).replace("NULL", "0"));
				i.putExtra("Pcode", code);
				i.putExtra("Dname", " ");
				startActivity(i);
				dialog.dismiss();
			}
		});
		btn_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				player.stopPlayer();
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
	private void show(String text){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.instruction);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
		TextView instruction=(TextView)dialog.findViewById(R.id.instruction);
		instruction.setTypeface(tf);
		instruction.setText(text);
		dialog.show();
	}
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
		new SynthesisTask().execute(result);

	}
	private class SynthesisTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			System.out.println("CHECKING RESULT FOR TTS == "+params[0]);
			com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech service = new com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech();
			service.setUsernameAndPassword(Data.ttsUsername, Data.ttsPassword);
			List<Voice> voices = service.getVoices().execute();


			System.out.println(voices);
			player.playStream(service.synthesize(params[0],Voice.EN_MICHAEL).execute());
			return "Did syntesize";
		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetProvidersCity extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
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
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetProviders extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Data.providerList.clear();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
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
					MainActivity.this, Data.providerList,
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
			ServiceHandler sh = new ServiceHandler();// Making a request to url and getting response
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

						String access = c.getString(TAG_ACCESS);
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
					MainActivity.this, Data.providerList,
					R.layout.provider_list, new String[] { TAG_PNAME, TAG_CITY,
							TAG_TEL,TAG_PCODE, TAG_LAT, TAG_LONG }, new int[] { R.id.name,
							R.id.address, R.id.mobile, R.id.pcode, R.id.lat, R.id.lon });
			lv_provider.setAdapter(Provideradapter);
			lv_provider.setVisibility(View.VISIBLE);
			current.setVisibility(View.GONE);
		}

	}
	private class getDirection extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(lv_provider.getVisibility()==View.VISIBLE){
			lv_provider.setVisibility(View.GONE);
			current.setVisibility(View.VISIBLE);
		}
		else {
			Data.sprovider = null;
			Data.City = null;
			Intent i = new Intent(this, ChatBubbleActivity.class);
			startActivity(i);
			Data.lat1.clear();
			Data.lon1.clear();
			Data.name1.clear();
			Data.tel.clear();
			Data.address.clear();
			Data.pcode.clear();
			super.onBackPressed();
			finish();
		}
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
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);

		Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(convertToPixels(context, 11));

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		Canvas canvas = new Canvas(bm);

		//If the text is bigger than the canvas , reduce the font size
		if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
			paint.setTextSize(convertToPixels(context, 7));        //Scaling needs to be used for different dpi's

		//Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

		//"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

		canvas.drawText(text, xPos, yPos, paint);

		return  bm;
	}



	public static int convertToPixels(Context context, int nDP)
	{
		final float conversionScale = context.getResources().getDisplayMetrics().density;

		return (int) ((nDP * conversionScale) + 0.5f) ;

	}
}
