package com.ost.back_testing_back.controller;

import com.ost.back_testing_back.dto.BacktestDto;
import com.ost.back_testing_back.entity.BacktestRes;
import com.ost.back_testing_back.exception.NoDataFoundException;
import com.ost.back_testing_back.service.BacktestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backtest")
public class BacktestController {

    private final BacktestService backtestService;

    @PostMapping
    public ResponseEntity<?> createBacktest(
            @RequestBody BacktestDto.CreateBacktestRequest request
    ) {
        // ticker, startDate, endDate 가 없으면 400 에러
        if (request.ticker() == null || request.ticker().isEmpty()
            || request.startDate() == null || request.endDate() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ticker, startDate, and endDate are required"));
        }
        try {
            Double totalReturn = backtestService.processBacktest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Backtest request created successfully",
                "totalReturn", totalReturn
            ));
        } catch (NoDataFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
