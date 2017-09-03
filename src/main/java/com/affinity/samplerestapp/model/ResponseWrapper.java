/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Najeeb
 */
@XmlRootElement
public class ResponseWrapper {
    
    private List<DummyLargeTable> valueList;

    public ResponseWrapper() {
    }

    public List<DummyLargeTable> getValueList() {
        return valueList;
    }

    public void setValueList(List<DummyLargeTable> valueList) {
        this.valueList = valueList;
    }
    
    
}
