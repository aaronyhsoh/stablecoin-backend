package com.stablecoinbackend.demo.entities;

import com.stablecoinbackend.demo.enums.Enums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class RedemptionStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    private String pool;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column
    private Enums.ApprovalStatus approvalStatus;

    @Column
    private String burnId;

}
