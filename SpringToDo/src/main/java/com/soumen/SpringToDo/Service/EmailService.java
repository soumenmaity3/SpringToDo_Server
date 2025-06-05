package com.soumen.SpringToDo.Service;

import com.soumen.SpringToDo.OtpUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String otp=OtpUtils.generateOtp();
        helper.setTo(to);
        helper.setSubject("Your OTP Code");

        String htmlMsg = "<p>Hello,</p>" +"\n" +
                "              <p>Your <strong>One-Time Password (OTP)</strong> for verifying your account is:</p>" +"\n" +
                "              <h2 style='color:#4CAF50;'>\uD83D\uDD10 " + otp + "</h2>" +"\n" +
                "              <p>Please use this code within the next 5 minutes. Do not share it with anyone.</p>" +"\n" +
                "              <p>If you did not request this, please ignore this email.</p>" +"\n" +
                "              <br><p>Thanks,<br>The ToDo App Team</p>";

        helper.setText(htmlMsg, true); // Enable HTML
        helper.setFrom("ToDo App For Reset The Password<sm8939912@gmail.com>");

        mailSender.send(message);
    }
}