package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;


@Entity(name = "UserCredentials")
@Table(name = "USERCREDENTIALS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username") })
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class UserCredentials {

    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Role role;


}
