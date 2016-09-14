package com.app.heyphil;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterScreen extends Activity {
	  
	static final String    DEFAULT_LINK    = "https://apps.philcare.com.ph/PCareWebServices/"; // Prod  
      private Context context;
      private Context context1=this;
	  private static String CURRENT_SCREEN = "RegisterScreen";
	  private Intent intent;

	  private String registration_link;
	  private String successful_flag = "";
	  private String message;

	  private DialogFragment newFragment;
	  private MenuItem menuItem;
 
	  private Button btn_submit;
	  private Button btn_cancel;
	  private static EditText et_cert_no;
	  private static EditText et_birthdate;
	  private EditText et_mobile_no;
	  private static EditText et_email;
	  private EditText et_password;
	  private EditText et_confirm_password;

	  private EditText et_bldg_no;
	  private EditText et_street;
	  private EditText et_brgy;
	  private EditText et_city;
	  private EditText et_province;
	  private EditText et_username;

	  private EditText et_home_country_code;
	  private EditText et_local_no;
	  private EditText et_home_no;
	  private EditText et_country_code;

	  private TextView tv_header;
	  private TextView tv_cert_no;
	  private TextView tv_birthdate;
	  private TextView tv_home_no;
	  private TextView tv_mobile_no;
	  private TextView tv_email;
	  private TextView tv_password;
	  private TextView tv_confirm_password;
	  private TextView tv_btn_t_and_c;
	  private TextView tv_username;

	  private TextView tv_home_address;
	  private TextView tv_bldg_no;
	  private TextView tv_street;
	  private TextView tv_brgy;
	  private TextView tv_city;
	  private TextView tv_province;

	  private String text_last_name;
	  private String text_first_name;
	  private String text_cert_no;
	  private String text_birthdate;
	  private String text_mobile_no;
	  private String text_email;
	  private String text_password;

	  private String text_home_no;
	  private String text_username;
	  
	  static ProgressDialog  pDialog;
	  private Dialog custom_dialog;
	  private Dialog custom_dialog_pp;
	  private CheckBox cb_agreement;
	  
	  private Calendar myCalendar = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Boogaloo-Regular.ttf");
		btn_submit=(Button)findViewById(R.id.btn_submit);
		btn_cancel=(Button)findViewById(R.id.btn_cancel);
		et_cert_no=(EditText)findViewById(R.id.et_cert_no);
		et_birthdate=(EditText)findViewById(R.id.et_birthdate);
		et_mobile_no = (EditText) findViewById(R.id.et_mobile_no);
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);

		et_bldg_no = (EditText) findViewById(R.id.et_bldg_no);
		et_street = (EditText) findViewById(R.id.et_street);
		et_brgy = (EditText) findViewById(R.id.et_brgy);
		et_city = (EditText) findViewById(R.id.et_city);
		et_province = (EditText) findViewById(R.id.et_province);
		et_username = (EditText) findViewById(R.id.et_username);

		et_home_country_code = (EditText) findViewById(R.id.et_home_country_code);
		et_local_no = (EditText) findViewById(R.id.et_local_no);
		et_home_no = (EditText) findViewById(R.id.et_home_no);
		et_country_code = (EditText) findViewById(R.id.et_country_code);

		tv_header = (TextView) findViewById(R.id.tv_header);
		tv_cert_no = (TextView) findViewById(R.id.tv_cert_no);
		tv_birthdate = (TextView) findViewById(R.id.tv_birthdate);
		tv_home_no = (TextView) findViewById(R.id.tv_home_no);
		tv_mobile_no = (TextView) findViewById(R.id.tv_mobile_no);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_password = (TextView) findViewById(R.id.tv_password);
		tv_confirm_password = (TextView) findViewById(R.id.tv_confirm_password);
		tv_btn_t_and_c = (TextView) findViewById(R.id.tv_btn_t_and_c);
		tv_username = (TextView) findViewById(R.id.tv_username);

		tv_home_address = (TextView) findViewById(R.id.tv_home_address);
		tv_bldg_no = (TextView) findViewById(R.id.tv_bldg_no);
		tv_street = (TextView) findViewById(R.id.tv_street);
		tv_brgy = (TextView) findViewById(R.id.tv_brgy);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_province = (TextView) findViewById(R.id.tv_province);

		cb_agreement = (CheckBox) findViewById(R.id.cb_agreement);
		tv_cert_no.setTypeface(tf);
		tv_home_address.setTypeface(tf);
		tv_bldg_no.setTypeface(tf);
		tv_street.setTypeface(tf);
		tv_brgy.setTypeface(tf);
		tv_city.setTypeface(tf);
		tv_province.setTypeface(tf);
		tv_home_no.setTypeface(tf);
		tv_mobile_no.setTypeface(tf);
		tv_birthdate.setTypeface(tf);
		tv_email.setTypeface(tf);
		tv_username.setTypeface(tf);
		tv_password.setTypeface(tf);
		tv_confirm_password.setTypeface(tf);
		tv_btn_t_and_c.setTypeface(tf);
		btn_cancel.setOnClickListener(myClickListener);
		btn_submit.setOnClickListener(null);
		btn_submit.setEnabled(false);
		tv_btn_t_and_c.setOnClickListener(myClickListener);
		et_birthdate.setOnClickListener(myClickListener);
		cb_agreement.setOnClickListener(myClickListener);
		
		et_bldg_no.setOnTouchListener(myTouchListener);
		et_street.setOnTouchListener(myTouchListener);
		et_brgy.setOnTouchListener(myTouchListener);
		et_city.setOnTouchListener(myTouchListener);
		et_province.setOnTouchListener(myTouchListener);
		
		et_bldg_no.setOnFocusChangeListener(focus);
		et_street.setOnFocusChangeListener(focus);
		et_brgy.setOnFocusChangeListener(focus);
		et_city.setOnFocusChangeListener(focus);
		et_province.setOnFocusChangeListener(focus);
		
		et_birthdate.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            new DatePickerDialog(RegisterScreen.this, date, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	        }
	    });
	}
	

	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {
	        // TODO Auto-generated method stub
	        myCalendar.set(Calendar.YEAR, year);
	        myCalendar.set(Calendar.MONTH, monthOfYear);
	        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	        updateLabel();
	    }

	};

	      private void updateLabel() {

	    String myFormat = "MM/dd/yy"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

	    et_birthdate.setText(sdf.format(myCalendar.getTime()));
	    }

	// Special Characters Validation
		  private OnTouchListener myTouchListener=new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Pattern p = Pattern.compile("[^A-Za-z0-9]");
				switch(v.getId())
				{
				case R.id.et_bldg_no:
					et_bldg_no.setTextColor(getResources().getColor(R.color.edit_text_color));
					break;
				case R.id.et_street:
					et_street.setTextColor(getResources().getColor(R.color.edit_text_color));
					break;
				case R.id.et_brgy:
					et_brgy.setTextColor(getResources().getColor(R.color.edit_text_color));
					break;
				case R.id.et_city:
					et_city.setTextColor(getResources().getColor(R.color.edit_text_color));
					break;
				case R.id.et_province:
					et_province.setTextColor(getResources().getColor(R.color.edit_text_color));
					break;
				}
				return false;
			}};
			
			private OnFocusChangeListener focus=new OnFocusChangeListener(){

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					Pattern p = Pattern.compile("[^A-Za-z0-9]");
					// TODO Auto-generated method stub
					if(!hasFocus){
						 if (p.matcher(et_bldg_no.getText().toString().replaceAll("\\s+", "").trim()).find())
							 {
							 et_bldg_no.setTextColor(getResources().getColor(R.color.red));
							 /*GeneralFunctions.normalDialog(CURRENT_SCREEN, context, "Invalid Input!", "", "OK",
									    "Special Characters is not allowed.", 1, "stat_notify_error");*/
							 }
					  	 else et_bldg_no.setTextColor(getResources().getColor(R.color.edit_text_color));
					  	 if (p.matcher(et_street.getText().toString().replaceAll("\\s+", "").trim()).find())
					  	 	{
					  		 et_street.setTextColor(getResources().getColor(R.color.red));
					  		/*GeneralFunctions.normalDialog(CURRENT_SCREEN, context, "Invalid Input!", "", "OK",
								    "Special Characters is not allowed.", 1, "stat_notify_error");*/
					  	 	}
					  	  else et_street.setTextColor(getResources().getColor(R.color.edit_text_color));
						  if (p.matcher(et_brgy.getText().toString().replaceAll("\\s+", "").trim()).find())
							  {
							  et_brgy.setTextColor(getResources().getColor(R.color.red));
							  /*GeneralFunctions.normalDialog(CURRENT_SCREEN, context, "Invalid Input!", "", "OK",
									    "Special Characters is not allowed.", 1, "stat_notify_error");*/
							  }
						  else et_brgy.setTextColor(getResources().getColor(R.color.edit_text_color));
						  if (p.matcher(et_city.getText().toString().trim()).find()) 
							  {
							  et_city.setTextColor(getResources().getColor(R.color.red));
							  /*GeneralFunctions.normalDialog(CURRENT_SCREEN, context, "Invalid Input!", "", "OK",
									    "Special Characters is not allowed.", 1, "stat_notify_error");*/
							  }
						  else et_city.setTextColor(getResources().getColor(R.color.edit_text_color));
						  if (p.matcher(et_province.getText().toString().trim()).find()) 
							  {
							  et_province.setTextColor(getResources().getColor(R.color.red));
							 /* GeneralFunctions.normalDialog(CURRENT_SCREEN, context, "Invalid Input!", "", "OK",
									    "Special Characters is not allowed.", 1, "stat_notify_error");*/
							  }
						  else et_province.setTextColor(getResources().getColor(R.color.edit_text_color));
					}
				}};
	// Return Required Field to default text color
	private void setRequiredFieldDefaultTextColor()
		  {
				tv_cert_no.setTextColor(getResources().getColor(R.color.display_text_color));
				tv_email.setTextColor(getResources().getColor(R.color.display_text_color));
				tv_username.setTextColor(getResources().getColor(R.color.display_text_color));
				tv_password.setTextColor(getResources().getColor(R.color.display_text_color));
				tv_confirm_password.setTextColor(getResources().getColor(R.color.display_text_color));
				tv_cert_no.setTextColor(getResources().getColor(R.color.display_text_color));
				tv_birthdate.setTextColor(getResources().getColor(R.color.display_text_color));
				cb_agreement.setTextColor(getResources().getColor(R.color.display_text_color));
		  }
