/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Najeeb
 */
@XmlRootElement
public class ResponseWrapper {
    
    private List<DummyLargeTable> valueList;
    private int size;

    public ResponseWrapper() {
        valueList=Collections.EMPTY_LIST;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public List<DummyLargeTable> getValueList() {
        return valueList;
    }

    public void setValueList(List<DummyLargeTable> valueList) {
        this.valueList = valueList;
    }

    public void updateSize() {
        setSize(valueList.size());
    }
    
    
}
