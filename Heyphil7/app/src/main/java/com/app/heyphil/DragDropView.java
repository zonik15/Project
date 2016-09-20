package com.app.heyphil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * @author Niko
 * View Container to place the view wanted to be dragged
 */
public class DragDropView extends FrameLayout {
	/**
	 * Default Constructor
	 * @param context
	 */
	float m_downXValue;
	Context context;
	public DragDropView(Context context) {
		super(context);
	}
	
	/**
	 * Default Constructor
	 * @param context
	 * @param attrs
	 */
	public DragDropView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Default Constructor
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DragDropView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/** Adding draggable object to the dragView
	 * @param// draggableView - object to be dragged
	 * @param - x horizontal position of the view
	 * @param - y vertical position of the view
	 * @param - width width of the view
	 * @param - height height of the view
	 */
	public void AddDraggableView(View draggableObject, int x, int y, int width, int height) {
		LayoutParams lpDraggableView = new LayoutParams(width, height);
		lpDraggableView.gravity = Gravity.TOP;
		lpDraggableView.leftMargin = x;
		lpDraggableView.topMargin = y;
		if(draggableObject instanceof ImageView) {
			ImageView ivDrag = (ImageView) draggableObject;
			ivDrag.setLayoutParams(lpDraggableView);
			ivDrag.setOnTouchListener(OnTouchToDrag);
			this.addView(ivDrag);
		}

		//TODO implement to do other type of view
//		else if(draggableObject instanceof TextView) {
//			TextView tvDrag = (TextView) draggableObject;
//			tvDrag.setLayoutParams(lpDraggableView);
//			tvDrag.setOnTouchListener(OnTouchToDrag);
//			this.addView(tvDrag);
//		} 
//		else if(draggableObject instanceof Button) {
//			Button btnDrag = (Button) draggableObject;
//			btnDrag.setLayoutParams(lpDraggableView);
//			btnDrag.setOnTouchListener(OnTouchToDrag);
//			this.addView(btnDrag);
//		}
		
	}
	
