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
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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

import com.cete.dynamicpdf.Document;
import com.cete.dynamicpdf.Font;
import com.cete.dynamicpdf.Page;
import com.cete.dynamicpdf.PageOrientation;
import com.cete.dynamicpdf.PageSize;
import com.cete.dynamicpdf.TextAlign;
import com.cete.dynamicpdf.pageelements.Label;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CreateLOA extends Activity {
	private TextToSpeech tts;
	String text;

	private TextView provider_name;
	private TextView provider_add;
	private TextView provider_contact;
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
	private static String Complainturl="https://apps.philcare.com.ph/PhilcareNFCv2Test/Search.svc/SearchChiefComplaints/";

	// JSON Node names (provider)
	private static final String TAG_DOCTOR = "SearchDoctorsResult";
	private static final String TAG_SCHEDULE = "ClinicSchedule";
	private static final String TAG_CODE = "DoctorCode";
	private static final String TAG_NAME = "DoctorName";
	private static final String TAG_ROOM = "Room";
	private static final String TAG_SECRETARY = "Secretary";
	private static final String TAG_SPECIALIZATION = "Specialization";
	private static final String TAG_TEL = "TelphoneNo";
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
	private static String FILE = Environment.getExternalStorageDirectory()
			+ "/HelloWorld.pdf";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_loa);
		// Create a document and set it's properties
		Document objDocument = new Document();
		objDocument.setCreator("DynamicPDFHelloWorld.java");
		objDocument.setAuthor("Your Name");
		objDocument.setTitle("Hello World");

		// Create a page to add to the document
		Page objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT,
				54.0f);

		// Create a Label to add to the page
		String strText = "Hello World...\nFrom DynamicPDF™ Generator "
				+ "for Java\nDynamicPDF.com";
		Label objLabel = new Label(strText, 0, 0, 504, 100,
				Font.getHelvetica(), 18, TextAlign.CENTER);

		// Add label to page
		objPage.getElements().add(objLabel);

		// Add page to document
		objDocument.getPages().add(objPage);

		try {
			// Outputs the document to file
			objDocument.draw(FILE);
			Toast.makeText(this, "File has been written to :" + FILE,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this,
					"Error, unable to write to file\n" + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		Bundle extra=getIntent().getExtras();
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Boogaloo-Regular.ttf");
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
		new GetComplaints().execute();
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
		if(hasFocus){
			text="Type name of Doctor or Specialization registered in "+provider_name.getText().toString()+" and select your refering Physician.";
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			Providerurl= "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchDoctors/?ProviderCode="+pcode+"&DoctorName="+Dname.substring(0,Dname.indexOf(" "))+"&Specialization=";
			System.out.println("++++++++"+Providerurl);
			new GetProviders().execute();
		}
		
	}});
doctor.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		lv_doctor.setVisibility(View.VISIBLE);
		complaint.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		submit.setVisibility(View.GONE);
		ccomplaint.setVisibility(View.GONE);
		lv_complaint.setVisibility(View.GONE);
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
				complaint.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				submit.setVisibility(View.VISIBLE);
				ccomplaint.setVisibility(View.VISIBLE);
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				text="Select or Type your Chief Complaint regarding with Health problems.";
				tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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
		complaint.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				lv_complaint.setVisibility(View.VISIBLE);
				
			}
		});
		complaint.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				((Filterable) Complaintadapter).getFilter().filter(s);
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
				complaint.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				submit.setVisibility(View.VISIBLE);
				ccomplaint.setVisibility(View.VISIBLE);
				text="Please verify or check if your indicated physician and chief complaint is correct! You may now click submit to process your LOA as soon as possible. ";
				tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LOAurl= "https://apps.philcare.com.ph/PhilcareWatsonTest/AuthorizationNo.svc/GenerateAuthorizationNo/?CertNo="+Data.cert+"&ProviderCode="+pcode+"&ADoctorCode="+dcode+"&RDoctorCode="+dcode+"&ChiefComplaint=Fever"+complaint.getText().toString().replace(" ", "+");
				new GetLOA().execute();
				
			}
		});
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
						if(name==""||name==null||name.isEmpty())
						{
						name="No Doctor Found";
							doctor.put(TAG_NAME, name);
							doctor.put(TAG_SPECIALIZATION, special);
							doctor.put(TAG_TEL, tel);
							doctor.put(TAG_CODE, code);
							doctorList.add(doctor);
							
						}
						else{
							doctor.put(TAG_NAME, name);
							doctor.put(TAG_SPECIALIZATION, special);
							doctor.put(TAG_TEL, tel);
							doctor.put(TAG_CODE, code);
							doctorList.add(doctor);
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
						String message = c.getString(TAG_MESSAGE);
						String providername = c.getString(TAG_PROVIDERNAME);
						String servicetype = c.getString(TAG_SERVICETYPE);
						Data.LOA=(message+"\nCase No."+caseno+"\nLOA No."+loa);
						
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
			AlertDialog.Builder dialog = new AlertDialog.Builder(CreateLOA.this);
	        dialog.setTitle("Generate LOA");
	        dialog.setMessage(Data.LOA);
	        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	           Intent i=new Intent(getApplicationContext(),ChatBubbleActivity.class);
	           startActivity(i);
	            }
	        });
	         dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) {
	             }
	         });
	         dialog.show();
	 	}
	}

}
