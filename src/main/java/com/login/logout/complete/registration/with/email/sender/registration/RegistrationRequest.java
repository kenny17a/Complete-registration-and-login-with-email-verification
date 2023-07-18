package com.login.logout.complete.registration.with.email.sender.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String role
) {
}
