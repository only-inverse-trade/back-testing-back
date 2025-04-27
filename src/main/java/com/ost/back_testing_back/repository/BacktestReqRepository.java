package com.ost.back_testing_back.repository;

import com.ost.back_testing_back.entity.BacktestReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacktestReqRepository extends JpaRepository<BacktestReq, Long> {

}
