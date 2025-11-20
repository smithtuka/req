package com.galbern.req.jpa.entities;


public enum Role {
    USER,
    ADMIN,
    ACCOUNTANT,
    ENGINEER,
    CONSULTANT,
    CUSTOMER,
    SUPPLIER,
    SUPERVISOR,
    DIRECTOR;

    @Override
    public String toString() {
        return super.toString();
    }
}
