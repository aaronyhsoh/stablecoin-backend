package com.stablecoinbackend.demo.repository;

import com.stablecoinbackend.demo.entities.IssuanceStatus;
import com.stablecoinbackend.demo.enums.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IssuanceStatusRepository extends JpaRepository<IssuanceStatus, Integer> {
    public List<IssuanceStatus> findByApprovalStatus(Enums.ApprovalStatus approvalStatus);

    public IssuanceStatus findByIdAndApprovalStatus(int id, Enums.ApprovalStatus approvalStatus);


}
