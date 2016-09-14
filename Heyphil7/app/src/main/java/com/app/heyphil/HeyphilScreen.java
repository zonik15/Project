package com.app.heyphil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.TextView;

public class HeyphilScreen extends Activity {
	private WebView		wv_heyphil;
	
	private TextView	tv_member_name;
	private TextView	tv_cert_no;
	private String      link;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heyphil_screen);
		
		tv_member_name=(TextView)findViewById(R.id.tv_membername);
		tv_cert_no=(TextView)findViewById(R.id.tv_certno);
		String membername= Data.Fname+" "+ Data.Lname;
		String certno=""+ Data.cert;
		tv_member_name.setText(membername);
		tv_cert_no.setText(certno);
		
		
		wv_heyphil=(WebView)findViewById(R.id.wv_heyphil);
		loadwebview();
	}
	@SuppressWarnings("deprecation")
	private void loadwebview(){
		String certno= Data.cert;
		link="https://heyphil.mybluemix.net/cert="+certno;
		wv_heyphil.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
		wv_heyphil.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
		wv_heyphil.getSettings().setAllowFileAccess(true);
		wv_heyphil.getSettings().setAppCacheEnabled(true);
		wv_heyphil.getSettings().setJavaScriptEnabled(true);
		wv_heyphil.getSettings().setPluginState(PluginState.ON);
		//wv_heyphil.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		wv_heyphil.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; SM-J700F Build/LMY48B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Mobile Safari/537.36");
		wv_heyphil.getSettings().setDomStorageEnabled(true);
		wv_heyphil.loadUrl(link);
	}

	  //AlertDialog
	  private void alertdialog(){
		  AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				    HeyphilScreen.this);

				// Setting Dialog Title
				alertDialog2.setTitle("Logout Account");

				// Setting Dialog Message
				alertDialog2.setMessage("Are you sure you want to logout?");

				// Setting Icon to Dialog
				alertDialog2.setIcon(R.drawable.icon1);

				// Setting Positive "Yes" Btn
				alertDialog2.setPositiveButton("YES",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				            // Write your code here to execute after dialog
				        	Intent intent=new Intent(getApplicationContext(),LoginScreen.class);
				        	startActivity(intent);
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
}
