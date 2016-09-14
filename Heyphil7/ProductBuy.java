package com.app.heyphil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by ABDalisay on 8/1/2016.
 */
public class ProductBuy extends Activity {
    TextView tv_PromotionalCode;TextView tv_Fname;TextView tv_Lname;
    TextView tv_BA;TextView tv_BillingAddress;TextView tv_Bldg_no;
    TextView tv_Street;TextView tv_Brgy;TextView tv_City;
    TextView tv_Province;TextView tv_Postalcode;TextView tv_Email;
    TextView tv_Mobile;TextView tv_Creditcard;TextView tv_Other;
    TextView tv_Terms;TextView tv_Payment;TextView tv_Product;
    TextView tv_Price;TextView tv_Amount;
    EditText et_PromoCode;EditText et_Fname;EditText et_Lname;
    EditText et_Bldg_no;EditText et_Street;EditText et_Brgy;
    EditText et_City;EditText et_Province;EditText et_Postalcode;
    EditText et_Email;EditText et_Mobile;EditText et_Quantity;
    CheckBox cb_Creditcard;CheckBox cb_Other; CheckBox cb_Terms;
    Button btn_Checkout;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_buy);
     final Bundle product=getIntent().getExtras();
     Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/Boogaloo-Regular.ttf");
     tv_PromotionalCode=(TextView)findViewById(R.id.tv_code);
     tv_BA=(TextView)findViewById(R.id.tv_BA);
     tv_Fname=(TextView)findViewById(R.id.tv_fname);
     tv_Lname=(TextView)findViewById(R.id.tv_lname);
     tv_BillingAddress=(TextView)findViewById(R.id.tv_billing_address);
     tv_Bldg_no=(TextView)findViewById(R.id.tv_bldg_no);
     tv_Street=(TextView)findViewById(R.id.tv_street);
     tv_Brgy=(TextView)findViewById(R.id.tv_brgy);
     tv_City=(TextView)findViewById(R.id.tv_city);
     tv_Province=(TextView)findViewById(R.id.tv_province);
     tv_Postalcode=(TextView)findViewById(R.id.tv_postalcode);
     tv_Email=(TextView)findViewById(R.id.tv_email);
     tv_Mobile=(TextView)findViewById(R.id.tv_mobile_no);
     tv_Payment=(TextView)findViewById(R.id.tv_paymentmode);
     tv_Creditcard=(TextView)findViewById(R.id.tv_creditcard);
     tv_Other=(TextView)findViewById(R.id.tv_other);
     tv_Terms=(TextView)findViewById(R.id.tv_btn_t_and_c);
     tv_Product=(TextView)findViewById(R.id.tv_productname);
     tv_Price=(TextView)findViewById(R.id.tv_productprice);
     tv_Amount=(TextView)findViewById(R.id.tv_productamount);
     et_PromoCode=(EditText)findViewById(R.id.et_code);
     et_Fname=(EditText)findViewById(R.id.et_fname);
     et_Lname=(EditText)findViewById(R.id.et_lname);
     et_Bldg_no=(EditText)findViewById(R.id.et_bldg_no);
     et_Street=(EditText)findViewById(R.id.et_street);
     et_Brgy=(EditText)findViewById(R.id.et_brgy);
     et_City=(EditText)findViewById(R.id.et_city);
     et_Province=(EditText)findViewById(R.id.et_province);
     et_Postalcode=(EditText)findViewById(R.id.et_postalcode);
     et_Email=(EditText)findViewById(R.id.et_email);
     et_Mobile=(EditText)findViewById(R.id.et_mobile_no);
     et_Quantity=(EditText)findViewById(R.id.tv_productqty);
     btn_Checkout=(Button)findViewById(R.id.btn_checkout);
     cb_Creditcard=(CheckBox)findViewById(R.id.cb_creditcard);
     cb_Other=(CheckBox)findViewById(R.id.cb_other);
     cb_Terms=(CheckBox)findViewById(R.id.cb_agreement);
     tv_PromotionalCode.setTypeface(tf);tv_BA.setTypeface(tf);tv_Fname.setTypeface(tf);tv_Lname.setTypeface(tf);tv_BillingAddress.setTypeface(tf);
     tv_Bldg_no.setTypeface(tf);tv_Street.setTypeface(tf);tv_Brgy.setTypeface(tf);tv_City.setTypeface(tf);tv_Province.setTypeface(tf);tv_Postalcode.setTypeface(tf);
     tv_Email.setTypeface(tf);tv_Mobile.setTypeface(tf);tv_Payment.setTypeface(tf);tv_Creditcard.setTypeface(tf);tv_Other.setTypeface(tf);
     tv_Terms.setTypeface(tf);tv_Product.setText(product.getString("Product"));tv_Price.setText(product.getString("Price"));tv_Amount.setText(""+Integer.parseInt(et_Quantity.getText().toString())* Integer.parseInt(product.getString("Price")));
     et_PromoCode.setTypeface(tf);et_Fname.setTypeface(tf);et_Lname.setTypeface(tf);et_Bldg_no.setTypeface(tf);et_Street.setTypeface(tf);
     et_Brgy.setTypeface(tf);et_City.setTypeface(tf);et_Province.setTypeface(tf);et_Postalcode.setTypeface(tf);et_Email.setTypeface(tf);
     et_Mobile.setTypeface(tf);btn_Checkout.setTypeface(tf);
     et_Fname.setText(Data.first_name);
     et_Lname.setText(Data.last_name);
     et_Bldg_no.setText(Data.bldg_no);
     et_Street.setText(Data.street);
     et_Brgy.setText(Data.brgy);
     et_City.setText(Data.city);
     et_Province.setText(Data.province);
     et_Email.setText(Data.email);
     et_Mobile.setText(Data.mobile_number.replace("+","").replace("63 ",""));
     et_Quantity.setOnKeyListener(new View.OnKeyListener() {
         public boolean onKey(View v, int keyCode, KeyEvent event) {
             if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                     tv_Amount.setText(""+Integer.parseInt(et_Quantity.getText().toString()) * Integer.parseInt(product.getString("Price")));
             }
             return false;
         }
     });
    }
}
