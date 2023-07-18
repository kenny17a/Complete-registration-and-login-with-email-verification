package com.login.logout.complete.registration.with.email.sender.appuser;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private String role;
    private boolean enabled = false;



}
