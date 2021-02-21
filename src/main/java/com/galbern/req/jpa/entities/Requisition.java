package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REQUISITIONS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "required_date")
    private Date requiredDate; // = Calendar.getInstance().getTime();

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stage stage;

    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @OrderBy(value="price DESC")
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.RECEIVED;

    @OneToMany(mappedBy = "requisition", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> requisitionComments;

    public void addItem(Item item) {
        item.setRequisition(this);
        items.add(item);
        item.setRequisition(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setRequisition(null); // itemDao.deleteById(item.getId)
    }

    public Requisition(Date requiredDate, User requester, Stage stage, List<Item> items, ApprovalStatus approvalStatus, List<Comment> requisitionComments) {
        this.requiredDate = requiredDate;
        this.requester = requester;
        this.stage = stage;
        this.items = items;
        this.approvalStatus = approvalStatus;
        this.requisitionComments = requisitionComments;
    }

    public Requisition(Long id, Date requiredDate, User requester, Stage stage, List<Item> items, ApprovalStatus approvalStatus, List<Comment> requisitionComments) {
        this.id = id;
        this.requiredDate = requiredDate;
        this.requester = requester;
        this.stage = stage;
        this.items = items;
        this.approvalStatus = approvalStatus;
        this.requisitionComments = requisitionComments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        for (Item i : items) {
            i.setRequisition(this);
        }
        this.items = items;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<Comment> getRequisitionComments() {
        return requisitionComments;
    }

    public void setRequisitionComments(List<Comment> requisitionComments) {
        for(Comment comment : requisitionComments) comment.setRequisition(this);
        this.requisitionComments = requisitionComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Requisition)) return false;
        Requisition that = (Requisition) o;
        return id.equals(that.id) &&
                requiredDate.equals(that.requiredDate) &&
                requester.equals(that.requester) &&
                stage.equals(that.stage) &&
                items.equals(that.items) &&
                approvalStatus == that.approvalStatus &&
                Objects.equals(requisitionComments, that.requisitionComments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requiredDate, requester, stage, items, approvalStatus, requisitionComments);
    }
}
