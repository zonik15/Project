package com.app.heyphil;

import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.visual_recognition.v1.model.Label;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;

public class LoginScreen extends Activity {
	
	private EditText	et_username;
	private EditText	et_password;
	
	private Button		btn_login;
	
	private TextView	tv_signup;
	private TextView	tv_forgotpassword;
	
	private String		login_link;
	private String		Link="https://heyphil.mybluemix.net/cert=";
	private String		APILink="https://apps.philcare.com.ph/PCareWebServices/login.svc/";
	private String		user;
	private String		pass;
	
	List<String>            list_xml_link           = new ArrayList<String>();
	List<String>            list_xml_data           = new ArrayList<String>();
	private String          successful_flag         = "";
	private String         	text_cert         = "";
	private String          fname         = "";
	private String         	lname         = "";
	private String         	email         = "";
	
	private int             maxBarValue;
	private int             total;
	private int             currentDownload         = 1;
	static ProgressDialog  	pDialog;
	static AlertDialog     aDialog;
	private Context			context=this;
	private Context         context1;
	private static String   CURRENT_SCREEN          = "LoginScreen";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Boogaloo-Regular.ttf");
		et_username=(EditText)findViewById(R.id.et_username);
		et_password=(EditText)findViewById(R.id.et_password);
		btn_login=(Button)findViewById(R.id.btn_login);
		tv_signup=(TextView)findViewById(R.id.tv_btn_sign_up);
		tv_forgotpassword=(TextView)findViewById(R.id.tv_btn_forgot_password);
		tv_signup.setTypeface(tf);
		tv_forgotpassword.setTypeface(tf);
		et_username.setTypeface(tf);
		et_password.setTypeface(tf);
		btn_login.setTypeface(tf);
		SharedPreferences sp1=this.getSharedPreferences("Login",0);
		final String pass = sp1.getString("Psw", "");
		final String user = sp1.getString("User", "");
		if (pass!=null&&!pass.isEmpty()&&user!=null&&!user.isEmpty()){
			final Builder alertDialog = new Builder(LoginScreen.this);
			alertDialog.setTitle("Login New Account");
			alertDialog.setMessage("Are you sure to use other account?");
			alertDialog.setIcon(R.drawable.icon1);
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							et_username.setText(user);
							et_password.setText(pass);
							onLogin();
						}
					});
			alertDialog.show();
		}
		
		btn_login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkNetwork(context)){
					SharedPreferences sp=getSharedPreferences("Login", 0);
					SharedPreferences.Editor Ed=sp.edit();
					Ed.putString("User",et_username.getText().toString());
					Ed.putString("Psw",et_password.getText().toString());
					Ed.commit();
					    onLogin();
				}
				else{
					Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
			}});
		tv_signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getBaseContext(),RegisterScreen.class);
				startActivity(intent);
				finish();
			}});
	}
	 // check Login input
	  private void onLogin()
	  {
			if (et_username.getText().toString().equals("") || et_password.getText().toString().equals(""))
			{
				  successful_flag = "False";
				  Toast.makeText(getBaseContext(), "Please fill up the required field!", Toast.LENGTH_SHORT).show();
				  
				  
			}
			else
			{
				  
				  new onLoginAsync().execute();
				  
				
			}
	  }
	  // get Login Information
	  private class onLoginAsync extends AsyncTask<String, String, String>
	  {
			protected void onPreExecute()
			{
				  getLoginXML();
				  progressDialog(context, "Verifying data. Please wait...");
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

				  closeProgressDialog();
			}
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
							successful_flag = "True";
							Intent intent =new Intent(getBaseContext(),ChatBubbleActivity.class);
							startActivity(intent);
							et_username.setText("");
							et_password.setText("");
							finish();
							
					  }
					  else
					  {
							successful_flag = "False";
							
					  }

				}
		  }



		  // get username and password
		  private void getLoginXML()
		  {
				user = et_username.getText().toString();
				pass= et_password.getText().toString();
				login_link = "Login/?Username=" + user + "&pwd=" + pass;
				login_link = APILink + login_link;
				list_xml_link.clear();
				System.out.println("==========url"+convertToUrl(login_link));
				list_xml_link.add(convertToUrl(login_link));
		  }
		  // get XML value Data
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
							if (xpp.getName().equals("SuccessFlag") || xpp.getName().equals("a:SuccessFlag"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) successful_flag = xpp.getText();
							}
							else if (xpp.getName().equals("CertNo") || xpp.getName().equals("a:CertNo"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) text_cert = xpp.getText();
								  Data.cert=text_cert;
							}
							else if (xpp.getName().equals("Email") || xpp.getName().equals("a:Email"))
							{
								if (xpp.next() == XmlPullParser.TEXT) email = xpp.getText();
								Data.email=email;
							}
							else if (xpp.getName().equals("FirstName") || xpp.getName().equals("a:FirstName"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) fname = xpp.getText();
								  Data.Fname=fname;
							}
							else if (xpp.getName().equals("LastName") || xpp.getName().equals("a:LastName"))
							{
								  if (xpp.next() == XmlPullParser.TEXT) lname = xpp.getText();
								  Data.Lname=lname;
							}
					  }
					  eventType = xpp.next();
					  
				}

				return successful_flag;

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
		  public String getXML(String service)
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
		/**
		   * Progress Dialog/ Loading Dialog
		   * 
		   * Method call if you want to display Loading Dialog
		   * 
		   * @param c
		   *              - Context
		   * @param msg
		   *              - Message to be display in dialog
		   */
		  public static void progressDialog(Context c, String msg)
		  {
				pDialog = ProgressDialog.show(c, "", msg, true);
				pDialog.show();
		  }
		  /**
		   * Close Dialog
		   * 
		   * Method call if you want to close dialog
		   */
		  public static void closeProgressDialog()
		  {
				pDialog.dismiss();
		  }
		 /* public static boolean checkNetwork(Context c)
		  {
				ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

				NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

				return false;
		  }*/
		  
		  //AlertDialog
		  private void alertdialog(){
			  Builder alertDialog2 = new Builder(
					    LoginScreen.this);

					// Setting Dialog Title
					alertDialog2.setTitle("Exit Application");

					// Setting Dialog Message
					alertDialog2.setMessage("Are you sure you want to exit Heyphil?");

					// Setting Icon to Dialog
					alertDialog2.setIcon(R.drawable.icon1);

					// Setting Positive "Yes" Btn
					alertDialog2.setPositiveButton("YES",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            // Write your code here to execute after dialog
					        	finish();
					        }
					    });

					// Setting Negative "NO" Btn
					alertDialog2.setNegativeButton("NO",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            // Write your code here to execute after dialog
					            dialog.cancel();
					        }
					    });

					// Showing Alert Dialog
					alertDialog2.show();
		  }
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			alertdialog();
		}
	  public static boolean checkNetwork(Context c)
	  {
			ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

			return false;
	  }
}
