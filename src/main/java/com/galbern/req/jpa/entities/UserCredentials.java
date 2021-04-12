package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity(name = "UserCredentials")
@Table(name = "USERCREDENTIALS")
@Getter
@ToString
@EqualsAndHashCode
//@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class UserCredentials {

    @Id
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
//    @OneToOne
//    @JoinColumn
//    private User user;

    @JsonIgnore
    @Transient
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String getUsername() {
        return username;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
