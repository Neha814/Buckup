package com.example.buckup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

public class Setting extends Activity {

	Button back;

	Button submit;

	EditText amount_et, monthly_limit_et;

	Switch notification_switch;
	Switch updates_switch ,buckup_onoff_switch;

	 Spinner search_spinner;

	String notification_switch_text;
	String update_switch_text , buckup_switch_text;

	TransparentProgressDialog db;

	SharedPreferences sp;

	boolean isSuccess = false;

	private Button update_button;

	private boolean isConnected;

	private String amount_text;
	
	String[] id_list;

	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(Setting.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						if (isSuccess) {
							finish();
						}
					}
				});
		localBuilder.create().show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		back = (Button) findViewById(R.id.back);
		submit = (Button) findViewById(R.id.submit);
		amount_et = (EditText) findViewById(R.id.amount_et);
		update_button = (Button) findViewById(R.id.update_button);
		update_button.setOnClickListener(updateListener);
		monthly_limit_et = (EditText) findViewById(R.id.monthly_limit_et);
		notification_switch = (Switch) findViewById(R.id.notification_switch);
		isConnected = NetConnection.checkInternetConnectionn(this);
		updates_switch = (Switch) findViewById(R.id.updates_switch);
		search_spinner = (Spinner) findViewById(R.id.search_spinner);
		buckup_onoff_switch = (Switch) findViewById(R.id.buckup_onoff_switch);

		if (isConnected) {
			new getSetting().execute(new Void[0]);
		}
		else {
			showAlertToUser(Constants.NO_INTERNET);
		}

		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		new FavCharityList().execute(new Void[0]);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		notification_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// on
					notification_switch_text = "1";
				}
				else {
					// off
					notification_switch_text = "0";
				}
			}
		});
		
		buckup_onoff_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// on
					buckup_switch_text = "1";
				}
				else {
					// off
					buckup_switch_text = "0";
				}
			}
		});

		updates_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// on
					update_switch_text = "1";
				}
				else {
					// off
					update_switch_text = "0";
				}
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					amount_text = amount_et.getText().toString();

					double a = Double.parseDouble(amount_text);

					if (a < 0.05) {

						amount_et.setError("Amount should be greater than $0.05");

					}
					else {

						String monthly_limit_text = monthly_limit_et.getText().toString();
						//String spinner_text = search_spinner.getSelectedItem().toString();
						int spinnder_pos = search_spinner.getSelectedItemPosition();
						String spinner_text = id_list[spinnder_pos]; 

						new setting_notification(amount_text, monthly_limit_text, spinner_text,
								notification_switch_text, update_switch_text,buckup_switch_text).execute(new Void[0]);
					}
				}
				catch (Exception e) {

					e.printStackTrace();

					String monthly_limit_text = monthly_limit_et.getText().toString();
					String spinner_text = search_spinner.getSelectedItem().toString();

					new setting_notification(amount_text, monthly_limit_text, spinner_text, notification_switch_text,
							update_switch_text,buckup_switch_text).execute(new Void[0]);
				}

			}
		});
	}

	public OnClickListener updateListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Constants.iscomingfromSetting = true;

			Intent intent = new Intent(Setting.this, Home.class);
			startActivity(intent);
			finish();

		}
	};

	public class FavCharityList extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
				result = function.getCharityList(localArrayList);

			}
			catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			/* db.dismiss(); */

			try {
				if (result.size() > 0) {
					String[] list = new String[result.size()];
					 id_list = new String[result.size()];
					for (int i = 0; i < result.size(); i++) {
						list[i] = result.get(i).get("charity_name");
						id_list[i] = result.get(i).get("id");
					}

					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Setting.this,
							R.layout.simple_spinner_item, R.id.text, list);
					String spinner_value = sp.getString("setting_fav", "");

					search_spinner.setAdapter(dataAdapter);
					ArrayAdapter myAdap = (ArrayAdapter) search_spinner.getAdapter();
					int spinnerPosition = myAdap.getPosition(spinner_value);
					search_spinner.setSelection(spinnerPosition);
				}
				else {
					String[] list = new String[] { "No fav list" };
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Setting.this,
							R.layout.simple_spinner_item, R.id.text, list);

					search_spinner.setAdapter(dataAdapter);
				}
			}
			catch (Exception ae) {

				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			// db = new TransparentProgressDialog(Setting.this, R.drawable.loading);
			// db.show();
		}

	}

	public class setting_notification extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String set_amount, monthly_limit, spinner_fav_text, notification_text, update_text , buckup_text;

		public setting_notification(String amount_text, String monthly_limit_text, String spinner_text,
				String notification_switch_text, String update_switch_text,String buckup_switch_text) {
			this.set_amount = amount_text;
			this.monthly_limit = monthly_limit_text;
			this.spinner_fav_text = spinner_text;
			this.notification_text = notification_switch_text;
			this.update_text = update_switch_text;
			this.buckup_text = buckup_switch_text;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("is_purchase_notification", this.notification_text));
				localArrayList.add(new BasicNameValuePair("donate_to_fav", this.spinner_fav_text));
				localArrayList.add(new BasicNameValuePair("monthly_limit", this.monthly_limit));
				localArrayList.add(new BasicNameValuePair("default_donation_to_purchase", this.set_amount));
				localArrayList.add(new BasicNameValuePair("is_updates_from_us", this.update_text));
				localArrayList.add(new BasicNameValuePair("On_Off_Buckup", this.buckup_text));
				result = function.settingNotification(localArrayList);

			}
			catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					isSuccess = true;
					showAlertToUser("Data updated successfully.");
					Editor e = sp.edit();
					e.putString("setting_user_id", Constants.USER_ID);
					e.putString("setting_notification_onoff", this.notification_text);
					e.putString("setting_update_onoff", this.update_text);
					e.putString("setting_monthly_limit", this.monthly_limit);
					e.putString("setting_set_amount", this.set_amount);
					e.putString("setting_fav", this.spinner_fav_text);
					e.putString("On_Off_Buckup", this.buckup_text);
					e.commit();

				}
				else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser(Constants.ERROR_MSG);
				}
			}
			catch (Exception ae) {

				ae.printStackTrace();
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Setting.this, R.drawable.loading);
			db.show();
		}

	}

	public class getSetting extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		public getSetting() {

		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
				result = function.settingsGetUpdate(localArrayList);

			}
			catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			Log.d("result: ", result + "");
			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {

					String id = (String) result.get("id");
					String user_type = (String) result.get("user_type");
					String emailid = (String) result.get("emailid");
					String username = (String) result.get("username");
					String password = (String) result.get("password");
					String fname = (String) result.get("fname");
					String lname = (String) result.get("lname");
					String address = (String) result.get("address");
					String zip = (String) result.get("zip");
					String phone = (String) result.get("phone");
					String access_token = (String) result.get("access_token");
					String default_rounded_amount = (String) result.get("default_rounded_amount");
					String default_charity = (String) result.get("default_charity");
					String is_purchase_notification = (String) result.get("is_purchase_notification");
					String default_donation_to_purchase = (String) result.get("default_donation_to_purchase");
					String monthly_limit = (String) result.get("monthly_limit");
					String is_donate_to_my_fav = (String) result.get("is_donate_to_my_fav");
					String is_updates_from_us = (String) result.get("is_updates_from_us");
					String cron_date = (String) result.get("cron_date");
					String created = (String) result.get("created");
					String buckup_on_off = (String) result.get("On_Off_Buckup");

					loadDatatoViews(id, user_type, emailid, username, password, fname, lname, address, zip, phone,
							access_token, default_rounded_amount, default_charity, is_purchase_notification,
							default_donation_to_purchase, monthly_limit, is_donate_to_my_fav, is_updates_from_us,
							cron_date, created,buckup_on_off);

				}
				else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Something went wrong while submitting your settings. Please try again after some time.");
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Setting.this, R.drawable.loading);
			db.show();
		}

	}

	public void loadDatatoViews(String id, String user_type, String emailid, String username, String password,
			String fname, String lname, String address, String zip, String phone, String access_token,
			String default_rounded_amount, String default_charity, String is_purchase_notification,
			String default_donation_to_purchase, String monthly_limit, String is_donate_to_my_fav,
			String is_updates_from_us, String cron_date, String created,String buckup_on_off) {

		if (is_purchase_notification.equals("0")) {
			notification_switch.setChecked(false);
			notification_switch_text = "0";
		}
		else if (is_purchase_notification.equals("1")) {
			notification_switch.setChecked(true);
			notification_switch_text = "1";
		}
		
		if (buckup_on_off.equals("0")) {
			buckup_onoff_switch.setChecked(false);
			buckup_switch_text = "0";
		}
		else if (buckup_on_off.equals("1")) {
			buckup_onoff_switch.setChecked(true);
			buckup_switch_text = "1";
		}

		if (is_updates_from_us.equals("0")) {
			updates_switch.setChecked(false);
			update_switch_text = "0";
		}
		else if (is_updates_from_us.equals("1")) {
			updates_switch.setChecked(true);
			update_switch_text = "1";
		}

		if (default_donation_to_purchase.trim().length() > 0) {
			amount_et.setText(default_donation_to_purchase);
		}
		else {
			amount_et.setHint("$");
			amount_et.setTextColor(Color.parseColor("#ffffff"));
		}

		Log.e("monthly_limit==", "" + monthly_limit);
		if (monthly_limit.trim().length() > 0) {
			monthly_limit_et.setText(monthly_limit);
		}
		else {
			monthly_limit_et.setHint("$");
			monthly_limit_et.setTextColor(Color.parseColor("#ffffff"));
		}
		String spinner_value = sp.getString("setting_fav", "");
		Log.e("spinner_value===", "" + spinner_value);
		search_spinner.setPrompt(spinner_value);

	}
}
