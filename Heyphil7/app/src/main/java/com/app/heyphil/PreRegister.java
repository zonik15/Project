package com.app.heyphil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ABDalisay on 9/22/2016.
 */
public class PreRegister extends Activity {
    TextView tv_message;
    EditText et_cert;
    EditText et_birthday;
    Button  btn_verify;
    private Context context=this;
    JSONArray jsonArray = null;
    String certno, bday;
    String duplicate;
    String fname;
    String lname;
    String mismatch;
    String success;
    private Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_register);
        tv_message=(TextView)findViewById(R.id.message);
        et_cert=(EditText)findViewById(R.id.cert);
        et_birthday=(EditText)findViewById(R.id.birthday);
        btn_verify=(Button)findViewById(R.id.verify);
        et_cert.setAllCaps(true);
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PreRegister.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
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

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_birthday.setText(sdf.format(myCalendar.getTime()));
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
        if (!et_cert.getText().toString().trim().isEmpty()&&!et_birthday.getText().toString().trim().isEmpty()){
            certno=et_cert.getText().toString();
            bday=et_birthday.getText().toString();
            new Verify().execute();
        }
        else{
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setText("Unable to proceed. Please provide all needed information.");
        }
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class Verify extends AsyncTask<Void, Void, Void>
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
            String jsonStr = sh.makeServiceCall("https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/PhilcareWatsonMemberVerification/?certNo="+certno+"&birthDate="+bday, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("PhilcareWatsonMemberVerificationResult");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        duplicate=c.getString("DuplicateFlag");
                        fname=c.getString("FirstName");
                        lname=c.getString("LastName");
                        mismatch=c.getString("MismatchFlag");
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

            if(duplicate=="1"){
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("You have already an existing account.");
            }
            else if(mismatch=="1"){
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("Your Certificate Number and Birthday doesn't match!");
            }
            else if(success=="1"){
                Intent i=new Intent(PreRegister.this,Registration.class);
                i.putExtra("Cert",certno);
                i.putExtra("Bday",bday);
                i.putExtra("Firstname",fname);
                i.putExtra("Lastname",lname);
                startActivity(i);
                finish();
            }
        }
    }
}
