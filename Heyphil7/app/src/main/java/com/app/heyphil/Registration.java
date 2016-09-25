package com.app.heyphil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by ABDalisay on 9/21/2016.
 */
public class Registration extends Activity {
    RelativeLayout rl_info;
    RelativeLayout rl_success;
    Button facebook;
    TextView tv_skip, tv_continue, tv_message;
    EditText et_firstname,et_lastname,et_mi,et_email,et_contact,et_password,et_confirmpassword;
    Button btn_submit, btn_cancel;
    private CallbackManager callbackManager;
    ProgressDialog progress;
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        rl_info=(RelativeLayout)findViewById(R.id.info);
        rl_success=(RelativeLayout)findViewById(R.id.success);
        facebook=(Button) findViewById(R.id.facebook);
        tv_skip=(TextView)findViewById(R.id.tv_skip);
        tv_continue=(TextView)findViewById(R.id.tv_continue);
        tv_message=(TextView)findViewById(R.id.tv_message);
        et_firstname=(EditText)findViewById(R.id.et_firstname);
        et_lastname=(EditText)findViewById(R.id.et_lastname);
        et_mi=(EditText)findViewById(R.id.et_mi);
        et_email=(EditText)findViewById(R.id.et_email);
        et_contact=(EditText)findViewById(R.id.et_contact);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirmpassword=(EditText)findViewById(R.id.et_confirm_password);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        progress=new ProgressDialog(Registration.this);
        progress.setMessage("Please wait");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        facebook_id=f_name= m_name= l_name= gender= profile_image= full_name= email_id="";
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_continue.setVisibility(View.GONE);
                tv_skip.setVisibility(View.GONE);
                facebook.setVisibility(View.GONE);
                rl_info.setVisibility(View.VISIBLE);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_info.setVisibility(View.GONE);
                rl_success.setVisibility(View.VISIBLE);
                tv_message.setText("Thank you for registering! An email confirmation has been automatically sent to the email you have provided.");
            }
        });
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
                Toast.makeText(Registration.this,"Wait...",Toast.LENGTH_SHORT).show();
                //System.out.println("TOKEN ID+++++"+AccessToken.getCurrentAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                tv_continue.setVisibility(View.GONE);
                                tv_skip.setVisibility(View.GONE);
                                facebook.setVisibility(View.GONE);
                                rl_info.setVisibility(View.VISIBLE);
                                    progress.dismiss();
                                // Application code
                                try {
                                    //System.out.println("TOKEN ID+++++"+object.getString("email")+object.getString("birthday"));
                                    et_firstname.setText(f_name);
                                    et_lastname.setText(l_name);
                                    et_mi.setText(m_name);
                                    et_email.setText(object.getString("email"));
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
                LoginManager.getInstance().logInWithReadPermissions(Registration.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday", "username"));
            }
        });
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
}
