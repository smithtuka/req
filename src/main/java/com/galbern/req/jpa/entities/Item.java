package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


@NoArgsConstructor
@Entity
@Table(name="ITEMS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal quantity;
    private BigDecimal price;
    @OneToOne
    @JoinColumn(name = "supplier_id")
    private User supplier;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="requisition_id")
    @Fetch(FetchMode.JOIN)
    private Requisition requisition;

    public Item(String description, BigDecimal quantity, BigDecimal price, Requisition requisition) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.requisition = requisition;
    }

    public Item(Long id, String description, BigDecimal quantity, BigDecimal price, Requisition requisition) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.requisition = requisition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public void setRequisition(Requisition requisition) {
        this.requisition = requisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id.equals(item.id) &&
                description.equals(item.description) &&
                quantity.equals(item.quantity) &&
                price.equals(item.price) &&
                requisition.equals(item.requisition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, quantity, price, requisition);
    }
}
