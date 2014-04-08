/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.xpresstek.ejb;

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
    
    public List<Event> getByCalendarID(Calendar calendarID)
    {
        TypedQuery<Event> query = getEntityManager().
                createNamedQuery("Event.findByCalendarID", Event.class);
        query.setParameter("calendarID", calendarID);
        return query.getResultList();
    }
    
}
