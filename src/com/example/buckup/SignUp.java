package com.example.buckup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adapter.TabsPagerAdapter;
import com.example.fragments.SignUp1;
import com.example.fragments.SignUp2;
import com.example.fragments.SignUp3;
import com.example.fragments.SignUp4;
import com.example.fragments.SignUp5;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

public class SignUp extends FragmentActivity {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	Button continue_button;
	public static TextView page_no;
	ImageView icon1 , icon2 , icon3 , icon4,icon5;
	int count = 0;
	int backCount = 0;
	Boolean isConnected;
	 TransparentProgressDialog db;
	 Button back;
	 
	 SharedPreferences sp;
	
	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				SignUp.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

					}
				});
		localBuilder.create().show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		
		continue_button = (Button) findViewById(R.id.continue_button);
		page_no = (TextView) findViewById(R.id.page_no);
		icon1 = (ImageView) findViewById(R.id.icon1);
		icon2 = (ImageView) findViewById(R.id.icon2);
		icon3 = (ImageView) findViewById(R.id.icon3);
		icon4 = (ImageView) findViewById(R.id.icon4);
		icon5 = (ImageView) findViewById(R.id.icon5);
		back = (Button) findViewById(R.id.back);
 		
		isConnected = NetConnection.checkInternetConnectionn(SignUp.this);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		

		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
		viewPager.setOnTouchListener(new OnTouchListener()
	    {           
	        
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return true;
			}
	    });
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backCount = getItem(+1);
				if(backCount>1){
					viewPager.setCurrentItem(getItem(-1), true);
				} 
				else if(backCount==1){
					Intent i = new Intent(SignUp.this , Login.class);
					startActivity(i);
				}
			}
		});
		
		continue_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("count==",""+getItem(+1));
				Boolean isTrueORFalse;
				count = getItem(+1);
				if(count==1){
					isTrueORFalse =	SignUp1.checkDetailIsFilledOrNot();
					if(isTrueORFalse){
						
					 if(Constants.PROMOCODE.trim().length()>=1){
						checkForPromoCode();
						} else {
							CheckForUsernameEmailExistense();
						//viewPager.setCurrentItem(getItem(+1), true);
						}
					} 
				} 
				else if(count==2){
					isTrueORFalse =	SignUp5.checkDetailIsFilledOrNot();
				
					if(isTrueORFalse){
						viewPager.setCurrentItem(getItem(+1), true);
					} 
				}
				else if(count==3){
					isTrueORFalse =	SignUp2.checkDetailIsFilledOrNot();
					if(isTrueORFalse){
						viewPager.setCurrentItem(getItem(+1), true);
					}
				} else if(count==4){
					isTrueORFalse =	SignUp3.checkDetailIsFilledOrNot();
					if(isTrueORFalse){
						viewPager.setCurrentItem(getItem(+1), true);
					}
				} else if(count==5){
					
					isTrueORFalse =	SignUp4.checkDetailIsFilledOrNot();
					if(isTrueORFalse){
						//viewPager.setCurrentItem(getItem(+1), true);
						// implement signup API
						if (isConnected) {
							
							sp.edit().clear().commit();
							new SignUp_info(Constants.EMAIL, Constants.PASSWORD,
									Constants.ZIP_CODE)
									.execute(new Void[0]);
						} else {
							showAlertToUser(Constants.NO_INTERNET);
						}
					}
				}
	
			}
		});
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
				if(arg0 ==0){
					page_no.setText("Page 1 of 5");
					icon1.setImageResource(R.drawable.active_round);
					icon2.setImageResource(R.drawable.in_active_round);
					icon3.setImageResource(R.drawable.in_active_round);
					icon4.setImageResource(R.drawable.in_active_round);
				} else if(arg0 ==1){
					page_no.setText("Page 2 of 5");
					icon1.setImageResource(R.drawable.in_active_round);
					icon2.setImageResource(R.drawable.active_round);
					icon3.setImageResource(R.drawable.in_active_round);
					icon4.setImageResource(R.drawable.in_active_round);
					icon5.setImageResource(R.drawable.in_active_round);
				} else if(arg0 ==2){
					page_no.setText("Page 3 of 5");
					icon1.setImageResource(R.drawable.in_active_round);
					icon2.setImageResource(R.drawable.in_active_round);
					icon3.setImageResource(R.drawable.active_round);
					icon4.setImageResource(R.drawable.in_active_round);
					icon5.setImageResource(R.drawable.in_active_round);
				} else if(arg0 ==3){
					page_no.setText("Page 4 of 5");
					icon1.setImageResource(R.drawable.in_active_round);
					icon2.setImageResource(R.drawable.in_active_round);
					icon3.setImageResource(R.drawable.in_active_round);
					icon4.setImageResource(R.drawable.active_round);
					icon5.setImageResource(R.drawable.in_active_round);
				}
				else if(arg0 ==4){
					page_no.setText("Page 5 of 5");
					icon1.setImageResource(R.drawable.in_active_round);
					icon2.setImageResource(R.drawable.in_active_round);
					icon3.setImageResource(R.drawable.in_active_round);
					icon4.setImageResource(R.drawable.in_active_round);
					icon5.setImageResource(R.drawable.active_round);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
			}
		});
		
	}



	protected void CheckForUsernameEmailExistense() {
		if (isConnected) {
			new CheckUsernameEmail(Constants.EMAIL , Constants.USERNAME).execute(new Void[0]);
		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}
	}

	protected void checkForPromoCode() {
		if (isConnected) {
			new CheckPRomoCode(Constants.PROMOCODE).execute(new Void[0]);
		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}
	}

	protected int getItem(int i) {
		return viewPager.getCurrentItem() + i;
	}
	public class SignUp_info extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	
		String email , password,zip;
		
		

		HashMap result = new HashMap();
		ArrayList localArrayList = new ArrayList();
		
		
		public SignUp_info(String eMAIL, String pASSWORD, String zIP_CODE) {
			
			this.email = eMAIL;
			this.password = pASSWORD;
			this.zip = zIP_CODE;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("access_token", Constants.ACESS_TOKEN));
				localArrayList.add(new BasicNameValuePair("emailid", Constants.EMAIL));
				localArrayList.add(new BasicNameValuePair("username",Constants.USERNAME));
				localArrayList.add(new BasicNameValuePair("password", Constants.PASSWORD));
				localArrayList.add(new BasicNameValuePair("fname", Constants.FIRSTNAME_SIGNUP));
				localArrayList.add(new BasicNameValuePair("lname", Constants.LASTNAME_SIGNUP));
				localArrayList.add(new BasicNameValuePair("zip", Constants.ZIP_CODE));
				localArrayList.add(new BasicNameValuePair("phone", ""));
				localArrayList.add(new BasicNameValuePair("address",""));
				localArrayList.add(new BasicNameValuePair("used_promocode_id",Constants.PROMOCODE_ID));
				
			
				
				result = function.SignUp_info(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			try {
				if(((String) result.get(Constants.RESPONSE_KEY)).equalsIgnoreCase("true")){
					Constants.USER_ID = (String) result.get(Constants.USER_ID_KEY);
					
					new insert_card_info(Constants.SPINNER_TEXT, Constants.NAME_ON_CARD,
					Constants.CARD_NO, Constants.EXPIRATION_DATE  , Constants.CVV_NUMBER,Constants.USER_ID)
					.execute(new Void[0]);

				} else {
					
					showAlertToUser((String) result.get(Constants.MESSAGE_WHAT_HAPPEN_KEY));
					
				}
			} catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}
		}
		
	

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(SignUp.this,
					R.drawable.loading);
			db.show();

		}

	}
	
	public class insert_card_info extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		
		String card_type, name_on_card, card_number, expiry_date , userID , cVV;

		HashMap result = new HashMap();
		ArrayList localArrayList = new ArrayList();

		

		public insert_card_info(String sPINNER_TEXT, String nAME_ON_CARD2,
				String cARD_NO, String eXPIRATION_DATE,String cVV, String uSER_ID) {
			this.card_type = sPINNER_TEXT;
			this.name_on_card = nAME_ON_CARD2;
			this.card_number = cARD_NO;
			this.expiry_date = eXPIRATION_DATE;
			this.userID = uSER_ID;
			this.cVV = cVV;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("card_type",
						this.card_type));
				localArrayList.add(new BasicNameValuePair("name_on_card",
						this.name_on_card));
				localArrayList.add(new BasicNameValuePair("card_number",
						this.card_number));
				localArrayList.add(new BasicNameValuePair("expiry_date",
						this.expiry_date));
				localArrayList.add(new BasicNameValuePair("cvv_number",
						this.cVV));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						this.userID));

				result = function.insert_cardInfo(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			try {
				if (((String) result.get(Constants.RESPONSE_KEY))
						.equalsIgnoreCase("true")) {
					Intent i = new Intent(SignUp.this , Login.class);
					startActivity(i);
				} else {
				
					showAlertToUser("Something went wrong.Please try again.");

				}
			} catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(SignUp.this,
					R.drawable.loading);
			db.show();

		}

	}

	public class CheckPRomoCode extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	
		String promocode;
		
		

		HashMap result = new HashMap();
		ArrayList localArrayList = new ArrayList();
		
		
		public CheckPRomoCode(String PromoCODE) {
			
			this.promocode = PromoCODE;
			
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("promocode", Constants.PROMOCODE));
			
				
				result = function.checkPromoCodeValidation(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			try {
				if(((String) result.get(Constants.RESPONSE_KEY)).equalsIgnoreCase("true")){
					
					Constants.PROMOCODE_ID = (String) result.get("promocode_id");
					viewPager.setCurrentItem(getItem(+1), true);

				} else {
					
					showAlertToUser("Invalid Promocode.");
					
				}
			} catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}
		}
		
	

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(SignUp.this,
					R.drawable.loading);
			db.show();

		}

	}
	
	
	public class CheckUsernameEmail extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	
		String username , email;
		
		

		HashMap result = new HashMap();
		ArrayList localArrayList = new ArrayList();
		

		public CheckUsernameEmail(String eMAIL, String uSERNAME) {
			this.username = uSERNAME;
			this.email = eMAIL ;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				
				
				/*http://vimadollc.com/Web_API/CheckForUserName.php?
					authkey=Buckupkey2015&emailid=&username=mcarr87*/
				
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("emailid", this.email));
				localArrayList.add(new BasicNameValuePair("username", this.username));
			
				
				result = function.CheckUsernameEmail(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			try {
				if(((String) result.get(Constants.RESPONSE_KEY)).equalsIgnoreCase("true")){
					
					viewPager.setCurrentItem(getItem(+1), true);

				} else {
					
					showAlertToUser("Username Or Email Id Already Exists.");
					
				}
			} catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}
		}
		
	

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(SignUp.this,
					R.drawable.loading);
			db.show();

		}

	}
	
	
}

