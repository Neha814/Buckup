package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.buckup.Home;
import com.example.buckup.R;

public class ShareFragment extends Fragment {
	Button fb, google, twitter, linkedin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.share, container, false);
		fb = (Button) rootView.findViewById(R.id.fb);
		twitter = (Button) rootView.findViewById(R.id.twitter);
		google = (Button) rootView.findViewById(R.id.google);
		linkedin = (Button) rootView.findViewById(R.id.linkedin);

		twitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_TEXT, "It's a Tweet!"
							+ "#BuckUp");
					intent.setType("text/plain");
					final PackageManager pm = getActivity().getPackageManager();
					final List<?> activityList = pm.queryIntentActivities(
							intent, 0);
					int len = activityList.size();
					for (int i = 0; i < len; i++) {

						final ResolveInfo app = (ResolveInfo) activityList
								.get(i);

						if ((app.activityInfo.name.contains("twitter"))) {
							Log.i("twitter==<>", "" + app.activityInfo.name);

							final ActivityInfo activity = app.activityInfo;
							final ComponentName x = new ComponentName(
									activity.applicationInfo.packageName,
									activity.name);

							intent = new Intent(Intent.ACTION_SEND);
							intent.addCategory(Intent.CATEGORY_LAUNCHER);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							intent.setComponent(x);

							intent.putExtra(Intent.EXTRA_TEXT,
									"It's a tweet #BuckUp");
							intent.setType("application/twitter");
							startActivity(intent);

							break;

						} else {
							if (i + 1 == len) {
								String link = "https://play.google.com/store/apps/details?id=com.twitter.android&hl=en";
								Log.e("else else", "else else");
								showDailog(link);
								break;
							}

						}
					}
				} catch (Exception ae) {
					ae.printStackTrace();
				}

			}
		});

		fb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					String urlToShare = "www.buckupforchange.com";
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");

					intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
					intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
					final PackageManager pm = getActivity().getPackageManager();
					final List<?> activityList = pm.queryIntentActivities(
							intent, 0);
					int len = activityList.size();
					for (int i = 0; i < len; i++) {

						final ResolveInfo app = (ResolveInfo) activityList
								.get(i);
						Log.i("<>==<>", "" + app.activityInfo.packageName);
						if (app.activityInfo.packageName.toLowerCase()
								.startsWith("com.facebook.katana")) {
							String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
									+ urlToShare;
							intent = new Intent(Intent.ACTION_VIEW, Uri
									.parse(sharerUrl));
							startActivity(intent);

							break;

						} else {
							if (i + 1 == len) {
								String link = "https://play.google.com/store/apps/details?id=com.twitter.android&hl=en";
								Log.e("else else", "else else");
								showDailog(link);
								break;
							}

						}
					}
				} catch (Exception ae) {
					ae.printStackTrace();
				}

			}
		});

		google.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					List<Intent> targetedShareIntents = new ArrayList<Intent>();
					Intent share = new Intent(
							android.content.Intent.ACTION_SEND);
					share.setType("text/plain");
					List<ResolveInfo> resInfo = getActivity()
							.getPackageManager()
							.queryIntentActivities(share, 0);
					if (!resInfo.isEmpty()) {
						for (ResolveInfo info : resInfo) {
							Intent targetedShare = new Intent(
									android.content.Intent.ACTION_SEND);
							targetedShare.setType("text/plain"); // put here
																	// your mime
																	// type
							if (info.activityInfo.packageName.toLowerCase()
									.contains("plus")
									|| info.activityInfo.name.toLowerCase()
											.contains("plus")) {
								System.out
										.println("inside the if conditionsssss");
								targetedShare.putExtra(Intent.EXTRA_SUBJECT,
										"Check Out BuckUp");
								targetedShare.putExtra(Intent.EXTRA_TEXT,
										"Check Out BuckUp...");
								targetedShare
										.setPackage(info.activityInfo.packageName);
								targetedShareIntents.add(targetedShare);
							}
						}
						Intent chooserIntent = Intent.createChooser(
								targetedShareIntents.remove(0),
								"Select app to share");
						chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
								targetedShareIntents
										.toArray(new Parcelable[] {}));
						startActivity(chooserIntent);
					}
				} catch (Exception e) {
					Log.v("VM", "Exception while sending image on" + "plus"
							+ " " + e.getMessage());
				}
			}

		});

		linkedin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String urlToShare = "www.buckupforchange.com";
				Intent intent1 = new Intent(Intent.ACTION_SEND);
				intent1.setType("text/plain");

				intent1.putExtra(Intent.EXTRA_TEXT, urlToShare);
				intent1.putExtra(Intent.EXTRA_SUBJECT, "subject");
				final PackageManager pm = getActivity().getPackageManager();
				final List<?> activityList = pm.queryIntentActivities(
						intent1, 0);
				int len = activityList.size();
				for (int i = 0; i < len; i++) {

					final ResolveInfo app = (ResolveInfo) activityList
							.get(i);
					Log.i("<>==<>", "" + app.activityInfo.packageName);
					if(app.activityInfo.packageName.contains("com.linkedin.android")){
//						Intent shareIntent = new Intent(Intent.ACTION_SEND);
//					    shareIntent.setClassName("com.linkedin.android",
//					            "com.linkedin.android.home.UpdateStatusActivity"); 
//					    shareIntent.setType("text/*");
//					    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out BuckUp");           
//					    startActivity(shareIntent);
//					    break;
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse("linkedin://you"));
						final PackageManager packageManager = getActivity()
								.getPackageManager();
						final List<ResolveInfo> list = packageManager
								.queryIntentActivities(intent,
										PackageManager.MATCH_DEFAULT_ONLY);
						if (list.isEmpty()) {
							intent = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("http://www.linkedin.com/shares/view?id=you"));
							intent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out BuckUp");   
						}
						startActivity(intent);
						break ;
					}
					else {
						if(i+1==len){
							String link = "https://play.google.com/store/apps/details?id=com.linkedin.android&hl=en";
							Log.e("else else", "else else");
							showDailog(link);
							break;
						}
					}
				}

			}
		});
		return rootView;
	}

	protected void showDailog(final String link) {
		final Dialog dialog;
		dialog = new Dialog(getActivity());
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(0);
		dialog.getWindow().setBackgroundDrawable(d);

		Button yes, no;

		dialog.setContentView(R.layout.app_not_installed_dialog);
		yes = (Button) dialog.findViewById(R.id.yes);
		no = (Button) dialog.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent i = new Intent(android.content.Intent.ACTION_VIEW);
				i.setData(Uri.parse(link));
				startActivity(i);
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
