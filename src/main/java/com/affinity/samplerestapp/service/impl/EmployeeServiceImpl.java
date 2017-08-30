/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.service.impl;

import com.affinity.samplerestapp.dao.EmployeesFacadeLocal;
import com.affinity.samplerestapp.model.Employees;
import com.affinity.samplerestapp.qualifiers.DefaultEmpService;
import com.affinity.samplerestapp.service.EmployeeService;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Najeeb
 */
@RequestScoped
@DefaultEmpService
public class EmployeeServiceImpl implements EmployeeService{

    @EJB
    private EmployeesFacadeLocal empFacade;
    
    @Override
    public List<Employees> getAllEmployees() {
        return empFacade.findAll();
    }
    
    
    
}
