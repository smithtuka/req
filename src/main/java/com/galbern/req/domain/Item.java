package com.galbern.req.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name="ITEMS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  Long id;
    private String description;
    private BigDecimal quantity;
    private BigDecimal price;


    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Requisition requisition;

//    M-1 handling


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(description, item.description) &&
                Objects.equals(quantity, item.quantity) &&
                Objects.equals(price, item.price) &&
                Objects.equals(supplier, item.supplier) &&
                Objects.equals(requisition, item.requisition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, quantity, price, supplier, requisition);
    }
}
