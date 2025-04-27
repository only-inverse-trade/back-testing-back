package com.ost.back_testing_back.repository;

import com.ost.back_testing_back.entity.BacktestRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacktestResRepository extends JpaRepository<BacktestRes, Long> {

}
