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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "Event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title"),
    @NamedQuery(name = "Event.findByNumberOfGuests", query = "SELECT e FROM Event e WHERE e.numberOfGuests = :numberOfGuests"),
    @NamedQuery(name = "Event.findByEventStart", query = "SELECT e FROM Event e WHERE e.eventStart = :eventStart"),
    @NamedQuery(name = "Event.findByEventEnd", query = "SELECT e FROM Event e WHERE e.eventEnd = :eventEnd"),
    @NamedQuery(name = "Event.findByCalendarID", query = "SELECT e FROM Event e WHERE e.calendarID = :calendarID"),
    @NamedQuery(name = "Event.findByCalendarIDandStart", query = "SELECT e FROM Event e WHERE e.calendarID = :calendarID AND e.eventStart >= :eventStart"),
    @NamedQuery(name = "Event.findByCalendarIDandEnd", query = "SELECT e FROM Event e "
            + "WHERE e.calendarID = :calendarID AND e.eventEnd >= :eventEnd "
            + "ORDER BY e.eventEnd ASC")})
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "Title", nullable = false, length = 255)
    private String title;
    @Column(name = "NumberOfGuests")
    private Integer numberOfGuests;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EventStart", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventStart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EventEnd", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventEnd;
    @Lob
    @Size(max = 65535)
    @Column(name = "Notes", length = 65535)
    private String notes;
    @Basic(optional = true)
    @Column(name = "Price", nullable = true)
    private double price;
    @OneToMany(mappedBy = "eventID")
    private Collection<Payment> paymentCollection;
    @JoinColumn(name = "CustomerID", referencedColumnName = "id")
    @ManyToOne
    private Customer customerID;
    @JoinColumn(name = "CalendarID", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Calendar calendarID;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, String title, Date eventStart, Date eventEnd) {
        this.id = id;
        this.title = title;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public Event(Event source) {
        this.id = source.id;
        this.title = source.title;
        this.eventStart = source.eventStart;
        this.eventEnd = source.eventEnd;
        this.notes = source.notes;
        this.customerID = source.customerID;
        this.calendarID = source.calendarID;
        this.price = source.price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @XmlTransient
    public Collection<Payment> getPaymentCollection() {
        return paymentCollection;
    }

    public void setPaymentCollection(Collection<Payment> paymentCollection) {
        this.paymentCollection = paymentCollection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Date getEventStart() {
        return eventStart;
    }

    public void setEventStart(Date eventStart) {
        this.eventStart = eventStart;
    }

    public Date getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Date eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    public Calendar getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(Calendar calendarID) {
        this.calendarID = calendarID;
    }

    public double getAmountRemaining() {
        double amount_rem = this.price;

        if (paymentCollection != null) {
            for (Payment payment : paymentCollection) {
                if (payment != null) {
                    amount_rem -= payment.getAmount();
                }
            }
        }
        return amount_rem;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "net.xpresstek.ejb.Event[ id=" + id + " ]";
    }

}
