/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.xpresstek.ejb;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alex Pavlunenko <alexp at xpresstek.net>
 */
@Entity
@Table(name = "PaymentType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentType.findAll", query = "SELECT p FROM PaymentType p"),
    @NamedQuery(name = "PaymentType.findById", query = "SELECT p FROM PaymentType p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentType.findByName", query = "SELECT p FROM PaymentType p WHERE p.name = :name")})
public class PaymentType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Lob
    @Size(max = 65535)
    @Column(name = "note", length = 65535)
    private String note;
    @OneToMany(mappedBy = "paymentTypeID")
    private Collection<Payment> paymentCollection;

    public PaymentType() {
    }

    public PaymentType(Integer id) {
        this.id = id;
    }

    public PaymentType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @XmlTransient
    public Collection<Payment> getPaymentCollection() {
        return paymentCollection;
    }

    public void setPaymentCollection(Collection<Payment> paymentCollection) {
        this.paymentCollection = paymentCollection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentType)) {
            return false;
        }
        PaymentType other = (PaymentType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.xpresstek.ejb.PaymentType[ id=" + id + " ]";
    }
    
}
