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
@Table(name="addresses")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zip;
    private String street;
    private String district;
    private String State;
}