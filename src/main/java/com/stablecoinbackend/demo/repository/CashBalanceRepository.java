
package com.stablecoinbackend.demo.repository;

import com.stablecoinbackend.demo.entities.CashBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CashBalanceRepository extends JpaRepository<CashBalance, Integer> {
    public List<CashBalance> findByUserId(String userId);
}
