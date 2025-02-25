package com.casestudy.api.repository;

import com.casestudy.api.model.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Ordered, Long> {
    List<Ordered> findByNotifiedFalse();
}
