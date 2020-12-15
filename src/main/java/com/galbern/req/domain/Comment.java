package com.galbern.req.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

//@MappedSuperclass
//@Inheritance()
@Entity(name="COMMENTS")
@Table(name="COMMENTS")
@DynamicUpdate
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Comment { // notifications in front-end
    @Id
    @GeneratedValue
    private Long id;
    private String narration; // add writer functionality -- look at events with kafka

    //do sef-reference
//    @Embedded
//    Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Requisition requisition;




}
