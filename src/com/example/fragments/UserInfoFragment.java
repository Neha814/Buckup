package com.example.fragments;

import com.example.buckup.R;
import com.example.fragments.MyAccount.my_account;
import com.example.functions.Constants;
import com.example.utils.StringUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class UserInfoFragment extends Fragment {
	
	static EditText emailET,usernameET,passwordET,confirmPasswordET,firstnameET,lastnameET;
	static EditText zipcodeET;
	private static String email;
	private static String userName;
	private static String password;
	private static String confirmPassword;
	private static String zipCode;
	
	ImageView edit;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.user_info, container, false);
		
		emailET = (EditText) rootView.findViewById(R.id.emailET);
		usernameET = (EditText) rootView.findViewById(R.id.usernameET);
		passwordET = (EditText) rootView.findViewById(R.id.passwordET);
		confirmPasswordET = (EditText) rootView.findViewById(R.id.confirmPasswordET);
		zipcodeET = (EditText) rootView.findViewById(R.id.zipcodeET);
		firstnameET = (EditText) rootView.findViewById(R.id.firstnameET);
		lastnameET = (EditText) rootView.findViewById(R.id.lastnameET);
		edit = (ImageView) rootView.findViewById(R.id.edit);
		
		/*String email;
		String userName;
		String password;
		String zipCode;*/
		
		
		emailET.setEnabled(false);
		usernameET.setEnabled(false);
		passwordET.setEnabled(false);
		confirmPasswordET.setEnabled(false);
		zipcodeET.setEnabled(false);
		firstnameET.setEnabled(false);
		lastnameET.setEnabled(false);
		
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				emailET.setEnabled(true);
				usernameET.setEnabled(true);
				passwordET.setEnabled(true);
				confirmPasswordET.setEnabled(true);
				zipcodeET.setEnabled(true);
				firstnameET.setEnabled(true);
				lastnameET.setEnabled(true);
				
			}
		});
		
		
		emailET.setText(Constants.email);
		usernameET.setText(Constants.userName);
		firstnameET.setText(Constants.FIRSTNAME_MYACCOUNT);
		lastnameET.setText(Constants.LASTNAME_MYACCOUNT);
		zipcodeET.setText(Constants.zipCode);
		
	
		
		
		return rootView;
	}

	static boolean getData() {
		
		password = passwordET.getText().toString();
		confirmPassword = confirmPasswordET.getText().toString();
		
		String email_text  = emailET.getText().toString().trim();
		String username_text = usernameET.getText().toString();
		String zipcode_text  = zipcodeET.getText().toString();
		String firstname_text = firstnameET.getText().toString();
		String lastname_text = lastnameET.getText().toString();
		if(firstname_text.length()<1){
			firstnameET.setError("Please enter firstname.");
			return false;
		}
		else if(lastname_text.length()<1){
			lastnameET.setError("Please enter lastname.");
			return false;
		} else if(!(StringUtils.verify(email_text))){
			emailET.setError("Please enter valid email address.");
			return false;
		} else if(username_text.length()<1){
			usernameET.setError("Please enter username.");
			return false;
		} 
		
		else if(password.trim().length()>0 || confirmPassword.trim().length()>0){
			if(!password.equals(confirmPassword)){
				confirmPasswordET.setError("password and confirm password did not match.");
				return false;
			}
			else if(password.equals(confirmPassword)){
				Constants.new_password = password;
				return true;
			}
			}
		else if(zipcode_text.length()<1){
			zipcodeET.setError("Please enter zipcode.");
			return false;
		}
			else {
				
				Constants.new_password = password;
				Constants.userName = username_text;
				Constants.zipCode = zipcode_text;
				Constants.FIRSTNAME_MYACCOUNT = firstname_text;
				Constants.LASTNAME_MYACCOUNT = lastname_text;
				Constants.email = email_text;
				
				
				return true;
			}
		
		
		
			Constants.new_password = password;
			Constants.userName = username_text;
			Constants.zipCode = zipcode_text;
			Constants.FIRSTNAME_MYACCOUNT = firstname_text;
			Constants.LASTNAME_MYACCOUNT = lastname_text;
			Constants.email = email_text;
			
		return true;
	
	}

}
