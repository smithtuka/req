package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "REQUISITIONS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate requestDate = LocalDate.now();

    @OneToOne
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY
    private Stage stage;

    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items; // LAZY

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

    public Requisition(Long id, LocalDate requestDate, User requester, Stage stage, List<Item> items, ApprovalStatus approvalStatus, List<Comment> requisitionComments) {
        this.id = id;
        this.requestDate = requestDate;
        this.requester = requester;
        this.stage = stage;
        this.items = items;
        this.approvalStatus = approvalStatus;
        this.requisitionComments = requisitionComments;
    }
}
