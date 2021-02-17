package com.galbern.req.sandbox;

import com.fasterxml.jackson.annotation.*;
import com.galbern.req.jpa.entities.Person;
import com.galbern.req.jpa.entities.Project;
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
//@Entity
@Table(name="CUSTOMERS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
//@AssociationOverride(
//        name = "id", joinColumns = @JoinColumn(name = "id"))
public class Customer extends Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Project project;


}
