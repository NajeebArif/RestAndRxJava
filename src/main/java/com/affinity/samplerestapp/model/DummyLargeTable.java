/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.affinity.samplerestapp.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Najeeb
 */
@Entity
@XmlRootElement
@Table(name = "DUMMY_LARGE_TABLE")
@NamedQueries({
    @NamedQuery(name = "DummyLargeTable.findAll",query = "SELECT d from DummyLargeTable d"),
    @NamedQuery(name = "DummyLargeTable.findForUserName",query = "SELECT d FROM DummyLargeTable d where d.username = :username"),
    @NamedQuery(name = "DummyLargeTable.totalNumberOfRecords", query = "Select count(d) from DummyLargeTable d"),
    @NamedQuery(name = "DummyLargeTable.enhancedPagination", query = "Select d from DummyLargeTable d where d.id > :id order by d.id desc")
})
public class DummyLargeTable implements Serializable{
    
    @Id
    private long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL")
    private String email;

    public DummyLargeTable() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DummyLargeTable{" + "id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + '}';
    }
    
    public String getJsonRep(){
        return "{\"id\":"+id+",\"username\":\""+username+"\",\"password\":\""+password+"\",\"email\":\""+email+"\"}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + Objects.hashCode(this.username);
        hash = 53 * hash + Objects.hashCode(this.password);
        hash = 53 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DummyLargeTable other = (DummyLargeTable) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }
    
    
}
