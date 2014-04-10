/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.xpresstek.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.xpresstek.jsf.EventController;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

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

    private static final String CLASSPAID = "eventPaid";
    private static final String CLASSUNPAID = "eventUnpaid";
    private static final String CLASSOVERPAID = "eventOverpaid";
    private static final String CLASSPARTIALLYPAID = "eventPartiallyPaid";
    private static final String CLASSPAST = "eventPast";

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

    @Transient
    private final ScheduleModel eventModel;

    public Calendar() {
        eventModel = new DefaultScheduleModel();
    }

    public Calendar(Calendar source) {
        this.name = source.name;
        this.isActive = source.isActive;
        this.startDate = source.startDate;
        this.endDate = source.endDate;
        this.eventCollection = source.eventCollection;
        this.eventModel = source.eventModel;
        this.dateEdited = new Date();
    }

    public Calendar(Integer id) {
        this.id = id;
        eventModel = new DefaultScheduleModel();
    }

    public Calendar(Integer id, String name, short isActive, Date dateEdited) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.dateEdited = dateEdited;
        eventModel = new DefaultScheduleModel();
    }

    public void buildEventModel(List<Event> events) {
        if (eventModel != null && events != null) {
            for (Event e : events) {
                DefaultScheduleEvent event = new DefaultScheduleEvent(e.getTitle(),
                        new Date(e.getEventStart().getTime()),
                        new Date(e.getEventEnd().getTime()),
                        e.getId());
                setEventStyle(event);
                eventModel.addEvent(event);

            }
        }
    }

    public static void setEventStyle(DefaultScheduleEvent e) {
        if (e != null && e.getData() != null) {
            EventController ec = EventController.getController();
            Event event = ec.getEvent((Integer) e.getData());
            double remainingAmount=EventController.getRemainingAmountByEvent(event);
            
            if (e.getEndDate().getTime() < new Date().getTime()) {
                e.setStyleClass(CLASSPAST);
                return;
            }
            
            if(event.getPrice() >0 && event.getPrice() == remainingAmount)
            {
                e.setStyleClass(CLASSUNPAID);
                return;
            }
            
            if(remainingAmount>0)
            {
                e.setStyleClass(CLASSPARTIALLYPAID);
                return;
            }
            
            if(remainingAmount<0)
            {
                e.setStyleClass(CLASSOVERPAID);
                return;
            }
            e.setStyleClass(CLASSPAID);
        }
    }

    public void resetEventModel() {
        if (eventModel != null) {
            eventModel.clear();
        }
    }

    public ScheduleModel getEventModel() {
        return eventModel;
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
        return isActive == 1;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = (short) (isActive ? 1 : 0);
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
