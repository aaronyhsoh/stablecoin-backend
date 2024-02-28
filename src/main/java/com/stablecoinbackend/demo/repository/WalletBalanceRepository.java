
package com.stablecoinbackend.demo.repository;

import com.stablecoinbackend.demo.entities.IssuanceStatus;
import com.stablecoinbackend.demo.entities.WalletBalance;
import com.stablecoinbackend.demo.enums.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Integer> {
    public WalletBalance findOneByUserIdAndPool(String userId, String pool);

    public List<WalletBalance> findByUserId(String userId);

    public WalletBalance findByUserIdAndPool(String userId, String pool);
}
