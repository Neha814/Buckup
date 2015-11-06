package com.example.buckup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.StringUtils;
import com.example.utils.TransparentProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassword extends Activity{
	
	EditText email;
	Button submit,back;
	static Animation shake ;
	Boolean isConnected;
	Boolean isResultCome = false ;
	 TransparentProgressDialog db;
	
	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				ForgotPassword.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						if(isResultCome){
							Intent i = new Intent(ForgotPassword.this , Login.class);
							startActivity(i);
						}

					}
				});
		localBuilder.create().show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		email = (EditText) findViewById(R.id.email);
		submit = (Button) findViewById(R.id.submit);
		back = (Button) findViewById(R.id.back);
		
		 shake = AnimationUtils.loadAnimation(ForgotPassword.this, R.anim.shake);
		 isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
		 
		 back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent i = new Intent(ForgotPassword.this , Login.class);
			startActivity(i);
			}
		});
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			String email_text = email.getText().toString();
			if(email_text.equals("") || email_text.equals(" ")){
				email.setError("Please enter email id");
				email.startAnimation(shake);
				
			}
			else if(!(StringUtils.verify(email_text))){
				email.setError("Please enter valid email address.");
				
			}
			else {
				if(isConnected){
					new ForgotPasswordTask(email_text).execute(new Void[0]);
				}
				else {
					showAlertToUser(Constants.NO_INTERNET);
				}
			}
			}
		});
	}
	public class ForgotPasswordTask extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		Dialog dialog;
		
		HashMap result = new HashMap();
		ArrayList localArrayList = new ArrayList();
		String id;

		public ForgotPasswordTask(String email) {
			this.id = email;
		
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("email",this.id));
				localArrayList.add(new BasicNameValuePair("authkey","Buckupkey2015"));
				result = function.forgot_password(localArrayList);
				
			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			
			try{
				if(result.get(Constants.RESPONSE_KEY).equals("true")){
					String msg = (String)result.get(Constants.MESSAGE_WHAT_HAPPEN_KEY);
					isResultCome = true;
					showAlertToUser(msg);
				
				} else if(result.get(Constants.RESPONSE_KEY).equals("false")){
//					String msg = (String)result.get(Constants.MESSAGE_WHAT_HAPPEN_KEY);
					String msg = "Email does not exist in the database.";
					isResultCome = true;
					showAlertToUser(msg);
				}
			}
			
			catch (Exception ae){
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(ForgotPassword.this,
					R.drawable.loading);
			db.show();
		}

	}
}
