package com.example.functions;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {

	public static String RESPONSE_KEY = "ResponseCode";
	public static String MESSAGE_WHAT_HAPPEN_KEY = "MessageWhatHappen";
	public static String USER_ID_KEY = "UserId";
	public static String ID_KEY = "id";
	public static String USER_TYPE_KEY = "user_type";
	public static String EMAILID_KEY = "emailid";
	public static String USERNAME_KEY = "username";
	public static String PASSWORD_KEY = "password";
	public static String FNAME_KEY = "fname";
	public static String LNAME_KEY = "lname";
	public static String ADDRESS_KEY = "address";
	public static String ZIP_KEY = "zip";
	public static String PHONE_KEY = "phone";
	public static String CREATED_KEY = "created";
	public static String ERROR_MSG = "Something went wrong while processing the request.Please try again after some time.";
	public static String NO_INTERNET = "No internet connection.";
	public static String ACCESS_TOKEN_KEY = "access_token";

	public static String REGISTRATIO_ID;

	public static ArrayList<HashMap<String, String>> item_list = new ArrayList<HashMap<String, String>>();

	/**
	 * Login
	 */

	public static String USER_ID;
	public static String BANK_TYPE;
	public static String ACESS_TOKEN;
	public static String NAME;
	public static String FIRSTNAME;
	public static String LASTNAME;
	public static String EMAILID;

	/**
	 * SiGNUP 1
	 */

	public static String EMAIL;
	public static String PASSWORD;
	public static String ZIP_CODE;
	public static String USERNAME;
	
	public static String FIRSTNAME_SIGNUP;
	public static String LASTNAME_SIGNUP;
	
	public static String PROMOCODE;
	public static String PROMOCODE_ID = "";

	/**
	 * home item list
	 */

	public static String ITEM_ID_KEY = "id";
	public static String ITEM_CODE_KEY = "item_code";
	public static String ITEM_NAME_KEY = "item_name";
	public static String ITEM_PURCHASE_AMOUNT_KEY = "purchase_amount";
	public static String CHARITY_PREFER_BYADMIN_KEY = "charity_prefer_byadmin";
	public static String ROUNDED_AMOUNT_BYADMIN = "rounded_amount_byadmin";
	public static boolean iscomingfromSetting;

	public static String SPINNER_TEXT;
	public static String NAME_ON_CARD;
	public static String CARD_NO;
	public static String EXPIRATION_DATE;
	public static String CVV_NUMBER;

	/**
	 * Account
	 */

	public static String email;
	public static String userName;
	public static String password;
	public static String new_password;
	public static String zipCode;
	
	public static String LASTNAME_MYACCOUNT;
	public static String FIRSTNAME_MYACCOUNT;

	public static String card_type;
	public static String name_on_card;
	public static String card_no;
	public static String cvv_no;
	public static String GLOBAL_CARD_NO ;
	public static String GLOBAL_CVV_NO ;
	public static String expiry_date;
	
	public static double TOTAL_AMOUNT = 0;

}
