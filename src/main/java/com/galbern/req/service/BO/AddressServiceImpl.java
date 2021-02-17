package com.galbern.req.service.BO;

import com.galbern.req.dao.AddressDao;
import com.galbern.req.jpa.entities.Address;
import com.galbern.req.service.AddressService;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.HibernateQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    public static Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    public AddressDao addressDao;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;





    @Override
    public Address addAddress(Address a) {
        return addressDao.save( a);
    }

    @Override
    public Set<Address> getAddresses(List<Long> addressIds) {
        LOGGER.info(" fetching addresses: {}", addressIds);
        Set<Address> result = new HashSet<>();

        if(null == addressIds){
            for(Address a : addressDao.findAll()){
                result.add(a);
            }
            return  result;
        }

//        try {
//            sf = new Configuration().configure().buildSessionFactory();
//        } catch (Throwable ex) {
//            LOGGER.error("Failed to create sessionFactory object." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//         ThreadLocal session = new ThreadLocal();
//          session = (Session)session;
//
//          HibernateEntityManager entityManager;
//          entityManager.createQuery("", Address.class).setParameterList("", )
//
//        session = session == null ?  sf.openSession() : session ;
//        Transaction tx = session.beginTransaction();

        Session session;

        TypedQuery<Address> query = entityManager.createQuery("select a from Address a where a.id in (?1)", Address.class)
                .setParameter(1, addressIds);
        result = query.getResultStream().collect(Collectors.toSet());

        return result;
    }

    @Override
    public Address findAddressById(Long id) {
        return addressDao.findById(id).orElse(null); // AddressNotFoundException::new
    }


}
