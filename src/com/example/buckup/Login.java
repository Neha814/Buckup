package com.example.buckup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.PrefStore;
import com.example.utils.TransparentProgressDialog;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Login extends Activity {
	EditText username, password;

	CheckBox checkBox1;

	TextView forgot_password;

	Button sign_in, sign_up;

	Boolean isConnected;

	SharedPreferences sp;

	String username_text, password_text;

	String android_id;

	TransparentProgressDialog db;

	String PROJECT_NUMBER = "18098928435";

	GoogleCloudMessaging gcm;

	String regid;

	private PrefStore store;

	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(Login.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

					}
				});
		localBuilder.create().show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		forgot_password = (TextView) findViewById(R.id.forgot_password);
		sign_in = (Button) findViewById(R.id.sign_in);
		sign_up = (Button) findViewById(R.id.sign_up);
		store = new PrefStore(this);
		isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		String name_fromSp = sp.getString("username", "");
		String password_fromSp = sp.getString("password", "");

		Log.e("name sp==", "" + name_fromSp);
		Log.e("pass sp==", "" + password_fromSp);

		if (!(name_fromSp.equals("") || name_fromSp.equals(" "))) {
			username.setText(name_fromSp);
			password.setText(password_fromSp);
		}

		SpannableString content1 = new SpannableString("Forgot Password?");
		content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
		forgot_password.setText(content1);

		forgot_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				forgot_password.setTextColor(Color.parseColor("#22c5a8"));
				Intent i = new Intent(Login.this, ForgotPassword.class);
				startActivity(i);
			}
		});

		// ********* get device id *****************

		android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
		Log.e("android id ==", "" + android_id);

		sign_in.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				username_text = username.getText().toString();
				password_text = password.getText().toString();
				checkForRememberME(username_text, password_text);
				if (username_text.equals("") || username_text.equals(" ")) {
					showAlertToUser("Please enter username");
				}
				else if (password_text.equals("") || password_text.equals(" ")) {
					showAlertToUser("Please enter password");
				}
				else {
					if (isConnected) {

						new LoginTask(username_text, password_text, android_id, Constants.REGISTRATIO_ID)
								.execute(new Void[0]);

					}
					else {
						showAlertToUser(Constants.NO_INTERNET);
					}
				}
			}
		});
		sign_up.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Login.this, SignUp.class);
				startActivity(i);
			}
		});
	}

	protected void checkForRememberME(String name, String password) {
		Editor e = sp.edit();
		e.putString("username", name);
		e.putString("password", password);
		e.commit();
	}

	public class LoginTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String username, pass, device_id, token_id;

		public LoginTask(String username_text, String password_text, String android_id, String regID) {
			this.username = username_text;
			this.pass = password_text;
			this.device_id = android_id;
			this.token_id = regID;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("username", this.username));
				localArrayList.add(new BasicNameValuePair("password", this.pass));
				localArrayList.add(new BasicNameValuePair("deviceId", "0"));
				localArrayList.add(new BasicNameValuePair("token_id", this.token_id));
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				result = function.login(localArrayList);

			}
			catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					Constants.USER_ID = (String) result.get(Constants.ID_KEY);
					Constants.ACESS_TOKEN = (String) result.get(Constants.ACCESS_TOKEN_KEY);
					Constants.NAME = (String) result.get(Constants.USERNAME_KEY);
					Constants.FIRSTNAME = (String) result.get(Constants.FNAME_KEY);
					Constants.LASTNAME = (String) result.get(Constants.LNAME_KEY);
					Constants.EMAILID = (String) result.get(Constants.EMAILID_KEY);

					store.setString("userId", Constants.USER_ID);
					store.setString("accessToken", Constants.ACESS_TOKEN);
					store.setString("userName", username_text);
					store.setString("emailId", Constants.EMAILID);
					store.setString("password", password_text);

					Intent i = new Intent(Login.this, Home.class);
					startActivity(i);
					finish();
				}
				else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Invalid username or password.");
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(Login.this, R.drawable.loading);
			db.show();
		}

	}

	@Override
	public void onBackPressed() {

		final Dialog dialog;
		dialog = new Dialog(Login.this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;

		dialog.setContentView(R.layout.backpressed);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finishAffinity();
				dialog.dismiss();
			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();

	}
}
