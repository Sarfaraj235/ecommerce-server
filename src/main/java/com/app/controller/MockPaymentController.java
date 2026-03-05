package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.dto.MockPaymentRecordRequest;
import com.app.exceptions.UserException;
import com.app.pojos.MockPaymentTransaction;
import com.app.service.MockPaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments/mock")
@RequiredArgsConstructor
public class MockPaymentController {
	
	@Autowired
    private  MockPaymentService mockPaymentService;
    @PostMapping("/record")
    public ResponseEntity<MockPaymentTransaction> record(
            @RequestHeader("Authorization") String jwt,
            @RequestBody MockPaymentRecordRequest request
    ) throws UserException {
        return new ResponseEntity<>(mockPaymentService.record(jwt, request), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<MockPaymentTransaction>> myPayments(
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        return ResponseEntity.ok(mockPaymentService.myPayments(jwt));
    }

}
