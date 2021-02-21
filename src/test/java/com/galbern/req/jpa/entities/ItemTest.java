package com.galbern.req.jpa.entities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ItemTest {

    @InjectMocks
    private Item item;

    @Before
    public void setUp(){
        item = new Item("Cement", BigDecimal.TEN,BigDecimal.valueOf(3),new Requisition());
    }

    @Test
    public void test(){
        assertEquals("Cement", item.getDescription());
        assertNotNull(item.getRequisition());
    }

}