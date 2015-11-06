package com.example.fragments;

import com.example.buckup.R;
import com.example.buckup.SignUp;
import com.example.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class SignUp4 extends Fragment{
	
	static CheckBox agree_checkbox;
	static Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.sign_up_four, container, false);
		agree_checkbox = (CheckBox) rootView.findViewById(R.id.agree_checkbox);
		context = getActivity();

		
		return rootView;
	}

	public static Boolean checkDetailIsFilledOrNot() {
		 if (agree_checkbox.isChecked()) {
			 return true;
		 }
		 else {
			return showAlertToUser("Please accept terms and conditions.", context);
		 }
	}

	private static Boolean showAlertToUser(String string, Context context2) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				context2);
		localBuilder.setMessage(string).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

					}
				});
		localBuilder.create().show();
		return false;
	}

}
