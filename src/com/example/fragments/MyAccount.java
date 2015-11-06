package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.adapter.TabsPagerAdapter;
import com.example.adapter.TabsPagerAdapter1;
import com.example.buckup.R;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

public class MyAccount extends Fragment {
	
	private ViewPager viewPager;

	private View accountView;


	private Boolean isTrueOrFalse;

	private Animation shake;

	private Boolean isConnected;

	private Boolean isSuccessfullyConnected = false;
	
	private TabsPagerAdapter1 mAdapter;

	public boolean isTrueOrFalseBank = false ;

	private TransparentProgressDialog db;

	Boolean isValidDate, successfullyValidated;
	Button done , next, back;

	public void showAlertToUser(String paramString) {
		try {
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(
					getActivity());
			localBuilder
					.setMessage(paramString)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramAnonymousDialogInterface,
										int paramAnonymousInt) {
									paramAnonymousDialogInterface.cancel();
									if (isSuccessfullyConnected) {
										refreshView();

									}
								}
							});
			localBuilder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
				.getWindowToken(), 0);
		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		accountView = inflater.inflate(R.layout.test, container,
				false);

		initUI();
		return accountView;
	}

	private void initUI() {

		viewPager = (ViewPager) accountView.findViewById(R.id.pager);
		done = (Button) accountView.findViewById(R.id.done);
		next = (Button) accountView.findViewById(R.id.next);
		back = (Button) accountView.findViewById(R.id.back);
		
		back.setVisibility(View.INVISIBLE);

		
		/*mAdapter = new TabsPagerAdapter1(getActivity().getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);*/

		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

		if (isConnected) {
			new my_accountFilled().execute(new Void[0]);
		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}

		viewPager.setOnTouchListener(new OnTouchListener()
	    {           
	        
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return true;
			}
	    });
		
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				isTrueOrFalseBank = BankInfoFragment.getDatainfo();
				
				Log.e("card_type=",""+Constants.card_type);
				Log.e("name_on_card=",""+Constants.name_on_card);
				Log.e("expiry_date=",""+Constants.expiry_date);
				Log.e("cvv_no=",""+Constants.cvv_no);
				Log.e("card_no=",""+Constants.card_no);
				
				if(isTrueOrFalseBank){
				Log.e("password===",""+Constants.password);
				Log.e("new password===",""+Constants.new_password);
				new my_account(Constants.email, Constants.userName, Constants.new_password, Constants.zipCode,
						Constants.card_type, Constants.name_on_card, Constants.card_no,
						Constants.expiry_date , Constants.FIRSTNAME_MYACCOUNT , Constants.LASTNAME_MYACCOUNT,Constants.cvv_no).execute(new Void[0]);
				}
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back.setVisibility(View.INVISIBLE);
				next.setVisibility(View.VISIBLE);
				done.setVisibility(View.INVISIBLE);
				viewPager.setCurrentItem(getItem(-1), true);
				
			}
			protected int getItem(int i) {
				return viewPager.getCurrentItem() + i;
			}
		});
		
		next.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				
				isTrueOrFalse = UserInfoFragment.getData();
				if(isTrueOrFalse){
					next.setVisibility(View.INVISIBLE);
					done.setVisibility(View.VISIBLE);
					back.setVisibility(View.VISIBLE);
				viewPager.setCurrentItem(getItem(+1), true);
				}
			}

			protected int getItem(int i) {
				return viewPager.getCurrentItem() + i;
			}
		});

	}

	public OnClickListener submitDetailListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

//			email = emailET.getText().toString();
//			userName = usernameET.getText().toString();
//			password = passwordET.getText().toString();
//			confirmPassword = confirmPasswordET.getText().toString();
//			zipCode = zipcodeET.getText().toString();
//			card_type_text = card_type.getText().toString();
//			card_name_text = card_name.getText().toString();
//			card_no_text = card_no.getText().toString();
//			expiry_date_text = expiry_date.getText().toString();
//			isValidDate = expiry_date_text
//					.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");

