/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.service;

import com.affinity.samplerestapp.model.DummyLargeTable;
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
    
}
