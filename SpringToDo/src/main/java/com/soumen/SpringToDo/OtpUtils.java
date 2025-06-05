package com.soumen.SpringToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OtpUtils {
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public LocalDateTime getExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(10);
    }
}