//			if (!confirmPassword.equals(password)) {
//
////				confirmPasswordET.setError("Confirm Password not match");
////				confirmPasswordET.startAnimation(shake);
//
//			}

//			else {

				if (isConnected) {

//					new my_account(email, userName, password, zipCode,
//							card_type_text, card_name_text, card_no_text,
//							expiry_date_text).execute(new Void[0]);
				} else {
					showAlertToUser(Constants.NO_INTERNET);
				}
	//		}

		}

	};

	public class my_account extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		private String userName;

		private String password;

		private String zipCode;

		private String email;
		public String card_type, card_name, card_no, expiry_date , FirstN , LastN , cVVN;

		public my_account(String email2, String userName2, String password2,
				String zipCode2, String card_type_text, String card_name_text,
				String card_no_text, String expiry_date_text , String fname , String lname,String cVV) {
			this.email = email2;
			this.userName = userName2;
			this.password = password2;
			this.zipCode = zipCode2;
			this.card_type = card_type_text;
			this.card_name = card_name_text;
			this.card_no = card_no_text;
			this.expiry_date = expiry_date_text;
			this.FirstN = fname;
			this.LastN = lname;
			this.cVVN = cVV;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList
						.add(new BasicNameValuePair("emailid", this.email));
				localArrayList.add(new BasicNameValuePair("username",
						this.userName));
				localArrayList.add(new BasicNameValuePair("password",
						this.password));
				localArrayList.add(new BasicNameValuePair("zip", this.zipCode));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));

				localArrayList.add(new BasicNameValuePair("card_type",
						this.card_type));
				localArrayList.add(new BasicNameValuePair("name_on_card",
						this.card_name));
				localArrayList.add(new BasicNameValuePair("card_number",
						this.card_no));
				localArrayList.add(new BasicNameValuePair("expiry_date",
						this.expiry_date));
				
				localArrayList.add(new BasicNameValuePair("lname",
						this.LastN));
				
				localArrayList.add(new BasicNameValuePair("fname",
						this.FirstN));
				localArrayList.add(new BasicNameValuePair("cvv_number",
						this.cVVN));
				
				
				result = function.profileUpdate(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {

					isSuccessfullyConnected = true;
					showAlertToUser("Your account updated successfully.");

				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Something went wrong while submitting your account detail. Please try again after some time.");
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loading);
			db.show();
		}

	}

	public class my_accountFilled extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		public my_accountFilled() {

		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				result = function.profileGetUpdate(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

		
			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					
			
					Log.d("result: ", result + "");
					Constants.email = (String) result.get("Email");
					Constants.userName = (String) result.get("UserName");
					Constants.password = (String) result.get("Password");
					Constants.zipCode = (String) result.get("Zip");
					
					Constants.LASTNAME_MYACCOUNT = (String) result.get("lname");
					Constants.FIRSTNAME_MYACCOUNT = (String) result.get("fname");

					Constants.card_type = (String) result.get("card_type");
					Constants.name_on_card = (String) result.get("name_on_card");
					Constants.card_no = (String) result.get("card_number");
					Constants.expiry_date = (String) result.get("expiry_date");
					Constants.cvv_no = (String) result.get("cvv_number");
					Constants.cvv_no = (String) result.get("cvv_number");
					
					mAdapter = new TabsPagerAdapter1(getChildFragmentManager());
					new setAdapterTask().execute();
					

					
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Something went wrong while submitting your account detail. Please try again after some time.");
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loading);
			db.show();
		}

	}
	
	private class setAdapterTask extends AsyncTask<Void,Void,Void>{
	      protected Void doInBackground(Void... params) {
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Void result) {
	        	viewPager.setAdapter(mAdapter);
	        }
	}

	public void refreshView() {
		Log.i("refresh view====","************refreshView");
		// Add FragmentMain as the initial fragment
		FragmentManager fm = MyAccount.this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		MyAccount fragment = new MyAccount();

		ft.replace(R.id.frame_layout, fragment);

		ft.commit();
		
	}


}
