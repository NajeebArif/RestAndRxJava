/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.endpoints;

import com.affinity.samplerestapp.model.Employees;
import com.affinity.samplerestapp.qualifiers.DefaultEmpService;
import com.affinity.samplerestapp.service.EmployeeService;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Najeeb
 */
@Path("employees")
public class EmplyeesRestEndpoint {

    public EmplyeesRestEndpoint() {
    }
    
    @Inject @DefaultEmpService
    private EmployeeService empService;
    
    @GET
    @Path("list")
    public List<Employees> getALlEmployeesList(){
        return empService==null?Collections.EMPTY_LIST:empService.getAllEmployees();
    }
    
    @GET
    public Response getAllEmps(){
        GenericEntity<List<Employees>> ge = new GenericEntity<List<Employees>>(empService.getAllEmployees()){};
        return Response.ok(ge, MediaType.APPLICATION_JSON).build();
    }
}
