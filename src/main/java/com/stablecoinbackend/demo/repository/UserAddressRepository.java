package com.stablecoinbackend.demo.repository;

import com.stablecoinbackend.demo.entities.UserAddress;
import com.stablecoinbackend.demo.entities.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository  extends JpaRepository<UserAddress, Integer> {
    public UserAddress findOneByUserId(String userId);
}
