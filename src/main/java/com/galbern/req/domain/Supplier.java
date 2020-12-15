package com.galbern.req.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.util.List;

//@EqualsAndHashCode(callSuper=false)
@Data
@NoArgsConstructor
@Entity
@Table(name="SUPPLIERS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // edit to have clean table
    private List<Item> items;


    public void addItem(Item item){
        items.add(item);
        item.setSupplier(this);
    }

    public void removeItem(Item item){
        items.remove(item);
        item.setSupplier(null);

    }


}
