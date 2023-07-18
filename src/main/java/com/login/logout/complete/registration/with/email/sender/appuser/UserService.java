package com.login.logout.complete.registration.with.email.sender.appuser;

import com.login.logout.complete.registration.with.email.sender.registration.RegistrationRequest;
import com.login.logout.complete.registration.with.email.sender.registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<Users> getUsers();
    Users registerUser(RegistrationRequest request);
    Optional<Users> findByEmail(String email);

    void saveUserVerificationToken(Users theUser, String verificationToken);

    String validateToken(String theToken);
}
