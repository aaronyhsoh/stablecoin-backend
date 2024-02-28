package com.stablecoinbackend.demo.controllers;

import com.stablecoinbackend.demo.dto.request.*;
import com.stablecoinbackend.demo.dto.response.*;
import com.stablecoinbackend.demo.entities.IssuanceStatus;
import com.stablecoinbackend.demo.services.FireflyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
public class FireflyController {
    @Autowired
    FireflyService fireflyService;

    @PostMapping("/submit/issuance/request")
    public ResponseEntity issuanceRequest(@RequestBody IssuanceSubmitRequestDto dto) {
        fireflyService.submitIssuanceRequest(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/query/issuance/pending/{userId}")
    public ResponseEntity getPendingIssuanceRequests(@PathVariable String userId) {
        List<IssuanceStatus> pendingList = fireflyService.getAllIssuanceRequestsByUserId(userId);
        return ResponseEntity.ok(pendingList);
    }

    @PostMapping("/submit/issuance/approve")
    public ResponseEntity submitIssuanceApprove(@RequestBody IssuanceApproveRequestDto dto) throws IOException {
        IssuanceApproveResponseDto responseDto = fireflyService.issueToken(dto);
        if (responseDto.isSuccess()) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/submit/issuance/reject")
    public ResponseEntity submitIssuanceReject(@RequestBody IssuanceRejectRequestDto dto) {
        IssuanceRejectResponseDto responseDto = fireflyService.rejectToken(dto);
        if (responseDto.isSuccess()) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/query/wallet/balance/{userId}")
    public ResponseEntity queryWalletBalance(@PathVariable String userId) {
        WalletBalanceResponseDto responseDto = fireflyService.queryWalletBalance(userId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/query/cash/balance/{userId}")
    public ResponseEntity queryCashBalance(@PathVariable String userId) {
        CashBalanceResponseDto responseDto = fireflyService.queryCashBalance(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/submit/redemption/request")
    public ResponseEntity redemptionRequest(@RequestBody RedemptionSubmitRequestDto dto) {
        RedemptionSubmitResponseDto responseDto = fireflyService.submitRedemptionRequest(dto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/query/issuance/admin")
    public ResponseEntity queryAllIssuance() {
        List<IssuanceStatus> pendingList = fireflyService.getAllIssuanceRequests();
        return ResponseEntity.ok(pendingList);
    }

    @PostMapping("/submit/transfer")
    public ResponseEntity transferToken(@RequestBody TransferTokenRequestDto dto) {
        fireflyService.transferToken(dto);
        return ResponseEntity.ok().build();
    }
}
