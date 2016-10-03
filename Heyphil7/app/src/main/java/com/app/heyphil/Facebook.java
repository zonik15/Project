package com.app.heyphil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ABDalisay on 9/27/2016.
 */
public class Facebook extends Activity {
    Button facebook;
    private CallbackManager callbackManager;
    ProgressDialog progress;
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        facebook=(Button) findViewById(R.id.facebook);
        progress=new ProgressDialog(Facebook.this);
        progress.setMessage("Please wait");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        facebook_id=f_name= m_name= l_name= gender= profile_image= full_name= email_id="";
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progress.show();
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    facebook_id=profile.getId();
                    f_name=profile.getFirstName();
                    m_name=profile.getMiddleName();
                    l_name=profile.getLastName();
                    full_name=profile.getName();
                    profile_image=profile.getProfilePictureUri(400, 400).toString();
                }
                Toast.makeText(Facebook.this,"Wait...",Toast.LENGTH_SHORT).show();
                //System.out.println("TOKEN ID+++++"+AccessToken.getCurrentAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                               // tv_continue.setVisibility(View.GONE);
                                //tv_skip.setVisibility(View.GONE);
                                facebook.setVisibility(View.GONE);
                               // rl_info.setVisibility(View.VISIBLE);
                                progress.dismiss();
                                // Application code
                                //System.out.println("TOKEN ID+++++"+object.getString("email")+object.getString("birthday"));
                                // et_firstname.setText(f_name);
                                // et_lastname.setText(l_name);
                                //  et_mi.setText(m_name);
                                // et_email.setText(object.getString("email"));
                                // registrationUrl="https://apps.philcare.com.ph/PhilcareWatsonTest/Members.svc/PhilcareWatsonMemberRegistration/?fromFB=1&email="+object.getString("email")+"&certNo="+Certno+"&userID="+facebook_id+"&profilePicURL="+profile_image+"&mobileNo=&userName=&password=&bldgNo=&street=&brgy=&city=&province=&homeNo=&birthDate="+bday+"&firstName="+et_firstname.getText().toString()+"&lastName="+et_lastname.getText().toString();
                                // new Register().execute();
                                //logoutFromFacebook();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Facebook.this,"cancel",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Facebook.this,"login failed",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Facebook.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "username"));
            }
        });
    }
}
