/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.service.impl;

import com.affinity.samplerestapp.model.DummyLargeTable;
import com.affinity.samplerestapp.qualifiers.HrService12c;
import com.affinity.samplerestapp.service.HrService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author narif
 */
@Stateless
@HrService12c
public class HrServiceImpl implements HrService{
    
    @Resource(lookup = "jdbc/12cHrDataSource")
    private DataSource ds;
    
    @PostConstruct
    private void init(){
       
    }
    
    @Override
    public List<DummyLargeTable> getAllValues(int start, int maxValue){
        int a = start;
        int b = start+maxValue;
        List<DummyLargeTable> list = new ArrayList<>();
        String query = "Select * from DUMMY_LARGE_TABLE where id>="+a+" and id<"+b+" fetch first "+maxValue+" rows only";
        try(Connection con = ds.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query)){
            while(rs.next()){
                DummyLargeTable d = new DummyLargeTable();
                d.setId(rs.getLong("ID"));
                d.setEmail(rs.getString("EMAIL"));
                d.setPassword(rs.getString("PASSWORD"));
                d.setUsername(rs.getString("USERNAME"));
                list.add(d);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
