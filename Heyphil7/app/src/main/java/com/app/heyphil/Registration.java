package com.app.heyphil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ABDalisay on 9/21/2016.
 */
public class Registration extends Activity {
    private Context context=this;
    RelativeLayout rl_info;
    RelativeLayout rl_success;
    Button facebook;
    TextView tv_skip,tv_message, tv_returnmessage;
    EditText et_firstname,et_lastname,et_mi,et_bldg_no,et_street, et_brgy, et_city, et_province,et_homecontact,et_email,et_contact,et_password,et_confirmpassword;
    Button btn_submit, btn_cancel;
    private CallbackManager callbackManager;
    ProgressDialog progress;
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    JSONArray jsonArray = null;
    String registrationUrl;
    String Certno, bday;
    String duplicate;
    String success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bundle extra=getIntent().getExtras();
        rl_info=(RelativeLayout)findViewById(R.id.info);
        rl_success=(RelativeLayout)findViewById(R.id.success);
        facebook=(Button) findViewById(R.id.facebook);
        tv_skip=(TextView)findViewById(R.id.tv_skip);
        tv_message=(TextView)findViewById(R.id.tv_message);
        tv_returnmessage=(TextView)findViewById(R.id.returnmessage);
        et_firstname=(EditText)findViewById(R.id.et_firstname);
        et_lastname=(EditText)findViewById(R.id.et_lastname);
        et_mi=(EditText)findViewById(R.id.et_mi);
        et_bldg_no=(EditText)findViewById(R.id.et_bldg_no);
        et_street=(EditText)findViewById(R.id.et_street);
        et_brgy=(EditText)findViewById(R.id.et_brgy);
        et_city=(EditText)findViewById(R.id.et_city);
        et_province=(EditText)findViewById(R.id.et_province);
        et_homecontact=(EditText)findViewById(R.id.et_homecontact);
        et_email=(EditText)findViewById(R.id.et_email);
        et_contact=(EditText)findViewById(R.id.et_contact);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirmpassword=(EditText)findViewById(R.id.et_confirm_password);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        et_firstname.setText(extra.getString("Firstname"));
        et_lastname.setText(extra.getString("Lastname"));
        Certno=extra.getString("Cert");
        bday=extra.getString("Bday");
        progress=new ProgressDialog(Registration.this);
        progress.setMessage("Please wait");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        facebook_id=f_name= m_name= l_name= gender= profile_image= full_name= email_id="";
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNetwork(context))
                {
                    onSubmit();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
               /* facebook.setVisibility(View.GONE);
                tv_skip.setVisibility(View.GONE);
                rl_info.setVisibility(View.GONE);
                rl_success.setVisibility(View.VISIBLE);
                tv_message.setText("Thank you for registering! An email confirmation has been automatically sent to the email you have provided.");*/
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progress.show();
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    //facebook_id=profile.getId();
                    f_name=profile.getFirstName();
                    m_name=profile.getMiddleName();
                    l_name=profile.getLastName();
                    full_name=profile.getName();
                    profile_image=profile.getProfilePictureUri(400, 400).toString();
                }
                Toast.makeText(Registration.this,"Wait...",Toast.LENGTH_SHORT).show();
                //System.out.println("TOKEN ID+++++"+AccessToken.getCurrentAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                progress.dismiss();
                                // Application code
                                try {
                                    //System.out.println("TOKEN ID+++++"+object.getString("email")+object.getString("birthday"));
                                    et_firstname.setText(f_name);
                                    et_lastname.setText(l_name);
                                    et_mi.setText(m_name);
                                    facebook_id=object.getString("id");
                                    et_email.setText(object.getString("email"));
                                    registrationUrl="https://apps.philcare.com.ph/PhilcareWatsonTest/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=1&email="+object.getString("email")+"&certNo="+Certno+"&userID="+facebook_id+"&profilePicURL="+profile_image+"&mobileNo=&userName="+object.getString("email")+"=&password=philcare&bldgNo=&street=&brgy=&city=&province=&homeNo=&birthDate="+bday+"&firstName="+et_firstname.getText().toString()+"&lastName="+et_lastname.getText().toString();
                                    System.out.println("URL"+registrationUrl);
                                    new Register().execute();
                                    logoutFromFacebook();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Registration.this,"cancel",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Registration.this,"login failed",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Registration.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
            }
        });
    }

    public static boolean checkNetwork(Context c)
    {
        ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo netInfo2 = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo.isConnected() || netInfo2.isConnected()) { return true; }

        return false;
    }
    private void onSubmit(){
        /*registrationUrl="https://apps.philcare.com.ph/PhilcareWatsonTest/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=0&email="+et_email.getText().toString()+"&certNo="+Certno+"&userID=&profilePicURL=&mobileNo="+et_contact.getText().toString()+"&userName="+et_email.getText().toString()+"&password="+et_password.getText().toString()+"&bldgNo="+et_bldg_no.getText().toString().replaceAll(" ","+")+"&street="+et_street.getText().toString().replaceAll(" ","+")+"&brgy="+et_brgy.getText().toString().replaceAll(" ","+")+"&city="+et_city.getText().toString().replaceAll(" ","+")+"&province="+et_province.getText().toString().replaceAll(" ","+")+"&homeNo="+et_homecontact.getText().toString().replaceAll(" ","+")+"&birthDate="+bday+"&firstName="+et_firstname.getText().toString().replaceAll(" ","+")+"&lastName="+et_lastname.getText().toString();
        System.out.println("URL"+registrationUrl);
        new Register().execute();*/
        if(!et_bldg_no.getText().toString().trim().isEmpty()&&!et_street.getText().toString().trim().isEmpty()&&!et_brgy.getText().toString().trim().isEmpty()&&
                !et_city.getText().toString().trim().isEmpty()&&!et_province.getText().toString().trim().isEmpty()&&!et_homecontact.getText().toString().trim().isEmpty()&&
                !et_contact.getText().toString().trim().isEmpty()&&!et_email.getText().toString().trim().isEmpty()&&!et_password.getText().toString().trim().isEmpty()&&!et_confirmpassword.getText().toString().trim().isEmpty()){
            if(et_password.getText().toString().equals(et_confirmpassword.getText().toString())){
                registrationUrl="https://apps.philcare.com.ph/PhilcareWatsonTest/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=0&email="+et_email.getText().toString()+"&certNo="+Certno+"&userID=&profilePicURL=&mobileNo="+et_contact.getText().toString()+"&userName="+et_email.getText().toString()+"&password="+et_password.getText().toString()+"&bldgNo="+et_bldg_no.getText().toString().replaceAll(" ","+")+"&street="+et_street.getText().toString().replaceAll(" ","+")+"&brgy="+et_brgy.getText().toString().replaceAll(" ","+")+"&city="+et_city.getText().toString().replaceAll(" ","+")+"&province="+et_province.getText().toString().replaceAll(" ","+")+"&homeNo="+et_homecontact.getText().toString().replaceAll(" ","+")+"&birthDate="+bday+"&firstName="+et_firstname.getText().toString().replaceAll(" ","+")+"&lastName="+et_lastname.getText().toString();
                System.out.println("URL"+registrationUrl);
                new Register().execute();

            }
            else
            {
                tv_returnmessage.setVisibility(View.VISIBLE);
                tv_returnmessage.setText("Password and Confirm Password doesn't match!");
            }
        }
        else
        {
            tv_returnmessage.setVisibility(View.VISIBLE);
            tv_returnmessage.setText("Unable to proceed. Please provide all needed information.");
        }
    }
    private void logoutFromFacebook(){
        try {
            if (AccessToken.getCurrentAccessToken() == null) {
                return; // already logged out
            }
            long fb_id=Long.parseLong(facebook_id); //get fb id from sharedprefrences
            GraphRequest graphRequest=new GraphRequest(AccessToken.getCurrentAccessToken(), "/ "+fb_id+"/permissions/", null,
                    HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                }
            });

            graphRequest.executeAsync();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class Register extends AsyncTask<Void, Void, Void>
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
            String jsonStr = sh.makeServiceCall(registrationUrl,ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("PhilcareWatsonMemberRegistrationResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        duplicate=c.getString("DuplicateFlag");
                        success=c.getString("SuccessFlag");
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
            if(success=="1"){
                facebook.setVisibility(View.GONE);
                tv_skip.setVisibility(View.GONE);
                rl_info.setVisibility(View.GONE);
                rl_success.setVisibility(View.VISIBLE);
                tv_message.setText("Thank you for registering! An email confirmation has been automatically sent to the email you have provided.");
            }
            else if(duplicate=="1")
            {
                rl_info.setVisibility(View.VISIBLE);
                tv_returnmessage.setVisibility(View.VISIBLE);
                tv_message.setText("Email you have used is already exist!");
            }

        }
    }
}
