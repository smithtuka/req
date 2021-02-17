package com.galbern.req.jpa.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "UserCredentials")
@Table(name = "USERCREDENTIALS")
@Data
public class UserCredentials {

    @Id
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @OneToOne
    @JoinColumn
    private User user;
}
