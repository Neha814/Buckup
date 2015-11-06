package com.example.buckup;

import java.io.IOException;

import com.example.functions.Constants;
import com.example.utils.PrefStore;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends ActionBarActivity {

	String PROJECT_NUMBER = "18098928435";

	GoogleCloudMessaging gcm;

	String regid;

	private PrefStore store;

	private String inHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getSupportActionBar().hide();
		setContentView(R.layout.splash);
		
	/*	String amount_string = String.format( "Value of a: %.2f", 0.1 );
		
		Toast.makeText(getApplicationContext(), amount_string, Toast.LENGTH_SHORT).show();*/

		try {
			store = new PrefStore(this);

			inHome = store.getString("inHome");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread t = new Thread() {
			public void run() {

				try {
					// sleep(3 * 1000);
					getRegID();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}

			private String getRegID() {
				new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {
						String msg = "";
						try {
							if (gcm == null) {
								gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
							}
							regid = gcm.register(PROJECT_NUMBER);
							msg = regid;

							Log.e("GCM", msg);

						}
						catch (IOException ex) {
							msg = "Error :" + ex.getMessage();
							Intent i = new Intent(SplashScreen.this, Login.class);
							startActivity(i);
							finish();

						}
						return msg;
					}

					@Override
					protected void onPostExecute(String msg) {
						Constants.REGISTRATIO_ID = msg;

						if (inHome.equals("true")) {

							Constants.USER_ID = store.getString("userId");
							Constants.ACESS_TOKEN = store.getString("accessToken");
							Constants.NAME = store.getString("userName");
							Constants.EMAILID = store.getString("emailId");
							store.getString("password");
							Constants.FIRSTNAME = "";
							Constants.LASTNAME = "";

							Intent i = new Intent(SplashScreen.this, Home.class);
							startActivity(i);
							finish();

						}
						else {

							Intent i = new Intent(SplashScreen.this, Login.class);
							startActivity(i);
							finish();
						}

					}
				}.execute(null, null, null);
				return regid;

			}
		};
		t.start();
	}

}
