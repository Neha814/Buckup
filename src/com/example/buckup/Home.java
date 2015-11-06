package com.example.buckup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fragments.Agreements;
import com.example.fragments.FavoriteFragment;
import com.example.fragments.HomeFragment;
import com.example.fragments.MakeDonation;
import com.example.fragments.MyAccount;
import com.example.fragments.MyDonations;
import com.example.fragments.ShareFragment;
import com.example.functions.Constants;
import com.example.utils.PrefStore;

public class Home extends ActionBarActivity {

	ActionBarDrawerToggle actionBarDrawerToggle;

	DrawerLayout drawerLayout;

	Button menu_button;

	ImageView logo;

	ListView left_drawer;

	MyListAdapter mAdapter;

	String[] lvMenuItems1 = new String[20];

	public String[] lvMenuItems2;

	public Integer[] images1;

	Button setting_button;

	Button share, favorite, my_donations;

	TextView email_id, name;

	Integer[] images = { R.drawable.home_icon, R.drawable.star_rating_icon, R.drawable.dollar_icon,
			R.drawable.dination_icon, R.drawable.user, R.drawable.agreement, R.drawable.message_icon,
			R.drawable.log_out };

	private boolean inHome;

	private PrefStore store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.home);

		store = new PrefStore(this);
		inHome = true;
		
		Constants.USER_ID = store.getString("userId");
		 Constants.ACESS_TOKEN = store.getString("accessToken");
		// username_text = store.getString("userName");
		 Constants.EMAILID = store.getString("emailId");
		

		store.setString("inHome", inHome + "");
		drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
		menu_button = (Button) findViewById(R.id.menu_button);
		left_drawer = (ListView) findViewById(R.id.left_drawer);
		logo = (ImageView) findViewById(R.id.logo);
		setting_button = (Button) findViewById(R.id.setting_button);
		share = (Button) findViewById(R.id.share);
		favorite = (Button) findViewById(R.id.favorite);
		my_donations = (Button) findViewById(R.id.my_donations);

		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {

			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {

			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		lvMenuItems1[0] = "Home";
		lvMenuItems1[1] = "Favorite";
		lvMenuItems1[2] = "My Donations";
		lvMenuItems1[3] = "Make Donations";

		lvMenuItems1[4] = "My Account";
		lvMenuItems1[5] = "Agreements";
		lvMenuItems1[6] = "Contact Us";
		lvMenuItems1[7] = "Logout";
		mAdapter = new MyListAdapter(lvMenuItems1, images, Home.this);
		left_drawer.setAdapter(mAdapter);

		// View view = View.inflate(Home.this, R.layout.header, null);
		// left_drawer.addHeaderView(view);
		// email_id = (TextView)view.findViewById(R.id.email_id);
		// name = (TextView)view.findViewById(R.id.name);
		//
		// email_id.setText(Constants.EMAILID);
		// name.setText(Constants.NAME);

		left_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onMenuItemClick(parent, view, position, id);
				view.setSelected(true);
			}

			private void onMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedItem = lvMenuItems1[position];
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
				FragmentManager fm = Home.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Fragment fragment = null;

				if (selectedItem.compareTo("Home") == 0) {
					fragment = new HomeFragment();
				}
				else if (selectedItem.compareTo("Favorite") == 0) {
					fragment = new FavoriteFragment();
				}
				else if (selectedItem.compareTo("My Donations") == 0) {
					fragment = new MyDonations();
				}
				else if (selectedItem.compareTo("Make Donations") == 0) {
					fragment = new MakeDonation();

				}
				else if (selectedItem.compareTo("My Account") == 0) {
					fragment = new MyAccount();
				}
				else if (selectedItem.compareTo("Agreements") == 0) {

					fragment = new Agreements();

				}
				else if (selectedItem.compareTo("Contact Us") == 0) {
					Intent i = new Intent(Home.this, ContactUs.class);
					startActivity(i);
				}
				else if (selectedItem.compareTo("Logout") == 0) {
					showLogoutDialog();
				}
				drawerLayout.closeDrawer(left_drawer);
				if (fragment != null) {
					ft.replace(R.id.frame_layout, fragment);
					ft.commit();

				}
			}

		});

		// Add FragmentMain as the initial fragment
		FragmentManager fm = Home.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;
		if (Constants.iscomingfromSetting == true) {

			fragment = new MyAccount();
		}
		else {
			fragment = new HomeFragment();
		}

		// fm.popBackStack(null,
		// FragmentManager.POP_BACK_STACK_INCLUSIVE);

		if (fragment != null) {

			ft.replace(R.id.frame_layout, fragment);
		}
		else {
			ft.add(R.id.frame_layout, fragment);
		}
		// ft.addToBackStack(null);
		ft.commit();

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					drawerLayout.closeDrawer(left_drawer);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				FragmentManager fm = Home.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();

				ShareFragment fragment = new ShareFragment();

				if (fragment != null) {

					ft.replace(R.id.frame_layout, fragment);
				}
				else {

					ft.add(R.id.frame_layout, fragment);

				}
				ft.commit();
			}
		});

		my_donations.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					drawerLayout.closeDrawer(left_drawer);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				FragmentManager fm = Home.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();

				MyDonations fragment = new MyDonations();

				if (fragment != null) {

					ft.replace(R.id.frame_layout, fragment);
				}
				else {

					ft.add(R.id.frame_layout, fragment);

				}
				ft.commit();
			}
		});
		favorite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					drawerLayout.closeDrawer(left_drawer);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				FragmentManager fm = Home.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();

				FavoriteFragment fragment = new FavoriteFragment();

				if (fragment != null) {

					ft.replace(R.id.frame_layout, fragment);
				}
				else {

					ft.add(R.id.frame_layout, fragment);

				}
				ft.commit();
			}
		});

		menu_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(left_drawer);
			}
		});

		setting_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Home.this, Setting.class);
				startActivity(i);
			}
		});

		
	}

	protected void showLogoutDialog() {
		final Dialog dialog;
		dialog = new Dialog(Home.this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;

		dialog.setContentView(R.layout.logout);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				inHome = false;
				store.setString("inHome", inHome + "");
				Intent i = new Intent(Home.this, Login.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				dialog.dismiss();
			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();
	}

	class MyListAdapter extends BaseAdapter {
		Resources res = getResources();

		LayoutInflater mInflater = null;

		public MyListAdapter(String[] lvMenuItems1, Integer[] images, Home context) {

			lvMenuItems2 = lvMenuItems1;
			images1 = images;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {

			return images1.length;
		}

		@Override
		public Object getItem(int position) {

			return images1[position];
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.simple_list_item_1, null);
				holder.textview = (TextView) convertView.findViewById(R.id.text);
				holder.image = (ImageView) convertView.findViewById(R.id.image);

			}
			else {
				holder = (ViewHolder) convertView.getTag();

			}
			try {
				holder.textview.setText(lvMenuItems2[position]);
				holder.image.setImageResource(images1[position]);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

	}

	class ViewHolder {
		TextView textview;

		ImageView image;

	}

	@Override
	public void onBackPressed() {

		final Dialog dialog;
		dialog = new Dialog(Home.this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;

		dialog.setContentView(R.layout.backpressed);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finishAffinity();
				dialog.dismiss();
			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();

	}

}
