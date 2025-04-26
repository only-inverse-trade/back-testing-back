package com.ost.back_testing_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BacktestRes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private BacktestReq requestId;

    @Column(name = "total_return", nullable = false)
    private Double totalReturn;
}
