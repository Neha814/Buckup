package com.example.fragments;

import com.example.buckup.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Agreements extends Fragment {

	private View agreementView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		agreementView = inflater.inflate(R.layout.fragment_agreements, container, false);

		initUi();

		return agreementView;
	}

	private void initUi() {

	}

}
