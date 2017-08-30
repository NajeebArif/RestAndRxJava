/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Najeeb
 */
@Path("test")
public class RestEndPointOne {
    
    @GET
    @Path("greet")
    public String sayHello(){
        return "Hello World!!";
    }
}
