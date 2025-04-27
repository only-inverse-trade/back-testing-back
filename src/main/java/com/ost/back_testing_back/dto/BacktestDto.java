package com.ost.back_testing_back.dto;


public class BacktestDto {

    public record CreateBacktestRequest(
            String ticker,
            String startDate,
            String endDate,
            String strategy
    ) {
    }
    public record CreateBacktestResponse(
            Double totalReturn
    ) {
    }
}
