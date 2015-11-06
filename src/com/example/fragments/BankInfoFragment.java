package com.example.fragments;

import com.example.buckup.R;
import com.example.functions.Constants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class BankInfoFragment extends Fragment {

	public static EditText card_no;
	public static EditText card_name, card_type, expiry_date;
	public static String card_type_text;
	public static String card_name_text;
	public static String card_no_text;
	public static String expiry_date_text;
	public static EditText cvv_no;
	public static String cvv_no_text;
	
	ImageView edit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.bank_info, container, false);

		card_type = (EditText) rootView.findViewById(R.id.card_type);
		card_name = (EditText) rootView.findViewById(R.id.card_name);
		card_no = (EditText) rootView.findViewById(R.id.card_no);
		expiry_date = (EditText) rootView.findViewById(R.id.expiry_date);
		cvv_no = (EditText) rootView.findViewById(R.id.cvv_no);
		edit = (ImageView) rootView.findViewById(R.id.edit);

		/*
		 * String card_type; String name_on_card; String card_no; String
		 * expiry_date;
		 */
		card_no.setEnabled(false);
		card_type.setEnabled(false);
		card_name.setEnabled(false);
		expiry_date.setEnabled(false);
		cvv_no.setEnabled(false);
		
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				card_no.setEnabled(true);
				card_type.setEnabled(true);
				card_name.setEnabled(true);
				expiry_date.setEnabled(true);
				cvv_no.setEnabled(true);
				
			}
		});
		

		if (Constants.card_type.equals("") || Constants.card_type == null) {
			this.card_type.setText("Card Type");
		} else {
			this.card_type.setText(Constants.card_type);
		}
		if (Constants.expiry_date.equals("") || Constants.expiry_date == null) {
			this.expiry_date.setText("Expiry Date");
		} else {
			this.expiry_date.setText(Constants.expiry_date);
		}
		if (Constants.name_on_card.equals("") || Constants.name_on_card == null) {
			this.card_name.setText("Name on Card");
		} else {
			card_name.setText(Constants.name_on_card);
		}
		if (Constants.card_no.equals("") || Constants.card_no == null) {
			this.card_no.setText("Card Number");
		} else {
			try {
				
				Constants.GLOBAL_CARD_NO = Constants.card_no;
				
				String sub_string = Constants.card_no.substring(12, 16);
				String card_no_value = "************" + sub_string;
				
				this.card_no.setText(card_no_value);
			} catch (Exception e) {
				e.printStackTrace();
				this.card_no.setText("Card Number");
			}
		}if (Constants.cvv_no.equals("") || Constants.cvv_no == null) {
			this.cvv_no.setText("CVV Number");
		} else {
			Constants.GLOBAL_CVV_NO = Constants.cvv_no;
			
			//cvv_no.setText(Constants.cvv_no);
			
			cvv_no.setText("***");
		}

		getData();
		return rootView;
	}

	private void getData() {
		card_type_text = card_type.getText().toString();
		card_name_text = card_name.getText().toString();
		card_no_text = card_no.getText().toString();
		expiry_date_text = expiry_date.getText().toString();
		cvv_no_text = cvv_no.getText().toString();
	}

	static boolean getDatainfo() {

		card_type_text = card_type.getText().toString();
		card_name_text = card_name.getText().toString();
		card_no_text = card_no.getText().toString();
		expiry_date_text = expiry_date.getText().toString();
		cvv_no_text = cvv_no.getText().toString();
		

		if (card_type_text.trim().length() <1) {
			card_type.setError("please enter card type");
			return false;
		}
		else if(card_name_text.trim().length()<1){
			card_name.setError("please enter name on card.");
			return false;
		}
		
		else if(card_no_text.trim().length()<1){
			card_no.setError("please enter card number.");
			return false;
		}
		else if(cvv_no_text.trim().length()<1){
			cvv_no.setError("please enter CVV number.");
			return false;
		}
		
		else if(expiry_date_text.trim().length()<1){
			expiry_date.setError("please enter card expiry date.");
			return false;
		}
		
		else {
			Constants.card_type = card_type.getText().toString();
			Constants.name_on_card = card_name.getText().toString();
			Constants.expiry_date = expiry_date.getText().toString();
			//Constants.cvv_no = cvv_no.getText().toString();
			//Constants.card_no = card_no.getText().toString();
			
			if(cvv_no.getText().toString().contains("*")){
				Constants.cvv_no = Constants.GLOBAL_CVV_NO;	
			} else {
				Constants.cvv_no  = cvv_no.getText().toString();
			}
			if(card_no.getText().toString().contains("*")){
				Constants.card_no = Constants.GLOBAL_CARD_NO;
			} else {
				Constants.card_no  = card_no.getText().toString();
			}
			
			
			Log.e("card_type=",""+Constants.card_type);
			Log.e("name_on_card=",""+Constants.name_on_card);
			Log.e("expiry_date=",""+Constants.expiry_date);
			Log.e("cvv_no=",""+Constants.cvv_no);
			Log.e("card_no=",""+Constants.card_no);
			
			
			return true;
		}
		

	}

}
