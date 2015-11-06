package com.example.fragments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buckup.Home;
import com.example.buckup.R;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class HomeFragment extends Fragment {

	ListView listview;

	Boolean isConnected;

	Button yes_to_all;

	MyAdapter mAdapter;

	MyAdapter1 mAdapter1;

	String globalItemId, globalId, globalItemAmount;

	Boolean isSuccessfullyDonated = false;

	TransparentProgressDialog db;

	Boolean isClicked = false;

	ArrayList<String> added_to_fav_list = new ArrayList<String>();

	ArrayList<HashMap<String, String>> pic_A_fav;
	//ArrayList<HashMap<String, String>> pic_A_favToDonateAllAmount;

	int global_pos = -1;

	String AMOUNT, IDS;
	double donate_amount;

	// pay pal variables
	PayPalPayment thingToBuy;

	private View rootView;
	
	Boolean isDonateToAll = false;
	
	SharedPreferences sp;

	private static final String TAG = "paymentExample";

	// private static final String CONFIG_ENVIRONMENT =
	// PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "AUcUg5oQHvPZyc6sYU4yZj9FDYYxSzQC0zy2dkC1NsUpbUJgujKvzHy9MrtEirI3XjBdWTct4iC_z4Yv";

	private static final int REQUEST_CODE_PAYMENT = 1;

	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Hipster Store")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	public void showAlertToUser(String paramString) {
		try {
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(
					getActivity());
			localBuilder
					.setMessage(paramString)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramAnonymousDialogInterface,
										int paramAnonymousInt) {
									paramAnonymousDialogInterface.cancel();
									if (isSuccessfullyDonated) {
										isSuccessfullyDonated = false;
										FragmentManager fm = getActivity()
												.getSupportFragmentManager();
										FragmentTransaction ft = fm
												.beginTransaction();

										HomeFragment fragment = new HomeFragment();

										fm.popBackStack(
												null,
												FragmentManager.POP_BACK_STACK_INCLUSIVE);

										if (fragment != null) {

											ft.replace(R.id.frame_layout,
													fragment);
										} else {
											ft.add(R.id.frame_layout, fragment);
										}

										ft.commit();
									}
								}
							});
			localBuilder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAlertToUser2(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

						FragmentManager fm = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();

						HomeFragment fragment = new HomeFragment();

						fm.popBackStack(null,
								FragmentManager.POP_BACK_STACK_INCLUSIVE);

						if (fragment != null) {

							ft.replace(R.id.frame_layout, fragment);
						} else {
							ft.add(R.id.frame_layout, fragment);
						}

						ft.commit();

					}
				});
		localBuilder.create().show();
	}

	public void showAlertToUser3(String paramString) {
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

		rootView = inflater.inflate(R.layout.homefragment, container, false);
		listview = (ListView) rootView.findViewById(R.id.listview);
		yes_to_all = (Button) rootView.findViewById(R.id.yes_to_all);

		Intent intent = new Intent(getActivity(), PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		getActivity().startService(intent);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

		yes_to_all.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isDonateToAll = true;
				if (Constants.item_list.size() > 0) {
					List<String> ids = new ArrayList<String>();
					List<String> amount = new ArrayList<String>();
					 donate_amount = 0;
					for (int i = 0; i < Constants.item_list.size(); i++) {
						donate_amount = donate_amount
								+ Double.parseDouble(Constants.item_list.get(i)
										.get(Constants.ROUNDED_AMOUNT_BYADMIN));
						amount.add(Constants.item_list.get(i).get(
								Constants.ROUNDED_AMOUNT_BYADMIN));
						ids.add(Constants.item_list.get(i).get(
								Constants.ITEM_ID_KEY));
					}
					
					donate_amount = (double) Math.round(donate_amount * 100) / 100;
					
					String donate_amount_text  = String.format( "%.2f", donate_amount );
					
					
					AMOUNT = amount.toString().replace("[", "")
							.replace("]", "").replace(", ", ",");
					IDS = ids.toString().replace("[", "").replace("]", "")
							.replace(", ", ",");
					Log.e("AMOUNT==", "" + AMOUNT);
					Log.e("IDS==", "" + IDS);
					showDonateAllAmountConfirmationDialog(donate_amount,donate_amount_text);
				} else {
					showAlertToUser("No charity is added.");
				}
			}
		});

		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected) {
			String hitOnce = sp.getString("hitOnce", "no");
			if (hitOnce.equalsIgnoreCase("no")) {
				new getTransaction().execute(new Void[0]);
			} else {
				new item_list().execute(new Void[0]);
			}

		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}
		return rootView;
	}

	public void showDonateAllAmountConfirmationDialog(double donate_amount ,String TotalAmt) {
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

		donate_text.setText("Donate $ " + TotalAmt
				+ " to your favorite charity.");

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (isConnected) {
					 new pic_A_favToDonateAllCharity().execute(new
					 Void[0]);
				} else {
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

	@Override
	public void onResume() {
		super.onResume();

	}

	public class item_list extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList result;

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				result = function.item_list(localArrayList);

				Log.e("result item lit==", "" + result);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {

					Constants.item_list = result;
					mAdapter = new MyAdapter(Constants.item_list, getActivity());
					listview.setAdapter(mAdapter);
					
					for (int i = 0; i < Constants.item_list.size(); i++) {
						Constants.TOTAL_AMOUNT = Constants.TOTAL_AMOUNT
								+ Double.parseDouble(Constants.item_list.get(i)
										.get(Constants.ROUNDED_AMOUNT_BYADMIN));
					
					}
					
					Constants.TOTAL_AMOUNT = (double) Math.round(Constants.TOTAL_AMOUNT * 100) / 100;
				} else {
					showAlertToUser("No charity assigned");
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

	public class getTransaction extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("access_token",
						Constants.ACESS_TOKEN));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				result = function.get_transaction(localArrayList);

				Log.e("result item lit==", "" + result);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (((String) result.get(Constants.RESPONSE_KEY))
						.equalsIgnoreCase("true")) {
					
					Editor e = sp.edit();
					e.putString("hitOnce", "yes");
					e.commit();
					new item_list().execute(new Void[0]);
				} else {
					showAlertToUser(((String) result
							.get(Constants.MESSAGE_WHAT_HAPPEN_KEY)));
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

	class MyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter(ArrayList<HashMap<String, String>> list,
				Activity activity) {
			mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {

			return Constants.item_list.size();
		}

		@Override
		public Object getItem(int position) {

			return Constants.item_list.get(position);
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
				convertView = mInflater.inflate(R.layout.home_item, null);
				holder.donate_amount_name = (TextView) convertView
						.findViewById(R.id.donate_amount_name);
				holder.amount = (TextView) convertView
						.findViewById(R.id.amount);
				holder.star = (ImageView) convertView.findViewById(R.id.star);
				holder.pic_a_fav = (Button) convertView
						.findViewById(R.id.pic_a_fav);
				holder.yes = (Button) convertView.findViewById(R.id.yes);
				holder.no = (Button) convertView.findViewById(R.id.no);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.star.setTag(position);
			holder.pic_a_fav.setTag(position);
			holder.yes.setTag(position);
			holder.no.setTag(position);
			
			
			
			double amount = Double.parseDouble(Constants.item_list.get(position).get(Constants.ROUNDED_AMOUNT_BYADMIN));
			
				String amount_string = String.format( "%.2f", amount );

				double item_amount = Double.parseDouble(Constants.item_list.get(position).get(
						Constants.ITEM_PURCHASE_AMOUNT_KEY));

				String item_amount_string = String.format( "%.2f", item_amount );
				
				double rounded_amount =Double.parseDouble( Constants.item_list.get(position).get(
						Constants.ROUNDED_AMOUNT_BYADMIN));
				
				String rounded_amount_string = String.format( "%.2f", rounded_amount );

			holder.donate_amount_name.setText("Donate $ "
					+ amount_string
					+ " "
					+ "to "
					+ Constants.item_list.get(position).get(
							Constants.CHARITY_PREFER_BYADMIN_KEY)
					+ " charity for spending $ "
					+ item_amount_string
					+ " at "
					+ Constants.item_list.get(position).get(
							Constants.ITEM_NAME_KEY) + ".");

			holder.amount.setText("$ "+rounded_amount_string);

			Log.e("star tag=", "" + holder.star.getTag());

			if (added_to_fav_list.size() > 0) {
			} else {
				if (Constants.item_list.get(position)
						.get("is_already_added_to_fav").equals("0")) {
					holder.star.setImageResource(R.drawable.unmark_rating);
				} else {
					holder.star.setImageResource(R.drawable.star_rating_icon);
				}
			}

			holder.yes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					isDonateToAll = false; 
					Integer arg = null;
					arg = (Integer) v.getTag();
					String item_id = Constants.item_list.get(arg).get(
							Constants.ITEM_ID_KEY);
					String charity_id = Constants.item_list.get(arg).get(
							"charity_id");
					String charity_name = Constants.item_list.get(arg).get(
							Constants.CHARITY_PREFER_BYADMIN_KEY);
					String donate_amount = Constants.item_list.get(arg).get(
							Constants.ROUNDED_AMOUNT_BYADMIN);

					showConfirmationDialog(item_id, charity_id, donate_amount,
							charity_name);

				}
			});
			
			
			holder.no.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					isDonateToAll = false; 
					Integer arg = null;
					arg = (Integer) v.getTag();
					String item_id = Constants.item_list.get(arg).get(
							Constants.ITEM_ID_KEY);
					String charity_id = Constants.item_list.get(arg).get(
							"charity_id");
					String charity_name = Constants.item_list.get(arg).get(
							Constants.CHARITY_PREFER_BYADMIN_KEY);
					String donate_amount = Constants.item_list.get(arg).get(
							Constants.ROUNDED_AMOUNT_BYADMIN);
					
				
					Log.e("pos  ================>>>",""+arg);
					Log.e("item id================>>>",""+item_id);
					Log.e("charity id================>>>",""+charity_id);
					Log.e("charity name================>>>",""+charity_name);
					
					if (isConnected) {
						/*
						 * http://vimadollc.com/Web_API/hide_item.php?item_id=221
						 * &authkey=Buckupkey2015
						 */
						
						Log.e("ID==================>>>",""+item_id);
						new hide_item(item_id).execute(new Void[0]);
						} else {
						showAlertToUser(Constants.NO_INTERNET);
					}

					/*showDeleteDialog(item_id, charity_id, donate_amount,
							charity_name);*/

				}
			});
			
			

			holder.star.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Integer arg = null;
					arg = (Integer) v.getTag();
					Log.e("list=", "" + Constants.item_list);
					Log.e("arg=", "" + arg);
					Log.e("added value==",
							""
									+ Constants.item_list.get(arg).get(
											"is_already_added_to_fav"));
					if (Constants.item_list.get(arg)
							.get("is_already_added_to_fav").equals("0")) {
						showDialog(arg);
					} else {
						showAlertToUser("Already added to favorite.");
					}
				}
			});

			holder.pic_a_fav.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					isDonateToAll = false; 
					Integer arg = null;
					arg = (Integer) v.getTag();
					String pic_a_fav_item_id = Constants.item_list.get(arg)
							.get(Constants.ITEM_ID_KEY);
					String amount = Constants.item_list.get(arg).get(
							Constants.ROUNDED_AMOUNT_BYADMIN);
					new pic_A_fav(pic_a_fav_item_id, amount)
							.execute(new Void[0]);

				}
			});

			return convertView;
		}
		
		
		protected void showDeleteDialog(final String item_id,
				final String charity_id, final String donate_amount,
				final String charity_name) {
			
			final String ID = item_id;
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

			donate_text.setText("Are you sure you want to delete "+charity_name+" charity ?");

			yes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (isConnected) {
						/*
						 * http://vimadollc.com/Web_API/hide_item.php?item_id=221
						 * &authkey=Buckupkey2015
						 */
						
						Log.e("ID==================>>>",""+ID);
						new hide_item(ID).execute(new Void[0]);
						} else {
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

		protected void showConfirmationDialog(final String item_id,
				final String charity_id, final String donate_amount,
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

			donate_text.setText("Donate $ " + donate_amount + " to "
					+ charity_name + ".");

			yes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (isConnected) {
						// ******** pay pal integration ****************//
						// new DonateCharity(item_id ,
						// charity_id,donate_amount).execute(new Void[0]);

						globalItemId = item_id;
						globalId = charity_id;
						globalItemAmount = donate_amount;

						thingToBuy = new PayPalPayment(new BigDecimal(
								donate_amount), "USD", charity_name,
								PayPalPayment.PAYMENT_INTENT_SALE);
						Intent intent = new Intent(getActivity(),
								PaymentActivity.class);

						intent.putExtra(PaymentActivity.EXTRA_PAYMENT,
								thingToBuy);

						startActivityForResult(intent, REQUEST_CODE_PAYMENT);
					} else {
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
					String id = Constants.item_list.get(pos).get("charity_id");
					String name = Constants.item_list.get(pos).get(
							Constants.CHARITY_PREFER_BYADMIN_KEY);
					new makeFavorite(id, pos, name).execute(new Void[0]);
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
		TextView donate_amount_name;

		TextView amount;

		ImageView star;

		Button pic_a_fav;

		Button yes, no;

	}

	public void onFuturePaymentPressed(View pressed) {
		Intent intent = new Intent(getActivity(),
				PayPalFuturePaymentActivity.class);

		startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("result ok", "result ok");

				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				Log.e("confirm==", "" + confirm);
				if (confirm != null) {
					try {
						Log.e(TAG, confirm.toJSONObject().toString(4));
						Log.e(TAG, confirm.getPayment().toJSONObject()
								.toString(4));

						Log.i("JSON==", "" + confirm.toJSONObject().toString());

						Toast.makeText(
								getActivity(),
								"Payment Confirmation info received from PayPal",
								Toast.LENGTH_LONG).show();
						JSONObject jobj = new JSONObject();
						jobj = confirm.toJSONObject();

						JSONObject respose = jobj.getJSONObject("response");
						String transaction_id = respose.getString("id");
						String response_type = jobj.getString("response_type");
						
						if(isDonateToAll){
							new DonateCharityToAll(globalItemId, globalId,
									globalItemAmount, transaction_id, response_type)
									.execute(new Void[0]);
							
						} else {
						new DonateCharity(globalItemId, globalId,
								globalItemAmount, transaction_id, response_type)
								.execute(new Void[0]);
						}

					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ",
								e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		} else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject()
								.toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(getActivity(),
								"Future Payment code received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample",
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
		}
	}

	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	}

	public void onFuturePaymentPurchasePressed(View pressed) {
		// Get the Application Correlation ID from the SDK
		String correlationId = PayPalConfiguration
				.getApplicationCorrelationId(getActivity());

		Log.i("FuturePaymentExample", "Application Correlation ID: "
				+ correlationId);

		// TODO: Send correlationId and transaction details to your server for
		// processing with
		// PayPal...
		Toast.makeText(getActivity(), "App Correlation ID received from SDK",
				Toast.LENGTH_LONG).show();
	}

	// ****************** make favorite
	// ***************************************//

	public class makeFavorite extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String id;

		int fav_pos;
		String name;

		public makeFavorite(String id2, int pos, String name) {
			this.id = id2;
			this.fav_pos = pos;
			this.name = name;
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
					showAlertToUser2("Added to favorite successfully.");
					added_to_fav_list.add(this.name);
					// mAdapter.notifyDataSetChanged();
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser("Charity is already added to favorite.");
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

	// ******************************* Pic a Favorite
	// ******************************************//

	public class pic_A_fav extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList result = new ArrayList();

		ArrayList localArrayList = new ArrayList();

		String picFavItemId, amount;

		public pic_A_fav(String pic_a_fav_item_id, String amount) {
			this.picFavItemId = pic_a_fav_item_id;
			this.amount = amount;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				result = function.pic_a_fav(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					pic_A_fav = new ArrayList<HashMap<String, String>>();
					pic_A_fav.addAll(result);
					showPicAFavDialog(this.picFavItemId, this.amount);

				} else {
					showAlertToUser("No charity is added to favorite.");
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
	
	public class pic_A_favToDonateAllCharity extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList result = new ArrayList();

		ArrayList localArrayList = new ArrayList();

		String picFavItemId, amount;

		

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				result = function.pic_a_fav(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.size() > 0) {
					pic_A_fav = new ArrayList<HashMap<String, String>>();
					pic_A_fav.addAll(result);
					showPicAFavDialogToDonateAllAmount();

				} else {
					showAlertToUser("No charity is added to favorite.");
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

	protected void showPicAFavDialog(String itemId, String amount) {

		final Dialog dialog;
		Button cancel, submit;
		ListView pic_a_fav_listview;
		final String picFavItemId = itemId;
		final String picFavItemAmount = amount;
		dialog = new Dialog(getActivity(), R.style.full_screen_dialog);
		dialog.setCancelable(true);
		if (isClicked == false) {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		isClicked = true;

		dialog.setContentView(R.layout.pic_fav_dialog);

		cancel = (Button) dialog.findViewById(R.id.cancel);
		submit = (Button) dialog.findViewById(R.id.submit);
		pic_a_fav_listview = (ListView) dialog.findViewById(R.id.listview);

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

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (global_pos >= 0) {
					String id = pic_A_fav.get(global_pos).get("id");
					String name = pic_A_fav.get(global_pos).get("charity_name");

					// new DonateCharity(picFavItemId, id, picFavItemAmount)
					// .execute(new Void[0]);

					globalItemId = picFavItemId;
					globalId = id;
					globalItemAmount = picFavItemAmount;

					thingToBuy = new PayPalPayment(new BigDecimal(
							picFavItemAmount), "USD", name,
							PayPalPayment.PAYMENT_INTENT_SALE);
					Intent intent = new Intent(getActivity(),
							PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, REQUEST_CODE_PAYMENT);
					dialog.dismiss();
				} else {
					showAlertToUser3("Please select charity.");
				}
			}
		});

		mAdapter1 = new MyAdapter1(pic_A_fav, getActivity());
		pic_a_fav_listview.setAdapter(mAdapter1);

		pic_a_fav_listview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						global_pos = position;
					}
				});
	}

	// *************** pic a fav my adapter class ***************************//

	protected void showPicAFavDialogToDonateAllAmount() {

		final Dialog dialog;
		Button cancel, submit;
		ListView pic_a_fav_listview;
		
		dialog = new Dialog(getActivity(), R.style.full_screen_dialog);
		dialog.setCancelable(true);
		if (isClicked == false) {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		isClicked = true;

		dialog.setContentView(R.layout.pic_fav_dialog);

		cancel = (Button) dialog.findViewById(R.id.cancel);
		submit = (Button) dialog.findViewById(R.id.submit);
		pic_a_fav_listview = (ListView) dialog.findViewById(R.id.listview);

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

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (global_pos >= 0) {
					String id = pic_A_fav.get(global_pos).get("id");
					String name = pic_A_fav.get(global_pos).get("charity_name");

					// new DonateCharity(picFavItemId, id, picFavItemAmount)
					// .execute(new Void[0]);

					globalItemId = IDS;
					globalId = id;
					globalItemAmount = AMOUNT;
					
					

					thingToBuy = new PayPalPayment(new BigDecimal(
							donate_amount), "USD", name,
							PayPalPayment.PAYMENT_INTENT_SALE);
					Intent intent = new Intent(getActivity(),
							PaymentActivity.class);

					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

					startActivityForResult(intent, REQUEST_CODE_PAYMENT);
					dialog.dismiss();
				} else {
					showAlertToUser3("Please select charity.");
				}
			}
		});

		mAdapter1 = new MyAdapter1(pic_A_fav, getActivity());
		pic_a_fav_listview.setAdapter(mAdapter1);

		pic_a_fav_listview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						global_pos = position;
					}
				});
	}

	class MyAdapter1 extends BaseAdapter {

		LayoutInflater mInflater = null;

		public MyAdapter1(ArrayList<HashMap<String, String>> list,
				Activity activity) {
			mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {

			return pic_A_fav.size();
		}

		@Override
		public Object getItem(int position) {

			return pic_A_fav.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
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
			holder.charity_name.setText(pic_A_fav.get(position).get(
					"charity_name"));

			return convertView;
		}

	}

	class ViewHolder1 {
		TextView charity_name;

	}

	// *********************** donate charity
	// ***********************************//

	public class DonateCharity extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String itemId, charityId, donateAmount, transaction_id, type;

		public DonateCharity(String item_id, String charity_id,
				String donate_amount, String transaction_id,
				String response_type) {
			this.itemId = item_id;
			this.charityId = charity_id;
			this.donateAmount = donate_amount;
			this.transaction_id = transaction_id;
			this.type = response_type;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("item_id",
						this.itemId));
				localArrayList.add(new BasicNameValuePair("donate_amount",
						this.donateAmount));
				localArrayList.add(new BasicNameValuePair("charity_id",
						this.charityId));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("transaction_id",
						this.transaction_id));
				localArrayList
						.add(new BasicNameValuePair("card_id", "card_id"));
				result = function.donateCharity(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					isSuccessfullyDonated = true;
					showAlertToUser("Amount donated successfully.");
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser((String) result
							.get(Constants.MESSAGE_WHAT_HAPPEN_KEY));
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
	
	//****** donate charity to all ************//
	
	public class DonateCharityToAll extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String itemId, charityId, donateAmount, transaction_id, type;

		public DonateCharityToAll(String item_id, String charity_id,
				String donate_amount, String transaction_id,
				String response_type) {
			this.itemId = item_id;
			this.charityId = charity_id;
			this.donateAmount = donate_amount;
			this.transaction_id = transaction_id;
			this.type = response_type;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				localArrayList.add(new BasicNameValuePair("item_id",
						this.itemId));
				localArrayList.add(new BasicNameValuePair("donate_amount",
						this.donateAmount));
				localArrayList.add(new BasicNameValuePair("charity_id",
						this.charityId));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("transaction_id",
						this.transaction_id));
				localArrayList
						.add(new BasicNameValuePair("card_id", "card_id"));
				result = function.donateCharityToAll(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					isSuccessfullyDonated = true;
					showAlertToUser("Amount donated successfully.");
					Constants.item_list.clear();
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					showAlertToUser((String) result
							.get(Constants.MESSAGE_WHAT_HAPPEN_KEY));
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

	@Override
	public void onDestroy() {
		// Stop service when done
		getActivity().stopService(
				new Intent(getActivity(), PayPalService.class));
		super.onDestroy();
	}
	
	
	public class hide_item extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		HashMap result = new HashMap();

		ArrayList localArrayList = new ArrayList();

		String ItemID;

		public hide_item(String item_id) {
			this.ItemID = item_id;
			
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {

				localArrayList.add(new BasicNameValuePair("item_id",
						this.ItemID));
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				result = function.hide(localArrayList);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {
				if (result.get(Constants.RESPONSE_KEY).equals("true")) {
					Toast.makeText(getActivity(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
					//showAlertToUser2("Successfully deleted.");
					
					Intent i = new Intent(getActivity(), Home.class);
					startActivity(i);
					
				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					Toast.makeText(getActivity(), "Not able to delete", Toast.LENGTH_SHORT).show();
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
}
