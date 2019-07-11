package com.valuequo.buckswise.repository;

import com.valuequo.buckswise.domain.Amc;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AmcRepository extends JpaRepository<Amc, String> {
	
	// @Query("select a.amc_name, a.amc_code from Amc a")
	// List<Amc> findAmcode();
}