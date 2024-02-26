package com.stablecoinbackend.demo.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stablecoinbackend.demo.dto.request.IssuanceApproveRequestDto;
import com.stablecoinbackend.demo.dto.request.IssuanceRejectRequestDto;
import com.stablecoinbackend.demo.dto.request.IssuanceSubmitRequestDto;
import com.stablecoinbackend.demo.dto.response.CashBalanceResponseDto;
import com.stablecoinbackend.demo.dto.response.IssuanceApproveResponseDto;
import com.stablecoinbackend.demo.dto.response.IssuanceRejectResponseDto;
import com.stablecoinbackend.demo.dto.response.WalletBalanceResponseDto;
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

    @GetMapping("/query/issuance/pending")
    public ResponseEntity getPendingIssuanceRequests() {
        List<IssuanceStatus> pendingList = fireflyService.getAllPendingIssuanceRequests();
        return ResponseEntity.ok(pendingList);
    }

    @PostMapping("/submit/issuance/approve")
    public ResponseEntity submitIssuanceApprove(@RequestBody IssuanceApproveRequestDto dto) throws IOException {
        IssuanceApproveResponseDto responseDto = fireflyService.issueToken(dto);
        if (responseDto.isSuccess()) {
            Gson gson = new Gson();
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/submit/issuance/reject")
    public ResponseEntity submitIssuanceReject(@RequestBody IssuanceRejectRequestDto dto) {
        IssuanceRejectResponseDto responseDto = fireflyService.rejectToken(dto);
        if (responseDto.isSuccess()) {
            Gson gson = new Gson();
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
}
