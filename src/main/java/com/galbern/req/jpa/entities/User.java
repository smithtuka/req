package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
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
@Table(name ="USERS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Person implements Serializable { // class of composite Ids must implement serializable public static final Long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String email;
    @Enumerated(EnumType.STRING) // otherwise wd automatically be mapped to value 0--- (ORDINAL)
    private Role role;





}
