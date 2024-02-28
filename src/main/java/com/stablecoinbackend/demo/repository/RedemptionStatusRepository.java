package com.stablecoinbackend.demo.repository;

import com.stablecoinbackend.demo.entities.RedemptionStatus;
import com.stablecoinbackend.demo.enums.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedemptionStatusRepository extends JpaRepository<RedemptionStatus, Integer> {
    public List<RedemptionStatus> findByApprovalStatus(Enums.ApprovalStatus approvalStatus);

    public List<RedemptionStatus> findByUserId(String userId);

    public RedemptionStatus findByIdAndApprovalStatus(int id, Enums.ApprovalStatus approvalStatus);


}
