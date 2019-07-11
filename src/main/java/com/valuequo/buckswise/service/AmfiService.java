package com.valuequo.buckswise.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.*;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import com.valuequo.buckswise.domain.Amc;
import com.valuequo.buckswise.domain.Amfi;
import com.valuequo.buckswise.repository.AmcRepository;
import com.valuequo.buckswise.repository.AmfiRepository;
import com.valuequo.buckswise.service.dto.AmfiDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class AmfiService {

    @Autowired
    private AmfiRepository amfiRepository;

    @Autowired
    private AmcRepository amcRepository;

    private SessionFactory hibernateFactory;
    
    Session session = null;
    
    Transaction tx;
    
    
    AmfiService(EntityManagerFactory factory) {
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }
    
    @Transactional
    @Modifying
    public void save(ArrayList<AmfiDTO> al) {
        List<Amfi> amfiDTO = amfiRepository.findAll();
        if (amfiDTO.isEmpty()) {
            int size = al.size();
            int count = 0;
            ArrayList<Amfi> amf = new ArrayList<Amfi>();
            for (AmfiDTO data : al) {
                Amfi amfi = new Amfi();
                amfi.setDate(data.getDate());
                amfi.setSchemeCode(data.getSchemeCode());
                amfi.setISINDivPayoutISINGrowth(data.getISINDivPayoutISINGrowth());
                amfi.setISINDivReinvestment(data.getISINDivReinvestment());
                amfi.setSchemeName(data.getSchemeName());
                amfi.setDay1(data.getDay1());
                amfi.setDate(data.getDate());
                amf.add(amfi);
                if ((count + 1) % 1000 == 0 || (count + 1) == size) {
                    amfiRepository.save(amf);
                }
                count++;
            }
        } else {
            session = this.hibernateFactory.openSession();
            ArrayList<Amfi> amf1 = new ArrayList<Amfi>();
            for (AmfiDTO result : al) {
                Amfi amfi = new Amfi();
                amfi.setDate(result.getDate());
                amfi.setSchemeCode(result.getSchemeCode());
                amfi.setDay1(result.getDay1());

                String schemeCode = amfi.getSchemeCode();
                String date = amfi.getDate();
                String nav = amfi.getDay1();

                Date day = new Date(date);
                int dayValue = day.getDate();
                String dayVal = Integer.toString(dayValue);
                String days = "day" + dayVal;
                update(schemeCode, nav, days);
            }
            session.close();
        }
    }

    private Transaction OpenDBConnection() {
        session = this.hibernateFactory.openSession();
        tx = session.beginTransaction();
        return tx;
    }

    private Transaction closeDBConnection(Transaction txx) {
        txx.commit();
        session.close();
        return txx;
    }

    public void update(String schemeCode, String nav, String days) {
        String str1 = new String(days);
        Date date = new Date();
        int dayValue = date.getDate();
        String dayVal = Integer.toString(dayValue);
        String currentDay = "day" + dayVal;
        String str2 = new String(currentDay);
        boolean isEqual = str1.equals(str2);
        if (isEqual) {
            try {
                // session = this.hibernateFactory.openSession();
                tx = session.beginTransaction();
                String sqlQuery = "UPDATE Amfi a SET a." + days + "=" + nav + " where a.SchemeCode = " + schemeCode;
                Query query = session.createQuery(sqlQuery);
                query.executeUpdate();
                session.flush();
                session.clear();
                tx.commit();
            } catch (Exception e) {
                try {
                    tx.rollback();
                } catch (RuntimeException rte) {
                    rte.printStackTrace();
                }
                throw e;
            }
        } else {
			try {
                // session = this.hibernateFactory.openSession();
                tx = session.beginTransaction();
                String sqlQuery = "UPDATE Amfi a SET a.day1 = " + nav + " where a.SchemeCode = " + schemeCode;
                Query query = session.createQuery(sqlQuery);
                query.executeUpdate();
                session.flush();
                session.clear();
                tx.commit();
            } catch (Exception e) {
                try {
                    tx.rollback();
                } catch (RuntimeException rt) {
                    rt.printStackTrace();
                }
                throw e;
            }
				
		}
    }

    @Transactional
    public void getAmfiCode() {
        List<Amc> amc = amcRepository.findAll();
        for (Amc result : amc) {
            String amc_code = result.getAmc_code();
            amfiRepository.update(amc_code);
        }
    }

    public List<Amfi> getAmcName(String name) {
        List<Amfi> nav = amfiRepository.findByAmc_code(name);
        return nav;

    }

    public List<Amc> getAllAmc() {
        List<Amc> nav = amcRepository.findAll();
        return nav;
    }

}