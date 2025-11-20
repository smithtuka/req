package com.galbern.req.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.Temporal;

import jakarta.persistence.*;
import java.util.Date;

@Entity(name="COMMENTS")
@Table(name="COMMENTS")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment { // notifications in front-end
    @Id
    @GeneratedValue
    private Long id;
    private String narration;
    @Basic // Temporal
    private Date commentDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Requisition requisition;

    @OneToOne
    @JoinColumn
    private User commenter;






}
