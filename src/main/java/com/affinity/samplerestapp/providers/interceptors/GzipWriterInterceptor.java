/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.providers.interceptors;

import com.affinity.samplerestapp.qualifiers.Compress;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 *
 * @author narif
 */
@Provider
@Compress
public class GzipWriterInterceptor implements WriterInterceptor{

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        MultivaluedMap<String,Object> headers = context.getHeaders();
        headers.add("Content-Encoding", "gzip");
        System.out.println("COMPRESSING THE DATA USING GZIP.");
        final OutputStream os = context.getOutputStream();
        context.setOutputStream(new GZIPOutputStream(os));
        context.proceed();
    }
    
}
