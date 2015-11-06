package com.example.adapter;

import com.example.fragments.BankInfoFragment;

import com.example.fragments.UserInfoFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter1 extends FragmentPagerAdapter {

	public TabsPagerAdapter1(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			
			return new UserInfoFragment();
			
		case 1:
			
			return new BankInfoFragment();
			
		
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
