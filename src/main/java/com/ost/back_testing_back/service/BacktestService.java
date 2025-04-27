package com.ost.back_testing_back.service;

import com.ost.back_testing_back.dto.BacktestDto;
import com.ost.back_testing_back.entity.BacktestReq;
import com.ost.back_testing_back.entity.BacktestRes;
import com.ost.back_testing_back.repository.BacktestReqRepository;
import com.ost.back_testing_back.repository.BacktestResRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BacktestService {

    private final BacktestReqRepository backtestReqRepository;
    private final BacktestResRepository backtestResRepository;
    private final WebClient webClient;

    public void processBacktest(BacktestDto.CreateBacktestRequest requestDto) {
        try {
            BacktestDto.CreateBacktestResponse success = runBacktestLogic(requestDto);
//            String status = success ? "SUCCESS" : "FAILED";
            String status = "SUCCESS";
            BacktestReq req = saveBacktestRequest(requestDto, status);
            saveBacktestResult(req, success.totalReturn());

        } catch (Exception e) {
            // 실패 시 예외 처리
        }
    }

    private BacktestReq saveBacktestRequest(BacktestDto.CreateBacktestRequest requestDto, String status) {
        return backtestReqRepository.save(
                BacktestReq.builder()
                        .ticker(requestDto.ticker())
                        .startDate(LocalDate.parse(requestDto.startDate()))
                        .endDate(LocalDate.parse(requestDto.endDate()))
                        .strategy(requestDto.strategy())
                        .status(status)
                        .build()
        );
    }

    private void saveBacktestResult(BacktestReq req, double totalReturn) {
        backtestResRepository.save(
                BacktestRes.builder()
                        .requestId(req)
                        .totalReturn(totalReturn)
                        .build()
        );
    }

    private BacktestDto.CreateBacktestResponse runBacktestLogic(BacktestDto.CreateBacktestRequest requestDto) {
        return webClient.post()
                .uri("/api/backtest") // 추후 수정
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(BacktestDto.CreateBacktestResponse.class)
                .block(); // 결과 기다림
    }
}
