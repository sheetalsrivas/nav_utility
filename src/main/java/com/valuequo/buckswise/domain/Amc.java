package com.valuequo.buckswise.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "amc")
@Component
public class Amc implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AMC_code")
    private String amc_code;

    @Column(name = "AMC_Name")
    private String amc_name;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the amc_code
     */
    public String getAmc_code() {
        return amc_code;
    }

    /**
     * @param amc_code the amc_code to set
     */
    public void setAmc_code(String amc_code) {
        this.amc_code = amc_code;
    }

    /**
     * @return the amc_name
     */
    public String getAmc_name() {
        return amc_name;
    }

    /**
     * @param amc_name the amc_name to set
     */
    public void setAmc_name(String amc_name) {
        this.amc_name = amc_name;
    }
}