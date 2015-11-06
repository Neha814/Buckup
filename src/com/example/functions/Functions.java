package com.example.functions;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Html;

public class Functions {

	JSONParser json = new JSONParser();
	public static String url = "http://vimadollc.com/Web_API/";

	// public static String url =
	// "http://phphosting.osvin.net/Matt-Buckup/Web_API/";

	/**
	 * Add user plaid API
	 */

	public HashMap adduser_plaidapi(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "adduser_plaid_api.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.ACCESS_TOKEN_KEY,
						localJSONObject.getString("access_token"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Plaid API to get BAnk detials
	 */

	public ArrayList getBankdetail() {
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();

		try {

			JSONArray localJSONArray = new JSONArray(
					Html.fromHtml(
							this.json
									.BankDetailRequest("https://tartan.plaid.com/institutions"))
							.toString());

			for (int i = 0; i < localJSONArray.length(); i++) {
				HashMap localhashmap = new HashMap();
				localhashmap.put("type",
						localJSONArray.getJSONObject(i).get("type"));
				locallist.add(localhashmap);
			}

			return locallist;

		} catch (Exception ae) {
			ae.printStackTrace();
			return locallist;

		}

	}

	/*
	 * login
	 */

	public HashMap login(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "SignIn.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");

				JSONObject msg = localJSONObject
						.getJSONObject("MessageWhatHappen");
				localHashMap.put(Constants.ID_KEY, msg.getString("id"));
				localHashMap.put(Constants.USER_TYPE_KEY,
						msg.getString("user_type"));
				localHashMap.put(Constants.EMAILID_KEY,
						msg.getString("emailid"));
				localHashMap.put(Constants.USERNAME_KEY,
						msg.getString("username"));
				localHashMap.put(Constants.PASSWORD_KEY,
						msg.getString("password"));
				localHashMap.put(Constants.FNAME_KEY, msg.getString("fname"));
				localHashMap.put(Constants.LNAME_KEY, msg.getString("lname"));
				localHashMap.put(Constants.ADDRESS_KEY,
						msg.getString("address"));
				localHashMap.put(Constants.ZIP_KEY, msg.getString("zip"));
				localHashMap.put(Constants.PHONE_KEY, msg.getString("phone"));
				localHashMap.put(Constants.CREATED_KEY,
						msg.getString("created"));
				localHashMap.put(Constants.ACCESS_TOKEN_KEY,
						msg.getString("access_token"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * card info
	 * 
	 * @param localArrayList
	 * @return
	 */
	public HashMap insert_cardInfo(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "insert_card_info.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * for signUp
	 */

	public HashMap SignUp_info(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "SignUp.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.USER_ID_KEY,
						localJSONObject.getString("UserId"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * get item_list
	 */

	public ArrayList<HashMap<String, String>> item_list(ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "item_list.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray MessageWhatHappen = localJSONObject
						.getJSONArray("MessageWhatHappen");
				for (int i = 0; i < MessageWhatHappen.length(); i++) {
					HashMap localhashMap = new HashMap();
					localhashMap.put(Constants.ITEM_ID_KEY, MessageWhatHappen
							.getJSONObject(i).getString("id"));
					localhashMap.put(Constants.ITEM_CODE_KEY, MessageWhatHappen
							.getJSONObject(i).getString("item_code"));
					localhashMap.put(Constants.ITEM_NAME_KEY, MessageWhatHappen
							.getJSONObject(i).getString("item_name"));
					localhashMap.put(
							Constants.ITEM_PURCHASE_AMOUNT_KEY,
							MessageWhatHappen.getJSONObject(i).getString(
									"purchase_amount"));
					localhashMap.put(
							Constants.CHARITY_PREFER_BYADMIN_KEY,
							MessageWhatHappen.getJSONObject(i).getString(
									"charity_prefer_byadmin"));
					localhashMap.put(
							Constants.ROUNDED_AMOUNT_BYADMIN,
							MessageWhatHappen.getJSONObject(i).getString(
									"rounded_amount_byadmin"));
					localhashMap.put("charity_id", MessageWhatHappen
							.getJSONObject(i).getString("charity_id"));
					localhashMap.put(
							"is_already_added_to_fav",
							MessageWhatHappen.getJSONObject(i).getString(
									"is_already_added_to_fav"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * to get transaction
	 */

	public HashMap get_transaction(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "plaid_api_get_trans.php?",
							"POST", localArrayList)).toString());
			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * forgot password
	 */

	public HashMap forgot_password(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "forgetpassword.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * get fav charity list
	 */

	public ArrayList<HashMap<String, String>> TextToSearch(
			ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "search.php", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray MessageWhatHappen = localJSONObject
						.getJSONArray("MessageWhatHappen");
				for (int i = 0; i < MessageWhatHappen.length(); i++) {
					HashMap localhashMap = new HashMap();
					localhashMap.put("id", MessageWhatHappen.getJSONObject(i)
							.getString("id"));
					localhashMap.put("charity_name", MessageWhatHappen
							.getJSONObject(i).getString("charity_name"));

					localhashMap.put("description", MessageWhatHappen
							.getJSONObject(i).getString("description"));
					localhashMap.put("website", MessageWhatHappen
							.getJSONObject(i).getString("website"));
					
					localhashMap.put("total_donation", MessageWhatHappen.getJSONObject(i).getString("total_donation"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * search functionality
	 */
	public ArrayList<HashMap<String, String>> getCharityList(
			ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url
							+ "favourite_charities_list.php", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray MessageWhatHappen = localJSONObject
						.getJSONArray("MessageWhatHappen");
				for (int i = 0; i < MessageWhatHappen.length(); i++) {
					HashMap localhashMap = new HashMap();
					localhashMap.put("id", MessageWhatHappen.getJSONObject(i)
							.getString("id"));
					localhashMap.put("charity_name", MessageWhatHappen
							.getJSONObject(i).getString("charity_name"));
					localhashMap.put("address", MessageWhatHappen
							.getJSONObject(i).getString("address"));
					localhashMap.put("phone", MessageWhatHappen
							.getJSONObject(i).getString("phone"));
					localhashMap.put("description", MessageWhatHappen
							.getJSONObject(i).getString("description"));
					localhashMap.put("website", MessageWhatHappen
							.getJSONObject(i).getString("website"));
					localhashMap.put("EIN_number", MessageWhatHappen
							.getJSONObject(i).getString("EIN_number"));
					localhashMap.put("business_id", MessageWhatHappen
							.getJSONObject(i).getString("business_id"));
					localhashMap.put("created", MessageWhatHappen
							.getJSONObject(i).getString("created"));
					localhashMap.put("business_name", MessageWhatHappen
							.getJSONObject(i).getString("business_name"));
					
					localhashMap.put("total_donation", MessageWhatHappen.getJSONObject(i).getString("total_donation"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * make favorite
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap make_fav(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "make_favourite.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * pick a fav
	 */

	public ArrayList<HashMap<String, String>> pic_a_fav(ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "pick_favourite.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray MessageWhatHappen = localJSONObject
						.getJSONArray("MessageWhatHappen");
				for (int i = 0; i < MessageWhatHappen.length(); i++) {
					HashMap localhashMap = new HashMap();
					localhashMap.put("id", MessageWhatHappen.getJSONObject(i)
							.getString("id"));
					localhashMap.put("charity_name", MessageWhatHappen
							.getJSONObject(i).getString("charity_name"));
					localhashMap.put("phone", MessageWhatHappen
							.getJSONObject(i).getString("phone"));
					localhashMap.put("description", MessageWhatHappen
							.getJSONObject(i).getString("description"));
					localhashMap.put("website", MessageWhatHappen
							.getJSONObject(i).getString("website"));
					localhashMap.put("EIN_number", MessageWhatHappen
							.getJSONObject(i).getString("EIN_number"));
					localhashMap.put("business_id", MessageWhatHappen
							.getJSONObject(i).getString("business_id"));
					localhashMap.put("created", MessageWhatHappen
							.getJSONObject(i).getString("created"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * to submit app feedback
	 */

	public HashMap feedback(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "app_feedback.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * to update User profile in MyAccount
	 */
	public HashMap profileUpdate(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "set_profile.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * to Get User profile in MyAccount
	 */
	public HashMap profileGetUpdate(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "get_profile.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put("Email", localJSONObject.getString("Email"));
				localHashMap.put("UserName",
						localJSONObject.getString("UserName"));
				localHashMap.put("Password",
						localJSONObject.getString("Password"));
				localHashMap.put("Zip", localJSONObject.getString("Zip"));

				localHashMap.put("card_type",
						localJSONObject.getString("card_type"));
				localHashMap.put("name_on_card",
						localJSONObject.getString("name_on_card"));
				localHashMap.put("card_number",
						localJSONObject.getString("card_number"));
				localHashMap.put("expiry_date",
						localJSONObject.getString("expiry_date"));
				
				localHashMap.put("fname",localJSONObject.getString("fname"));
				
				localHashMap.put("cvv_number",localJSONObject.getString("cvv_number"));
				
				localHashMap.put("lname",localJSONObject.getString("lname"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * to Get User settings in Setting Activity
	 */
	public HashMap settingsGetUpdate(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(
							url + "setting_notification.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				JSONObject data = localJSONObject.getJSONObject("settingdata");
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put("id", data.getString("id"));
				localHashMap.put("user_type", data.getString("user_type"));
				localHashMap.put("emailid", data.getString("emailid"));
				localHashMap.put("username", data.getString("username"));
				localHashMap.put("password", data.getString("password"));
				localHashMap.put("fname", data.getString("fname"));
				localHashMap.put("lname", data.getString("lname"));
				localHashMap.put("address", data.getString("address"));
				localHashMap.put("zip", data.getString("zip"));
				localHashMap.put("phone", data.getString("phone"));
				localHashMap
						.put("access_token", data.getString("access_token"));
				localHashMap.put("default_rounded_amount",
						data.getString("default_rounded_amount"));
				localHashMap.put("default_charity",
						data.getString("default_charity"));
				localHashMap.put("is_purchase_notification",
						data.getString("is_purchase_notification"));
				localHashMap.put("default_donation_to_purchase",
						data.getString("default_donation_to_purchase"));
				localHashMap.put("monthly_limit",
						data.getString("monthly_limit"));
				localHashMap.put("is_donate_to_my_fav",
						data.getString("is_donate_to_my_fav"));
				localHashMap.put("is_updates_from_us",
						data.getString("is_updates_from_us"));
				localHashMap.put("cron_date", data.getString("cron_date"));
				localHashMap.put("created", data.getString("created"));
				localHashMap.put("On_Off_Buckup", data.getString("On_Off_Buckup"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * my donations
	 */

	public ArrayList<HashMap<String, String>> my_donations(
			ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "myDonations.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray MessageWhatHappen = localJSONObject
						.getJSONArray("MessageWhatHappen");
				for (int i = 0; i < MessageWhatHappen.length(); i++) {
					HashMap localhashMap = new HashMap();
					localhashMap.put("username", MessageWhatHappen
							.getJSONObject(i).getString("username"));
					localhashMap.put("id", MessageWhatHappen.getJSONObject(i)
							.getString("id"));
					localhashMap.put("charity_name", MessageWhatHappen
							.getJSONObject(i).getString("charity_name"));
					localhashMap
							.put("total_donated_amount",
									MessageWhatHappen.getJSONObject(i)
											.getString("total_donated_amount"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * Donate charity
	 */
	public HashMap donateCharity(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "insertdonate_amount.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Donate chariyt t o all
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap donateCharityToAll(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url
							+ "insertdonate_amountALL.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap make_unfav(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "make_un_favourite.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap settingNotification(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(
							url + "setting_notification.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * make donation
	 */

	public ArrayList<HashMap<String, String>> Make_donation_list(
			ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "charity_list.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray MessageWhatHappen = localJSONObject
						.getJSONArray("MessageWhatHappen");
				for (int i = 0; i < MessageWhatHappen.length(); i++) {
					HashMap localhashMap = new HashMap();
					localhashMap.put("id", MessageWhatHappen.getJSONObject(i)
							.getString("id"));
					localhashMap.put("charity_name", MessageWhatHappen
							.getJSONObject(i).getString("charity_name"));
					localhashMap.put("address", MessageWhatHappen
							.getJSONObject(i).getString("address"));
					localhashMap.put("phone", MessageWhatHappen
							.getJSONObject(i).getString("phone"));
					localhashMap.put("description", MessageWhatHappen
							.getJSONObject(i).getString("description"));
					localhashMap.put("website", MessageWhatHappen
							.getJSONObject(i).getString("website"));
					localhashMap.put("EIN_number", MessageWhatHappen
							.getJSONObject(i).getString("EIN_number"));
					localhashMap.put("business_id", MessageWhatHappen
							.getJSONObject(i).getString("business_id"));
					localhashMap.put("created", MessageWhatHappen
							.getJSONObject(i).getString("created"));
					localhashMap.put("business_name", MessageWhatHappen
							.getJSONObject(i).getString("business_name"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}
	
	
	public HashMap hide(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "hide_item.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}
	
	public HashMap checkPromoCodeValidation(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "check_promocode.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
				localHashMap.put("promocode_id",
						localJSONObject.getString("promocode_id"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}
	
	public HashMap CheckUsernameEmail(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "CheckForUserName.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
				

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				localHashMap.put(Constants.MESSAGE_WHAT_HAPPEN_KEY,
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}
	
	/**
	 * get default amount
	 * @param localArrayList
	 * @return
	 */
	
	public HashMap getDefaultAmount(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "FetchdefaultAmount.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				localHashMap.put(Constants.RESPONSE_KEY, "true");
				localHashMap.put("default_donation_to_purchase",
						localJSONObject.getString("default_donation_to_purchase"));

			} else {
				localHashMap.put(Constants.RESPONSE_KEY, "false");
				
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}
	
	public ArrayList<HashMap<String, String>> MoreBankDetails(
			ArrayList localArrayList) {
		ArrayList localArrayList1 = new ArrayList();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "AlllBankLists.php", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {
				
				JSONObject MessageWhatHappen = localJSONObject
						.getJSONObject("MessageWhatHappen");

				JSONArray results = MessageWhatHappen
						.getJSONArray("results");
				for (int i = 0; i < results.length(); i++) {
					HashMap localhashMap = new HashMap();
					
					localhashMap.put("name", results.getJSONObject(i)
							.getString("name"));
					localhashMap.put("type", results.getJSONObject(i)
							.getString("type"));

					try {
						localhashMap.put("child_name", results.getJSONObject(i)
								.getString("child_name"));
						} 
					catch(Exception e){
							e.printStackTrace();
							localArrayList1.add(localhashMap);
						}

					

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

}
