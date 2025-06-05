package com.soumen.SpringToDo;

import java.util.Random;

public class OtpUtils {
    static String otpReturn="";
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpReturn=String.valueOf(otp);
        return otpReturn;
    }
    public String otp(){
        return otpReturn;
    }
}
