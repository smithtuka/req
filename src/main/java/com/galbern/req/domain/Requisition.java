package com.galbern.req.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@Entity(name = "REQUISITIONS")
@Table(name = "REQUISITIONS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    private User submittedBy;
//
//    @OneToMany
//    private List<User> approvedBy;

    // request data
    // issue date

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> requisitionComments;

        public void addItem(Item item) {
        items.add(item);
        item.setRequisition(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setRequisition(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requisition that = (Requisition) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(stage, that.stage) &&
                Objects.equals(items, that.items) &&
                approvalStatus == that.approvalStatus &&
                Objects.equals(requisitionComments, that.requisitionComments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stage, items, approvalStatus, requisitionComments);
    }
}
