package com.login.logout.complete.registration.with.email.sender.registration;

import com.login.logout.complete.registration.with.email.sender.appuser.Users;
import com.login.logout.complete.registration.with.email.sender.appuser.UserService;
import com.login.logout.complete.registration.with.email.sender.event.RegistrationCompleteEvent;
import com.login.logout.complete.registration.with.email.sender.registration.token.VerificationToken;
import com.login.logout.complete.registration.with.email.sender.registration.token.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository verificationTokenRepository;
    @PostMapping
    public String register (@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        Users users = userService.registerUser(registrationRequest);
        //publish registration event
        publisher.publishEvent(new RegistrationCompleteEvent(users, applicationUrl(request)));
        return "Success! Please, check your email to complete registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = verificationTokenRepository.findByToken(token);
        if (theToken.getUsers().isEnabled()){
            return "This account has already been verified, pls login";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully, now you can login to your account";
        }
        return "Invalid verification token";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName() + ":"+request.getServerPort()+request.getContextPath();
    }
}
