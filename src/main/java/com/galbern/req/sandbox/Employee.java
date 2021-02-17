package com.galbern.req.sandbox;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


//@Entity
public class Employee {

    //    private field to be tested -- with not setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public Employee() {
    }

//    private method to be tested
    private String employeeToString(){
        return "id: " + getId() + "; name: " + getName();
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
