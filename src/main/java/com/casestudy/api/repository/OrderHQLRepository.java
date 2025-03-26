package com.casestudy.api.repository;

import com.casestudy.api.model.Ordered;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderHQLRepository {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<Ordered> getOrders() {
        Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();

        Query<Ordered> query = session.createQuery("from Ordered order by product", Ordered.class);
        List<Ordered> orders = query.getResultList();

        return orders;
    }

    public Ordered getOrderById(Long orderId) {
        Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();

        Query<Ordered> query = session.createQuery("from Ordered where id=:orderId order by product", Ordered.class);
        query.setParameter("orderId", orderId);
        Ordered ordered = query.getSingleResult();

        return ordered;
    }
}
