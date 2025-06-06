package com.soumen.SpringToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OtpUtils {
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    public LocalDateTime getExpiryDateTime() {
        return LocalDateTime.now().plusMinutes(10);
    }

}
