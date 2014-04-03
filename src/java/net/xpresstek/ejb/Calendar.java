/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.xpresstek.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alex Pavlunenko <alexp at xpresstek.net>
 */
@Entity
@Table(name = "Calendar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c"),
    @NamedQuery(name = "Calendar.findById", query = "SELECT c FROM Calendar c WHERE c.id = :id"),
    @NamedQuery(name = "Calendar.findByName", query = "SELECT c FROM Calendar c WHERE c.name = :name"),
    @NamedQuery(name = "Calendar.findByIsActive", query = "SELECT c FROM Calendar c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "Calendar.findByDateEdited", query = "SELECT c FROM Calendar c WHERE c.dateEdited = :dateEdited"),
    @NamedQuery(name = "Calendar.findByStartDate", query = "SELECT c FROM Calendar c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "Calendar.findByEndDate", query = "SELECT c FROM Calendar c WHERE c.endDate = :endDate"),
    @NamedQuery(name = "Calendar.findByDateActive", query = "SELECT c FROM Calendar c WHERE c.isActive = :isActive "
            + "AND c.startDate <= :startDate "
            + "AND c.endDate >= :endDate")})

public class Calendar implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "isActive", nullable = false)
    private short isActive;
    @Basic(optional = false)
    @Column(name = "date_edited", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEdited;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendarID")
    private Collection<Event> eventCollection;

    public Calendar() {
    }

    public Calendar(Integer id) {
        this.id = id;
    }

    public Calendar(Integer id, String name, short isActive, Date dateEdited) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.dateEdited = dateEdited;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsActive() {
        return isActive==1;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = (short)(isActive?1:0);
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @XmlTransient
    public Collection<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(Collection<Event> eventCollection) {
        this.eventCollection = eventCollection;
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
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar other = (Calendar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.xpresstek.ejb.Calendar[ id=" + id + " ]";
    }
    
}
