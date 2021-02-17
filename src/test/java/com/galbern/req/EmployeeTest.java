package com.galbern.req;


//import com.galbern.req.sandbox.Employee;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration() // classes = { PersistenceJPAConfig.class }
//@Transactional
//@TransactionConfiguration
public class EmployeeTest {

@BeforeClass
public void init(){

}

    @Test
    public void whenNonPublicField_thenReturnEmployee() {
//        Employee employee = new Employee();
//        ReflectionTestUtils.setField(employee, "id", 1L);
//        employee.setName("Smith");
//        Assertions.assertEquals(1L, employee.getId());
    }

}
