package net.xpresstek.jsf;

import net.xpresstek.ejb.Event;
import net.xpresstek.jsf.util.JsfUtil;
import net.xpresstek.jsf.util.JsfUtil.PersistAction;
import net.xpresstek.ejb.EventFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.xpresstek.ejb.Calendar;
import net.xpresstek.ejb.Payment;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;

@Named("eventController")
@SessionScoped
public class EventController implements Serializable {

    @EJB
    private net.xpresstek.ejb.EventFacade ejbFacade;
    private List<Event> items = null;
    private Event selected;

    public EventController() {
    }

    public Event getSelected() {
        return selected;
    }

    public void setSelected(Event selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EventFacade getFacade() {
        return ejbFacade;
    }

    public Event prepareCreate() {
        selected = new Event();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EventCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EventUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EventDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Event> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public double getRemainingAmount()
    {
        return getRemainingAmountByEvent(selected);
    }

    public static double getRemainingAmountByEvent(Event event)
    {
         double retval = 0;
        
        if(event !=null)
        {
            retval = event.getPrice();
            List<Payment> payments = 
                    PaymentController.getController().findByEventID(event);
            for(Payment p : payments)
            {
                retval -=p.getAmount();
            }
        }
        return retval;
    }
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Event getEvent(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Event> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Event> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Event> getByCalendarID(Calendar calendarID) {
        return getFacade().getByCalendarID(calendarID);
    }
    
    public List<Event> getUpcomingByCalendarID(Calendar calendarID) {
        return getFacade().getByCalendarIDandEnd(calendarID, new Date());
    }

    public static EventController getController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (EventController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "eventController");
    }

    public void onDateSelect(SelectEvent selectEvent) {
        selected = new Event();
        selected.setEventStart((Date) selectEvent.getObject());
        selected.setEventEnd((Date) selectEvent.getObject());
        selected.setCalendarID(CalendarController.getController().getSelected());

    }

    public void onEventSelect(SelectEvent selectEvent) {

        ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
        if (event != null && event.getData() != null) {
            setSelected(ejbFacade.find((Integer) event.getData()));
        }
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        ScheduleEvent sevent = event.getScheduleEvent();
        FacesMessage message = null;
        if (sevent != null) {
            if (moveEvent(sevent)) {
                message = new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Event moved", "Event Moved");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error moving event!", "Error moving event!");
            }
            addMessage(message);
        }

    }

    private boolean moveEvent(ScheduleEvent sevent) {
        boolean retval = false;

        if (sevent != null) {
            Event ev = ejbFacade.find(sevent.getData());
            if (ev != null) {
                 ev.setEventStart(new Date(sevent.getStartDate().getTime()));
                 ev.setEventEnd(new Date(sevent.getEndDate().getTime()));

                setSelected(ev);
                update();
                retval = true;
            }
        }
        return retval;
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        ScheduleEvent sevent = event.getScheduleEvent();
        if (sevent != null) {
            FacesMessage message = null;
           if (moveEvent(sevent)) {      
                message = new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Event adjusted", "Event adjusted");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error adjusting event!", "Error adjusting event!");
            }
            addMessage(message);
        }
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @FacesConverter(forClass = Event.class)
    public static class EventControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EventController controller = (EventController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "eventController");
            return controller.getEvent(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Event) {
                Event o = (Event) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Event.class.getName()});
                return null;
            }
        }

    }

}
