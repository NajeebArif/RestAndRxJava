/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.dao;

import com.affinity.samplerestapp.model.DummyLargeTable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Najeeb
 */
@Stateless
public class DummyLargeTableFacade extends AbstractFacade<DummyLargeTable> {

    @PersistenceContext(unitName = "com.affinity_sampleRestApp_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DummyLargeTableFacade() {
        super(DummyLargeTable.class);
    }
    
    public DummyLargeTable getValueForUsername(String username){
        Query q = em.createNamedQuery("DummyLargeTable.findForUserName", DummyLargeTable.class);
        q.setParameter("username", username);
        DummyLargeTable value = (DummyLargeTable)q.getSingleResult();
        return value;
    }
    
    public int totalNumberOfRecords(){
        return em.createNamedQuery("DummyLargeTable.totalNumberOfRecords",Long.class).getSingleResult().intValue();
    }
    
    public List<DummyLargeTable> getRecordsInRange(int startPos, int maxFetchSize){
        return em.createNamedQuery("DummyLargeTable.findAll").setFirstResult(startPos).setMaxResults(maxFetchSize).getResultList();
    }
    
    public List<DummyLargeTable> getResultListForEnhancedPagination(long id,long endId, int noOfRecords){
        String sql = "Select * from DUMMY_LARGE_TABLE where ID >= ?1 and ID < ?2 fetch first ?3 rows only;";
        Query q = em.createNativeQuery(sql, DummyLargeTable.class);
        q.setParameter(1, id);
        q.setParameter(2, endId);
        q.setParameter(3, noOfRecords);
        List<DummyLargeTable> result = q.getResultList();
        return result;
    }
    
}
