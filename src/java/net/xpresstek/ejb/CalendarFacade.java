/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.xpresstek.ejb;

import java.util.Date;
import java.util.GregorianCalendar;
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
public class CalendarFacade extends AbstractFacade<Calendar> {
    @PersistenceContext(unitName = "ZCalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CalendarFacade() {
        super(Calendar.class);
    }

    public List<Calendar> findActive() {
        TypedQuery<Calendar> query = getEntityManager().
                createNamedQuery("Calendar.findByIsActive", Calendar.class);
        
        query.setParameter("isActive", 1);
        return query.getResultList();
    }
    
    public List<Calendar> findActiveDate() {
        TypedQuery<Calendar> query = getEntityManager().
                createNamedQuery("Calendar.findByDateActive", Calendar.class);
        
        java.util.Calendar today_start = new GregorianCalendar();
        java.util.Calendar today_end = new GregorianCalendar();
        
        today_start.set(java.util.Calendar.SECOND, 0);
        today_start.set(java.util.Calendar.MINUTE, 0);
        today_start.set(java.util.Calendar.HOUR, 0);
        
        today_end.set(java.util.Calendar.SECOND, 59);
        today_end.set(java.util.Calendar.MINUTE, 59);
        today_end.set(java.util.Calendar.HOUR, 23);
        
        query.setParameter("isActive", 1);
        query.setParameter("startDate", today_start.getTime());
        query.setParameter("endDate", today_end.getTime());
        
        return query.getResultList();
    }
    
}
