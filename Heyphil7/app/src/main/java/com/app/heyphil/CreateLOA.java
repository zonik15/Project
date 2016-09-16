package com.app.heyphil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CreateLOA extends Activity{
	private TextToSpeech tts;
	String text;

	private TextView provider_name;
	private TextView provider_add;
	private TextView provider_contact;
	private TextView emptyView;
	private String pcode;
	private String dcode;
	private TextView cdoctor;
	private TextView ccomplaint;

	private EditText doctor;
	private EditText complaint;
	private ListView lv_doctor;
	private ListView lv_complaint;
	private Button cancel;
	private Button submit;
	ArrayAdapter<String>adapter=null;
	ListAdapter Doctoradapter;
	ListAdapter Complaintadapter;

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String Providerurl;
	private static String LOAurl;
	private static String LOAurlData;
	private static String Complainturl="https://apps.philcare.com.ph/PhilcareNFCv2Test/Search.svc/SearchChiefComplaints/";

	// JSON Node names (provider)
	private static final String TAG_DOCTOR = "SearchDoctorsPerHospitalResult";
	private static final String TAG_SCHEDULE = "ClinicSchedule";
	private static final String TAG_CODE = "DoctorCode";
	private static final String TAG_NAME = "DoctorName";
	private static final String TAG_ROOM = "RM";
	private static final String TAG_SECRETARY = "Secretary";
	private static final String TAG_SPECIALIZATION = "Specialization";
	private static final String TAG_TEL = "Telephone";
	// JSON Node names (LOA)
	private static final String TAG_GAR = "GenerateAuthorizationNoResult";
	private static final String TAG_CASENO = "CaseNo";
	private static final String TAG_DATEAVAILED = "DateAvailed";
	private static final String TAG_LOA = "LOANo";
	private static final String TAG_MESSAGE = "Message";
	private static final String TAG_PROVIDERNAME = "ProviderName";
	private static final String TAG_SERVICETYPE = "ServiceType";
	// JSON Node names (complaint)
	private static final String TAG_COMPLAINT = "SearchChiefComplaintsResult";
	private static final String TAG_COMPLAINTS = "Complaints";

	// contacts JSONArray
	JSONArray contacts = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> doctorList;
	ArrayList<HashMap<String, String>> complaintsList;
	String Dname;
	String message;
	AlertDialog Dialog;
	private StreamPlayer player = new StreamPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
		setContentView(R.layout.create_loa);
		/*LayoutInflater inflater = getLayoutInflater();
		View alertLayout = inflater.inflate(R.layout.instruction, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("My Dialog");
		builder.setMessage("Check out the transition!");
		builder.setView(alertLayout);
		Dialog = builder.create();
		Dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
		Dialog.show();*/
		new GetComplaints().execute();
		Bundle extra=getIntent().getExtras();
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		doctorList = new ArrayList<HashMap<String, String>>();
		complaintsList = new ArrayList<HashMap<String, String>>();
		provider_name=(TextView)findViewById(R.id.providername);
		provider_add=(TextView)findViewById(R.id.provideradd);
		provider_contact=(TextView)findViewById(R.id.providercontact);
		cdoctor=(TextView)findViewById(R.id.tv_doctor);
		ccomplaint=(TextView)findViewById(R.id.tv_complaint);
		doctor=(EditText)findViewById(R.id.et_doctor);
		complaint=(EditText)findViewById(R.id.et_complaint);
		lv_doctor=(ListView)findViewById(R.id.lv_doctor);
		lv_complaint=(ListView)findViewById(R.id.lv_complaint);
		cancel=(Button)findViewById(R.id.btncancel);
		submit=(Button)findViewById(R.id.btnsubmit);
		provider_name.setText(extra.getString("Provider_Name"));
		provider_add.setText(extra.getString("Provider_Address"));
		provider_contact.setText(extra.getString("Provider_Contact"));
		pcode=extra.getString("Pcode");
		Dname=extra.getString("Dname");
		provider_name.setTypeface(tf);
		doctor.setTypeface(tf);
		complaint.setTypeface(tf);
		cdoctor.setTypeface(tf);
		ccomplaint.setTypeface(tf);
		cancel.setTypeface(tf);
		submit.setTypeface(tf);
		if(Data.cloa){
			complaint.setText(Data.Complaint);
			doctor.setText(Data.doctorName);
			dcode= Data.doctorCode;
			Data.cloa=false;
			Providerurl= "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctorsPerHospital/?ProviderCode="+pcode+"&DoctorName="+Dname.substring(0,Dname.indexOf(" "))+"&Specialization";
			System.out.println("++++++++"+Providerurl);
			new GetDoctors().execute();
		}
		else {
			Providerurl= "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctorsPerHospital/?ProviderCode="+pcode+"&DoctorName="+Dname.substring(0,Dname.indexOf(" "))+"&Specialization";
			System.out.println("++++++++"+Providerurl);
			new GetDoctors().execute();
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
		doctor.setOnFocusChangeListener(new OnFocusChangeListener() {

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus && complaint.getText().toString()!=null&& !complaint.getText().toString().isEmpty()){
			lv_doctor.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.GONE);
			submit.setVisibility(View.GONE);
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
		else{
			Toast.makeText(getApplicationContext(),"Please specify your chief complaint first!",Toast.LENGTH_SHORT).show();
		}
		
		}});
		doctor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!complaint.getText().toString().isEmpty()){
					lv_doctor.setVisibility(View.VISIBLE);
					System.out.println("doctor"+doctorList.toString());
					if(doctorList.isEmpty()) {
					}
					cancel.setVisibility(View.GONE);
					submit.setVisibility(View.GONE);
					player.stopPlayer();
					//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				}
				else{
					Toast.makeText(getApplicationContext(),"Please specify your chief complaint first!",Toast.LENGTH_SHORT).show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.stopPlayer();
				//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				finish();
			}
		});
		lv_doctor.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				return false;
			}
		});
		lv_complaint.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				return false;
			}
		});
	lv_doctor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				dcode=((TextView) view.findViewById(R.id.pcode))
                        .getText().toString();
				doctor.setText(((TextView) view.findViewById(R.id.name))
                        .getText().toString());
				lv_doctor.setVisibility(View.GONE);
				cancel.setVisibility(View.VISIBLE);
				submit.setVisibility(View.VISIBLE);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				player.stopPlayer();
				//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				text="Please verify or check if your indicated physician and chief complaint is correct! You may now click submit to process your LOA as soon as possible. ";
				updateResult(text);
			}
		});
		lv_doctor.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String phone=((TextView) view.findViewById(R.id.mobile))
                        .getText().toString();
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+phone));
				startActivity(intent); 
				return false;
			}
		});
		doctor.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				try {
					((Filterable) Doctoradapter).getFilter().filter(s);
				}
				catch(Exception e){

				}
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
		complaint.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					text = "Select or Type your Chief Complaint regarding with Health problems.";
					updateResult(text);
				}
			}
		});
		complaint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lv_complaint.setVisibility(View.VISIBLE);
				cdoctor.setVisibility(View.GONE);
				doctor.setVisibility(View.GONE);
				lv_doctor.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				submit.setVisibility(View.GONE);
			}
		});
		complaint.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId== EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_NEXT){
					lv_complaint.setVisibility(View.GONE);
					cdoctor.setVisibility(View.VISIBLE);
					cancel.setVisibility(View.VISIBLE);
					submit.setVisibility(View.VISIBLE);
					doctor.setVisibility(View.VISIBLE);
					doctor.setFocusable(true);
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					//https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctorsPerHospital/?ProviderCode="+pcode+"&DoctorName="+Dname.substring(0,Dname.indexOf(" "))+"&Specialization
					player.stopPlayer();
					//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
					text="Type name of Doctor or Specialization registered in "+provider_name.getText().toString()+" and select your refering Physician.";
					if(doctor.getText().toString().isEmpty()||doctor.getText().toString()=="null"){
					updateResult(text);
					}
				}
				return false;
			}
		});
		complaint.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				try {
					((Filterable) Complaintadapter).getFilter().filter(s);
				}
				catch (Exception e){

				}
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
		lv_complaint.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				complaint.setText(((TextView) view.findViewById(R.id.lb_complaint))
                        .getText().toString());
				lv_complaint.setVisibility(View.GONE);
				cdoctor.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				submit.setVisibility(View.VISIBLE);
				doctor.setVisibility(View.VISIBLE);
				doctor.setFocusable(true);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				/*https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctorsPerHospital/?ProviderCode="+pcode+"&DoctorName="+Dname.substring(0,Dname.indexOf(" "))+"&Specialization
				Providerurl= "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctorsPerHospital/?ProviderCode="+pcode+"&DoctorName="+Dname.substring(0,Dname.indexOf(" "))+"&Specialization";
				System.out.println("++++++++"+Providerurl);
				new GetDoctors().execute();*/
				player.stopPlayer();
				//com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
				text="Type name of Doctor or Specialization registered in "+provider_name.getText().toString()+" and select your refering Physician.";
				if(doctor.getText().toString().isEmpty()||doctor.getText().toString()=="null"){
					updateResult(text);
				}

			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Data.ProviderName=provider_name.getText().toString();
				Data.DoctorName=doctor.getText().toString();
				LOAurl= "https://apps.philcare.com.ph/PhilcareWatsonTest/AuthorizationNo.svc/GenerateAuthorizationNo/?CertNo="+ Data.cert+"&ProviderCode="+pcode+"&ADoctorCode="+dcode+"&RDoctorCode="+dcode+"&ChiefComplaint="+complaint.getText().toString().replace(" ", "+");
				System.out.println("++++++++"+LOAurl);
				new GetLOA().execute();
				
			}
		});
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
			player.playStream(service.synthesize(params[0],Voice.EN_MICHAEL).execute());
			return "Did syntesize";
		}
	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetDoctors extends AsyncTask<Void, Void, Void> {

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

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Providerurl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					contacts = jsonObj.getJSONArray(TAG_DOCTOR);

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String sched = c.getString(TAG_SCHEDULE);
						String code = c.getString(TAG_CODE);
						String name = c.getString(TAG_NAME);
						String room = c.getString(TAG_ROOM);
						String secretary = c.getString(TAG_SECRETARY);
						String special = c.getString(TAG_SPECIALIZATION);
						String tel = c.getString(TAG_TEL);

						// tmp hashmap for single contact
						HashMap<String, String> doctor = new HashMap<String, String>();

						// adding each child node to HashMap key => value
							doctor.put(TAG_NAME, name);
							doctor.put(TAG_SPECIALIZATION, special);
							doctor.put(TAG_TEL, tel);
							doctor.put(TAG_CODE, code);
							doctorList.add(doctor);
						
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
			Doctoradapter = new SimpleAdapter(
					CreateLOA.this, doctorList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_SPECIALIZATION,
							TAG_TEL,TAG_CODE }, new int[] { R.id.name,
							R.id.specialization, R.id.mobile, R.id.pcode });
			lv_doctor.setAdapter(Doctoradapter);
			((BaseAdapter) Doctoradapter).notifyDataSetChanged();
		}

	}
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetComplaints extends AsyncTask<Void, Void, Void> {

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

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Complainturl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					contacts = jsonObj.getJSONArray(TAG_COMPLAINT);

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String complaints = c.getString(TAG_COMPLAINTS);
						HashMap<String, String> complaint= new HashMap<String, String>();

						// adding each child node to HashMap key => value
							complaint.put(TAG_COMPLAINTS, complaints);
							complaintsList.add(complaint);
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
			Complaintadapter = new SimpleAdapter(
					CreateLOA.this, complaintsList,
					R.layout.single_layout, new String[] {TAG_COMPLAINTS}, new int[] {R.id.lb_complaint});
			lv_complaint.setAdapter(Complaintadapter);
		}

	}
	private class GetLOA extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CreateLOA.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(LOAurl, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					contacts = jsonObj.getJSONArray(TAG_GAR);

					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String caseno = c.getString(TAG_CASENO);
						String dateavailed = c.getString(TAG_DATEAVAILED);
						String loa = c.getString(TAG_LOA);
						message = c.getString(TAG_MESSAGE);
						String providername = c.getString(TAG_PROVIDERNAME);
						String servicetype = c.getString(TAG_SERVICETYPE);
						Data.LOA=loa;
						Data.caseno=caseno;
						Data.Date=dateavailed;
						System.out.println("++++++++"+caseno);
						HashMap<String, String> generatedloa = new HashMap<String, String>();
						generatedloa.put("dateavailed", dateavailed);
						generatedloa.put("loa", loa);
						generatedloa.put("provider", providername);
						Data.Loahistory.add(generatedloa);

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
			if (pDialog.isShowing()){
				pDialog.dismiss();
			}
			AlertDialog.Builder dialog = new AlertDialog.Builder(CreateLOA.this);
			dialog.setTitle("Generate LOA");
			dialog.setMessage(message);
			dialog.setPositiveButton("View", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent i=new Intent(getApplicationContext(),PdfCreator.class);
					startActivity(i);
					finish();
				}
			});
			dialog.setNegativeButton("Download", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent i=new Intent(getApplicationContext(),Pdf_Download.class);
					startActivity(i);
					finish();
				}
			});
			/*dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});*/
			dialog.show();
	 	}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		player.stopPlayer();
		// /com.heyphilv2.speech.text_to_speech.v1.TextToSpeech.sharedInstance().tryStop();
		finish();
	}
}
