package com.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.pojos.MockPaymentTransaction;

public interface MockPaymentTransactionRepository extends JpaRepository<MockPaymentTransaction, Long> {
    List<MockPaymentTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}
