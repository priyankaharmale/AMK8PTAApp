package com.hnweb.amk8ptaapp.contants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Priyanka H on 13/06/2018.
 */
public class AppConstant {


    /*============================================Register==================================================*/
    public static final String KEY_ID = "rid";
    public static final String KEY_EMAIL = "remail";
    public static final String KEY_FIRSTNAME = "rfname";
    public static final String KEY_LASTNAME = "rlname";
    public static final String KEY_MOBILENO = "rphone";
    public static final String KEY_PASSWORD = "rpassword";



    public static final String BASE_URL = "http://104.37.185.20/~tech599/tech599.com/johnaks/AMK8/api/";

    /*******************************User***************************************************************************************************/
    /*=================================================Register User=========================================================*/
    public static final String API_REGISTER_USER = BASE_URL + "register.php";
    /*=================================================Login User=========================================================*/
    public static final String API_LOGIN_USER = BASE_URL + "login.php";
    /*=================================================Forgot Password User=========================================================*/
    public static final String API_FORGOTPWD_USER = BASE_URL + "forgot-password.php";
    /*=================================================Get Messages List=========================================================*/
    public static final String API_GET_MESSAGES = BASE_URL + "get_msg.php";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


}
