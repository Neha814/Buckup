package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import com.example.buckup.R;

import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyDonations extends Fragment {
	
	TextView total_donations;
	ListView listview;
	Boolean isConnected;
	MyAdapter mAdapter;
	TransparentProgressDialog db;
	ArrayList<HashMap<String, String>> my_donations;
	
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

		View rootView = inflater.inflate(R.layout.my_donations, container, false);
		total_donations = (TextView) rootView.findViewById(R.id.total_donations);
		listview = (ListView) rootView.findViewById(R.id.listview);
		
		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected) {
			new My_donations().execute(new Void[0]);
		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}
		return rootView;
		
	}
	public class My_donations extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList result;
		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				result = function.my_donations(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if(result.size()>0){
					 my_donations = new ArrayList<HashMap<String,String>>();
					 my_donations.addAll(result);
					 double totalAmount = 0;
					 for(int i = 0;i<my_donations.size();i++){
						  totalAmount = totalAmount +Double.parseDouble(my_donations.get(i).get("total_donated_amount"));
					
					 }
					 
					 String total_amnt = String.format( "%.2f", totalAmount );
					 
					 total_donations.setText("Total Donations : "+"$ "+total_amnt);
					 
					mAdapter = new MyAdapter(my_donations, getActivity());
					listview.setAdapter(mAdapter);
				}else {
					showAlertToUser("You have not made any donation till yet.");
				}
			} catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
				ae.printStackTrace();
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

		public MyAdapter(ArrayList<HashMap<String, String>> list,
				Activity activity) {
			mInflater = LayoutInflater.from(getActivity());
			
		}


		@Override
		public int getCount() {

			return my_donations.size();
		}

		@Override
		public Object getItem(int position) {

			return my_donations.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.my_donations_list_item, null);
				holder.charity_name = (TextView) convertView
						.findViewById(R.id.charity_name);
				holder.donation_amount = (TextView) convertView
						.findViewById(R.id.donation_amount);
			
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.charity_name.setText(my_donations.get(position).get("charity_name"));
			String donate_amount_text  = String.format( "%.2f", Float.parseFloat(my_donations.get(position).get("total_donated_amount")));
			
			holder.donation_amount.setText("$ "+donate_amount_text); 

			return convertView;
		}

	}

	class ViewHolder {
		TextView charity_name,donation_amount;
		
	}
}