@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	Intent intent=new Intent(getApplicationContext(),LoginScreen.class);
	startActivity(intent);
	finish();
	}
private OnClickListener myClickListener = new OnClickListener()
{
	@Override
	public void onClick(final View v)
	{
		  switch (v.getId())
		  {
				case R.id.btn_submit:
					  if (checkNetwork(context1))
					  {
							onSubmit();
					  }
					  else
					  {
						  Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
					  }
					  break;
				case R.id.btn_cancel:
					  finish();
					  break;
				case R.id.tv_btn_t_and_c:
					  //showCustomDialog();
					  break;
				case R.id.cb_agreement:
					  if (cb_agreement.isChecked())
					  {
							btn_submit.setOnClickListener(myClickListener);
							btn_submit.setEnabled(true);
					  }
					  else
					  {
							btn_submit.setOnClickListener(null);
							btn_submit.setEnabled(false);
					  }
					  break;
		  }

	}
};

/*// Load Terms and Conditions
private void showCustomDialog()
{
	custom_dialog = new Dialog(context1, R.style.CustomDialogTheme);
	custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	custom_dialog.setContentView(R.layout.custom_dialog);
	custom_dialog.setCancelable(true);

	custom_dialog.show();

	Window window = custom_dialog.getWindow();
	window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	TextView tv = (TextView) custom_dialog.findViewById(R.id.tv_t_and_c);
	Button btn_ok = (Button) custom_dialog.findViewById(R.id.btn_ok);

	tv.setText(Html.fromHtml(TandC()));

	btn_ok.setOnClickListener(new OnClickListener() {
		  public void onClick(View arg0)
		  {
				custom_dialog.dismiss();
		  }

	});

	clickify(tv, "PhilCare Privacy Policy", new ClickSpan.OnClickListener() {
		  @Override
		  public void onClick()
		  {
				showPrivatePolicy();
		  }
	});

	Log.e("trace", "Build.VERSION.SDK_INT == " + Build.VERSION.SDK_INT);
	Log.e("trace", "Build.VERSION_CODES.ICE_CREAM_SANDWICH == " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);

}

// Load Private Policy
private void showPrivatePolicy()
{
	custom_dialog_pp = new Dialog(context1, R.style.CustomDialogTheme);
	custom_dialog_pp.requestWindowFeature(Window.FEATURE_NO_TITLE);
	custom_dialog_pp.setContentView(R.layout.custom_dialog);
	custom_dialog_pp.setCancelable(true);

	custom_dialog_pp.show();

	Window window = custom_dialog_pp.getWindow();
	window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	TextView tv = (TextView) custom_dialog_pp.findViewById(R.id.tv_t_and_c);
	Button btn_ok = (Button) custom_dialog_pp.findViewById(R.id.btn_ok);

	tv.setText(Html.fromHtml(PrivatePolicy()));

	btn_ok.setOnClickListener(new OnClickListener() {
		  public void onClick(View arg0)
		  {
				custom_dialog_pp.dismiss();
		  }

	});

}*/

