package com.example.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buckup.R;
import com.example.fragments.HomeFragment.MyAdapter1;
import com.example.fragments.HomeFragment.ViewHolder1;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;

public class SignUp5 extends Fragment {

	GridView gridview;
	Boolean isConnected;
	static Boolean sendTrue;
	ImageAdapter mAdapter;
	Boolean isSelected = false;
	TextView view_more;
	
	ListView listview;
	MyAdapter mAdapter1;
	int count = 2000 ;
	int offset = 0;
	Boolean isClicked = false;
	
	boolean secondTime = false ;
	TransparentProgressDialog db;
	
	String uri;
	ArrayList<ViewHolder> hold= new ArrayList<ViewHolder>();
	ArrayList<Integer> temp = new ArrayList<Integer>();
	
	ArrayList<HashMap<String, String>> bank_list = new ArrayList<HashMap<String,String>>();

	ArrayList<HashMap<String, String>> credit_card_images;
	ArrayList<HashMap<String, String>> temp_list;

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

		View rootView = inflater.inflate(R.layout.sign_up_five, container,
				false);
		gridview = (GridView) rootView.findViewById(R.id.gridview);
		view_more = (TextView) rootView.findViewById(R.id.view_more);

		isConnected = NetConnection.checkInternetConnectionn(getActivity());

		if (isConnected) {
			new getBAnkDetail().execute(new Void[0]);
		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				temp.clear();
				Constants.BANK_TYPE = credit_card_images.get(position).get(
						"type");
				temp.add(0, position);
				mAdapter.notifyDataSetChanged();
				sendTrue = true;
			
			}
		});
		
		view_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMoreBanks();
			}
		});
		
		
		return rootView;
	}

	public static Boolean checkDetailIsFilledOrNot() {
		try{
		if (sendTrue) {
			return true;
		} else {
			return false;
		}
		} catch(Exception e){
			return false;
		}
	}

	public class getBAnkDetail extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ArrayList localArrayList = new ArrayList();
		String username, pass, device_id;

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("", ""));

				result = function.getBankdetail();

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {

			try {
				if (result.size() > 0) {
					credit_card_images = new ArrayList<HashMap<String, String>>();

					credit_card_images.addAll(result);
					mAdapter = new ImageAdapter(getActivity(),
							credit_card_images);
					gridview.setAdapter(mAdapter);
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();

		}

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;

	

		public ImageAdapter(Context context,
				ArrayList<HashMap<String, String>> mobileValues) {
			this.context = context;
			
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final ViewHolder holder;

			if (convertView == null) {

				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.grid_list_item, null);

				holder.imageView = (ImageView) convertView
						.findViewById(R.id.grid_item_image);
				
				

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(temp.size()>0) {
				int pos = temp.get(0);
				String image_name = credit_card_images.get(position).get("type");
				image_name = image_name.replace(".png", "");
				String uri;
				if(pos==position){
				 uri = "@drawable/" + image_name+"_s";
				}else{
					 uri = "@drawable/" + image_name;
				}
				int imageResource = context.getResources().getIdentifier(uri, null,
						context.getPackageName());
				try {
					Drawable res = context.getResources()
							.getDrawable(imageResource);
					holder.imageView.setImageDrawable(res);
					
				} catch (Exception e) {
					e.printStackTrace();
					holder.imageView.setImageResource(R.drawable.ic_launcher);
					
				}
			}
			else {
				String image_name = credit_card_images.get(position).get("type");
				image_name = image_name.replace(".png", "");
				String uri = "@drawable/" + image_name;
				int imageResource = context.getResources().getIdentifier(uri, null,
						context.getPackageName());
				try {
					Drawable res = context.getResources()
							.getDrawable(imageResource);
					holder.imageView.setImageDrawable(res);
				} catch (Exception e) {
					e.printStackTrace();
					holder.imageView.setImageResource(R.drawable.ic_launcher);
				}
			}


			return convertView;
		}

		@Override
		public int getCount() {
			return credit_card_images.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		

	}

	class ViewHolder {
		ImageView imageView;

	}
	
	
	
	protected void showMoreBanks() {

		final Dialog dialog;
		Button cancel, submit;
		final EditText search;
		
		dialog = new Dialog(getActivity(), R.style.full_screen_dialog);
		dialog.setCancelable(true);
		if (isClicked == false) {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		isClicked = true;

		dialog.setContentView(R.layout.bank_list_dialog);

		cancel = (Button) dialog.findViewById(R.id.cancel);
		submit = (Button) dialog.findViewById(R.id.submit);
		listview = (ListView) dialog.findViewById(R.id.listview);
		search = (EditText) dialog.findViewById(R.id.search);

		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(145);
		dialog.getWindow().setBackgroundDrawable(d);

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.dialog_animation_top;
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.show();
		
		new getMoreBAnkDetail().execute(new Void[0]);
		
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

				String text = search.getText().toString()
						.toLowerCase(Locale.getDefault());
				mAdapter1.filter(text);
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		

		/*mAdapter1 = new MyAdapter1(pic_A_fav, getActivity());
		pic_a_fav_listview.setAdapter(mAdapter1);*/

		listview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Constants.BANK_TYPE = bank_list.get(position).get(
								"type");
						
						sendTrue = true;
						
						dialog.dismiss();
					}
				});
	}
	
	public class getMoreBAnkDetail extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
	

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ArrayList localArrayList = new ArrayList();
		String username, pass, device_id;
		
		/**
		 * http://vimadollc.com/Web_API/AlllBankLists.php?authkey=Buckupkey2015&count=20&offset=0
		 */

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				
				
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("count", String.valueOf(count)));
				localArrayList.add(new BasicNameValuePair("offset", String.valueOf(offset)));
				
				result = function.MoreBankDetails(localArrayList);

			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			
			db.dismiss();

			try {
				if (result.size() > 0) {
					
					bank_list.addAll(result);
					
					
					mAdapter1 = new MyAdapter(bank_list ,getActivity());
					listview.setAdapter(mAdapter1);
					
					
				}
			}

			catch (Exception ae) {
				ae.printStackTrace();
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loading);
			db.show();
		}

	}
	
	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		
		private ArrayList<HashMap<String, String>> mDisplayedValues;

		public MyAdapter(ArrayList<HashMap<String, String>> list,
				Activity activity) {
			mInflater = LayoutInflater.from(getActivity());
			
			mDisplayedValues = new ArrayList<HashMap<String, String>>();
			mDisplayedValues.addAll(bank_list);
		}

		@Override
		public int getCount() {

			return bank_list.size();
		}

		@Override
		public Object getItem(int position) {

			return bank_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}
		
		public void filter(String category_text) {
			category_text = category_text.toLowerCase(Locale.getDefault());

			bank_list.clear();
			if (category_text.length() == 0) {

				bank_list.addAll(mDisplayedValues);
			} else {

				for (int i = 0; i < mDisplayedValues.size(); i++) {

					if (mDisplayedValues.get(i).get("name")
							.toLowerCase(Locale.getDefault())
							.contains(category_text)) {
						bank_list.add(mDisplayedValues.get(i));
					}
				}
			}
			notifyDataSetChanged();

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder1 holder;
			if (convertView == null) {

				holder = new ViewHolder1();
				convertView = mInflater.inflate(R.layout.pic_a_fav_item, null);
				holder.charity_name = (TextView) convertView
						.findViewById(R.id.charity_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder1) convertView.getTag();
			}
			holder.charity_name.setText(bank_list.get(position).get(
					"name"));

			return convertView;
		}

	}

	class ViewHolder1 {
		TextView charity_name;

	}

}
