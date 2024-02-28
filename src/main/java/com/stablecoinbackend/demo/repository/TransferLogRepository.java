package com.stablecoinbackend.demo.repository;

import com.stablecoinbackend.demo.entities.TransferLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferLogRepository  extends JpaRepository<TransferLog, Integer> {

}
