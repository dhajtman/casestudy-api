package com.casestudy.api.repository;

import com.casestudy.api.model.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Ordered, Long> {
}