	/**
	 * Draggable object ontouch listener
	 * Handle the movement of the object when dragged and dropped
	 */
	private OnTouchListener OnTouchToDrag = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			LayoutParams dragParam = (LayoutParams) v.getLayoutParams();
			switch(event.getAction())
			{
				case MotionEvent.ACTION_MOVE:
				{
					m_downXValue = event.getX();
					dragParam.topMargin = (int)event.getRawY() - (v.getHeight());
					dragParam.leftMargin = (int)event.getRawX() - (v.getWidth()/2);
					v.setLayoutParams(dragParam);
					break;
				}
				case MotionEvent.ACTION_UP:
				{
					float currentX = event.getX();
					//	dragParam.height = 150;
				//	dragParam.width = 150;
					dragParam.height = v.getHeight();
                		        dragParam.width = v.getWidth();
					dragParam.topMargin = (int)event.getRawY() - (v.getHeight());
					dragParam.leftMargin = (int)event.getRawX() - (v.getWidth()/2);
					v.setLayoutParams(dragParam);
					if(m_downXValue==currentX){
						System.out.println(""+m_downXValue+""+currentX);
							final Dialog dialog = new Dialog(getContext());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.menu_layout);
							//dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
							Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
							CircularImageView user=(CircularImageView)dialog.findViewById(R.id.circularImageView);
							TextView myaccount=(TextView)dialog.findViewById(R.id.myaccount);
							final TextView bmi=(TextView)dialog.findViewById(R.id.bmi);
							TextView calorie=(TextView)dialog.findViewById(R.id.calorie);
							TextView setting=(TextView)dialog.findViewById(R.id.setting);
							final TextView logout=(TextView)dialog.findViewById(R.id.logout);
							myaccount.setTypeface(tf);bmi.setTypeface(tf);calorie.setTypeface(tf);setting.setTypeface(tf);logout.setTypeface(tf);
							user.setImageBitmap(Data.bitmap);
							myaccount.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View view) {
									MyAccount();
								}
							});
							bmi.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									bmi();
								}
							});
						calorie.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								calories();
							}
						});
							logout.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									logout();
								}
							});
							dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
							dialog.show();

					}
					break;
				}
				case MotionEvent.ACTION_DOWN:
				{
					m_downXValue = event.getX();
				//	dragParam.height = 100;
				//	dragParam.width = 100;
				        dragParam.height = v.getHeight();
                		        dragParam.width = v.getWidth();
					v.setLayoutParams(dragParam);
					break;
				}
			}
			return true;
		}

	};
	private void MyAccount(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.my_account);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		final RelativeLayout rln=(RelativeLayout)dialog.findViewById(R.id.first);
		final RelativeLayout rledit=(RelativeLayout)dialog.findViewById(R.id.editinfo);
		final LinearLayout lld=(LinearLayout)dialog.findViewById(R.id.ll_dependent);
		final RelativeLayout rlb=(RelativeLayout)dialog.findViewById(R.id.second);
		TextView tv_lastname=(TextView)dialog.findViewById(R.id.tv_lastname);
		TextView lastname=(TextView)dialog.findViewById(R.id.lastname);
		TextView tv_firstname=(TextView)dialog.findViewById(R.id.tv_firstname);
		TextView firstname=(TextView)dialog.findViewById(R.id.firstname);
		TextView tv_middle=(TextView)dialog.findViewById(R.id.tv_middle);
		TextView middle=(TextView)dialog.findViewById(R.id.middle);
		TextView tv_gender=(TextView)dialog.findViewById(R.id.tv_gender);
		TextView gender=(TextView)dialog.findViewById(R.id.gender);
		TextView tv_civil=(TextView)dialog.findViewById(R.id.tv_civil);
		TextView civil=(TextView)dialog.findViewById(R.id.civil);
		TextView tv_bday=(TextView)dialog.findViewById(R.id.tv_bday);
		TextView bday=(TextView)dialog.findViewById(R.id.bday);
		TextView tv_dependents=(TextView)dialog.findViewById(R.id.tv_dependents);
		TextView dependents=(TextView)dialog.findViewById(R.id.dependents);
		final TextView tv_homeaddress=(TextView)dialog.findViewById(R.id.tv_homeaddress);
		final TextView homeaddress=(TextView)dialog.findViewById(R.id.homeaddress);
		final TextView tv_mobile=(TextView)dialog.findViewById(R.id.tv_mobile);
		final TextView mobile=(TextView)dialog.findViewById(R.id.mobile);
		TextView tv_certificate=(TextView)dialog.findViewById(R.id.tv_certicate);
		TextView certificate=(TextView)dialog.findViewById(R.id.certificate);
		TextView tv_membertype=(TextView)dialog.findViewById(R.id.tv_membertype);
		TextView membertype=(TextView)dialog.findViewById(R.id.membertype);
		TextView tv_agreement=(TextView)dialog.findViewById(R.id.tv_agreement);
		TextView agreement=(TextView)dialog.findViewById(R.id.agreement);
		TextView tv_effective=(TextView)dialog.findViewById(R.id.tv_effective);
		TextView effective=(TextView)dialog.findViewById(R.id.effective);
		TextView tv_expiration=(TextView)dialog.findViewById(R.id.tv_expiration);
		TextView expiration=(TextView)dialog.findViewById(R.id.expiration);
		TextView tv_agreementno=(TextView)dialog.findViewById(R.id.tv_agreementno);
		TextView agreementno=(TextView)dialog.findViewById(R.id.agreementno);
		TextView tv_policyno=(TextView)dialog.findViewById(R.id.tv_policyno);
		TextView policyno=(TextView)dialog.findViewById(R.id.policyno);
		TextView tv_riders=(TextView)dialog.findViewById(R.id.tv_riders);
		TextView riders=(TextView)dialog.findViewById(R.id.riders);
		TextView tv_ape=(TextView)dialog.findViewById(R.id.tv_ape);
		TextView ape=(TextView)dialog.findViewById(R.id.ape);
		TextView tv_back=(TextView)dialog.findViewById(R.id.tv_back);
		TextView tv_next=(TextView)dialog.findViewById(R.id.tv_next);
		TextView tv_edit=(TextView)dialog.findViewById(R.id.edit);
		Button save=(Button) dialog.findViewById(R.id.save);
		Button cancel=(Button) dialog.findViewById(R.id.cancel);
		save.setTypeface(tf);
		cancel.setTypeface(tf);
		tv_next.setTypeface(tf);
		tv_back.setTypeface(tf);
		tv_edit.setTypeface(tf);
		tv_lastname.setTypeface(tf);
		lastname.setTypeface(tf);
		tv_firstname.setTypeface(tf);
		firstname.setTypeface(tf);
		tv_middle.setTypeface(tf);
		middle.setTypeface(tf);
		tv_gender.setTypeface(tf);
		gender.setTypeface(tf);
		tv_civil.setTypeface(tf);
		civil.setTypeface(tf);
		tv_bday.setTypeface(tf);
		bday.setTypeface(tf);
		tv_dependents.setTypeface(tf);
		dependents.setTypeface(tf);
		tv_homeaddress.setTypeface(tf);
		homeaddress.setTypeface(tf);
		tv_mobile.setTypeface(tf);
		mobile.setTypeface(tf);
		tv_certificate.setTypeface(tf);
		certificate.setTypeface(tf);
		tv_membertype.setTypeface(tf);
		membertype.setTypeface(tf);
		tv_agreement.setTypeface(tf);
		agreement.setTypeface(tf);
		tv_effective.setTypeface(tf);
		effective.setTypeface(tf);
		tv_expiration.setTypeface(tf);
		expiration.setTypeface(tf);
		tv_agreementno.setTypeface(tf);
		agreementno.setTypeface(tf);
		tv_policyno.setTypeface(tf);
		policyno.setTypeface(tf);
		tv_riders.setTypeface(tf);
		riders.setTypeface(tf);
		tv_ape.setTypeface(tf);
		ape.setTypeface(tf);
		lastname.setText(Data.last_name);
		firstname.setText(Data.first_name);
		middle.setText(Data.mi);
		gender.setText(Data.sex);
		civil.setText(Data.civil_status);
		bday.setText(Data.birthday);
		dependents.setText(Data.dependent.toString().replaceAll("\\[|\\]", "").replaceAll(" ","").replaceAll(",","\n"));
		homeaddress.setText(Data.home_address);
		mobile.setText(Data.mobile_number);
		certificate.setText(Data.cert);
		membertype.setText(Data.member_type);
		agreement.setText(Data.agreement_name);
		effective.setText(Data.effectivity_date);
		expiration.setText(Data.expiration_date);
		policyno.setText(Data.policyno);
		agreementno.setText(Data.agreement_no);
		riders.setText(Data.riders);
		ape.setText(Data.ape);
		rledit.setVisibility(GONE);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rledit.setVisibility(GONE);
				tv_homeaddress.setVisibility(VISIBLE);
				homeaddress.setVisibility(VISIBLE);
				tv_mobile.setVisibility(VISIBLE);
				mobile.setVisibility(VISIBLE);
			}
		});
		tv_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rledit.setVisibility(VISIBLE);
				tv_homeaddress.setVisibility(GONE);
				homeaddress.setVisibility(GONE);
				tv_mobile.setVisibility(GONE);
				mobile.setVisibility(GONE);
			}
		});
		tv_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				rln.setVisibility(View.GONE);
				lld.setVisibility(GONE);
				rlb.setVisibility(View.VISIBLE);
			}
		});
		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				rln.setVisibility(View.VISIBLE);
				lld.setVisibility(VISIBLE);
				rlb.setVisibility(View.GONE);
			}
		});
		dialog.show();
	}
	public void calories(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.calorie_layout);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_calorie=(TextView)dialog.findViewById(R.id.tv_calorie);
		TextView tv_gender=(TextView)dialog.findViewById(R.id.tv_gender);
		TextView tv_age=(TextView)dialog.findViewById(R.id.tv_age);
		TextView tv_weight=(TextView)dialog.findViewById(R.id.tv_weight);
		TextView tv_height=(TextView)dialog.findViewById(R.id.tv_height);
		Spinner sp_gender=(Spinner) dialog.findViewById(R.id.sp_gender);
		EditText et_age=(EditText)dialog.findViewById(R.id.et_age);
		EditText et_weight=(EditText)dialog.findViewById(R.id.et_weight);
		EditText et_height=(EditText)dialog.findViewById(R.id.et_height);
		Spinner sp_exercise=(Spinner) dialog.findViewById(R.id.sp_exercise);
		Button btn_calculate=(Button)dialog.findViewById(R.id.btn_calculate);
		Button btn_reset=(Button)dialog.findViewById(R.id.btn_reset);
		tv_calorie.setTypeface(tf);tv_gender.setTypeface(tf);tv_age.setTypeface(tf);tv_weight.setTypeface(tf);tv_height.setTypeface(tf);
		et_age.setTypeface(tf);et_weight.setTypeface(tf);et_height.setTypeface(tf);btn_calculate.setTypeface(tf);btn_reset.setTypeface(tf);
		String[] gender = new String[] {"Male", "Female"};
		ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, gender);
		gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_gender.setAdapter(gender_adapter);
		String[] exercise = new String[] {"No exercise or little exercise", "Light exercise or sports, 1-3x per week", "Moderate exercise and/or play sports, 3-5x per week", "Strenuous sports or hard exercise, 6-7x per week", "Very physically challenging jobs or exercise, 2x per day"};
		ArrayAdapter<String> exercise_adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, exercise);
		exercise_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_exercise.setAdapter(exercise_adapter);
		btn_calculate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		btn_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		dialog.show();

	}
	public void bmi(){
		Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);
		final Dialog dialog = new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bmi_layout);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_bmi=(TextView)dialog.findViewById(R.id.tv_bmi);
		TextView tv_weight=(TextView)dialog.findViewById(R.id.tv_weight);
		final EditText et_weight=(EditText) dialog.findViewById(R.id.et_weight);
		TextView tv_height=(TextView)dialog.findViewById(R.id.tv_height);
		final EditText et_height=(EditText) dialog.findViewById(R.id.et_height);
		Button btn_calculate=(Button)dialog.findViewById(R.id.btn_calculate);
		Button btn_reset=(Button)dialog.findViewById(R.id.btn_reset);
		final TextView bmi_result=(TextView)dialog.findViewById(R.id.bmi_result);
		final ImageView bmi_image=(ImageView)dialog.findViewById(R.id.bmi_image);
		final TextView tv_result=(TextView)dialog.findViewById(R.id.tv_result);
		final Spinner sp_weight=(Spinner)dialog.findViewById(R.id.sp_weight);
		final Spinner sp_height=(Spinner)dialog.findViewById(R.id.sp_height);
		tv_bmi.setTypeface(tf);tv_height.setTypeface(tf);tv_weight.setTypeface(tf);bmi_result.setTypeface(tf);
		et_height.setTypeface(tf);et_weight.setTypeface(tf);btn_calculate.setTypeface(tf);btn_reset.setTypeface(tf);tv_result.setTypeface(tf);
		String[] weight = new String[] {"kg", "lbs"};
		ArrayAdapter<String> weight_adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, weight);
		weight_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_weight.setAdapter(weight_adapter);
		String[] height = new String[] {"m", "cm", "inch", "ft"};
		ArrayAdapter<String> height_adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, height);
		height_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_height.setAdapter(height_adapter);
		sp_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(sp_weight.getItemAtPosition(position).toString()=="lbs")
				{
					if(et_weight.getText().toString().isEmpty())
					{

					}
					else
					{
						Data.Weight=Double.parseDouble(et_weight.getText().toString())*0.45;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		et_weight.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus)
				{
					if(et_weight.getText().toString().isEmpty())
					{

					}
					else
					{
						if(sp_weight.getSelectedItem().toString()=="lbs")
						{
							Data.Weight = Double.parseDouble(et_weight.getText().toString()) * 0.45;
						}
						else {
							Data.Weight = Double.parseDouble(et_weight.getText().toString());
						}
					}
				}
				else
				{
					if(et_weight.getText().toString().isEmpty())
					{

					}
					else
					{
						if(sp_weight.getSelectedItem().toString()=="lbs")
						{
							Data.Weight = Double.parseDouble(et_weight.getText().toString()) * 0.45;
						}
						else {
							Data.Weight = Double.parseDouble(et_weight.getText().toString());
						}
					}
				}
			}
		});
		sp_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(sp_height.getItemAtPosition(position).toString()=="cm")
				{
					if(et_height.getText().toString().isEmpty())
					{

					}
					else
					{
						Data.Height=Double.parseDouble(et_height.getText().toString())*0.01;
					}
				}
				else if(sp_height.getItemAtPosition(position).toString()=="inch")
				{
					if(et_height.getText().toString().isEmpty())
					{

					}
					else
					{
						Data.Height=Double.parseDouble(et_height.getText().toString())*0.025;
					}
				}
				else if(sp_height.getItemAtPosition(position).toString()=="ft")
				{
					if(et_height.getText().toString().isEmpty())
					{

					}
					else
					{
						Data.Height=Double.parseDouble(et_height.getText().toString())*0.30;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		et_height.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus)
				{
					if(et_height.getText().toString().isEmpty()){

					}
					else {
						if(sp_height.getSelectedItem().toString()=="cm")
						{
							Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.01;
						}
						else if(sp_height.getSelectedItem().toString()=="inch")
						{
							Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.025;
						}
						else if(sp_height.getSelectedItem().toString()=="ft")
						{
							Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.30;
						}
						else {
							Data.Height = Double.parseDouble(et_height.getText().toString());
						}
					}
				}
				else
				{
					if(et_height.getText().toString().isEmpty()){

					}
					else {
						if(sp_height.getSelectedItem().toString()=="cm")
						{
							Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.01;
						}
						else if(sp_height.getSelectedItem().toString()=="inch")
						{
							Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.025;
						}
						else if(sp_height.getSelectedItem().toString()=="ft")
						{
							Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.30;
						}
						else {
							Data.Height = Double.parseDouble(et_height.getText().toString());
						}
					}
				}
			}
		});
		btn_calculate.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(et_weight.getText().toString().isEmpty())
				{

				}
				else
				{
					if(sp_weight.getSelectedItem().toString()=="lbs")
					{
						Data.Weight = Double.parseDouble(et_weight.getText().toString()) * 0.45;
					}
					else {
						Data.Weight = Double.parseDouble(et_weight.getText().toString());
					}
				}
				if(et_height.getText().toString().isEmpty()){

				}
				else {
					if(sp_height.getSelectedItem().toString()=="cm")
					{
						Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.01;
					}
					else if(sp_height.getSelectedItem().toString()=="inch")
					{
						Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.025;
					}
					else if(sp_height.getSelectedItem().toString()=="ft")
					{
						Data.Height = Double.parseDouble(et_height.getText().toString()) * 0.30;
					}
					else {
						Data.Height = Double.parseDouble(et_height.getText().toString());
					}
				}
				return false;
			}
		});
		btn_calculate.setOnClickListener(new OnClickListener() {
			Double bmi;
			@Override
			public void onClick(View v) {

				if(!et_weight.getText().toString().trim().isEmpty()&&!et_height.getText().toString().trim().isEmpty())
				{
					bmi = (Data.Weight /(Data.Height *Data.Height));
					bmi_result.setVisibility(VISIBLE);
					bmi_result.setText("BMI RESULT: "+Math.round(bmi));
					if(bmi<18.5){
						bmi_image.setVisibility(VISIBLE);
						bmi_image.setImageResource(R.drawable.underweight_image);
						tv_result.setVisibility(VISIBLE);
						tv_result.setText("UNDERWEIGHT");
					}
					else if(bmi>18.5 && bmi<24.5){
						bmi_image.setVisibility(VISIBLE);
						bmi_image.setImageResource(R.drawable.normal_image);
						tv_result.setVisibility(VISIBLE);
						tv_result.setText("NORMAL");
					}
					else if(bmi>24.5 && bmi<29.50){
						bmi_image.setVisibility(VISIBLE);
						bmi_image.setImageResource(R.drawable.overweight_image);
						tv_result.setVisibility(VISIBLE);
						tv_result.setText("OVERWEIGHT");
					}
					else if(bmi>29.5){
						bmi_image.setVisibility(VISIBLE);
						bmi_image.setImageResource(R.drawable.obese_image);
						tv_result.setVisibility(VISIBLE);
						tv_result.setText("OBESITY");
					}
					//Toast.makeText(getContext(), "BMI" + bmi, Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getContext(), "Please fill up the required data!", Toast.LENGTH_SHORT).show();
				}

			}
		});

		btn_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				bmi();
			}
		});
		dialog.show();

	}
	private void logout(){
		Data.logout=true;
		Intent i= new Intent(getContext(), LoginScreen.class);
		getContext().startActivity(i);
	}

}
