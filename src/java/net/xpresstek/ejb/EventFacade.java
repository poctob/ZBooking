/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.xpresstek.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Alex Pavlunenko <alexp at xpresstek.net>
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "ZCalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacade() {
        super(Event.class);
    }

    public List<Event> getByCalendarID(Calendar calendarID) {
        TypedQuery<Event> query = getEntityManager().
                createNamedQuery("Event.findByCalendarID", Event.class);
        query.setParameter("calendarID", calendarID);
        return query.getResultList();
    }

    public List<Event> getByCalendarIDandStart(Calendar calendarID, Date start) {
        Date start_date = new Date();
        if (start != null) {
            start_date = start;
        }
        TypedQuery<Event> query = getEntityManager().
                createNamedQuery("Event.findByCalendarIDandStart", Event.class);
        query.setParameter("calendarID", calendarID);
        query.setParameter("eventStart", start_date);
        return query.getResultList();
    }

    public List<Event> getByCalendarIDandEnd(Calendar calendarID, Date end) {
        Date end_date = new Date();
        if (end != null) {
            end_date = end;
        }
        TypedQuery<Event> query = getEntityManager().
                createNamedQuery("Event.findByCalendarIDandEnd", Event.class);
        query.setParameter("calendarID", calendarID);
        query.setParameter("eventEnd", end_date);
        return query.getResultList();
    }

}
