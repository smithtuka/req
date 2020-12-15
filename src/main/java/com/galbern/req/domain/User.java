package com.galbern.req.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper=false)
@Data
@NoArgsConstructor
@Entity
@Table(name ="UZERS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Person implements Serializable { // class of composite Ids must implement serializable public static final Long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // = super.getId();

    @Column(name="UZERNAME")
    private String username;
    @Column(name="PAZZWORD")
    private String password;
    private String email;
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING) // otherwise wd automatically be mapped to value 0--- (ORDINAL)
    private Role role;



}
