package com.galbern.req.jpa.entities;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ItemTest {

    @InjectMocks
    private Item item;


    @Test
    public void test() {
        item = new Item("Cement", BigDecimal.TEN, BigDecimal.valueOf(3), new Requisition());
        assertEquals("Cement", item.getDescription());
        assertNotNull(item.getRequisition());
    }

}