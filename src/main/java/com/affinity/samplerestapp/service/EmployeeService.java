/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.service;

import com.affinity.samplerestapp.model.Employees;
import java.util.List;

/**
 *
 * @author Najeeb
 */
public interface EmployeeService {
    public List<Employees> getAllEmployees();
}
