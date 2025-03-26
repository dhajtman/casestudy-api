package com.casestudy.api.repository;

import com.casestudy.api.model.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderJPARepository extends JpaRepository<Ordered, Long> {
    List<Ordered> findByNotifiedFalse();

    List<Ordered> findByProduct(String product);

    @Query("SELECT o FROM Ordered o WHERE o.quantity >= :quantity")
    List<Ordered> findByMinQuantity(@Param("quantity") int quantity);

    @Query("SELECT o FROM Ordered o WHERE o.quantity < :quantity")
    List<Ordered> findByMaxQuantity(@Param("quantity") int quantity);
}
