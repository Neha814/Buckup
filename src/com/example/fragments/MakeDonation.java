package com.example.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.buckup.R;
import com.example.fragments.FavoriteFragment.MyAdapter;
import com.example.fragments.FavoriteFragment.SearchFromList;
import com.example.fragments.FavoriteFragment.ViewHolder;
import com.example.fragments.FavoriteFragment.makeFavorite;
import com.example.fragments.FavoriteFragment.makeUnFavorite;
import com.example.fragments.HomeFragment.DonateCharity;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class MakeDonation extends Fragment {

	ListView listview;

	EditText search;

	boolean isConnected;

	TransparentProgressDialog db;

	MyAdapter mAdapter;
	
	String GlobalDefualtAmount = "";

	ArrayList<HashMap<String, String>> fav_item_list;
	
	ArrayList<HashMap<String, String>> global_fav_item_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> global_search_item_list = new ArrayList<HashMap<String, String>>();

	ArrayList<HashMap<String, String>> mDisplayedValues=new ArrayList<HashMap<String, String>>();


	PayPalPayment thingToBuy;

	String globalId;

	private String globalItemAmount;

	private static final String TAG = "paymentExample";
	
	ArrayList<String> temp = new ArrayList<String>();

	// private static final String CONFIG_ENVIRONMENT =
	// PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "AUcUg5oQHvPZyc6sYU4yZj9FDYYxSzQC0zy2dkC1NsUpbUJgujKvzHy9MrtEirI3XjBdWTct4iC_z4Yv";

	private static final int REQUEST_CODE_PAYMENT = 1;

	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

	private static PayPalConfiguration config = new PayPalConfiguration().environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Hipster Store").merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

	public void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

					}
				});
		localBuilder.create().show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.make_donation, container, false);
		listview = (ListView) rootView.findViewById(R.id.listview);
		search = (EditText) rootView.findViewById(R.id.search);

		Intent intent = new Intent(getActivity(), PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		getActivity().startService(intent);

		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected) {
			new Make_Donation().execute(new Void[0]);
			//new getDefaultAmountValue().execute(new Void[0]);
		}
		else {
			showAlertToUser(Constants.NO_INTERNET);
		}

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				
				try{
					 String text =
					 search.getText().toString().toLowerCase(Locale.getDefault());
					 mAdapter.filter(text);
					 } catch(Exception e){
					 e.printStackTrace();
					 showAlertToUser("No charity found");
					 }
								
				/*String text = search.getText().toString().toLowerCase(Locale.getDefault());
				mAdapter.filter(text);*/
			}
		});
		return rootView;

	}

	public class Make_Donation extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
				result = function.getCharityList(localArrayList);
				// result = function.Make_donation_list(localArrayList);

			}
			catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					
					global_fav_item_list.addAll(result);
					
					for(int i = 0;i<global_fav_item_list.size();i++){
						
						temp.add(global_fav_item_list.get(i).get("charity_name"));
					}

					mAdapter = new MyAdapter(result, getActivity());
					listview.setAdapter(mAdapter);
					
					 new SearchFromList().execute(new Void[0]);
				}
				else {
					showAlertToUser("No data found");
				}
			}
			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
				ae.printStackTrace();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(), R.drawable.loading);
			db.show();
		}

	}
	
	public class SearchFromList extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ArrayList localArrayList = new ArrayList();
		
		

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("name",
						""));
				
				result = function.TextToSearch(localArrayList);
			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					global_search_item_list.clear();
					global_search_item_list.addAll(result);
					
					
					mDisplayedValues.addAll(global_search_item_list);
					
					Log.e("SEARCH LIST ==",""+global_search_item_list);
					
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
	
	/******************************** get default amount ********************************/
	
	public class getDefaultAmountValue extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

	HashMap<String, String> result = new HashMap<String, String>();
		ArrayList localArrayList = new ArrayList();
		
		

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				
				
				result = function.getDefaultAmount(localArrayList);
			} catch (Exception localException) {
				localException.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
			
				String getDefaultAmount = (String) result.get("default_donation_to_purchase");
				GlobalDefualtAmount = getDefaultAmount;
				
				//new Make_Donation().execute(new Void[0]);
				
			} catch (Exception ae) {
				new Make_Donation().execute(new Void[0]);
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

		public MyAdapter(ArrayList<HashMap<String, String>> list, Activity activity) {
			mInflater = LayoutInflater.from(getActivity());
			fav_item_list = new ArrayList<HashMap<String, String>>();
			fav_item_list.addAll(list);
			
		}

		public void filter(String charText) {
			Log.e("global_search_item_list==", "" + global_search_item_list);
			Log.e("mDisplayedValues==", "" + mDisplayedValues);
			charText = charText.toLowerCase(Locale.getDefault());
			fav_item_list.clear();
			if (charText.length() == 0) {
				//fav_item_list.addAll(mDisplayedValues);
				fav_item_list.addAll(global_fav_item_list);
			} else {
				Log.e("mDisplayedValues==",""+mDisplayedValues);
				for (int i = 0; i < mDisplayedValues.size(); i++) {

					if (mDisplayedValues.get(i).get("charity_name")
							.toLowerCase(Locale.getDefault())
							.startsWith(charText)) {

						fav_item_list.add(mDisplayedValues.get(i));

					}
				}
				Log.e("fav_item_list==",""+fav_item_list);
			}
			notifyDataSetChanged();

		}

		@Override
		public int getCount() {

			return fav_item_list.size();
		}

		@Override
		public Object getItem(int position) {

			return fav_item_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.make_donation_list_item, null);
				holder.charity_name = (TextView) convertView.findViewById(R.id.charity_name);
				holder.enter_amount = (EditText) convertView.findViewById(R.id.enter_amount);
				holder.donate = (Button) convertView.findViewById(R.id.donate);

				holder.star = (ImageView) convertView.findViewById(R.id.star);
				

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.enter_amount.setTag(position);
			holder.donate.setTag(position);
			holder.star.setTag(position);
			
			if(GlobalDefualtAmount==null || GlobalDefualtAmount.equals("")){
				holder.enter_amount.setHint("Enter Amount");
			}else {
				holder.enter_amount.setText(GlobalDefualtAmount);
				
			}
			
			
			Log.i("temp===>>",""+temp);
			Log.i("list item===>>",""+fav_item_list.get(position).get("charity_name"));
			if(temp.contains(fav_item_list.get(position).get("charity_name"))){
				
				Log.i("marked","if if");
				holder.star.setBackgroundResource(R.drawable.star_rating_icon);
			} else {
				Log.i("unmakred","else else");
				holder.star.setBackgroundResource(R.drawable.unmark_rating);
			}
			
			holder.star.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						Integer arg = null;
						arg = (Integer) v.getTag();
						String charity_name = fav_item_list.get(arg).get(
								"charity_name");
						if(temp.contains(charity_name)){
						showDialog(charity_name, arg);
						}else {
							showDialog(arg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			holder.donate.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer arg = null;
					arg = (Integer) v.getTag();

					String amount = holder.enter_amount.getText().toString();
					String charity_id = fav_item_list.get(position).get("id");

					String charity_name = fav_item_list.get(position).get("charity_name");

					if (amount.trim().length() < 1) {
						holder.enter_amount.setError("Enter amount");

					}
					else {

						globalId = charity_id;
						globalItemAmount = amount;
						showConfirmationDialog("self-donation", charity_id, amount, charity_name);
					}
				}
			});

			holder.charity_name.setText(fav_item_list.get(position).get("charity_name"));

			return convertView;
		}
		protected void showDialog(String charity_name, final Integer arg) {
			final Dialog dialog;
			dialog = new Dialog(getActivity());
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

			Drawable d = new ColorDrawable(Color.BLACK);
			d.setAlpha(0);
			dialog.getWindow().setBackgroundDrawable(d);

			Button yes, no;
			TextView tv;

			dialog.setContentView(R.layout.make_charity_unfav_dialog);
			yes = (Button) dialog.findViewById(R.id.yes);
			no = (Button) dialog.findViewById(R.id.no);
			tv = (TextView) dialog.findViewById(R.id.tv);

			tv.setText("Are you sure you want to make " + charity_name
					+ " unfavorite ?");

			yes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					String id = fav_item_list.get(arg).get("id");
					new makeUnFavorite(id, arg).execute(new Void[0]);
				}
			});

			no.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
		}
		
		
		
		protected void showDialog(final int pos) {
			final Dialog dialog;
			dialog = new Dialog(getActivity());
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

			Drawable d = new ColorDrawable(Color.BLACK);
			d.setAlpha(0);
			dialog.getWindow().setBackgroundDrawable(d);

			Button yes, no;

			dialog.setContentView(R.layout.add_to_fav_dialog);
			yes = (Button) dialog.findViewById(R.id.yes);
			no = (Button) dialog.findViewById(R.id.no);

			yes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					//String id = fav_item_list.get(pos).get("charity_id");
					
					String ID = fav_item_list.get(pos).get("id");
					
					//Toast.makeText(getActivity(), "id====="+id+"**"+ID, Toast.LENGTH_LONG).show();
					
					new makeFavorite(ID, pos).execute(new Void[0]);
				}
			});

			no.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
		}

	}

	class ViewHolder {

		ImageView star;

		TextView charity_name;

		EditText enter_amount;

		Button donate;

	}

	protected void showConfirmationDialog(final String item_id, final String charity_id, final String donate_amount,
			final String charity_name) {
		final Dialog dialog;
		dialog = new Dialog(getActivity());
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;
		TextView donate_text;

		dialog.setContentView(R.layout.confirmation_dialog);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);
		donate_text = (TextView) dialog.findViewById(R.id.donate_text);

		donate_text.setText("Donate $ " + donate_amount + " to " + charity_name + ".");

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (isConnected) {
					// ******** pay pal integration ****************//
					// new DonateCharity(item_id ,
					// charity_id,donate_amount).execute(new Void[0]);

					thingToBuy = new PayPalPayment(new BigDecimal(donate_amount), "USD", charity_name,
							PayPalPayment.PAYMENT_INTENT_SALE);
					Intent intent = new Intent(getActivity(), PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, REQUEST_CODE_PAYMENT);
				}
				else {
					showAlertToUser(Constants.NO_INTERNET);
				}

			}
		});

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public void onFuturePaymentPressed(View pressed) {
		Intent intent = new Intent(getActivity(), PayPalFuturePaymentActivity.class);

		startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("result ok", "result ok");

				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				Log.e("confirm==", "" + confirm);
				if (confirm != null) {
					try {
						Log.e(TAG, confirm.toJSONObject().toString(4));
						Log.e(TAG, confirm.getPayment().toJSONObject().toString(4));

						Log.i("JSON==", "" + confirm.toJSONObject().toString());

						Toast.makeText(getActivity(), "PaymentConfirmation info received from PayPal",
								Toast.LENGTH_LONG).show();
						JSONObject jobj = new JSONObject();
						jobj = confirm.toJSONObject();

						JSONObject respose = jobj.getJSONObject("response");
						String transaction_id = respose.getString("id");
						String response_type = jobj.getString("response_type");

						new DonateCharity("self-donation", globalId, globalItemAmount, transaction_id, response_type)
								.execute(new Void[0]);

					}
					catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ", e);
					}
				}
			}
			else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			}
			else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		}
		else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(getActivity(), "Future Payment code received from PayPal", Toast.LENGTH_LONG)
								.show();

					}
					catch (JSONException e) {
						Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
					}
				}
			}
			else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			}
			else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
		}
	}

	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	}

	public void onFuturePaymentPurchasePressed(View pressed) {
		// Get the Application Correlation ID from the SDK
		String correlationId = PayPalConfiguration.getApplicationCorrelationId(getActivity());

		Log.i("FuturePaymentExample", "Application Correlation ID: " + correlationId);

		// TODO: Send correlationId and transaction details to your server for processing with
		// PayPal...
		Toast.makeText(getActivity(), "App Correlation ID received from SDK", Toast.LENGTH_LONG).show();
	}

	// *********************** donate charity ***********************************//

	public class DonateCharity extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String itemId, charityId, donateAmount, transaction_id, type;

		public DonateCharity(String item_id, String charity_id, String donate_amount, String transaction_id,
				String response_type) {
			this.itemId = item_id;
			this.charityId = charity_id;
			this.donateAmount = donate_amount;
			this.transaction_id = transaction_id;
			this.type = response_type;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("user_id", Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("item_id", this.itemId));
				localArrayList.add(new BasicNameValuePair("donate_amount", this.donateAmount));
				localArrayList.add(new BasicNameValuePair("charity_id", this.charityId));
				localArrayList.add(new BasicNameValuePair("authkey", "Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("transaction_id", this.transaction_id));
				localArrayList.add(new BasicNameValuePair("card_id", "card_id"));
				result = function.donateCharity(localArrayList);

			}
			catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {

					showAlertToUser("Amount donated successfully.");
				}
				else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser((String) result.get(Constants.MESSAGE_WHAT_HAPPEN_KEY));
				}
			}

			catch (Exception ae) {
				showAlertToUser(Constants.ERROR_MSG);
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(), R.drawable.loading);
			db.show();
		}

	}

	@Override
	public void onDestroy() {
		// Stop service when done
		getActivity().stopService(new Intent(getActivity(), PayPalService.class));
		super.onDestroy();
	}

	
	

	public class makeFavorite extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String id;

		int fav_pos;
		

		public makeFavorite(String id2, int pos) {
			this.id = id2;
			this.fav_pos = pos;
			
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList
						.add(new BasicNameValuePair("charity_id", this.id));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				result = function.make_fav(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					
					Toast.makeText(getActivity(), "Added to favorite successfully.", Toast.LENGTH_SHORT).show();
					refreshScreen();

				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					Toast.makeText(getActivity(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
				}
			}

			catch (Exception ae) {
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
	
	public class makeUnFavorite extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String id;

		int fav_pos;

		public makeUnFavorite(String id2, int pos) {
			this.id = id2;
			this.fav_pos = pos;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList
						.add(new BasicNameValuePair("charity_id", this.id));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				result = function.make_unfav(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					showAlertToUser("Charity made unfavorite successfully.");
					
					refreshScreen();
					/*added_to_unfav_list.add(fav_pos);

					fav_item_list.remove(fav_item_list.get(this.fav_pos));

					mAdapter.notifyDataSetChanged();*/
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Something went wrong.Please try again after some time.");
				}
			}

			catch (Exception ae) {
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
	
	public void refreshScreen() {
		FragmentManager fm = getActivity()
				.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		MakeDonation fragment = new MakeDonation();

		fm.popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);

		if (fragment != null) {

			ft.replace(R.id.frame_layout, fragment);
		} else {
			ft.add(R.id.frame_layout, fragment);
		}

		ft.commit();
	}
}
