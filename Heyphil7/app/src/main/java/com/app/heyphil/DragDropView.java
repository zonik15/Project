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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
							TextView setting=(TextView)dialog.findViewById(R.id.setting);
							final TextView logout=(TextView)dialog.findViewById(R.id.logout);
							myaccount.setTypeface(tf);setting.setTypeface(tf);logout.setTypeface(tf);
							user.setImageBitmap(Data.bitmap);
							myaccount.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View view) {
									MyAccount();
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
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Boogaloo-Regular.ttf");
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
		dependents.setText(Data.dependent.toString().replaceAll("\\[|\\]", "").replaceAll(",","\n"));
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
	private void logout(){
		Data.logout=true;
		Intent i= new Intent(getContext(), LoginScreen.class);
		getContext().startActivity(i);
	}

}
