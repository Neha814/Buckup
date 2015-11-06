package com.example.buckup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

public class ContactUs extends Activity {

	Button back, submit;

	EditText comment;

	Boolean isConnected;

	Boolean isSuccessfullyConnected = false;

	TransparentProgressDialog db;
	LinearLayout ll1;

	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				ContactUs.this);
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						if (isSuccessfullyConnected) {
							finish();
						}
					}
				});
		localBuilder.create().show();
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                                                        INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);
		back = (Button) findViewById(R.id.back);
		comment = (EditText) findViewById(R.id.comment);
		submit = (Button) findViewById(R.id.submit);
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		
		comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
//		Animation bottomUp = AnimationUtils.loadAnimation(
//				getApplicationContext(), R.anim.listview_bottom_up);
//		ll1.startAnimation(bottomUp);
//		
		
		isConnected = NetConnection
				.checkInternetConnectionn(getApplicationContext());
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg_text = comment.getText().toString();
				if (msg_text.equals("") || msg_text.equals(" ")) {
					comment.setError("Please enter comment before submitting.");
				} else if (msg_text.trim().length() <= 0) {
					comment.setError("Please enter comment before submitting.");
				} else {
					if (isConnected) {
						new app_feedback(msg_text).execute(new Void[0]);
					} else {
						showAlertToUser(Constants.NO_INTERNET);
					}
				}
			}
		});
	}

	public class app_feedback extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String comment_text;

		public app_feedback(String msg_text) {
			this.comment_text = msg_text;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("message",
						this.comment_text));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				result = function.feedback(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					isSuccessfullyConnected = true;
					showAlertToUser("Your feedback submitted successfully.");
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Something went wrong while submitting your feedback. Please try again after some time.");
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(ContactUs.this,
					R.drawable.loading);
			db.show();
		}

	}
}
