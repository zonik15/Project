package com.app.heyphil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ibm.watson.developer_cloud.dialog.v1.DialogService;
import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.mikhaellopez.circularimageview.CircularImageView;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatBubbleActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
	//Conversation
	private ListView mListView;
	private CircularImageView mButtonSpeak;
	private Button Send;
	private EditText mEditTextMessage;
	private ChatArrayAdapter mAdapter;
	//SpeechRecognition
	private final int 		SPEECH_RECOGNITION_CODE = 1;
	//IBM WATSON CREDENTIAL
	public static final String DIALOG_NAME = "phil-dialog";
	private DialogService DialService;
	private Conversation conversation;
	private String conversationResult="";
	private boolean watsonInited = false;
	private String dialogId= "";
	private List<com.ibm.watson.developer_cloud.dialog.v1.model.Dialog> dialogs;
	private String Tag = "WATSON";
	public Integer clientId;
	public List dialogList;
	//GoogleMap
	GoogleApiClient mGoogleApiClient;
	Location mLastLocation;
	//JSON Parser
	JSONArray jsonArray = null;
	//ProgressDialog
	static ProgressDialog  	pDialog;
	//CustomDialog
	private Dialog custom_dialog;
	//ArrayList
	ArrayList<HashMap<String, String>> DependentList= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> UtilizationList= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> DentalList= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> DoctorList= new ArrayList<HashMap<String, String>>();
	List<String> dentalList= new ArrayList<String>();
	//Adapter
	ListAdapter Doctoradapter;
	ListAdapter Dependentadapter;
	ListAdapter Utilizationradapter;
	ListAdapter Dentaladapter;
	ListAdapter LoaHistoryAdapter;
	//EditText
	private EditText et_dentist;
	private EditText et_doctor;
	//TextView
	private ListView lv_dentist;
	//String
	private String last_name;
	private String first_name;
	private String mi;
	private String sex;
	private String civil_status;
	private String birthday;
	private String home_address;
	private String home_number;
	private String mobile_number;
	private String certificate_number;
	private String member_type;
	private String agreement_name;
	private String agreement_no;
	private String effectivity_date;
	private String expiration_date;
	private String bldg_no;
	private String street;
	private String brgy;
	private String city;
	private String province;
	private String ben_pack;
	private String plan_type;
	private String room_desc;
	private String ben_limit;
	private String hospitals;
	private String pre_ex;
	private String philhealth;
	private String riders;
	private String room_rate;
	private String ape;
	private String dental;
	private String policyno;
	private String doctor_link;
	private String pro;
	private String add;
	private String dentisturl;
	private String provider_link;
	private String pef_value;
	private String urinalysis;
	private String ecg;
	private String xray;
	private String ultrasound;
	private String doa;
	private String pos;
	String neurology="headache migraine";
	String gastroenterology=" stomachache diarrhea";
	String pulmonology="pneumonia";
	String result;
	private String DependentNames;
	private String DpNames;
	//Boolean
	boolean stat;
	private boolean dStatus;
	private boolean	search;
	//Context
	private Context context=this;
	//Listview
	private ListView lv_doctor;
	//Button
	private Button	btn_search;
	//variables for parsing XML
	List<String>            list_xml_link           = new ArrayList<String>();
	List<String>            list_xml_data           = new ArrayList<String>();
	private String          successful_flag         = "";
	private int             maxBarValue;
	private int             total;
	private int             currentDownload         = 1;
	List<String>            list_xml_link2           = new ArrayList<String>();
	List<String>            list_xml_data2           = new ArrayList<String>();
	private String          successful_flag2         = "";
	private int             maxBarValue2;
	private int             total2;
	private int             currentDownload2         = 1;
	List<String>            list_xml_link3          = new ArrayList<String>();
	List<String>            list_xml_data3           = new ArrayList<String>();
	private String          successful_flag3         = "";
	private int             maxBarValue3;
	private int             total3;
	private int             currentDownload3         = 1;
	//Touchevent
	private float x1,x2;
	static final int MIN_DISTANCE = 150;

	private AudioTrack audioTrack;
	public TestAdapter adapter;
	public ArrayList<TestModel> feedItemList = new ArrayList<TestModel>();
	public ListView listView;
	public static final String APP_KEY = "1gd4a33d7e99d5a400be6a2a0e7381d531f555636bg0g2g39ea0ceb";

	private StreamPlayer player = new StreamPlayer();
	private TextToSpeech textService;
	int wd;
	int ht;
	int w, h;
	private MediaPlayer mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
		setContentView(R.layout.activity_chat);
		mediaPlayer = MediaPlayer.create(this, R.raw.result);
		textService = initTextToSpeechService();
		/*
		com.ibm.mqa.config.Configuration configuration = new com.ibm.mqa.config.Configuration.Builder(this)
				.withAPIKey(APP_KEY) //Provides the quality assurance application APP_KEY
				.withMode(MQA.Mode.QA) //Selects the quality assurance production mode.  This example is for preproduction mode,
				//Use .withMode(Mode.MARKET) for production mode.
				.withReportOnShakeEnabled(true) //Enables shake report trigger in preproduction mode. In production mode it has not effect.
				.build();
		MQA.startNewSession(this, configuration);
		*/
		System.out.print("ttsuser "+Data.ttsUsername);
		System.out.print("ttsuser "+Data.ttsPassword);
		context = this;
		new onMedInfoAsync().execute();
		new GetSpecialization().execute();
		new GetDependents().execute();
		DependentNames= Data.depnames;
		buildGoogleApiClient();
		mGoogleApiClient.connect();
		doctor_link ="https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo="+ Data.cert+"&Province=&Area=&DoctorName=&Specialization=";
		mListView = (ListView) findViewById(R.id.listView);
		mButtonSpeak = (CircularImageView) findViewById(R.id.btn_speak);
		mEditTextMessage = (EditText) findViewById(R.id.et_message);
		Send=(Button)findViewById(R.id.btn_send);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mAdapter = new ChatArrayAdapter(this, new ArrayList<ChatMessage>());
		mListView.setAdapter(mAdapter);


		mButtonSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Data.avatar="none";
				startSpeechToText();
			}
		});
		Send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message = mEditTextMessage.getText().toString().trim();
				if (TextUtils.isEmpty(message)) {
				}
				else {
					Data.avatar="none";
					sendMessage(message);
				}
			}
		});
		mEditTextMessage.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
				{
					String message = mEditTextMessage.getText().toString().trim();
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					if (TextUtils.isEmpty(message)) {
					}
					else {
						Data.avatar="none";
						sendMessage(message);
					}
				}
				return false;
			}
		});
		mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						x1 = event.getX();
						break;
					case MotionEvent.ACTION_UP:
						x2 = event.getX();
						float deltaX = x2 - x1;

						if (Math.abs(deltaX) > MIN_DISTANCE)
						{
							// Left to Right swipe action
							if (x2 > x1)
							{
								//Toast.makeText(getBaseContext(), "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
							}

							// Right to left swipe action
							else
							{
								//Toast.makeText(getBaseContext(), "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();

							}

						}
						else
						{
							// consider as something else - a screen tap for example
						}
						break;
				}
				return onTouchEvent(event);
			}
		});
		initialConversation();
		Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		ht=(int)Math.round(display.getHeight()*0.1);
		wd= (int) Math.round(display.getWidth()*0.2-(display.getWidth()*0.2-display.getHeight()*0.1));
		//h=Math.round(display.getHeight()*0.1));
		//w=Integer.parseInt(String.valueOf((display.getWidth()*0.2-(display.getWidth()*0.2-display.getHeight()*0.1))));
		LinearLayout llContainerMain = (LinearLayout) findViewById(R.id.llMainContainer);
		DragDropView dragDropView = new DragDropView(context);
		dragDropView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		CircularImageView ivTest = new CircularImageView(context);
		ivTest.setBorderWidth(2);
		ivTest.bringToFront();
		if(!Data.Bitmap && Data.sex.equals("FEMALE"))
		{
			System.out.println("GENDER"+ Data.sex+"AVATAR"+ Data.bitmap);
			ivTest.setImageDrawable(context.getResources().getDrawable(R.drawable.femele_icon));
			Data.Bitmap=false;
		}
		else if(!Data.Bitmap && Data.sex.equals("MALE"))
		{
			ivTest.setImageDrawable(context.getResources().getDrawable(R.drawable.male_icon));
			Data.Bitmap=false;
		}
		else
		{
			ivTest.setImageBitmap(Data.bitmap);
		}
		dragDropView.AddDraggableView(ivTest,10,display.getHeight()-330, ht, wd);
		llContainerMain.addView(dragDropView);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
			Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
			//resume tasks needing this permission
		}
	}
	private void sendMessage(String message) {
		//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
		ChatMessage chatMessage = new ChatMessage(message, true);
		mAdapter.add(chatMessage);
		HttpStuff httpStuff = new HttpStuff(ChatBubbleActivity.this);
		AsyncTask doasync= httpStuff.execute(mEditTextMessage.getText().toString());
		//mEditTextMessage.setText("");
	}

	private void mimicOtherMessage(String message) {
		ChatMessage chatMessage = new ChatMessage(message, false);
		mAdapter.add(chatMessage);
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	private void startSpeechToText() {


		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		Intent detailsIntent =  new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
		player.stopPlayer();
		sendOrderedBroadcast(
				detailsIntent, null, new LanguageDetailsChecker(), null, Activity.RESULT_OK, null, null);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Speak something...");
		startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
	}

	private void startSpeechToText2() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		//intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getAvailableLocales());
		//intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-PH");
		Intent detailsIntent =  new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
//		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
		sendOrderedBroadcast(
				detailsIntent, null, new LanguageDetailsChecker(), null, Activity.RESULT_OK, null, null);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Speak something...");
		try {
			System.out.println("AUDIO TRACK IS "+audioTrack);

			if(audioTrack !=null){
				System.out.println("TRYING TO STOP222");
				if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {

					System.out.println("TRYING TO STOP");
					audioTrack.pause();
					audioTrack.flush();
					Toast.makeText(getApplicationContext(),
							"I am still speaking.",
							Toast.LENGTH_SHORT).show();
				}
			}else{
				startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
			}
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Speech recognition is not supported in this device.",
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				x1 = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				x2 = event.getX();
				float deltaX = x2 - x1;

				if (Math.abs(deltaX) > MIN_DISTANCE)
				{
					// Left to Right swipe action
					if (x2 > x1)
					{
						//Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();

					}

					// Right to left swipe action
					else
					{
						LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

						//final PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.heyphil_hints_activity, null, false),
						//		ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
						final PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.heyphil_hints_activity, null, false),
								ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
						popupWindow.setAnimationStyle(Animation.START_ON_FIRST_FRAME);

						popupWindow.setFocusable(true);
						popupWindow.setOutsideTouchable(true);
						popupWindow.setTouchable(true);
						popupWindow.setBackgroundDrawable(new BitmapDrawable());
						final View pvu = popupWindow.getContentView();


						listView = (ListView) pvu.findViewById(R.id.list);
// Attach the adapter to a ListView
						//listView = (ListView) pvu.findViewById(R.id.list);
						//listView.setAdapter(adapter);

						//mRecyclerView = (RecyclerView) pvu.findViewById(R.id.list);
						final String url = "http://philcare.com.ph/api/heyphil/api.php/hints";
						new cardInfo().execute(url);
						popupWindow.setTouchInterceptor(new View.OnTouchListener()
						{
							@Override
							public boolean onTouch(View v, MotionEvent event)
							{

								switch(event.getAction())
								{
									case MotionEvent.ACTION_DOWN:
										x1 = event.getX();
										break;
									case MotionEvent.ACTION_UP:
										x2 = event.getX();
										float deltaX = x2 - x1;

										if (Math.abs(deltaX) > MIN_DISTANCE) {
											// Left to Right swipe action
											if (x2 > x1) {
												feedItemList.clear();
												Log.d("POPUP_WINDOW", "v: "+v.getTag() + " | event: "+event.getAction());
												popupWindow.dismiss(); return true;
											}

										}

								}
								return false;
							}
						});
						findViewById(R.id.heyphil).post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								popupWindow.showAtLocation(findViewById(R.id.heyphil), Gravity.CENTER, 0, 0);

							}
						});


						System.out.println("Downloading data from below url");
					}

				}
				else
				{
					// consider as something else - a screen tap for example
				}
				break;
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case SPEECH_RECOGNITION_CODE: {
				if (resultCode == RESULT_OK && null != data) {

					ArrayList<String> result = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					String text = result.get(0);
					mEditTextMessage.setText(text);
					sendMessage(text);

				}
				break;
			}

		}
	}
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	public class cardInfo extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Integer doInBackground(String... params) {
			InputStream inputStream = null;
			Integer result = 0;
			HttpURLConnection urlConnection = null;

			try {
                /* forming th java.net.URL object */
				URL url = new URL(params[0]);

				urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
				urlConnection.setRequestMethod("GET");

				int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
				if (statusCode == 200) {

					BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
						response.append(line);
					}
					System.out.println("RESPONSE FROM API "+ response.toString());
					parseResult(response.toString());
					result = 1; // Successful
				} else {
					result = 0; //"Failed to fetch data!";
				}

			} catch (Exception e) {
				Log.d("TAG", e.getLocalizedMessage());
			}

			return result; //"Failed to fetch data!";
		}

		@Override
		protected void onPostExecute(Integer result) {

			setProgressBarIndeterminateVisibility(false);

            /* Download complete. Lets update UI */
			if (result == 1) {

				adapter = new TestAdapter(getBaseContext(), feedItemList);

				listView.setAdapter(adapter);

				System.out.println("THIS IS FROM CHATBUBBLE "+feedItemList);
			} else {
				Log.e("TAG", "FFailed to fetch data!");
			}
		}
	}

	private void parseResult(String result) {
		JSONObject jobjectness;
		try {
			//JSONObject response = new JSONObject(result);
			JSONObject jobbb = new JSONObject(result);
			//wp_post content
			jobjectness = jobbb.getJSONObject("hints");

			JSONArray posts = jobjectness.optJSONArray("records");
			System.out.println("GETTING DATA FROM API >>>> "+ posts);
			System.out.println("GETTING DATA FROM API >>>> "+ posts.length());

            /*Initialize array if null*/
			if (null == feedItemList) {
				//feedItemList = new ArrayList<HeyPhilHintsModel>();
				feedItemList = new ArrayList<TestModel>();
			}
			System.out.println("Start of adding item >>> "+feedItemList);
			for (int i = 0; i < posts.length(); i++) {
				//JSONObject post = posts.optJSONObject(i);
				JSONArray post = new JSONArray(posts.get(i).toString());

//				HeyPhilHintsModel item = new HeyPhilHintsModel();
				TestModel item = new TestModel();
				System.out.println("THIS SHOLD BE HINT NAME "+post.getString(1));
//				item.setTitle(post.getString(2));
//				item.setThumbnail(post.getString(1));
//				feedItemList.add(item);

				item.setTitle(post.getString(1));
				item.setDescription(post.getString(2));
				feedItemList.add(item);
			}
			System.out.println("AFTER ADDING ITEM >>>> "+feedItemList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void initialConversation(){
		if (!watsonInited) {
			System.out.println("Starting calling watson...");
			startDialog();
			Data.avatar="none";
			watsonInited = true;
		}
	}
	private void startDialog(){
		System.out.println("CERTIFICATE NOT IS ============ "+ Data.cert);
		String heypilAPI = "http://philcare.com.ph/heyphil-api/api/dialogs/view.json?certificate_no="+ Data.cert+"&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImV4cCI6MTUwMTE2NDMxM30.eh3l3rK0jNDCr1-Pv0GpDj3kYMjtu9q_2M6XKSI00Bs";
		//HeyPhilApi heyPhil = new HeyPhilApi(ChatBubbleActivity.this);
		//AsyncTask callAPI = heyPhil.execute(heypilAPI);
		String url = "https://philcare.com.ph/api/heyphil/api.php/dialogs?filter=certificate_no,eq,"+ Data.cert;
		WebAPI api = new WebAPI(ChatBubbleActivity.this);
		api.setDialog(url);
	}
	public URI getHost(String url){
		try {
			return new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void updateResult1(final String result) {
		mediaPlayer.start();
		ChatBubbleActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ChatMessage chatMessage = new ChatMessage(result, false);
				mAdapter.add(chatMessage);
				mEditTextMessage.setText("");
			}
		});
	}
	public void updateResult(final String result)
	{

		if(result.length()<300) {
			new SynthesisTask().execute(result);
		}
		else{
			new SynthesisTask().execute("Here's what I found.");
		}

		ChatBubbleActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ChatMessage chatMessage = new ChatMessage(result, false);
				mAdapter.add(chatMessage);
				mEditTextMessage.setText("");
			}
		});

		/*
		String username = Data.ttsUsername;
		String password = Data.ttsPassword;
		String serviceURL = "https://stream.watsonplatform.net/text-to-speech/api";
		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().initWithContext(this.getHost(serviceURL));
		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().setCredentials(username, password);
		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().setVoice("en-US_MichaelVoice");
		ChatBubbleActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				System.out.println("==========result"+result);
				ChatMessage chatMessage = new ChatMessage(result, false);
				mAdapter.add(chatMessage);
				mEditTextMessage.setText("");
				//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().synthesize(result);
				//mAdapter.notifyDataSetChanged();
			}
		});
		*/
	}

	private class SynthesisTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			System.out.println("CHECKING RESULT FOR TTS == "+params[0]);
			TextToSpeech service = new TextToSpeech();
			service.setUsernameAndPassword(Data.ttsUsername, Data.ttsPassword);
			List<Voice> voices = service.getVoices().execute();


			System.out.println(voices);
			player.playStream(service.synthesize(params[0],Voice.EN_MICHAEL).execute());
			return "Did syntesize";
		}
	}

	private TextToSpeech initTextToSpeechService() {
		TextToSpeech service = new TextToSpeech();
		String username = Data.ttsUsername;
		String password = Data.ttsPassword;
		service.setUsernameAndPassword(username, password);
		return service;
	}

	public void searchDialog(final String res)
	{
		System.out.println("SEARCHING DIALOG" + res);
		DialService = new DialogService();
		DialService.setUsernameAndPassword(Data.dialogUsername, Data.dialogPassword);


		TextToSpeech service = new TextToSpeech();
		service.setUsernameAndPassword(Data.ttsUsername, Data.ttsPassword);

		ChatMessage chatMessage = new ChatMessage(Data.heyphilHasNoAnswer, false);
		mAdapter.add(chatMessage);
		player.playStream(service.synthesize(Data.heyphilHasNoAnswer,Voice.EN_MICHAEL).execute());


		/* REMOVING Dialog Service
		String username = Data.ttsUsername;
		String password = Data.ttsPassword;
		String serviceURL = "https://stream.watsonplatform.net/text-to-speech/api";
		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().initWithContext(this.getHost(serviceURL));
		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().setCredentials(username, password);
		com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().setVoice("en-US_MichaelVoice");
		ChatBubbleActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					//conversation = DialService.converse(conversation, results);
					Map params = new HashMap();
					System.out.println("DIALOG ID ========= " + Data.dialogID);
					System.out.println("CLIENT ID ========= " + Data.clientID);
					System.out.println("CONVERSATION ID ========= " + Data.conversationID);
					System.out.println("RESULT IS ============ " + res);
					params.put(DialService.DIALOG_ID, Data.dialogID);
					params.put(DialService.CLIENT_ID, Data.clientID);
					params.put(DialService.INPUT, res);
					params.put(DialService.CONVERSATION_ID, Data.conversationID);
					com.ibm.watson.developer_cloud.dialog.v1.model.Conversation conversation = DialService.converse(params);
					Log.d("TAG IS ", "Conversation Result(JSON String) = " + conversation.toString());
					for (int i = 0; i < conversation.getResponse().size(); i++) {
						if (!conversation.getResponse().get(i).equals("")) {
							System.out.println("FIRST RESPONSE IS  ====================== "+conversation.getResponse().get(i));
							ChatMessage chatMessage = new ChatMessage(conversation.getResponse().get(i), false);
							mAdapter.add(chatMessage);
							com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().synthesize(conversation.getResponse().get(i));
							break;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		*/

	}
	//Method
	////Method to be called for Response////
	public void Dependent(){
		new GetDependent().execute();
	}
	public void Utilization(){
		new GetUtilization().execute();
	}
	public void Products(){
		new GetProducts().execute();
		new GetPrices().execute();
	}
	public void Dental(){
		new GetDentalProviders().execute();
	}
	public void menu(){
		final Dialog dialog = new Dialog(getBaseContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.menu_layout);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		TextView myaccount=(TextView)dialog.findViewById(R.id.myaccount);
		myaccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyAccount();
			}
		});
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}
	public void MyAccount(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.my_account);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		final RelativeLayout rln=(RelativeLayout)dialog.findViewById(R.id.first);
		final RelativeLayout rlb=(RelativeLayout)dialog.findViewById(R.id.second);
		final LinearLayout lld=(LinearLayout)dialog.findViewById(R.id.ll_dependent);
		final RelativeLayout rledit=(RelativeLayout)dialog.findViewById(R.id.editinfo);
		TextView tv_lastname=(TextView)dialog.findViewById(R.id.tv_lastname);
		TextView lastname=(TextView)dialog.findViewById(R.id.lastname);
		TextView tv_firstname=(TextView)dialog.findViewById(R.id.tv_firstname);
		TextView firstname=(TextView)dialog.findViewById(R.id.firstname);
		TextView tv_middle=(TextView)dialog.findViewById(R.id.tv_middle);
		TextView middle=(TextView)dialog.findViewById(R.id.middle);
		TextView tv_gender=(TextView)dialog.findViewById(R.id.tv_gender);
		TextView gender=(TextView)dialog.findViewById(R.id.gender);
		TextView tv_civil=(TextView)dialog.findViewById(R.id.tv_civil);
		TextView civil=(TextView)dialog.findViewById(R.id.civil);
		TextView tv_bday=(TextView)dialog.findViewById(R.id.tv_bday);
		TextView bday=(TextView)dialog.findViewById(R.id.bday);
		TextView tv_dependents=(TextView)dialog.findViewById(R.id.tv_dependents);
		TextView dependents=(TextView)dialog.findViewById(R.id.dependents);
		TextView tv_homeaddress=(TextView)dialog.findViewById(R.id.tv_homeaddress);
		TextView homeaddress=(TextView)dialog.findViewById(R.id.homeaddress);
		TextView tv_mobile=(TextView)dialog.findViewById(R.id.tv_mobile);
		TextView mobile=(TextView)dialog.findViewById(R.id.mobile);
		TextView tv_certificate=(TextView)dialog.findViewById(R.id.tv_certicate);
		TextView certificate=(TextView)dialog.findViewById(R.id.certificate);
		TextView tv_membertype=(TextView)dialog.findViewById(R.id.tv_membertype);
		TextView membertype=(TextView)dialog.findViewById(R.id.membertype);
		TextView tv_agreement=(TextView)dialog.findViewById(R.id.tv_agreement);
		TextView agreement=(TextView)dialog.findViewById(R.id.agreement);
		TextView tv_effective=(TextView)dialog.findViewById(R.id.tv_effective);
		TextView effective=(TextView)dialog.findViewById(R.id.effective);
		TextView tv_expiration=(TextView)dialog.findViewById(R.id.tv_expiration);
		TextView expiration=(TextView)dialog.findViewById(R.id.expiration);
		TextView tv_back=(TextView)dialog.findViewById(R.id.tv_back);
		TextView tv_next=(TextView)dialog.findViewById(R.id.tv_next);
		TextView tv_edit=(TextView)dialog.findViewById(R.id.edit);
		tv_next.setTypeface(tf);
		tv_back.setTypeface(tf);
		tv_lastname.setTypeface(tf);
		lastname.setTypeface(tf);
		tv_firstname.setTypeface(tf);
		firstname.setTypeface(tf);
		tv_middle.setTypeface(tf);
		middle.setTypeface(tf);
		tv_gender.setTypeface(tf);
		gender.setTypeface(tf);
		tv_civil.setTypeface(tf);
		civil.setTypeface(tf);
		tv_bday.setTypeface(tf);
		bday.setTypeface(tf);
		tv_dependents.setTypeface(tf);
		dependents.setTypeface(tf);
		tv_homeaddress.setTypeface(tf);
		homeaddress.setTypeface(tf);
		tv_mobile.setTypeface(tf);
		mobile.setTypeface(tf);
		tv_certificate.setTypeface(tf);
		certificate.setTypeface(tf);
		tv_membertype.setTypeface(tf);
		membertype.setTypeface(tf);
		tv_agreement.setTypeface(tf);
		agreement.setTypeface(tf);
		tv_effective.setTypeface(tf);
		effective.setTypeface(tf);
		tv_expiration.setTypeface(tf);
		expiration.setTypeface(tf);
		rledit.setVisibility(View.GONE);
		tv_edit.setVisibility(View.GONE);
		tv_dependents.setVisibility(View.GONE);
		dependents.setVisibility(View.GONE);
		tv_next.setVisibility(View.GONE);
		tv_back.setVisibility(View.GONE);
		lastname.setText(Data.last_name);
		firstname.setText(Data.first_name);
		middle.setText(Data.mi);
		gender.setText(Data.sex);
		civil.setText(Data.civil_status);
		bday.setText(Data.birthday);
		//dependents.setText(Data.dpnames.replace("null",""));
		homeaddress.setText(Data.home_address);
		mobile.setText(Data.mobile_number.replaceAll("[-+.^:,]","").replaceAll("63 ","0"));
		certificate.setText(Data.cert);
		membertype.setText(Data.member_type);
		agreement.setText(Data.agreement_name);
		effective.setText(Data.effectivity_date);
		expiration.setText(Data.expiration_date);
		tv_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				rln.setVisibility(View.GONE);
				rlb.setVisibility(View.VISIBLE);
			}
		});
		tv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rln.setVisibility(View.VISIBLE);
				rlb.setVisibility(View.GONE);
			}
		});
		dialog.show();
	}
	public void MyDependent(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dependent);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_dependent=(TextView)dialog.findViewById(R.id.tv_dependents);
		TextView tv_empty=(TextView)dialog.findViewById(R.id.empty);
		ListView lv_dependent=(ListView)dialog.findViewById(R.id.lv_dependents);
		tv_dependent.setTypeface(tf);
		tv_empty.setTypeface(tf);
		if(DependentList.size()!=0){
			tv_empty.setVisibility(View.GONE);
		}
		else{
			lv_dependent.setVisibility(View.GONE);
			tv_empty.setVisibility(View.VISIBLE);
		}
		Dependentadapter = new SimpleAdapter(ChatBubbleActivity.this, DependentList,
				R.layout.my_dependent, new String[] { "name", "cert",
				"type"}, new int[] { R.id.tv_name,
				R.id.tv_cert, R.id.tv_type});
		lv_dependent.setAdapter(Dependentadapter);
		lv_dependent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String cert=((TextView) view.findViewById(R.id.tv_cert)).getText().toString();

			}
		});


		Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
		Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);
		dialog.show();
	}
	public void MyCardInformation(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.membership_card_information);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_originaleffectivedate=(TextView)dialog.findViewById(R.id.tv_originaleffectivedate);
		TextView originaleffectivedate=(TextView)dialog.findViewById(R.id.originaleffectivedate);
		TextView tv_preexisting=(TextView)dialog.findViewById(R.id.tv_preexisting);
		TextView preexisting=(TextView)dialog.findViewById(R.id.preexisting);
		TextView tv_opemergency=(TextView)dialog.findViewById(R.id.tv_opemergency);
		TextView opemergency=(TextView)dialog.findViewById(R.id.opemergency);
		TextView tv_oplimit=(TextView)dialog.findViewById(R.id.tv_oplimit);
		TextView oplimit=(TextView)dialog.findViewById(R.id.oplimit);
		TextView tv_opmed=(TextView)dialog.findViewById(R.id.tv_opmed);
		TextView opmed=(TextView)dialog.findViewById(R.id.opmed);
		TextView tv_hospital=(TextView)dialog.findViewById(R.id.tv_hospital);
		TextView hospital=(TextView)dialog.findViewById(R.id.hospital);
		TextView tv_philhealth=(TextView)dialog.findViewById(R.id.tv_philhealth);
		TextView philhealth=(TextView)dialog.findViewById(R.id.philhealth);
		tv_originaleffectivedate.setTypeface(tf);
		originaleffectivedate.setTypeface(tf);
		tv_preexisting.setTypeface(tf);
		preexisting.setTypeface(tf);
		tv_opemergency.setTypeface(tf);
		opemergency.setTypeface(tf);
		tv_oplimit.setTypeface(tf);
		oplimit.setTypeface(tf);
		tv_opmed.setTypeface(tf);
		opmed.setTypeface(tf);
		tv_hospital.setTypeface(tf);
		hospital.setTypeface(tf);
		tv_philhealth.setTypeface(tf);
		philhealth.setTypeface(tf);
		originaleffectivedate.setText(Data.moed);
		preexisting.setText(Data.pre_ex);
		//opemergency.setText(Data.pef_value);
		//oplimit.setText(Data.urinalysis);
		//opmed.setText(Data.ecg);
		hospital.setText(Data.hospitals);
		philhealth.setText("REQUIRED");
		dialog.show();
	}
	public void MyBenefits(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.my_benefits);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_benefitpackage=(TextView)dialog.findViewById(R.id.tv_benefitpackage);
		TextView benefitpackage=(TextView)dialog.findViewById(R.id.benefitpackage);
		TextView tv_plantype=(TextView)dialog.findViewById(R.id.tv_plantype);
		TextView plantype=(TextView)dialog.findViewById(R.id.plantype);
		TextView tv_roomlimit=(TextView)dialog.findViewById(R.id.tv_roomlimit);
		TextView roomlimit=(TextView)dialog.findViewById(R.id.roomlimit);
		TextView tv_roomtype=(TextView)dialog.findViewById(R.id.tv_roomtype);
		TextView roomtype=(TextView)dialog.findViewById(R.id.roomtype);
		TextView tv_hospital=(TextView)dialog.findViewById(R.id.tv_hospital);
		TextView hospital=(TextView)dialog.findViewById(R.id.hospital);
		TextView tv_benefitlimit=(TextView)dialog.findViewById(R.id.tv_benefitlimit);
		TextView benefitlimit=(TextView)dialog.findViewById(R.id.benefitlimit);
		TextView tv_preexisting=(TextView)dialog.findViewById(R.id.tv_preexisting);
		TextView preexisting=(TextView)dialog.findViewById(R.id.preexisting);
		TextView tv_philhealth=(TextView)dialog.findViewById(R.id.tv_philhealth);
		TextView philhealth=(TextView)dialog.findViewById(R.id.philhealth);
		TextView tv_additional=(TextView)dialog.findViewById(R.id.tv_additional);
		TextView additional=(TextView)dialog.findViewById(R.id.additional);
		tv_benefitpackage.setTypeface(tf);
		benefitpackage.setTypeface(tf);
		tv_plantype.setTypeface(tf);
		plantype.setTypeface(tf);
		tv_roomtype.setTypeface(tf);
		roomtype.setTypeface(tf);
		tv_roomlimit.setTypeface(tf);
		roomlimit.setTypeface(tf);
		tv_hospital.setTypeface(tf);
		hospital.setTypeface(tf);
		tv_benefitlimit.setTypeface(tf);
		benefitlimit.setTypeface(tf);
		tv_preexisting.setTypeface(tf);
		preexisting.setTypeface(tf);
		tv_philhealth.setTypeface(tf);
		philhealth.setTypeface(tf);
		tv_additional.setTypeface(tf);
		additional.setTypeface(tf);
		benefitpackage.setText(Data.ben_pack);
		plantype.setText(Data.plan_type);
		roomtype.setText(Data.room_desc);
		roomlimit.setText(Data.room_rate);
		hospital.setText(Data.hospitals);
		benefitlimit.setText(Data.ben_limit);
		preexisting.setText(Data.pre_ex);
		philhealth.setText(Data.philhealth);
		additional.setText(Data.dental);
		dialog.show();
	}
	public void MyUtilization()
	{
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_utilization);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_utilization=(TextView)dialog.findViewById(R.id.tv_utilization);
		TextView tv_empty=(TextView)dialog.findViewById(R.id.empty);
		ListView lv_utilzation=(ListView)dialog.findViewById(R.id.lv_utilization);
		tv_utilization.setTypeface(tf);
		tv_empty.setTypeface(tf);
		if(UtilizationList.size()!=0)
		{
			tv_empty.setVisibility(View.GONE);
		}
		else {
			lv_utilzation.setVisibility(View.GONE);
			tv_empty.setVisibility(View.VISIBLE);
		}
		Utilizationradapter = new SimpleAdapter(ChatBubbleActivity.this, UtilizationList,
				R.layout.my_utilization, new String[] { "provider", "caseno",
				"dateavailed","datecreated", "illness", "nature"}, new int[] { R.id.tv_provider,
				R.id.tv_caseno, R.id.tv_dateavailed, R.id.tv_datecreated, R.id.tv_illness, R.id.tv_nature });
		lv_utilzation.setAdapter(Utilizationradapter);

		Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
		Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);
		dialog.show();

	}
	public void MyMedicalinfo(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.my_medicalinfo);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_dateofavailment=(TextView)dialog.findViewById(R.id.tv_dateofavailment);
		TextView dateofavailment=(TextView)dialog.findViewById(R.id.dateofavailment);
		TextView tv_placeofservice=(TextView)dialog.findViewById(R.id.tv_placeofservice);
		TextView placeofservice=(TextView)dialog.findViewById(R.id.placeofservice);
		TextView tv_aperesult=(TextView)dialog.findViewById(R.id.tv_aperesult);
		TextView aperesult=(TextView)dialog.findViewById(R.id.aperesult);
		TextView tv_urinalysis=(TextView)dialog.findViewById(R.id.tv_urinalysis);
		TextView urinalysis=(TextView)dialog.findViewById(R.id.urinalysis);
		TextView tv_ecg=(TextView)dialog.findViewById(R.id.tv_ecg);
		TextView ecg=(TextView)dialog.findViewById(R.id.ecg);
		TextView tv_xray=(TextView)dialog.findViewById(R.id.tv_xray);
		TextView xray=(TextView)dialog.findViewById(R.id.xray);
		TextView tv_ultrasound=(TextView)dialog.findViewById(R.id.tv_ultrasound);
		TextView ultrasound=(TextView)dialog.findViewById(R.id.ultrasound);
		tv_dateofavailment.setTypeface(tf);
		dateofavailment.setTypeface(tf);
		tv_placeofservice.setTypeface(tf);
		placeofservice.setTypeface(tf);
		tv_aperesult.setTypeface(tf);
		aperesult.setTypeface(tf);
		tv_urinalysis.setTypeface(tf);
		urinalysis.setTypeface(tf);
		tv_ecg.setTypeface(tf);
		ecg.setTypeface(tf);
		tv_xray.setTypeface(tf);
		xray.setTypeface(tf);
		tv_ultrasound.setTypeface(tf);
		ultrasound.setTypeface(tf);
		dateofavailment.setText(Data.doa);
		placeofservice.setText(Data.pos);
		aperesult.setText(Data.pef_value);
		urinalysis.setText(Data.urinalysis);
		ecg.setText(Data.ecg);
		xray.setText(Data.xray);
		ultrasound.setText(Data.ultrasound);
		dialog.show();
	}
	public void showAllDoctors()
	{
		custom_dialog = new Dialog(this, R.style.CustomDialogTheme);
		custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		custom_dialog.setContentView(R.layout.custom_doctor);
		custom_dialog.setCancelable(true);

		custom_dialog.show();

		Window window = custom_dialog.getWindow();
		window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		TextView tv_doctor=(TextView)custom_dialog.findViewById(R.id.tv_doctor);
		et_doctor=(EditText)custom_dialog.findViewById(R.id.et_doctor);
		btn_search=(Button)custom_dialog.findViewById(R.id.btn_search);
		lv_doctor=(ListView)custom_dialog.findViewById(R.id.lv_Doctor);
		et_doctor.setTypeface(tf);
		tv_doctor.setTypeface(tf);
		btn_search.setTypeface(tf);
		Doctoradapter = new SimpleAdapter(
				ChatBubbleActivity.this, DoctorList,
				R.layout.doctor_list, new String[] { "DoctorName", "ProviderName",
				"Specialization","Telephone", "ProviderCode", "BillingAddress","DoctorCode", "Latitude", "Longitude"}, new int[] { R.id.tv_name,
				R.id.tv_provider, R.id.tv_specialization, R.id.tv_mobile, R.id.pcode, R.id.tv_address, R.id.dcode, R.id.lat, R.id.lon });
		lv_doctor.setAdapter(Doctoradapter);
		btn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_doctor.setVisibility(View.GONE);
				int i=0;
				do {
					if(et_doctor.getText().toString().toLowerCase().contains(Data.specializationList.get(i).toString().toLowerCase())) {

						doctor_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo=" + Data.cert + "&Province=&Area=&DoctorName=&Specialization="+ Data.specializationList.get(i).toString();
						search=true;
						DoctorList.clear();
						new GetDoctor().execute();
						lv_doctor.setVisibility(View.GONE);
						i= Data.specializationList.size();
					}
					else
					{
						i++;
						if(i== Data.specializationList.size()){

							doctor_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo=" + Data.cert + "&Province=&Area=&DoctorName=" + et_doctor.getText().toString() + "&Specialization=";
							search=true;
							DoctorList.clear();
							lv_doctor.setVisibility(View.GONE);
							new GetDoctor().execute();
						}
					}
				}
				while (i< Data.specializationList.size());
			}
		});
		et_doctor.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				((Filterable) Doctoradapter).getFilter().filter(s);
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
		lv_doctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String name=((TextView) view.findViewById(R.id.tv_name))
						.getText().toString();
				final String provider=((TextView) view.findViewById(R.id.tv_provider))
						.getText().toString();
				final String ADD=((TextView) view.findViewById(R.id.tv_address))
						.getText().toString();
				String special=((TextView) view.findViewById(R.id.tv_specialization))
						.getText().toString();
				final String code=((TextView) view.findViewById(R.id.pcode))
						.getText().toString();
				String LAT=((TextView) view.findViewById(R.id.lat))
						.getText().toString().replace("NULL", "1");
				String LON=((TextView) view.findViewById(R.id.lon))
						.getText().toString().replace("NULL", "1");
				String dcode=((TextView) view.findViewById(R.id.dcode))
						.getText().toString();
				System.out.println("LATITUDE =============== "+LAT);
				System.out.println("LONGITUDE =============== "+LON);
				Data.doctorLat = Double.parseDouble(LAT);
				Data.doctorLon = Double.parseDouble(LON);

				Data.doctorAddress = ADD;
				Data.doctorSpecialization = special;
				Data.doctorName = name;
				Data.doctorPcode = code;
				Data.doctorProvider = provider;
				Data.doctorCode=dcode;
				//lv_doctor.setVisibility(View.GONE);
				AlertDialog.Builder dialog = new AlertDialog.Builder(ChatBubbleActivity.this);
				dialog.setTitle("Proceed to");
				dialog.setPositiveButton("Create LOA", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Data.cloa = true;
						Intent i=new Intent(getApplicationContext(),CreateLOA.class);
						i.putExtra("Provider_Name", provider);
						i.putExtra("Provider_Address",ADD);
						i.putExtra("Provider_Contact", "");
						i.putExtra("Pcode", code);
						i.putExtra("Dname",name);
						startActivity(i);
					}
				});
				dialog.setNegativeButton("View Location", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						callMap();
					}
				});
				dialog.show();
				// TODO Auto-generated method stub


			}
		});
		Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
		Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);

	}
	public void showDental()
	{
		custom_dialog = new Dialog(this, R.style.CustomDialogTheme);
		custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		custom_dialog.setContentView(R.layout.custom_dialog);
		custom_dialog.setCancelable(true);

		custom_dialog.show();

		Window window = custom_dialog.getWindow();
		window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		TextView tv_dental=(TextView)custom_dialog.findViewById(R.id.tv_dentist);
		et_dentist=(EditText)custom_dialog.findViewById(R.id.et_dentist);
		lv_dentist=(ListView)custom_dialog.findViewById(R.id.lv_dentist);
		lv_dentist.setAdapter(Dentaladapter);
		tv_dental.setTypeface(tf);
		et_dentist.setTypeface(tf);
		et_dentist.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				((Filterable) Dentaladapter).getFilter().filter(s);
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
		lv_dentist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				pro=((TextView) view.findViewById(R.id.name))
						.getText().toString();
				add=((TextView) view.findViewById(R.id.specialization))
						.getText().toString();
				String code=((TextView) view.findViewById(R.id.pcode))
						.getText().toString();
				dentisturl="https://apps.philcare.com.ph/IPhilCareWSTest/Provider.svc/DentalProviderDoctorsSchedule/?ClinicCode="+code;
				new GetDentist().execute();
				System.out.println("===="+dentisturl);
			}
		});
		Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
		Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);

	}
	public void Specialization()
	{
		custom_dialog = new Dialog(this, R.style.CustomDialogTheme);
		custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		custom_dialog.setContentView(R.layout.custom_dialog);
		custom_dialog.setCancelable(true);

		custom_dialog.show();

		Window window = custom_dialog.getWindow();
		window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		TextView tv_dental=(TextView)custom_dialog.findViewById(R.id.tv_dentist);
		et_dentist=(EditText)custom_dialog.findViewById(R.id.et_dentist);
		lv_dentist=(ListView)custom_dialog.findViewById(R.id.lv_dentist);
		lv_dentist.setAdapter(Dentaladapter);
		tv_dental.setTypeface(tf);
		et_dentist.setTypeface(tf);
		et_dentist.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				((Filterable) Dentaladapter).getFilter().filter(s);
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
		lv_dentist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				pro=((TextView) view.findViewById(R.id.name))
						.getText().toString();
				add=((TextView) view.findViewById(R.id.specialization))
						.getText().toString();
				String code=((TextView) view.findViewById(R.id.pcode))
						.getText().toString();
				dentisturl="https://apps.philcare.com.ph/IPhilCareWSTest/Provider.svc/DentalProviderDoctorsSchedule/?ClinicCode="+code;
				new GetDentist().execute();
				System.out.println("===="+dentisturl);
			}
		});
		Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
		Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);

	}
	public void callMap(){
		Data.isDoctor = true;
		Intent i= new Intent(this, MapActivity.class);
		startActivity(i);
	}
	public void callMain(){
		Intent i= new Intent(this, MainActivity.class);
		startActivity(i);
	}

	public void callProduct(){
		Intent i= new Intent(this, ProductList.class);
		startActivity(i);
	}
	public void byArea(){
		stat=true;
		Data.lat1.clear();
		Data.lon1.clear();
		Data.name1.clear();
		Data.tel.clear();
		Data.address.clear();
		Data.pcode.clear();
		if(mEditTextMessage.getText().toString().toLowerCase().contains("luzon")){
			provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location=&area=LUZON&Certno="+ Data.cert;
			new GetProviders().execute();
		}
		else if(mEditTextMessage.getText().toString().toLowerCase().contains("visayas")){
			provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location=&area=VISAYAS&Certno="+ Data.cert;
			new GetProviders().execute();
		}
		else if(mEditTextMessage.getText().toString().toLowerCase().contains("mindanao")){
			provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location=&area=MINDANAO&Certno="+ Data.cert;
			new GetProviders().execute();
		}
		else{

		}

	}
	public void Loahistory(){
		custom_dialog = new Dialog(this, R.style.CustomDialogTheme);
		custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		custom_dialog.setContentView(R.layout.custom_loahistory);
		custom_dialog.setCancelable(true);

		custom_dialog.show();

		Window window = custom_dialog.getWindow();
		window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		TextView tv_loahistory=(TextView)custom_dialog.findViewById(R.id.tv_loahistory);
		ListView lv_loahistory=(ListView)custom_dialog.findViewById(R.id.lv_loahistory);
		Button btn_none=(Button)custom_dialog.findViewById(R.id.none);
		tv_loahistory.setTypeface(tf);
		btn_none.setTypeface(tf);
		LoaHistoryAdapter = new SimpleAdapter(ChatBubbleActivity.this, Data.Loahistory,
				R.layout.my_loahistory, new String[] { "provider", "loa",
				"dateavailed"}, new int[] { R.id.tv_provider,
				R.id.tv_loano, R.id.tv_dateavailed});
		lv_loahistory.setAdapter(LoaHistoryAdapter);
		btn_none.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byComplaint();
				custom_dialog.dismiss();
			}
		});
		Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
		Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);
	}
	public void byLOA1(){
		if(mEditTextMessage.getText().toString().toLowerCase().contains("LOA")||mEditTextMessage.getText().toString().toLowerCase().contains("loa")||mEditTextMessage.getText().toString().toLowerCase().contains("letter of authorization")){
			String text="What do you feel right now, can you please specify your chief complaint regarding with your health issue?";
			ChatMessage chatMessage = new ChatMessage(text, false);
			mAdapter.add(chatMessage);
			Data.Complaint="none";
		}
		else {
			if(Data.Loahistory.toString()!="[]") {
				String text="You may check first if you have already existing LOA regarding with your chief complaint.";
				ChatMessage chatMessage = new ChatMessage(text, false);
				mAdapter.add(chatMessage);
				Data.Complaint = mEditTextMessage.getText().toString();
				Loahistory();
			}
			else {
				byComplaint();
				Data.Complaint = mEditTextMessage.getText().toString();
			}
		}
	}
	public void byLOA(){
		if(mEditTextMessage.getText().toString().toLowerCase().contains("LOA")||mEditTextMessage.getText().toString().toLowerCase().contains("loa")||mEditTextMessage.getText().toString().toLowerCase().contains("letter of authorization")){
			String text="What do you feel right now, can you please specify your chief complaint regarding with your health issue?";
			ChatMessage chatMessage = new ChatMessage(text, false);
			mAdapter.add(chatMessage);
		}
		else {
			byComplaint();
			Data.Complaint=mEditTextMessage.getText().toString();
		}
	}

	public void byComplaint(){
		if(neurology.contains(mEditTextMessage.getText().toString().toLowerCase())){
			result="NEUROLOGY";
			byCity();
		}
		else if(gastroenterology.contains(mEditTextMessage.getText().toString().toLowerCase())){
			result="GASTROENTEROLOGY";
			byCity();
		}
		else if(pulmonology.contains(mEditTextMessage.getText().toString().toLowerCase())){
			result="PULMONARY";
			byCity();
		}
		else{
			result="";
			bySpecialization();
		}
	}
	public void byHealth(){
		if(mEditTextMessage.getText().toString().toLowerCase().contains("ache")&&mEditTextMessage.getText().toString().toLowerCase().contains("sick")){
			Doctor();
		}
		else {
			Doctor();
		}
	}
	public void Doctor(){
		if(mEditTextMessage.getText().toString().toLowerCase().contains("doctor")){
			dStatus=true;
			bySpecialization();

		}
		else {
			bySpecialization();
		}

	}
	public void bySpecialization(){
		int i=0;
		do{
			if(mEditTextMessage.getText().toString().toLowerCase().contains(Data.specializationList.get(i).toLowerCase().substring(0, Data.specializationList.get(i).toLowerCase().replace("medicine","").length()-3))){
				//chatArrayAdapter.add(new ChatMessage(true, Data.specializationList.get(i)));
				result= Data.specializationList.get(i);
				dStatus=false;
				byCity();
				i= Data.specializationList.size();
			}
			else{
				System.out.println(Data.specializationList.get(i).toLowerCase().substring(0, Data.specializationList.get(i).toLowerCase().length()-3));
				System.out.println(i);
				i++;
				if(i== Data.specializationList.size()){
					result = "";
					byCity();
				}
			}
		}
		while(i< Data.specializationList.size());
	}
	public void byCity(){
		int ii=0;
		do{
			if(mEditTextMessage.getText().toString().toLowerCase().contains(Data.mylist.get(ii).toLowerCase())){
				Data.loc=   Data.mylist.get(ii);
				System.out.println("resultCITy--------"+Data.loc);
				/* provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+ Data.loc+"&area=&Certno="+ Data.cert;
				new onLoginAsync().execute();
				 Intent n=new Intent(getApplicationContext(),MainActivity.class);
			     startActivity(n);
				 i=Data.mylist.size();*/
				if(!result.trim().isEmpty()&&result!=null && Data.loc!=null&&!Data.loc.isEmpty()) {
					System.out.println("result=========="+(!result.trim().isEmpty()&&result!=null && Data.loc!=null&&!Data.loc.isEmpty()));
					updateResult("Select Doctor under "+result+" in "+ Data.loc+" to locate in google map.");
					load();
					result = "";
				}
				else if(Data.loc!=null&&!Data.loc.isEmpty()) {
					updateResult("Select Doctor in "+ Data.loc+" to locate in google map.");
					loadD();

				}
				else if(dStatus==false){
					Data.lat1.clear();
					Data.lon1.clear();
					Data.name1.clear();
					Data.tel.clear();
					Data.address.clear();
					Data.pcode.clear();
					stat = true;
					Data.City= Data.loc;
					updateResult("Select Health Provider in " + Data.loc + " to locate in google map.");
					new GetProviders().execute();
				}

				ii= Data.mylist.size();
			}
			else{
				System.out.println(Data.mylist.get(ii));
				System.out.println(ii);
				ii++;
				if(ii== Data.mylist.size()&&dStatus==false)
				{
					Data.loc="Select Location";
					System.out.print("replce"+ Data.loc.replace("Select Location","Sample"));
					updateResult("Select Doctor under "+result+" to locate in google map.");
					load();

				}
			}
		}
		while(ii< Data.mylist.size());
	}
	public void load(){
		DoctorList.clear();
		Data.City= Data.loc;
		if(Data.loc.equals("Select Location")){
			Data.loc="";
			doctor_link ="https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo="+ Data.cert+"&Province=&Area=&DoctorName=&Specialization="+result.replaceAll(" ","+").replaceAll("[&]","%26");
			dStatus=true;
			new GetDoctor().execute();
		}
		/*else if(Data.loc.contains("Makati")||Data.loc.contains("CITY")){
			doctor_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo=" + Data.cert + "&Province=&Area=" + Data.loc.replaceAll(" ","+").replaceAll("CITY","")+"City&DoctorName=&Specialization=" + result.replaceAll(" ","+");
			dStatus = true;
			Data.loc = "";
			new GetDoctor().execute();
		}*/
		else {
			doctor_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo=" + Data.cert + "&Province=&Area=" + Data.loc.replaceAll(" ","+")+ "&DoctorName=&Specialization=" + result.replaceAll(" ","+").replaceAll("[&]","%26");;
			dStatus = true;
			Data.loc = "";
			new GetDoctor().execute();
		}
	}
	public void loadD(){
		DoctorList.clear();
		Data.City= Data.loc;
		/*if(Data.loc.contains("Makati")||Data.loc.contains("CITY")) {
			doctor_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo=" + Data.cert + "&Province=&Area=" + Data.loc.replaceAll("CITY","").replaceAll(" ","+") + "City&DoctorName=&Specialization=";
			dStatus = true;
			Data.loc = "";
			new GetDoctor().execute();
		}
		else
		{*/
			doctor_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?CertNo=" + Data.cert + "&Province=&Area=" + Data.loc.replaceAll(" ","+")+"&DoctorName=&Specialization=";
			dStatus = true;
			Data.loc = "";
			new GetDoctor().execute();
		//}
	}

	public void byCities(){
		System.out.println("GET TEXT FROM CITY "+mEditTextMessage.getText().toString());
		int ii=0;
		do{
			if(mEditTextMessage.getText().toString().toLowerCase().contains(Data.mylist.get(ii).toLowerCase())){
				Data.loc= Data.mylist.get(ii);
				Data.City= Data.mylist.get(ii);
				provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+ Data.loc.replaceAll(" ","+")+"&area=&Certno="+ Data.cert;
				/* new onLoginAsync().execute();
				 Intent n=new Intent(getApplicationContext(),MainActivity.class);
			     startActivity(n);
				 i=Data.mylist.size();*/
				Data.lat1.clear();
				Data.lon1.clear();
				Data.name1.clear();
				Data.tel.clear();
				Data.address.clear();
				Data.pcode.clear();
				stat=true;
				new GetProviders().execute();
				ii= Data.mylist.size();
			}
			else{
				System.out.println(Data.mylist.get(ii));
				System.out.println(ii);
				ii++;
				if(ii== Data.mylist.size()){
					Data.City= Data.currentCity;
					provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+ Data.currentCity.replaceAll(" ","+")+"&area=&Certno="+ Data.cert;
					Data.lat1.clear();
					Data.lon1.clear();
					Data.name1.clear();
					Data.tel.clear();
					Data.address.clear();
					Data.pcode.clear();
					stat=true;
					new GetProviders().execute();
				}

			}
		}
		while(ii< Data.mylist.size());
		mEditTextMessage.setText("");
	}
	public void bmi(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bmi_layout);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();

	}
	public void calories(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.calorie_layout);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();

	}
	@Override
	public void onConnected(Bundle bundle) { if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
			|| ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);

		if (mLastLocation != null)
		{
			Data.lat=mLastLocation.getLatitude();
			Data.lon=mLastLocation.getLongitude();
			System.out.println("LONGLAT====="+ Data.lat+""+ Data.lon);
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());

			try {
				addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
				// Here 1 represent max location result to returned, by documents it recommended 1 to 5

				//String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
				Data.currentCity = addresses.get(0).getLocality();
				System.out.println("Current"+Data.currentCity);
				provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+Data.currentCity.replaceAll(" ","+")+"&area=&Certno="+ Data.cert;
				new GetProviders().execute();
				new GetCity().execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
			}
		}
		else{
			Data.currentCity =Data.province;
			System.out.println("Current"+Data.currentCity);
			provider_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Providers.svc/HospitalsPerMember/?Location="+Data.currentCity.replaceAll(" ","+")+"&area=&Certno="+ Data.cert;
			new GetProviders().execute();
			new GetCity().execute();
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
	Toast.makeText(getBaseContext(),"hello",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	}
	private class GetSpecialization extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchSpecialization/", ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("SearchSpecializationResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						String specialization = c.getString("Specialization");
						Data.specializationList.add(specialization);
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
		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetCity extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(Data.getCity) {
				Data.mylist.add(Data.currentCity);
			}
			// Showing progress dialog
			pDialog = new ProgressDialog(ChatBubbleActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

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
			if (pDialog.isShowing())
				pDialog.dismiss();
			System.out.println("City====="+ Data.mylist.toString());
		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetDependent extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			DependentList.clear();
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ChatBubbleActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchFamily/?CertNo="+ Data.cert, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("SearchFamilyResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> dependent = new HashMap<String, String>();
						JSONObject c = jsonArray.getJSONObject(i);
						String certNo = c.getString("CertNo");
						String name = c.getString("Name");
						String type = c.getString("Type");
						dependent.put("cert", certNo);
						dependent.put("name", name);
						dependent.put("type", type);
						DependentList.add(dependent);
					}
					Data.depnames=(DependentNames);
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
			if (pDialog.isShowing())
				pDialog.dismiss();
			MyDependent();
		}
	}
	private class GetDependents extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			Data.dependent.clear();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchFamily/?CertNo="+ Data.cert, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("SearchFamilyResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						String name = c.getString("Name");
						Data.dependent.add(name);
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
			System.out.println("SEX"+ Data.sex);
		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetUtilization extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/GetUtilization/?PolicyNo=pc00400-005&CertNo="+ Data.cert, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("GetUtilizationResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> dependent = new HashMap<String, String>();
						JSONObject c = jsonArray.getJSONObject(i);
						String case_no = c.getString("CaseNo");
						String date_availed = c.getString("DateAvailed");
						String date_created= c.getString("DateCreated");
						String illness= c.getString("Illness");
						String nature=c.getString("Nature");
						String provider=c.getString("Provider");
						String status=c.getString("Status");
						HashMap<String, String> utilization = new HashMap<String, String>();
						utilization.put("caseno", case_no);
						utilization.put("dateavailed", date_availed);
						utilization.put("datecreated", date_created);
						utilization.put("illness", illness);
						utilization.put("nature", nature);
						utilization.put("provider", provider);
						utilization.put("status", status);
						UtilizationList.add(utilization);
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
			MyUtilization();
		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetDentalProviders extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ChatBubbleActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			String Providerurl="https://apps.philcare.com.ph/IPhilCareWSTest/Provider.svc/DentalProviderClinics/?State=&Region=&Province=&Area=";
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Providerurl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("DentalProviderClinicsResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);

						String address = c.getString("Address");
						String code = c.getString("ClinicCode");
						String contact = c.getString("ContactNumber");
						String district = c.getString("DistCity");
						String provider = c.getString("ProviderName");
						String region = c.getString("Region");
						String state = c.getString("State");

						// tmp hashmap for single contact
						HashMap<String, String> dental = new HashMap<String, String>();

						// adding each child node to HashMap key => value

						dental.put("ProviderName", provider);
						dental.put("Address", address +"\n"+region);
						dental.put("ContactNumber", contact);
						dental.put("ClinicCode", code);
						DentalList.add(dental);
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
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			Dentaladapter = new SimpleAdapter(
					ChatBubbleActivity.this, DentalList,
					R.layout.list_item, new String[] { "ProviderName", "Address",
					"ContactNumber","ClinicCode" }, new int[] { R.id.name,
					R.id.specialization, R.id.mobile, R.id.pcode });
			showDental();
		}

	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetDentist extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ChatBubbleActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(dentisturl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);

					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("DentalProviderDoctorsScheduleResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);
						String fname = c.getString("FirstName");
						String lname=c.getString("LastName");
						String Schedule= c.getString("Schedule");
						dentalList.add(fname+" "+lname+"\nSchedule: "+Schedule+"\n");
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
			if (pDialog.isShowing())
				pDialog.dismiss();

			Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert_dialog);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			TextView tv_provider=(TextView)dialog.findViewById(R.id.textView);
			TextView tv_add=(TextView)dialog.findViewById(R.id.textView2);
			TextView tv_address=(TextView)dialog.findViewById(R.id.textView3);
			TextView tv_doc=(TextView)dialog.findViewById(R.id.textView4);
			TextView tv_doctor=(TextView)dialog.findViewById(R.id.textView5);
			Button btn_close=(Button)dialog.findViewById(R.id.button);
			ImageView exit=(ImageView)dialog.findViewById(R.id.exit);
			btn_close.setTypeface(tf);
			tv_provider.setTypeface(tf);
			tv_add.setTypeface(tf);
			tv_address.setTypeface(tf);
			tv_doc.setTypeface(tf);
			tv_doctor.setTypeface(tf);
			tv_provider.setText(pro);
			tv_address.setText(add);
			tv_doctor.setText(dentalList.toString().replaceAll("\\[", "").replaceAll("\\]","").replace(",",""));
			dialog.show();
			btn_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					dentalList.clear();
				}
			});
			exit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					dentalList.clear();
				}
			});

		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetDentalBenefits extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ChatBubbleActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			String dentalurl="https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/Dental/?Certno="+ Data.cert;
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(dentalurl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("GetDentalResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);

						String assignment = c.getString("Assignment");
						String attendingPhysician = c.getString("AttendingPhysician");
						String claimNo = c.getString("ClaimNo");
						String covered = c.getString("Covered");
						String dateAvailed = c.getString("DateAvailed");
						String dateDischarge= c.getString("DateDischarge");
						String dentalCode = c.getString("DentalCode");
						String diagnosis = c.getString("Diagnosis");
						String providerName = c.getString("ProviderName");

						// tmp hashmap for single contact
						HashMap<String, String> dentalbenefit = new HashMap<String, String>();
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
			if (pDialog.isShowing())
				pDialog.dismiss();
		}

	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetProviders extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			if(stat) {
				progressDialog(context, "Please wait...");
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(provider_link, ServiceHandler.GET);

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
			if(stat){
				stat=false;
				closeProgressDialog();
				Intent n = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(n);
				finish();
			}
		}

	}
	public static void progressDialog(Context c, String msg)
	{
		pDialog = ProgressDialog.show(c, "", msg, true);
		pDialog.show();
	}
	public static void closeProgressDialog()
	{
		pDialog.dismiss();

	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetDoctor extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			if(search) {
				pDialog = new ProgressDialog(ChatBubbleActivity.this);
				pDialog.setMessage("Please wait...");
				pDialog.setCancelable(false);
				pDialog.show();
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(doctor_link, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);
			Log.d("Response: ", "> " + doctor_link);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					// Getting JSON Array node
					jsonArray = jsonObj.getJSONArray("SearchDoctorsResult");

					// looping through All Contacts
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject c = jsonArray.getJSONObject(i);

						String BillingAddress = c.getString("BillingAddress");
						String DocCode = c.getString("DoctorCode");
						String DocName = c.getString("DoctorName");
						String Latitude = c.getString("Latitude");
						String Longitude = c.getString("Longitude");
						String ProviderCode = c.getString("ProviderCode");
						String ProviderName = c.getString("ProviderName");
						String Specialization= c.getString("Specialization");
						String Telephone = c.getString("Telephone");
						HashMap<String, String> doctor = new HashMap<String, String>();
						doctor.put("BillingAddress", BillingAddress);
						doctor.put("DoctorCode", DocCode);
						doctor.put("DoctorName", DocName);
						doctor.put("Latitude", Latitude);
						doctor.put("Longitude", Longitude);
						doctor.put("ProviderCode", ProviderCode);
						doctor.put("ProviderName", ProviderName);
						doctor.put("Specialization", Specialization);
						doctor.put("Telephone", Telephone);
						DoctorList.add(doctor);
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
			if(dStatus) {
				showAllDoctors();
				dStatus=false;
			}
			else if(search){
				pDialog.dismiss();
				lv_doctor.setVisibility(View.VISIBLE);
				Doctoradapter = new SimpleAdapter(
						ChatBubbleActivity.this, DoctorList,
						R.layout.doctor_list, new String[] { "DoctorName", "ProviderName",
						"Specialization","Telephone", "ProviderCode", "BillingAddress","DoctorCode", "Latitude", "Longitude"}, new int[] { R.id.tv_name,
						R.id.tv_provider, R.id.tv_specialization, R.id.tv_mobile, R.id.pcode, R.id.tv_address, R.id.dcode, R.id.lat, R.id.lon });
				lv_doctor.setAdapter(Doctoradapter);
				search=false;
			}
		}

	}
	private void getXMLMedInfo()
	{
		String medinfo_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Members.svc/MedicalInfo/?Certno="+ Data.cert;
		list_xml_link3.clear();
		System.out.println("==========url"+convertToUrlMedInfo(medinfo_link));
		list_xml_link3.add(convertToUrlMedInfo(medinfo_link));
	}
	public static String convertToUrlMedInfo(String urlStr)
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
	private class onMedInfoAsync extends AsyncTask<String, String, String>
	{
		protected void onPreExecute()
		{
			getXMLMedInfo();
		}


		protected String doInBackground(String... params)
		{

			String status = null;

			try
			{
				status =getXMLMedInfo(list_xml_link3.get(0));

				if (status.equals("error"))
				{
					status = "error";
				}
				else
				{
					status = getXMLValueMedInfo(status);
					total3++;
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
				new onGetAllXMLDataMedInfo().execute();
			}
			else if (result.equals("error"))
			{
				successful_flag3 = "False";

			}
		}
	}
	/**
	 * Get XML data.
	 *
	 * @paramsevice
	 *              - URL on string format
	 * @return String - return XML value
	 *
	 * @response String - returned if error occur
	 */
	public static String getXMLMedInfo(String service)
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
		}

		return result;
	}
	// get All XML Data
	private class onGetAllXMLDataMedInfo extends AsyncTask<String, String, String>
	{
		int list_xml_link_total;


		protected void onPreExecute()
		{
			maxBarValue3 = list_xml_link3.size();
		}


		protected String doInBackground(String... params)
		{
			list_xml_link_total = list_xml_link3.size();
			String s;
			for (int i = currentDownload3; i < list_xml_link_total; i++)
			{
				s = getXMLMedInfo(list_xml_link3.get(i));
				if (s.equals("error"))
				{
					currentDownload3 = i;
					return "false";
				}
				else
				{
					list_xml_data3.add(s);
					total3++;

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
				successful_flag3 = "False";

			}

		}
	}
	private String getXMLValueMedInfo(String xml) throws XmlPullParserException, IOException
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
				if (xpp.getName().equals("DateOfAvailment") || xpp.getName().equals("a:DateOfAvailment"))
				{
					if (xpp.next() == XmlPullParser.TEXT) doa = xpp.getText();
					Data.doa=doa;
					System.out.println("==========date"+doa);
				}
				else if (xpp.getName().equals("PlaceOfService") || xpp.getName().equals("a:PlaceOfService"))
				{
					if (xpp.next() == XmlPullParser.TEXT) pos = xpp.getText();
					Data.pos=pos;
				}
				else if (xpp.getName().equals("APEResult") || xpp.getName().equals("a:APEResult"))
				{
					if (xpp.next() == XmlPullParser.TEXT) pef_value = xpp.getText();
					Data.pef_value=pef_value;
				}
				else if (xpp.getName().equals("Urinalysis") || xpp.getName().equals("a:Urinalysis"))
				{
					if (xpp.next() == XmlPullParser.TEXT) urinalysis = xpp.getText();
					Data.urinalysis=urinalysis;
				}
				else if (xpp.getName().equals("ECG") || xpp.getName().equals("a:ECG"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ecg = xpp.getText();
					Data.ecg=ecg;
				}
				else if (xpp.getName().equals("XRay") || xpp.getName().equals("a:XRay"))
				{
					if (xpp.next() == XmlPullParser.TEXT) xray = xpp.getText();
					Data.xray=xray;
				}
				else if (xpp.getName().equals("UltraSound") || xpp.getName().equals("a:UltraSound"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ultrasound = xpp.getText();
					Data.ultrasound=ultrasound;
				}
			}
			eventType = xpp.next();
		}


		return successful_flag3;

	}
	private class GetProducts extends AsyncTask<Void, Void, Void> {

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
			String Producturl="http://philcare.com.ph/api/api.php/wp_posts?filter=post_type,eq,product";
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Producturl, ServiceHandler.GET);
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				JSONObject jobjectness;
				try {

					////whole response
					JSONObject jobbb = new JSONObject(jsonStr);
					//wp_post content
					jobjectness = jobbb.getJSONObject("wp_posts");
					//records content
					JSONArray jarr2 = jobjectness.getJSONArray("records");
					for (int i = 0; i < jarr2.length(); i++) {
						///reco
						JSONArray jarr3 = new JSONArray(jarr2.get(i).toString());
						if(jarr3.get(7).equals("publish")) {
							System.out.println("============inside parser123" + jarr3.get(7));
							System.out.println("============inside parser123" + jarr3.get(5));
							Data.productList.add("" + jarr3.get(5));
							Data.productID.add("" + jarr3.get(0));
							Data.productContent.add("" + jarr3.get(4));
						}
					}
				} catch (JSONException e) {

					e.printStackTrace();

				}
			}
			else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			callProduct();
		}

	}
	class GetPrices extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
		/*	pDialog = new ProgressDialog(CreateLOA.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
	*/		Data.PriceList.clear();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			String Producturl="http://philcare.com.ph/api/api.php/wp_postmeta?filter=meta_key,eq,_regular_price";
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Producturl, ServiceHandler.GET);
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				JSONObject jobjectness;
				try {

					////whole response
					JSONObject jobbb = new JSONObject(jsonStr);
					//wp_post content
					jobjectness = jobbb.getJSONObject("wp_postmeta");
					//records content
					JSONArray jarr2 = jobjectness.getJSONArray("records");
					for (int i = 0; i < jarr2.length(); i++) {
						HashMap<String, String> priceList = new HashMap<String, String>();
						JSONArray jarr3 = new JSONArray(jarr2.get(i).toString());
						System.out.println("============inside parser123"+jarr3.get(3));
						priceList.put(""+jarr3.get(1),""+jarr3.get(3));
						Data.PriceList.put(""+jarr3.get(1),""+jarr3.get(3));
					}
				} catch (JSONException e) {

					e.printStackTrace();

				}
			}
			else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			System.out.println("============inside parser123"+ Data.PriceList);
		}
	}
	public void logout(){
		Data.logout=true;
		Data.logstat=true;
		Intent i= new Intent(this, LoginScreen.class);
		startActivity(i);
		finish();
	}
	private void getXMLMemberinfo()
	{
		String mem_link = "https://apps.philcare.com.ph/PhilcareWatsonTest/Members.svc/MembersInfo/?CertNo="+ Data.cert;
		list_xml_link2.clear();
		System.out.println("==========url"+convertToUrlMember(mem_link));
		list_xml_link2.add(convertToUrlMember(mem_link));
	}
	public static String convertToUrlMember(String urlStr)
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
	private class onMemberAsync extends AsyncTask<String, String, String>
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
				status =getXMLMember(list_xml_link2.get(0));

				if (status.equals("error"))
				{
					status = "error";
				}
				else
				{
					status = getXMLValueMember(status);
					total2++;
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
				new onGetAllXMLDataMember().execute();
			}
			else if (result.equals("error"))
			{
				successful_flag2 = "False";

			}
		}
	}
	 /* *//**//**//**//**
	 * Get XML data.
	 *
	 * @param// sevice
	 *              - URL on string format
	 * @return String - return XML value
	 *
	 * @response String - returned if error occur
	 *//**//**//**//**/
	public static String getXMLMember(String service)
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
	private class onGetAllXMLDataMember extends AsyncTask<String, String, String>
	{
		int list_xml_link_total;


		protected void onPreExecute()
		{
			maxBarValue2 = list_xml_link2.size();
		}


		protected String doInBackground(String... params)
		{
			list_xml_link_total = list_xml_link2.size();
			String s;
			for (int i = currentDownload2; i < list_xml_link_total; i++)
			{
				s = getXMLMember(list_xml_link2.get(i));
				if (s.equals("error"))
				{
					currentDownload2 = i;
					return "false";
				}
				else
				{
					list_xml_data2.add(s);
					total2++;

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
				successful_flag2 = "False";

			}
		}
	}
	private String getXMLValueMember(String xml) throws XmlPullParserException, IOException
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
				if (xpp.getName().equals("APE") || xpp.getName().equals("a:APE"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ape = xpp.getText();
					Data.ape=ape;
				}
				else if (xpp.getName().equals("Address") || xpp.getName().equals("a:Address"))
				{
					if (xpp.next() == XmlPullParser.TEXT) home_address = xpp.getText();
					Data.home_address=home_address;
				}
				else if (xpp.getName().equals("AgreementName") || xpp.getName().equals("a:AgreementName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) agreement_name = xpp.getText();
					Data.agreement_name=agreement_name;
				}
				else if (xpp.getName().equals("AgreementNo") || xpp.getName().equals("a:AgreementNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) agreement_no = xpp.getText();
					Data.agreement_no=agreement_no;
				}
				else if (xpp.getName().equals("Barangay") || xpp.getName().equals("a:Barangay"))
				{
					if (xpp.next() == XmlPullParser.TEXT) brgy = xpp.getText();
					Data.brgy=brgy;
				}
				else if (xpp.getName().equals("BenefitLimit") || xpp.getName().equals("a:BenefitLimit"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ben_limit = xpp.getText();
					Data.ben_limit=ben_limit;
				}
				else if (xpp.getName().equals("BirthDate") || xpp.getName().equals("a:BirthDate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) birthday = xpp.getText();
					Data.birthday=birthday;
				}
				else if (xpp.getName().equals("CertNo") || xpp.getName().equals("a:CertNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) certificate_number = xpp.getText();
					Data.certificate_number=certificate_number;
				}
				else if (xpp.getName().equals("City") || xpp.getName().equals("a:City"))
				{
					if (xpp.next() == XmlPullParser.TEXT) city = xpp.getText();
					Data.city=city;
				}
				else if (xpp.getName().equals("CivilStat") || xpp.getName().equals("a:CivilStat"))
				{
					if (xpp.next() == XmlPullParser.TEXT) civil_status = xpp.getText();
					Data.civil_status=civil_status;
				}
				else if (xpp.getName().equals("ContactNumber") || xpp.getName().equals("a:ContactNumber"))
				{
					if (xpp.next() == XmlPullParser.TEXT) mobile_number = xpp.getText();
					Data.mobile_number=mobile_number;
				}
				else if (xpp.getName().equals("Dental") || xpp.getName().equals("a:Dental"))
				{
					if (xpp.next() == XmlPullParser.TEXT) dental = xpp.getText();
					Data.dental=dental;
				}
				else if (xpp.getName().equals("EffectiveDate") || xpp.getName().equals("a:EffectiveDate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) effectivity_date = xpp.getText();
					Data.effectivity_date=effectivity_date;
				}
				else if (xpp.getName().equals("ExpiryDate") || xpp.getName().equals("a:ExpiryDate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) expiration_date = xpp.getText();
					Data.expiration_date=expiration_date;
				}
				else if (xpp.getName().equals("FirstName") || xpp.getName().equals("a:FirstName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) first_name = xpp.getText();
					Data.first_name=first_name;
				}
				else if (xpp.getName().equals("HomeNo") || xpp.getName().equals("a:HomeNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) home_number = xpp.getText();
				}
				else if (xpp.getName().equals("Hospitals") || xpp.getName().equals("a:Hospitals"))
				{
					if (xpp.next() == XmlPullParser.TEXT) hospitals = xpp.getText();
					Data.hospitals=hospitals;
				}
				else if (xpp.getName().equals("HouseNo") || xpp.getName().equals("a:HouseNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) bldg_no = xpp.getText();
					Data.bldg_no=bldg_no;
				}
				else if (xpp.getName().equals("LastName") || xpp.getName().equals("a:LastName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) last_name = xpp.getText();
					Data.last_name=last_name;
				}
				else if (xpp.getName().equals("MemberType") || xpp.getName().equals("a:MemberType"))
				{
					if (xpp.next() == XmlPullParser.TEXT) member_type = xpp.getText();
					Data.member_type=member_type;

				}
				else if (xpp.getName().equals("MiddleName") || xpp.getName().equals("a:MiddleName"))
				{
					if (xpp.next() == XmlPullParser.TEXT) mi = xpp.getText();
					Data.mi=mi;
				}
				else if (xpp.getName().equals("PackageDescription") || xpp.getName().equals("a:PackageDescription"))
				{
					if (xpp.next() == XmlPullParser.TEXT) ben_pack = xpp.getText();
					Data.ben_pack=ben_pack;
				}
				else if (xpp.getName().equals("PhilHealth") || xpp.getName().equals("a:PhilHealth"))
				{
					if (xpp.next() == XmlPullParser.TEXT) philhealth = xpp.getText();
					Data.philhealth=philhealth;
				}
				else if (xpp.getName().equals("PlanType") || xpp.getName().equals("a:PlanType"))
				{
					if (xpp.next() == XmlPullParser.TEXT) plan_type = xpp.getText();
					Data.plan_type=plan_type;
				}
				else if (xpp.getName().equals("PolicyNo") || xpp.getName().equals("a:PolicyNo"))
				{
					if (xpp.next() == XmlPullParser.TEXT) policyno = xpp.getText();
					Data.policyno=policyno;
				}
				else if (xpp.getName().equals("PreEx") || xpp.getName().equals("a:PreEx"))
				{
					if (xpp.next() == XmlPullParser.TEXT) pre_ex = xpp.getText();
					Data.pre_ex=pre_ex;
				}
				else if (xpp.getName().equals("Province") || xpp.getName().equals("a:Province"))
				{
					if (xpp.next() == XmlPullParser.TEXT) province = xpp.getText();
					Data.province=province;
				}
				else if (xpp.getName().equals("Riders") || xpp.getName().equals("a:Riders"))
				{
					if (xpp.next() == XmlPullParser.TEXT) riders = xpp.getText();
					Data.riders=riders;
				}
				else if (xpp.getName().equals("RoomDescription") || xpp.getName().equals("a:RoomDescription"))
				{
					if (xpp.next() == XmlPullParser.TEXT) room_desc = xpp.getText();
					Data.room_desc=room_desc;
				}
				else if (xpp.getName().equals("RoomRate") || xpp.getName().equals("a:RoomRate"))
				{
					if (xpp.next() == XmlPullParser.TEXT) room_rate = xpp.getText();
					Data.room_rate=room_rate;
					System.out.println("Room-----"+room_rate);
				}
				else if (xpp.getName().equals("Sex") || xpp.getName().equals("a:Sex"))
				{
					if (xpp.next() == XmlPullParser.TEXT) sex = xpp.getText();
					Data.sex=sex;
				}
				else if (xpp.getName().equals("Street") || xpp.getName().equals("a:Street"))
				{
					if (xpp.next() == XmlPullParser.TEXT) street = xpp.getText();
					Data.street=street;
				}
			}
			eventType = xpp.next();
		}
		return successful_flag2;

	}

}