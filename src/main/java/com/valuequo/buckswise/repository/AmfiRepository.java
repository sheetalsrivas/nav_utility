package com.valuequo.buckswise.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.valuequo.buckswise.domain.Amfi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AmfiRepository extends JpaRepository<Amfi, Long>, JpaSpecificationExecutor<Amfi> {

	
	@Query("select a from Amfi a where a.amc_code = :name")
	List<Amfi> findByAmc_code(@Param("name") String name );
	
	@Transactional
	@Modifying
	@Query("UPDATE Amfi a SET a.amc_code = :amc_code WHERE a.SchemeName LIKE CONCAT('%',:amc_code,'%')")
	void update(@Param("amc_code") String amc_code);
	
	// @Query("Select a.NetAssetValue from Amfi a where a.SchemeCode =:schemeCode")
	// String findBySchemecode(@Param("schemeCode") String schemeCode);
	
	// @Query("select a.day1, a.day2, a.day3, a.day4, a.day5, a.day6, a.day7, a.day8, a.day9, a.day10, a.day11, a.day12, a.day13, a.day14, a.day15, a.day16, a.day17, a.day18, a.day19, a.day20,a.day21, a.day22, a.day23, a.day24, a.day25, a.day26, a.day27, a.day28, a.day29, a.day30, a.day31 from  Amfi a where a.SchemeCode = :schemecode")
	@Query("select a from Amfi a where a.SchemeCode =:schemecode")
	List<Amfi> getNav(@Param("schemecode") String schemecode);
}