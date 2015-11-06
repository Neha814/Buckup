package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buckup.R;
import com.example.fragments.HomeFragment.makeFavorite;
import com.example.fragments.HomeFragment.pic_A_fav;
import com.example.functions.Constants;
import com.example.functions.Functions;
import com.example.utils.NetConnection;
import com.example.utils.TransparentProgressDialog;

public class FavoriteFragment extends Fragment {

	ListView listview;

	EditText search;

	boolean isConnected;

	MyAdapter mAdapter;

	TransparentProgressDialog db;

	boolean NoFavPresent = true;

	ArrayList<HashMap<String, String>> fav_item_list;
	ArrayList<HashMap<String, String>> global_fav_item_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> global_search_item_list = new ArrayList<HashMap<String, String>>();
	ArrayList<String> temp = new ArrayList<String>();

	ArrayList<HashMap<String, String>> mDisplayedValues = new ArrayList<HashMap<String, String>>();

	ArrayList<Integer> added_to_unfav_list = new ArrayList<Integer>();

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

								}
							});
			localBuilder.create().show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.favorite, container, false);
		listview = (ListView) rootView.findViewById(R.id.listview);
		search = (EditText) rootView.findViewById(R.id.search);
		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected) {
			new FavCharityList().execute(new Void[0]);
		} else {
			showAlertToUser(Constants.NO_INTERNET);
		}

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String text = search.getText().toString()
							.toLowerCase(Locale.getDefault());

					if (fav_item_list != null && fav_item_list.size() > 0) {
						mAdapter.filter(text);
					} else {
						mAdapter = new MyAdapter(global_search_item_list,
								getActivity());
						mAdapter.filter(text);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// showAlertToUser("No favorite charity found");
				}

				// if (isConnected) {
				// String text = search.getText().toString()
				// .toLowerCase(Locale.getDefault());
				// if (text.length() > 0) {
				// new SearchFromList(text).execute(new Void[0]);
				// /*fav_item_list.clear();
				// mAdapter.notifyDataSetChanged();*/
				// } else {
				// fav_item_list.clear();
				// fav_item_list.addAll(global_fav_item_list);
				// mAdapter.notifyDataSetChanged();
				// }
				// } else {
				// showAlertToUser(Constants.NO_INTERNET);
				// }
			}
		});
		return rootView;
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
				localArrayList.add(new BasicNameValuePair("name", ""));

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

					Log.e("SEARCH LIST ==", "" + global_search_item_list);

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

	public class FavCharityList extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();

		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ArrayList localArrayList = new ArrayList();

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("authkey",
						"Buckupkey2015"));
				localArrayList.add(new BasicNameValuePair("user_id",
						Constants.USER_ID));
				result = function.getCharityList(localArrayList);
			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();

			try {

				if (result.size() > 0) {
					global_fav_item_list.addAll(result);

					for (int i = 0; i < global_fav_item_list.size(); i++) {

						temp.add(global_fav_item_list.get(i)
								.get("charity_name"));
					}

					NoFavPresent = false;
					mAdapter = new MyAdapter(result, getActivity());
					listview.setAdapter(mAdapter);
					// new SearchFromList().execute(new Void[0]);

				} else {

					NoFavPresent = true;
					showAlertToUser("No charity is added to your favorite.");
				}

				 new SearchFromList().execute(new Void[0]);
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
			fav_item_list = new ArrayList<HashMap<String, String>>();
			fav_item_list.addAll(list);

		}

		public void filter(String charText) {

			Log.e("global_search_item_list==", "" + global_search_item_list);
			Log.e("mDisplayedValues==", "" + mDisplayedValues);
			charText = charText.toLowerCase(Locale.getDefault());
			fav_item_list.clear();
			if (charText.length() == 0) {
				// fav_item_list.addAll(mDisplayedValues);
				fav_item_list.addAll(global_fav_item_list);
			} else {
				Log.e("mDisplayedValues==", "" + mDisplayedValues);
				for (int i = 0; i < mDisplayedValues.size(); i++) {

					if (mDisplayedValues.get(i).get("charity_name")
							.toLowerCase(Locale.getDefault())
							.startsWith(charText)) {

						fav_item_list.add(mDisplayedValues.get(i));

					}
				}
				Log.e("fav_item_list==", "" + fav_item_list);
			}

			if (!NoFavPresent) {

				Log.d(" ###fav_item_list (notify) ###", "" + fav_item_list);
				notifyDataSetChanged();

			} else if (NoFavPresent) {

				Log.d(" ***fav_item_list ***", "" + fav_item_list);

				mAdapter = new MyAdapter(fav_item_list, getActivity());
				listview.setAdapter(mAdapter);
			}

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.fav_list_item, null);
				holder.charity_name = (TextView) convertView
						.findViewById(R.id.charity_name);
				holder.charity_description = (TextView) convertView
						.findViewById(R.id.charity_description);

				holder.fav_star = (Button) convertView
						.findViewById(R.id.fav_star);
				holder.learn_more = (TextView) convertView
						.findViewById(R.id.learn_more);

				holder.donation_amount = (TextView) convertView
						.findViewById(R.id.donation_amount);
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.fav_star.setTag(position);
			holder.learn_more.setTag(position);

			Log.i("global lsit===", "" + global_fav_item_list);
			Log.i("fav lsit===",
					"" + fav_item_list.get(position).get("charity_name"));

			if (temp.contains(fav_item_list.get(position).get("charity_name"))) {
				holder.fav_star
						.setBackgroundResource(R.drawable.star_rating_icon);
			} else {
				holder.fav_star.setBackgroundResource(R.drawable.unmark_rating);
			}

			holder.learn_more.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Integer arg = null;
					arg = (Integer) v.getTag();

					String link = fav_item_list.get(arg).get("website");

					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(link));
					startActivity(i);
				}
			});

			holder.fav_star.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Integer arg = null;
						arg = (Integer) v.getTag();
						String charity_name = fav_item_list.get(arg).get(
								"charity_name");
						if (temp.contains(charity_name)) {
							showDialog(charity_name, arg);
						} else {
							showDialog(arg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			holder.charity_name.setText(fav_item_list.get(position).get(
					"charity_name"));
			holder.charity_description.setText(fav_item_list.get(position).get(
					"description"));
			// rounded_amount_byadmin

			String donate_amount_text = String.format(
					"%.2f",
					Float.parseFloat(fav_item_list.get(position).get(
							"total_donation")));
			holder.donation_amount.setText("$ " + donate_amount_text);
			//
			// if (fav_item_list.get(position).get("business_name").equals(null)
			// || fav_item_list.get(position).get("business_name")
			// .equals("null")
			// || fav_item_list.get(position).get("business_name")
			// .equals("")) {
			// holder.partners_with.setVisibility(View.INVISIBLE);
			//
			// } else {
			// holder.partners_with.setVisibility(View.VISIBLE);
			// holder.partners_with.setText("Partners with "
			// + fav_item_list.get(position).get("business_name"));
			// }

			return convertView;
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
					// String id = fav_item_list.get(pos).get("charity_id");

					String ID = fav_item_list.get(pos).get("id");

					// Toast.makeText(getActivity(), "id====="+id+"**"+ID,
					// Toast.LENGTH_LONG).show();

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

	}

	class ViewHolder {
		TextView charity_name, charity_description, donation_amount;
		TextView learn_more;
		Button fav_star;

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
					added_to_unfav_list.add(fav_pos);

					fav_item_list.remove(fav_item_list.get(this.fav_pos));

					// ******* changes ************//

					new SearchFromList().execute(new Void[0]);

					mAdapter.notifyDataSetChanged();
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

					Toast.makeText(getActivity(),
							"Added to favorite successfully.",
							Toast.LENGTH_SHORT).show();
					refreshScreen();

				} else if (result.get(Constants.RESPONSE_KEY).equals("false")) {
					Toast.makeText(getActivity(),
							"Something went wrong.Please try again.",
							Toast.LENGTH_SHORT).show();
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

	public void refreshScreen() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		FavoriteFragment fragment = new FavoriteFragment();

		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

		if (fragment != null) {

			ft.replace(R.id.frame_layout, fragment);
		} else {
			ft.add(R.id.frame_layout, fragment);
		}

		ft.commit();
	}

}
