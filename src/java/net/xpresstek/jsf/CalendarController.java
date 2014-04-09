package net.xpresstek.jsf;

import net.xpresstek.ejb.Calendar;
import net.xpresstek.jsf.util.JsfUtil;
import net.xpresstek.jsf.util.JsfUtil.PersistAction;
import net.xpresstek.ejb.CalendarFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.xpresstek.ejb.Event;
import org.primefaces.model.ScheduleModel;

@Named("calendarController")
@SessionScoped
public class CalendarController implements Serializable {

    @EJB
    private net.xpresstek.ejb.CalendarFacade ejbFacade;
    private List<Calendar> items = null;
    private Calendar selected;

    public CalendarController() {
       
    }

    public ScheduleModel getEventModel() {  
        if(selected != null)
        {
            return selected.getEventModel();
        }
        return null;
    }
    
    public void updateCalendarEvents()
    {
        if(selected != null)
        {
            selected.resetEventModel();
            List<Event> events = 
                    EventController.getController().getByCalendarID(selected);
            selected.buildEventModel(events);
        }
    }
      
    
    public List<Calendar> getActive()
    {
        return ejbFacade.findActiveDate();
    }

    public Calendar getSelected() {        
        return selected;
    }

    public void setSelected(Calendar selected) {        
        this.selected = selected;
        updateCalendarEvents();
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CalendarFacade getFacade() {
        return ejbFacade;
    }

    public Calendar prepareCreate() {
        selected = new Calendar();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CalendarCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
     public void copy(Calendar source) {
         if(source != null)
         {
             Calendar copy = new Calendar(source);
             copy.setName(copy.getName() + " copy");
             selected = copy;
             initializeEmbeddableKey();
             persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CalendarCreated"));
             if (!JsfUtil.isValidationFailed()) {
                 items = null;    // Invalidate list of items to trigger re-query.
             }
         }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CalendarUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CalendarDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    public void deactivate() {
        this.selected.setIsActive(false);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").
                getString("CalendarDeactivated"));    
    }
    
     public void activate() {
        this.selected.setIsActive(true);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").
                getString("CalendarActivated"));    
    }

    public List<Calendar> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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

    public Calendar getCalendar(java.lang.Integer id) {
        return getFacade().find(id);
    }
    
    public List<Event> getUpcomingEvents()
    {
        if(selected != null)
        {
            return EventController.getController().getUpcomingByCalendarID(selected);
        }
        return null;
    }

    public List<Calendar> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Calendar> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
     public static CalendarController getController()
    { 
         FacesContext facesContext = FacesContext.getCurrentInstance();
        return (CalendarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "calendarController");
    }

    @FacesConverter(forClass = Calendar.class)
    public static class CalendarControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CalendarController controller = (CalendarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "calendarController");
            return controller.getCalendar(getKey(value));
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
            if (object instanceof Calendar) {
                Calendar o = (Calendar) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Calendar.class.getName()});
                return null;
            }
        }

    }

}