public static void clickify(TextView view, final String clickableText, final ClickSpan.OnClickListener listener)
{
	CharSequence text = view.getText();
	String string = text.toString();
	ClickSpan span = new ClickSpan(listener);

	int start = string.indexOf(clickableText);
	int end = start + clickableText.length();
	if (start == -1) return;

	if (text instanceof Spannable)
	{
		  ((Spannable) text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	else
	{
		  SpannableString s = SpannableString.valueOf(text);
		  s.setSpan(span, start, end, Spanned.SPAN_MARK_MARK);
		  view.setText(s);
	}

	MovementMethod m = view.getMovementMethod();
	if ((m == null) || !(m instanceof LinkMovementMethod))
	{
		  view.setMovementMethod(LinkMovementMethod.getInstance());
	}
}

private String TandC()
{
	return "<span><center><b> PhilCare Go!Mobile Application Terms and Conditions </b></center></span><br><br> " + 
				 " " + 
				 "<span>1. INTRODUCTION: Welcome to our application called PhilCare Go!Mobile (the<b> �App� </b>).  This App is published by or on behalf of PhilHealthCare, Inc. (<b> �PhilCare� </b>or<b> �We� </b>or<b> �Us� </b>) a company registered in the Philippines with office address at the Penthouse, North Triangle Drive cor. Kalayaan Ave., PhilPlans Corporate Center, Fort Bonifacio, Taguig.</span><br><br> " + 
				 " " + 
				 "<span>By downloading or otherwise accessing the App you agree to be bound by the following terms and conditions (<b> �Terms� </b>) Privacy Policy (�Policy�). If you have any queries about the App or these Terms or the Policy, you can contact Us by any of the means set out in paragraph 11 of these Terms. </span><br><br> " + 
				 " " + 
				 "<span>2. GENERAL RULES RELATING TO CONDUCT: The App is made available for your own, personal use. The App must not be used for any commercial purpose whatsoever or for any illegal or unauthorised purpose. When you use the App you must comply with all applicable Philippine laws and with any applicable international laws, including the local laws in your country of residence (together referred to as<b> �Applicable Laws� </b>)..</span><br><br> " + 
				 " " + 
				 "<span>You agree that when using the App, you will comply with all Applicable Laws and these Terms. In particular, but without limitation, you agree not to:</span><br><br> " + 
				 " " + 
				 "<span>(a) Use the App in any unlawful manner or in a manner which promotes or encourages illegal activity including (without limitation) copyright infringement; or</span><br><br> " + 
				 " " + 
				 "<span>(b) Attempt to gain unauthorised access to the App or any networks, servers or computer systems connected to the App; or</span><br><br> " + 
				 " " + 
				 "<span>(c) Modify, adapt, translate or reverse engineer any part of the App or re-format or frame any portion of the pages comprising the App, save to the extent expressly permitted by these Terms or by law.</span><br><br> " + 
				 " " + 
				 "<span>You agree to indemnify PhilCare and its group companies in full and on demand from and against any loss, damage, costs or expenses which they suffer or incur directly or indirectly as a result of your use of the App otherwise than in accordance with these Terms or Applicable Laws.</span><br><br> " + 
				 " " + 
				 "<span>3. TERMINATION, DEACTIVATION AND SUSPENSION.  You may terminate this Agreement at any time by permanently deleting the App in its entirety from your owned devices, whereupon, and without need of notice from PhilCare any rights granted to you herein will automatically terminate. If you fail to comply with any provision of this Agreement, any rights granted to you herein will automatically terminate. In the event of such termination, you must immediately delete the App from any and all of your owned devices. Termination of the App and this Agreement shall not affect and be independent from any other agreements that you may have with PhilCare.</span><br><br> " + 
				 " " + 
				 "<span>Alternately, you may choose to temporarily de-activate your App account and access to this App by selecting the available option to do so within the App itself. This action shall suspend all rights and obligations provided by the App. You may resume use of your App account by re-activating the App in accordance with the instructions supplied within the App. Re-activation of the App will restore all rights and obligations conferred by the App and this Agreement immediately.</span><br><br> " + 
				 " " + 
				 "<span>If, for any reason, your status as a member is suspended or terminated, your access to the App and its functions shall be suspended. Access to the App shall be resumed only upon our confirmation of the resumption of the active status of your membership.</span><br><br> " + 
				 " " + 
				 "<span>3. TERMINATION, DEACTIVATION AND SUSPENSION.  You may terminate this Agreement at any time by permanently deleting the App in its entirety from your owned devices, whereupon, and without need of notice from PhilCare any rights granted to you herein will automatically terminate. If you fail to comply with any provision of this Agreement, any rights granted to you herein will automatically terminate. In the event of such termination, you must immediately delete the App from any and all of your owned devices. Termination of the App and this Agreement shall not affect and be independent from any other agreements that you may have with PhilCare.</span><br><br> " + 
				 " " + 
				 "<span>Alternately, you may choose to temporarily de-activate your App account and access to this App by selecting the available option to do so within the App itself. This action shall suspend all rights and obligations provided by the App. You may resume use of your App account by re-activating the App in accordance with the instructions supplied within the App. Re-activation of the App will restore all rights and obligations conferred by the App and this Agreement immediately.</span><br><br> " + 
				 " " + 
				 "<span>If, for any reason, your status as a member is suspended or terminated, your access to the App and its functions shall be suspended. Access to the App shall be resumed only upon our confirmation of the resumption of the active status of your membership.</span><br><br> " + 
				 " " + 
				 "<span>4. PROPRIETARY RIGHTS. You hereby acknowledge that PhilCare owns all rights, titles and interest in and to the App and to any and all proprietary and confidential information contained therein (\"PhilCare Information\"). The App and PhilCare Information are protected by applicable intellectual property and other laws, including patent law, copyright law, trade secret law, trademark law, unfair competition law, and any and all other proprietary rights, and any and all applications, renewals, extensions and restorations thereof, now or hereafter in force and effect worldwide. You agree that you will not (and will not allow any third party to) (i) modify, adapt, translate, prepare derivative works from, decompile, reverse engineer or disassemble the App or otherwise attempt to derive source code from the App; (ii) copy, distribute, transfer, sell or license the App; (iii) transfer the App to, or use the App on, a device other than the Authorized Device; (iv) take any action to circumvent, compromise or defeat any security measures implemented in the App; (v) use the App to access, copy, transfer, retransmit or transcode Content (as defined below) or any other content in violation of any law or third party rights; (vi) remove, obscure, or alter PhilCare's (or any third party's) copyright notices, trademarks, or other proprietary rights notices affixed to or contained within or accessed through the App.</span><br><br> " + 
				 " " + 
				 "<span>Content made available through the App (\"Content\") is protected by applicable intellectual property rights and is the property of PhilCare, its third party licensors and partners (as applicable), and other entities that provide such content to PhilCare. You may not (or enable others to) copy, distribute, display, modify, or otherwise use the Content except as it is provided to you through the App hereunder. PhilCare and its licensors make no representations or warranties regarding the accuracy or completeness of the Content.</span><br><br> " + 
				 " " + 
				 " " + 
				 "<span>5. PHILCARE PRIVACY POLICY: We take your privacy very seriously. PhilCare will only use your personal information in accordance with the terms of your membership agreement, pursuant to providing you with greater service efficiency and medical care that meets your expectations. Please refer to our <u>PhilCare Privacy Policy</u> for more information regarding this matter.  (How will this work with the Privacy Policy?  Maybe we can make reference to it in this provision?)</span><br><br> " + 
				 " " + 
				 "<span>6. DISCLAIMER / LIABILITY:<b> USE OF THE APP IS AT YOUR OWN RISK. THE APP IS PROVIDED ON AN �AS IS� BASIS. TO THE MAXIMUM EXTENT PERMITTED BY LAW: (A) PHILCARE DISCLAIMS ALL LIABILITY WHATSOEVER, WHETHER ARISING IN CONTRACT, TORT (INCLUDING NEGLIGENCE) OR OTHERWISE IN RELATION TO THE APP; AND (B) ALL IMPLIED WARRANTIES, TERMS AND CONDITIONS RELATING TO THE APP (WHETHER IMPLIED BY STATUE, COMMON LAW OR OTHERWISE), INCLUDING (WITHOUT LIMITATION) ANY WARRANTY, TERM OR CONDITION AS TO ACCURACY, COMPLETENESS, SATISFACTORY QUALITY, PERFORMANCE, FITNESS FOR PURPOSE OR ANY SPECIAL PURPOSE, AVAILABILITY, NON INFRINGEMENT, INFORMATION ACCURACY, INTEROPERABILITY, QUIET ENJOYMENT AND TITLE ARE, AS BETWEEN PHILCARE AND YOU, HEREBY EXCLUDED. IN PARTICULAR, BUT WITHOUT PREJUDICE TO THE FOREGOING, WE ACCEPT NO RESPONSIBILITY FOR ANY TECHNICAL FAILURE OF THE INTERNET AND/OR THE APP; OR ANY DAMAGE OR INJURY TO USERS OR THEIR EQUIPMENT AS A RESULT OF OR RELATING TO THEIR USE OF THE APP. YOUR STATUTORY RIGHTS ARE NOT AFFECTED. </b></span><br><br> " + 
				 " " + 
				 "<span>PhilCare will not be liable, in contract, quasi-delict (including, without limitation, negligence), under statute or otherwise, as a result of or in connection with the App, for any: (i) economic loss (including, without limitation, loss of revenues, profits, contracts, business or anticipated savings); or (ii) loss of goodwill or reputation; or (iii) special or indirect or consequential loss.</span><br><br> " + 
				 " " + 
				 "<span><b> IF PHILCARE IS LIABLE TO YOU DIRECTLY OR INDIRECTLY IN RELATION TO THE APP, THAT LIABILITY (HOWSOEVER ARISING) SHALL BE LIMITED TO THE CORRESPONDING INDIVIDUAL VALUE OF YOUR PRO-RATED ANNUAL MEMBERSHP FEE PAYMENT FOR THE SERVICES OF PHILCARE. </b></span><br><br> " + 
				 " " + 
				 "<span>Nothing in these Terms shall be construed as excluding or limiting the liability of PhilCare or its group companies for death or personal injury caused by its negligence or for any other liability which cannot be excluded by law.</span><br><br> " + 
				 " " + 
				 "<span>7. SERVICE SUSPENSION: If your PhilCare account becomes inactive or is suspended for any reason, your access to the App and the services provided by the same shall likewise be suspended, without need of notice or consent.</span><br><br> " + 
				 " " + 
				 "<span>8. ADVERTISERS IN THE APP: We accept no responsibility for advertisement contained within the App. If you agree to purchase goods and/or services from any third party who advertises in the App, you do so at your own risk. The advertiser, not PhilCare, is responsible for such goods and/or services and if you have any queries or complaints in relation to them, your only recourse is against the advertiser.</span><br><br> " + 
				 " " + 
				 " " + 
				 "<span>9. PROMOTION: If you take part in any promotion which is run in whole or in part, in or through the App (<b> �Promotion� </b>), you agree to be bound by the rules of that promotion and any other rules specified by PhilCare from time to time (<b> �Promotion Rules� </b>) and by the decisions of PhilCare, which are final in all matters relating to the Promotion. PhilCare reserves the right to disqualify any entrant and/or winner in its absolute discretion without notice in accordance with the Promotion Rules.</span><br><br> " + 
				 " " + 
				 "<span>The Promotion Rules shall at all times be subject to the rules and regulations of the Department of Trade and Industry (DTI), and any amendments to the same. Any relevant issuance or pronouncement of the DTI shall be considered incorporated into the Promotion Rules and govern the latter accordingly.</span><br><br> " + 
				 " " + 
				 "<span>10. GENERAL: These Terms and the Policy (as amended from time to time) constitute the entire agreement between you and PhilCare concerning your use of the App. In the event of a conflict between these Terms and the App, the Terms shall prevail. </span><br><br> " + 
				 " " + 
				 "<span>PhilCare reserves the right to update these Terms from time to time. If it does so, the updated version will be effective immediately, and the current Terms are available through a link in the App to this page.  You are responsible for regularly reviewing these Terms so that you are aware of any changes to them and you will be bound by the new policy upon your continued use of the App.  No other variation to these Terms shall be effective unless in writing and signed by an authorised representative on behalf of PhilCare.</span><br><br> " + 
				 " " + 
				 "<span>These Terms shall be governed by and construed in accordance with the laws of the Philippines and you agree to submit to the exclusive jurisdiction of the courts of the City of Taguig.</span><br><br> " + 
				 " " + 
				 "<span>If any provision(s) of these Terms is held by a court of competent jurisdiction to be invalid or unenforceable, then such provision(s) shall be construed, as nearly as possible, to reflect the intentions of the parties (as reflected in the provision(s)) and all other provisions shall remain in full force and effect.</span><br><br> " + 
				 " " + 
				 "<span>PhilCare�s failure to exercise or enforce any right or provision of these Terms shall not constitute a waiver of such right or provision unless acknowledged and agreed to by PhilCare in writing.</span><br><br> " + 
				 " " + 
				 "<span>Unless otherwise expressly stated, nothing in the Terms shall create any rights or any other benefits in favour of any person other than you and PhilCare.</span><br><br> " + 
				 " " + 
				 "<span>11.  CHANGES IN TERMS AND CONDITIONS.  In the event of any changes in the terms and conditions herein, the term modification date below will be updated to reflect the latest date that such changes were made, for your reference.</span><br><br> " + 
				 " " + 
				 "<span>12.  CONTACT US.  You may contact us using the information below. </span><br><br> " + 
				 " " + 
				 "<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>http://www.philcare.com.ph/contact-us/ " + 
				 "Twitter/Facebook/Instagram/philcare (using the logos of each) " + 
				 "24/7 Customer Hotline at (02) 462-1800.  Outside Metro Manila, please contact us at 1-800 -1888-3230 toll-free for PLDT and Smart subscribers.  " + 
				 "</span></span><br><br> " + 
				 " " + 
				 "<span>Last Modified On:  15 January 2014</span> ";
}

private String PrivatePolicy()
{
	return "<span><b>Privacy Policy</b></span><br><br> "
	            + " "
	            + "<span>1.&nbsp;&nbsp;&nbsp;What information do we collect?</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">We collect information from you when you register on our site, respond to a survey or fill out a form.</p> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">When ordering or registering on our website or PhilCare Go!Mobile App, as appropriate, you may be asked to enter your name, email address or phone number.</p> "
	            + " "
	            + "<span>2.&nbsp;&nbsp;&nbsp;How do we use your information?</span><br><br> "
	            + " "
	            + "<span><span style=\"margin-left:1.5em;\">Any of the information we collect from you may be used in one of the following ways: </span><span><br> "
	            + " "
	            + "<span><span style=\"margin-left:1.5em;\">�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To personalize your experience because your information helps us to better respond to your individual needs</span><span><br> "
	            + " "
	            + "<span><span style=\"margin-left:1.5em;\">�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To improve our website because we strive to improve our website offerings based on the information and feedback we receive from you</span><span><br> "
	            + " "
	            + "<span><span style=\"margin-left:1.5em;\">�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To improve customer service because your information helps us to more effectively respond to your customer service requests and needs</span><span><br> "
	            + " "
	            + "<span><span style=\"margin-left:1.5em;\">�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To send periodic emails.  We may use the email address you provide to send information, respond to inquiries, and/or other requests or questions.</span><span><br> "
	            + " "
	            + "<span><span style=\"margin-left:1.5em;\">�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To send periodic emails.  We may use the email address you provide to send information, respond to inquiries, and/or other requests or questions.</span><span><br><br> "
	            + " "
	            + "<span>3.&nbsp;&nbsp;&nbsp;How do we protect your information?</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">We implement a variety of security measures to maintain the safety of your personal information when you enter, submit, or access your personal information.</p> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">We offer the use of a secure server. All supplied sensitive/credit information is transmitted via Secure Socket Layer (SSL) technology and then encrypted into our Payment gateway providers database only to be accessible by those authorized with special access rights to such systems, and are required to keep the information confidential.</p> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">After a transaction, your private information (credit cards, billing address, financials, etc.) will not be kept on file for more than 60 days.</p> "
	            + " "
	            + "<span>4.&nbsp;&nbsp;&nbsp;Do we use cookies?</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">Yes.  Cookies are small files that a site or its service provider transfers to your computer�s hard drive through your Web browser (if you allow) that enables the sites or service providers systems to recognize your browser and capture and remember certain information.</p> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">We use cookies to understand and save your preferences for future visits.</p> "
	            + " "
	            + "<span>5.&nbsp;&nbsp;&nbsp;Do we disclose any information to outside parties? </span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">We do not sell, trade, or otherwise transfer to outside parties your personally identifiable information. This does not include trusted third parties who assist us in operating our website, conducting our business, or servicing you, so long as those parties agree to keep this information confidential. We may also release your information when we believe release is appropriate to comply with the law, enforce our site policies, or protect ours or others rights, property, or safety. However, non-personally identifiable visitor information may be provided to other parties for marketing, advertising, or other uses.</p> "
	            + " "
	            + "<span>6.&nbsp;&nbsp;&nbsp;Third Party Links </span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">Occasionally, at our discretion, we may include or offer third party products or services on our website. These third party sites have separate and independent privacy policies. We therefore have no responsibility or liability for the content and activities of these linked sites. Nonetheless, we seek to protect the integrity of our site and welcome any feedback about these sites.</p> "
	            + " "
	            + "<span>7.&nbsp;&nbsp;&nbsp;Online Privacy Policy</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">This online privacy policy applies only to information collected through our website and PhilCare Go!Mobile,and does not apply to information collected offline.</p> "
	            + " "
	            + "<span>8.&nbsp;&nbsp;&nbsp;Terms and Conditions</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">Please also visit our PhilCare Go!Mobile Terms and Conditions section establishing the use, disclaimers, and limitations of liability governing its use.</p> "
	            + " "
	            + "<span>9.&nbsp;&nbsp;&nbsp;Your Consent</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">By using our site, you consent to our online Privacy Policy.</p> "
	            + " "
	            + "<span>10.&nbsp;Changes to our Privacy Policy</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">If we decide to change our Privacy Policy, we will update the Privacy Policy modification date below.</p> "
	            + " "
	            + "<span>11.&nbsp;Contacting Us</span> "
	            + " "
	            + "<p style=\"margin-left:1.5em;\">If there are any questions regarding this Privacy Policy, you may contact us using the information below.</p> "
	            + " "
	            + "<span style=\"margin-left:1.5em;\">http://www.philcare.com.ph/contact-us/Twitter/Facebook/Instagram/philcare (using the logos of each) "
	            + "24/7 Customer Hotline at (02) 462-1800.  Outside Metro Manila, please contact us at 1-800 -1888-3230 toll-free for PLDT and Smart subscribers. "
	            + "</span><br><br> " + " " + "<span>Last Modified On:  15 January 2014</span> ";
}
// data validation before submitting registration
private void onSubmit()
{
		if (et_cert_no.getText().toString().equals("") || et_email.getText().toString().equals("")
		            || et_password.getText().toString().equals("") || et_confirm_password.getText().toString().equals("")
		            || et_username.getText().toString().equals("") || et_birthdate.getText().toString().equals("")
		            || et_brgy.getText().toString().equals("") || et_city.getText().toString().equals("") 
		            || et_province.getText().toString().equals("")
		            || !cb_agreement.isChecked())
		{

			  if (et_cert_no.getText().toString().equals("")) tv_cert_no.setTextColor(getResources().getColor(R.color.red));
			  else tv_cert_no.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_email.getText().toString().equals("")) tv_email.setTextColor(getResources().getColor(R.color.red));
			  else tv_email.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_password.getText().toString().equals("")) tv_password.setTextColor(getResources().getColor(R.color.red));
			  else tv_password.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_confirm_password.getText().toString().equals("")) tv_confirm_password.setTextColor(getResources().getColor(
				          R.color.red));
			  else tv_confirm_password.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_username.getText().toString().equals("")) tv_username.setTextColor(getResources().getColor(R.color.red));
			  else tv_username.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_birthdate.getText().toString().equals("")) tv_birthdate.setTextColor(getResources().getColor(R.color.red));
			  else tv_birthdate.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (!cb_agreement.isChecked()) cb_agreement.setTextColor(getResources().getColor(R.color.red));
			  else cb_agreement.setTextColor(getResources().getColor(R.color.display_text_color));
			  
			  if (et_brgy.getText().toString().equals("")) tv_brgy.setTextColor(getResources().getColor(R.color.red));
			  else tv_brgy.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_city.getText().toString().equals("")) tv_city.setTextColor(getResources().getColor(R.color.red));
			  else tv_city.setTextColor(getResources().getColor(R.color.display_text_color));
			  if (et_province.getText().toString().equals("")) tv_province.setTextColor(getResources().getColor(R.color.red));
			  else tv_province.setTextColor(getResources().getColor(R.color.display_text_color));
			  Toast.makeText(getBaseContext(), "Registration Failed \nPlease fill up required fields.", Toast.LENGTH_SHORT).show();
			  /*GeneralFunctions.normalDialog(CURRENT_SCREEN, context, "Registration Failed!", "", "OK",
				          "Please fill up required fields.", 1, "stat_notify_error");*/
		}
		else
		{
			  setRequiredFieldDefaultTextColor();

			  if (verifyEmailFormat(et_email))
			  {
					if (checkStringLength(et_username.getText().toString(), 8))
					{
						  if (!et_username.getText().toString().contains(" "))
						  {
								setRequiredFieldDefaultTextColor();

								if (verifyPassword(et_password, et_confirm_password))
								{
									  setRequiredFieldDefaultTextColor();

									  if (!et_password.getText().toString().contains(" ")
										          || !et_confirm_password.getText().toString().contains(" "))
									  {
											if (checkStringLength(et_password.getText().toString(), 6))
											{
												  if (checkIfPasswordIsAlphaNumeric(et_password.getText().toString()))
												  {
														setRequiredFieldDefaultTextColor();
														new onRegisterAsync().execute();
												  }
												  else
												  {
														tv_password.setTextColor(getResources().getColor(R.color.red));
														tv_confirm_password.setTextColor(getResources().getColor(R.color.red));
														Toast.makeText(getBaseContext(), "Registration Failed \n Password must be at least 6 alphanumeric characters, maximum of 20 length limits.", Toast.LENGTH_SHORT).show();
												  }
											}
											else
											{
												  tv_password.setTextColor(getResources().getColor(R.color.red));
												  tv_confirm_password.setTextColor(getResources().getColor(R.color.red));
												  Toast.makeText(getBaseContext(), "Registration Failed \n Password must be at least 6 alphanumeric characters, maximum of 20 length limits.", Toast.LENGTH_SHORT).show();
											}
									  }
									  else
									  {
											tv_password.setTextColor(getResources().getColor(R.color.red));
											tv_confirm_password.setTextColor(getResources().getColor(R.color.red));
											Toast.makeText(getBaseContext(), "Registration Failed \n Spaces is not allowed for creating Password.", Toast.LENGTH_SHORT).show();
									  }
								}
								else
								{
									  tv_password.setTextColor(getResources().getColor(R.color.red));
									  tv_confirm_password.setTextColor(getResources().getColor(R.color.red));
									  Toast.makeText(getBaseContext(), "Registration Failed \n Confirm Password must be the same as Password encoded.", Toast.LENGTH_SHORT).show();
								}

						  }
						  else
						  {
								tv_username.setTextColor(getResources().getColor(R.color.red));
								 Toast.makeText(getBaseContext(), "Registration Failed \n Spaces is not allowed for creating Username.", Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						  tv_username.setTextColor(getResources().getColor(R.color.red));
						  Toast.makeText(getBaseContext(), "Registration Failed \n Username must be at least 8 characters, maximum of 20 length limits.", Toast.LENGTH_SHORT).show();
					}
			  }
			  else
			  {
				  Toast.makeText(getBaseContext(), "Registration Failed \n Email must be valid and unique email address.", Toast.LENGTH_SHORT).show();
			  }
		}
}

private boolean verifyPassword(EditText et1, EditText et2)
{
		if (et_password.getText().toString().equals(et_confirm_password.getText().toString())) { return true; }
		return false;
}

private void getRegisterXML()
{
		text_cert_no = et_cert_no.getText().toString();
		text_birthdate = et_birthdate.getText().toString();
		text_email = et_email.getText().toString();
		text_password = et_password.getText().toString();

		text_home_no = et_local_no.getText().toString() + et_home_no.getText().toString();

		text_mobile_no = et_mobile_no.getText().toString();

		text_username = et_username.getText().toString();

		registration_link = "Registration.svc/?Certno=" + text_cert_no + "&UName=" + text_username + "&LName=" + text_last_name
		            + "&FName=" + text_first_name + "&BDate=" + text_birthdate + "&HouseNo="
		            + et_bldg_no.getText().toString().toUpperCase() + " " + "&Street=" + et_street.getText().toString().toUpperCase()
		            + " " + "&Barangay=" + et_brgy.getText().toString().toUpperCase() + " " + "&City="
		            + et_city.getText().toString().toUpperCase() + " " + "&Province=" + et_province.getText().toString().toUpperCase()
		            + "&HomeNo=" + text_home_no + "&Mobile=" + text_mobile_no + "&Email=" + text_email + "&Pwd=" + text_password;

		registration_link = DEFAULT_LINK  + registration_link;
		Log.e("trace", "link = " + registration_link);
}

private class onRegisterAsync extends AsyncTask<String, String, String>
{
		protected void onPreExecute()
		{
			  progressDialog(context1, "Sending data. Please wait...");
		}

		protected String doInBackground(String... params)
		{

			  String status = null;
			  getRegisterXML();

			  try
			  {
					status = getXML(convertToUrl(registration_link));
					status = (status.equals("error")) ? "error" : getXMLValue(status);
			  }
			  catch (XmlPullParserException e)
			  {
					e.printStackTrace();
					return "error";
			  }
			  catch (IOException e)
			  {
					e.printStackTrace();
					return "error";
			  }

			  return status;
		}

		protected void onPostExecute(String result)
		{
			  closeProgressDialog();
			  if (result.equals("False"))
			  {
				  Toast.makeText(getBaseContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
			  }
			  else if (result.equals("True"))
			  {
				  Toast.makeText(getBaseContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
			  }
			  else if (result.equals("error"))
			  {
					successful_flag = "False";
					closeProgressDialog();
					Toast.makeText(getBaseContext(), "Error connecting on server. Please try again later.", Toast.LENGTH_SHORT).show();
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
					if (xpp.getName().equals("SuccessFlag") || xpp.getName().equals("a:SuccessFlag"))
					{
						  if (xpp.next() == XmlPullParser.TEXT) successful_flag = xpp.getText();
					}
					else if (xpp.getName().equals("MessageReturn") || xpp.getName().equals("a:MessageReturn"))
					{
						  if (xpp.next() == XmlPullParser.TEXT) message = xpp.getText();
					}
			  }
			  eventType = xpp.next();
		}

		return successful_flag;

}

public void executeMethod()
{
		/* triggers when OK button is clicked on Dialog Screen */
		if (successful_flag.equals("True"))
		{
			  goToNextScreen();
		}
}

private void goToNextScreen()
{
		intent = new Intent(context, LoginScreen.class);
		startActivity(intent);
		finish();
}

public static void cancelDate()
{
		et_email.requestFocus();
}

public static void populateSetDate(int month, int day, int year)
{
		et_birthdate.setText(month + "/" + day + "/" + year);
		et_email.requestFocus();
}
public static boolean checkNetwork(Context c)
{
		ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

		return false;
}
/**
 * Email Verification.
 * 
 * @param et
 *              - passed EditText
 * @return true - if email is valid
 */
public static boolean verifyEmailFormat(EditText et)
{
		return isValidEmail(et.getText().toString());
}


private static boolean isValidEmail(CharSequence target)
{
		if (target == null) return false;
		else return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
}
/**
 * Validate Password(alphanumeric)
 * 
 * @param pwd
 *              = pass password
 */
public static boolean checkIfPasswordIsAlphaNumeric(String pwd)
{
		Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).{2,40})");
		Matcher matcher = pattern.matcher(pwd);
		return matcher.matches();
}


/**
 * Check String Length
 * 
 * @param s
 *              = pass String
 * @param i
 *              = max length of String
 */
public static boolean checkStringLength(String s, int i)
{
		if (s.length() < i) { return false; }
		return true;
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
/**
 * Covert to Valid URL.
 * 
 * @params
 *              - String to be converted
 * @return String - converted string
 */
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
 * Get XML data.
 * 
 * @paramsevice
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

}
