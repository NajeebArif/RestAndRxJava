/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.service;

import com.affinity.samplerestapp.model.DummyLargeTable;
import java.util.List;

/**
 *
 * @author narif
 */
public interface HrService {
    public List<DummyLargeTable> getAllValues(int start, int maxValue);
}
