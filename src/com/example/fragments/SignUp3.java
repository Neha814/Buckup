package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.buckup.R;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

public class SignUp3 extends Fragment {
	static Spinner card_type;
	static EditText name_on_card;
	static EditText card_number, expiration_date;
	static EditText cvv_number;
	static Boolean isConnected;
	
	static boolean sendTrue;
	static Boolean isValidDate;
	TransparentProgressDialog db;

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

		View rootView = inflater.inflate(R.layout.sign_up_three, container,
				false);
		name_on_card = (EditText) rootView.findViewById(R.id.name_on_card);
		cvv_number = (EditText) rootView.findViewById(R.id.cvv_number);
		card_number = (EditText) rootView.findViewById(R.id.card_number);
		expiration_date = (EditText) rootView
				.findViewById(R.id.expiration_date);
		card_type = (Spinner) rootView.findViewById(R.id.card_type);
		

		isConnected = NetConnection.checkInternetConnectionn(getActivity());

		String[] list = new String[] { "Credit Card Payment",
				"Debit Card Payment" };
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.simple_spinner_item, R.id.text, list);
//		dataAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		card_type.setAdapter(dataAdapter);

//		submit.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (isConnected) {
//					String spinner_text = card_type.getSelectedItem()
//							.toString();
//					String nameonCard_text = name_on_card.getText().toString();
//					String card_no_text = card_number.getText().toString();
//					String expiration_date_text = expiration_date.getText()
//							.toString();
//
//					isValidDate = expiration_date_text
//							.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
//
//					if (nameonCard_text.equals("")
//							|| nameonCard_text.equals(" ")) {
//						name_on_card.setError("Enter name on card");
//					} else if (card_no_text.equals("")
//							|| card_no_text.equals(" ")) {
//						card_number.setError("Enter card number");
//					}
//					else if (card_no_text.length()<16) {
//						card_number.setError("Enter 16 digit card number.");
//					}else if (expiration_date_text.equals("")
//							|| expiration_date_text.equals(" ")) {
//						expiration_date.setError("Enter expiration date");
//					} else if (!isValidDate) {
//						expiration_date.setError("Enter valid date.");
//					} else {
//						
//						Constants.SPINNER_TEXT = spinner_text;
//						Constants.NAME_ON_CARD = nameonCard_text;
//						Constants.CARD_NO = card_no_text;
//						Constants.EXPIRATION_DATE = expiration_date_text;
//						
//						sendTrue = true;
//
//					}
//				} else {
//					showAlertToUser(Constants.NO_INTERNET);
//				}
//
//			}
//		});
		return rootView;
	}

	public static Boolean checkDetailIsFilledOrNot() {
		
		String spinner_text = card_type.getSelectedItem()
				.toString();
		String nameonCard_text = name_on_card.getText().toString();
		String cvv_number_text = cvv_number.getText().toString();
		String card_no_text = card_number.getText().toString();
		String expiration_date_text = expiration_date.getText()
				.toString();
		
		isValidDate = expiration_date_text
				.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
		
		if (nameonCard_text.equals("")
				|| nameonCard_text.equals(" ")) {
			name_on_card.setError("Enter name on card");
			return false;
		} else if (card_no_text.equals("")
				|| card_no_text.equals(" ")) {
			card_number.setError("Enter card number");
			return false;
		}
		else if (card_no_text.length()<16) {
			card_number.setError("Enter 16 digit card number.");
			return false;
		}
		else if (cvv_number_text.length()<3) {
			cvv_number.setError("Enter 3 digit CVV number.");
			return false;
		}
		else if (expiration_date_text.equals("")
				|| expiration_date_text.equals(" ")) {
			expiration_date.setError("Enter expiration date");
			return false;
		} else if (!isValidDate) {
			expiration_date.setError("Enter valid date.");
			return false;
		} else {
			
			Constants.SPINNER_TEXT = spinner_text;
			Constants.NAME_ON_CARD = nameonCard_text;
			Constants.CARD_NO = card_no_text;
			Constants.EXPIRATION_DATE = expiration_date_text;
			Constants.CVV_NUMBER = cvv_number_text;
			
			return true;

		}
	}

	
}
