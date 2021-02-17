package com.galbern.req.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.galbern.req.jpa.entities.Item;
import com.galbern.req.jpa.entities.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.util.List;

//@EqualsAndHashCode(callSuper=false)
//@Data
//@NoArgsConstructor
////@Entity
//@Table(name="SUPPLIERS")
//@DynamicUpdate
//@DynamicInsert
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(mappedBy="supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // edit to have clean table
//    private List<Item> items;


    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }


}
