package com.galbern.req.sandbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

//@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Override
    public Customer addCustomer(Customer c){
      return customerDao.save(c);
    }

    @Override

    public List<Customer> getCustomers(){
//        TypedQuery<Customer> query = entityManager.createQuery(
//                "SELECT c FROM Customer c ", Customer.class); //LEFT JOIN c.address
//        return query.getResultList();
        return ((List<Customer>)customerDao.findAll() );
    }

    @Override
    public Customer findCustomerById(Long id) {

        EntityGraph<Customer> entityGraph = entityManager.createEntityGraph(Customer.class);
        entityGraph.addAttributeNodes("address", "project");
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c  where c.id = :id ", Customer.class)
                .setParameter("id", id)
                .setHint("javax.persistence.fetchgraph", entityGraph);
        var customer = Optional.ofNullable(query.getSingleResult());

        return customer.orElse(null);
    }

    @Override
    public Customer editCustomer(Long id) {
        return null;
    }

    @Override
    public Customer deleteCustomer(Long id) {
        return null;
    }



}
