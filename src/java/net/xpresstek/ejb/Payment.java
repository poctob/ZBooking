/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.xpresstek.ejb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alex Pavlunenko <alexp at xpresstek.net>
 */
@Entity
@Table(name = "Payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.id = :id"),
    @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount"),
    @NamedQuery(name = "Payment.findByDateAccepted", query = "SELECT p FROM Payment p WHERE p.dateAccepted = :dateAccepted"),
    @NamedQuery(name = "Payment.findByAcceptedBy", query = "SELECT p FROM Payment p WHERE p.acceptedBy = :acceptedBy"),
    @NamedQuery(name = "Payment.findByEventID", query = "SELECT p FROM Payment p WHERE p.eventID = :eventID")})
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount", nullable = false)
    private double amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateAccepted", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAccepted;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "acceptedBy", nullable = false, length = 255)
    private String acceptedBy;
    @JoinColumn(name = "eventID", referencedColumnName = "id")
    @ManyToOne
    private Event eventID;
    @JoinColumn(name = "paymentTypeID", referencedColumnName = "id")
    @ManyToOne
    private PaymentType paymentTypeID;

    public Payment() {
    }

    public Payment(Integer id) {
        this.id = id;
    }

    public Payment(Integer id, short amount, Date dateAccepted, String acceptedBy) {
        this.id = id;
        this.amount = amount;
        this.dateAccepted = dateAccepted;
        this.acceptedBy = acceptedBy;
    }

    public Event getEventID() {
        return eventID;
    }

    public void setEventID(Event eventID) {
        this.eventID = eventID;
    }

    public PaymentType getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(PaymentType paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDateAccepted() {
        return dateAccepted;
    }

    public void setDateAccepted(Date dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.xpresstek.ejb.Payment[ id=" + id + " ]";
    }
    
}
