package com.GoDo.user_api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender mailSender;

    public void sendTodoAssignedEmail(String to, String todoTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Yeni ToDo Ataması");
        message.setText("Size yeni bir görev atandı: " + todoTitle);
        mailSender.send(message);
    }
}
