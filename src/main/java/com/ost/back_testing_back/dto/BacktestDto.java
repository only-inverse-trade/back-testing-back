package com.ost.back_testing_back.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BacktestDto {

    public record CreateBacktestRequest(
            String ticker,
            String startDate,
            String endDate,
            String strategy
    ) {
    }
    public record CreateBacktestResponse(
            @JsonProperty("total_return")
            Double totalReturn
    ) {}
}
