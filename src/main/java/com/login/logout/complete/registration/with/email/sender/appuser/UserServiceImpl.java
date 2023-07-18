package com.login.logout.complete.registration.with.email.sender.appuser;

import com.login.logout.complete.registration.with.email.sender.exception.UserAlreadyExistsException;
import com.login.logout.complete.registration.with.email.sender.registration.RegistrationRequest;
import com.login.logout.complete.registration.with.email.sender.registration.token.VerificationToken;
import com.login.logout.complete.registration.with.email.sender.registration.token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users registerUser(RegistrationRequest request) {
        Optional<Users> user = userRepository.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistsException(
                    "Users with email" + request.email() + "already exists");
        }
        var newUser = new Users();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());

        return userRepository.save(newUser);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(Users theUsers, String token) {
        var verificationToken = new VerificationToken(token, theUsers);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = verificationTokenRepository.findByToken(theToken);
        if (token == null){
            return  "Invalid verification token";
        }
        Users users = token.getUsers();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(token);
            return "Token Already expired";
        }
        users.setEnabled(true);
        userRepository.save(users);
        return "valid";
    }
}
