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

    public void sendOtp(String to,String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your OTP Code");

        String htmlMsg = "<p>Hello,</p>" +
                "<p>Your <strong>One-Time Password (OTP)</strong> is:</p>" +
                "<h2 style='color:#4CAF50;'>🔐 " + otp + "</h2>" +
                "<p>Please use this code within the next 5 minutes. Do not share it with anyone.</p>" +
                "<p>If you did not request this, ignore this email.</p>" +
                "<br><p>Thanks,<br>The ToDo App Team</p>";

        helper.setText(htmlMsg, true);
        System.out.println(otp);
        helper.setFrom("ToDo App For One-Time Password (OTP) <todo.sm.89@gmail.com>");

        mailSender.send(message);
    }

}