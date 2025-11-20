package com.galbern.req.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import java.io.Serializable;


@MappedSuperclass // allows children to take the attributes
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // define Descriminator ppties and well type
@EqualsAndHashCode(callSuper=false)
@Data
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable {



    private String firstName;
    private String lastName;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;


}
