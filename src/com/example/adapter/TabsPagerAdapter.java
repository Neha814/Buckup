package com.example.adapter;

import com.example.buckup.SignUp;
import com.example.fragments.SignUp1;
import com.example.fragments.SignUp2;
import com.example.fragments.SignUp3;
import com.example.fragments.SignUp4;
import com.example.fragments.SignUp5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			
			return new SignUp1();
			
		case 1:
			
			return new SignUp5();
			
		case 2:
		
			return new SignUp2();
		case 3:
			
			return new SignUp3();
		case 4:
			
			return new SignUp4();
		}
		
		

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 5;
	}

}
