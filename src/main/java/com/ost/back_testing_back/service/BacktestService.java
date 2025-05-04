package com.ost.back_testing_back.service;

import com.ost.back_testing_back.dto.BacktestDto;
import com.ost.back_testing_back.entity.BacktestReq;
import com.ost.back_testing_back.entity.BacktestRes;
import com.ost.back_testing_back.exception.NoDataFoundException;
import com.ost.back_testing_back.repository.BacktestReqRepository;
import com.ost.back_testing_back.repository.BacktestResRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BacktestService {

    private final BacktestReqRepository backtestReqRepository;
    private final BacktestResRepository backtestResRepository;
    private final WebClient webClient;

    // 백테스트 요청을 처리하는 메서드
    @Transactional
    public double processBacktest(BacktestDto.CreateBacktestRequest requestDto) {
        System.out.println("[백테스트 요청] " + requestDto);
        try {
            BacktestDto.CreateBacktestResponse result = runBacktestLogic(requestDto);

            if (result == null) {
                throw new NoDataFoundException("No data found for ticker: " + requestDto.ticker());
            }

            BacktestReq req = saveBacktestRequest(requestDto, "SUCCESS");
            saveBacktestResult(req, result.totalReturn());

            return result.totalReturn();

        } catch (Exception e) {
            System.err.println("[백테스트 실패] " + e.getMessage());
            saveBacktestRequest(requestDto, "FAILED");
            throw new RuntimeException("ML 서버 통신 실패", e);
        }
    }

    // 백테스트 요청을 저장하는 메서드
    private BacktestReq saveBacktestRequest(BacktestDto.CreateBacktestRequest requestDto, String status) {
        return backtestReqRepository.save(
                BacktestReq.builder()
                        .ticker(requestDto.ticker())
                        .startDate(LocalDate.parse(requestDto.startDate()))
                        .endDate(LocalDate.parse(requestDto.endDate()))
                        .strategy(Optional.ofNullable(requestDto.strategy()).orElse(""))
                        .status(status)
                        .build()
        );
    }

    // 백테스트 결과를 저장하는 메서드
    private void saveBacktestResult(BacktestReq req, double totalReturn) {
        backtestResRepository.save(
                BacktestRes.builder()
                        .requestId(req)
                        .totalReturn(totalReturn)
                        .build()
        );
    }    // 백테스트 로직을 실행하는 메서드

    private BacktestDto.CreateBacktestResponse runBacktestLogic(BacktestDto.CreateBacktestRequest requestDto) {
        return webClient.post()
                .uri("https://2d83-115-138-25-15.ngrok-free.app/backtest") // 추후 수정
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(BacktestDto.CreateBacktestResponse.class)
                .block(); // 결과 기다림
    }


}
