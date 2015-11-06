package com.example.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.buckup.R;
import com.example.functions.Constants;
import com.example.utils.StringUtils;

public class SignUp1 extends Fragment {

	static EditText email, password_page1, confirm_password, zip_code, username , promo_code;
	
	static EditText firstname , lastname ;

	static Animation shake;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.sign_up_one, container, false);

		email = (EditText) rootView.findViewById(R.id.email);
		password_page1 = (EditText) rootView.findViewById(R.id.password_page1);
		confirm_password = (EditText) rootView.findViewById(R.id.confirm_password);
		zip_code = (EditText) rootView.findViewById(R.id.zip_code);
		username = (EditText) rootView.findViewById(R.id.username);
		promo_code = (EditText) rootView.findViewById(R.id.promo_code);
		
		firstname = (EditText) rootView.findViewById(R.id.firstname);
		lastname = (EditText) rootView.findViewById(R.id.lastname);

		shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

		return rootView;
	}

	public static Boolean checkDetailIsFilledOrNot() {
		
		String email_text = email.getText().toString();
		String password_text = password_page1.getText().toString();
		String confirmPass_text = confirm_password.getText().toString();
		String zip_text = zip_code.getText().toString();
		String username_text = username.getText().toString();
		String promo_text = promo_code.getText().toString();
		
		String firstname_text = firstname.getText().toString();
		String lastname_text = lastname.getText().toString();
		
		if(firstname_text.equals("") || firstname_text.equals(" ")){
			firstname.setError("Please enter firstname.");
			firstname.startAnimation(shake);

			return false;
		}
		
		else if(lastname_text.equals("") || lastname_text.equals(" ")){
			lastname.setError("Please enter lastname.");
			lastname.startAnimation(shake);

			return false;
		}

		else if (email_text.equals("") || email_text.equals(" ")) {
			email.setError("Please enter email");
			email.startAnimation(shake);

			return false;
		}
		
		else if (!(StringUtils.verify(email_text))) {
			email.setError("Please enter valid email address.");
			return false;
		}
		else if (username_text.equals("") || username_text.equals(" ")) {
			username.setError("Please enter username.");
			username.startAnimation(shake);
			return false;
		}
		else if (password_text.equals("") || password_text.equals(" ")) {
			password_page1.setError("Please enter password");
			password_page1.startAnimation(shake);
			return false;
		}
		else if (confirmPass_text.equals("") || confirmPass_text.equals(" ")) {
			confirm_password.setError("Please enter confirm password");
			confirm_password.startAnimation(shake);
			return false;
		}
		else if (!confirmPass_text.equals(password_text)) {
			confirm_password.setError("Password did not match");
			confirm_password.startAnimation(shake);
			return false;
		}
		else if (zip_text.equals("") || zip_text.equals(" ")) {
			zip_code.setError("Please enter zip code");
			zip_code.startAnimation(shake);
			return false;
		}

		else {
			Constants.EMAIL = email_text;
			Constants.PASSWORD = password_text;
			Constants.ZIP_CODE = zip_text;
			Constants.USERNAME = username_text;
			Constants.PROMOCODE = promo_text;
			
			Constants.FIRSTNAME_SIGNUP = firstname_text;
			Constants.LASTNAME_SIGNUP = lastname_text;
			
			return true;
		}
	}

}
