package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "STAGES")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal budget;
    @Transient
    private BigDecimal balance;
    private Boolean isActive;
    private Date startDate;
    private Date plannedEndDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore
//    @Fetch(FetchMode.SELECT)
    private Project project;

    @OneToMany(mappedBy = "stage", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Requisition> requisitions;

    public void addRequisition(Requisition requisition) {
        requisitions.add(requisition);
        requisition.setStage(this);
    }

    public void removeRequisition(Requisition requisition) {
        requisitions.remove(requisition);
        requisition.setStage(null);
    }

    public Stage(String name, BigDecimal budget, BigDecimal balance, Boolean isActive, Date startDate, Date plannedEndDate, Project project) {
        this.name = name;
        this.budget = budget;
        this.balance = balance;
        this.isActive = isActive;
        this.startDate = startDate;
        this.plannedEndDate = plannedEndDate;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Requisition> getRequisitions() {
        return requisitions;
    }

    public void setRequisitions(List<Requisition> requisitions) {
        for(Requisition requisition: requisitions)requisition.setStage(this);
        this.requisitions = requisitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stage)) return false;
        Stage stage = (Stage) o;
        return id.equals(stage.id) &&
                name.equals(stage.name) &&
                budget.equals(stage.budget) &&
                balance.equals(stage.balance) &&
                isActive.equals(stage.isActive) &&
                startDate.equals(stage.startDate) &&
                plannedEndDate.equals(stage.plannedEndDate) &&
                project.equals(stage.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, budget, balance, isActive, startDate, plannedEndDate, project);
    }
}