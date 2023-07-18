package com.login.logout.complete.registration.with.email.sender.event.listener;

import com.login.logout.complete.registration.with.email.sender.appuser.Users;
import com.login.logout.complete.registration.with.email.sender.appuser.UserService;
import com.login.logout.complete.registration.with.email.sender.event.RegistrationCompleteEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private Users theUsers ;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Get the newly registered user
        theUsers = event.getUsers();
        //create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //save the verification token
        userService.saveUserVerificationToken(theUsers, verificationToken);
        //build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/register/verifyEmail?token="+verificationToken;
        //send the email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration: {}", url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Complete User Registration Portal Service";
        String mailContent = "<p> Hi, "+ theUsers.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("ibrahimkenny17@gmail.com", senderName);
        messageHelper.setTo(theUsers.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);

    }
}
