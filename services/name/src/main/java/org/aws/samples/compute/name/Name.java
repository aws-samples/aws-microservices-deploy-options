package org.aws.samples.compute.name;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "NAME_SCHEMA")
@NamedQueries({
    @NamedQuery(name = "Name.findAll", query = "SELECT e FROM Name e"),
    @NamedQuery(name = "Name.findById", query = "SELECT e FROM Name e where e.id = :id")
})
@XmlRootElement
public class Name implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(length=40)
    private String name;
    
    public Name() { }
    
    public Name(String name) {
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
