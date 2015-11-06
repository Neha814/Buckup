package com.example.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import com.example.buckup.R;
import com.example.functions.Constants;
import com.example.functions.Functions;

import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

public class SignUp2 extends Fragment {

	static EditText id, password_page2;
	static Boolean isConnected;
	private static boolean sendTrue;
	TransparentProgressDialog db;
	Button login;

	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				getActivity());
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.sign_up_two, container, false);
		password_page2 = (EditText) rootView.findViewById(R.id.password_page2);
		id = (EditText) rootView.findViewById(R.id.id);
		login = (Button) rootView.findViewById(R.id.login);

		isConnected = NetConnection.checkInternetConnectionn(getActivity());

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isConnected) {
					String id_text = id.getText().toString();
					String password_text = password_page2.getText().toString();
					if (id_text.equals("") || id_text.equals(" ")) {
						id.setError("Enter username");
					} else if (password_text.equals("")
							|| password_text.equals(" ")) {
						password_page2.setError("Enter password");
					} else {
						new adduser_plaid_api(id_text, password_text)
								.execute(new Void[0]);
					}

				}
				else {
					showAlertToUser(Constants.NO_INTERNET);
				}
			}
		});

		return rootView;
	}

	public static Boolean checkDetailIsFilledOrNot() {
		if(sendTrue ){
			return true;
		}
		else {
			return false;
		}
	}
	public class adduser_plaid_api extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	
		String username , password;
		
		

		HashMap result = new HashMap();
		ArrayList localArrayList = new ArrayList();
		
		public adduser_plaid_api(String id_text, String password_text) {
			this.username = id_text;
			this.password = password_text;
			
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("username", this.username));
				localArrayList.add(new BasicNameValuePair("password", this.password));
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("type", Constants.BANK_TYPE));
			
				
				result = function.adduser_plaidapi(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			try {
				if(((String) result.get(Constants.RESPONSE_KEY)).equalsIgnoreCase("true")){
					
					Constants.ACESS_TOKEN = (String)result.get(Constants.ACCESS_TOKEN_KEY);
					showAlertToUser("Successfully Login...! Press continue to proceed further.");
					sendTrue = true;
				} else {
					sendTrue = false;
				
					showAlertToUser("Invalid username or password.");
					
				}
			} catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}
		}
		
	

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loading);
			db.show();

		}

	}
}
