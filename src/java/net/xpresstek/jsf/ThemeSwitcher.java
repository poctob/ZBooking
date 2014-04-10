/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.xpresstek.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import net.xpresstek.ejb.Theme;

/**
 *
 * @author Alex Pavlunenko <alexp at xpresstek.net>
 */
@Named("themeSwitcher")
@SessionScoped
public class ThemeSwitcher implements Serializable {

    private final Map<String, String> themes;

    private final List<Theme> advancedThemes;

    private String theme = "trontastic"; //default

    public Map<String, String> getThemes() {
        return themes;
    }

    public String getTheme() {
       Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("theme")) {
            theme = params.get("theme");
        }

        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public static String currentTheme() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        return ec.getInitParameter("primefaces.THEME");
    }

    public ThemeSwitcher() {

        advancedThemes = new ArrayList<>();
        advancedThemes.add(new Theme("aristo", "aristo.png"));
        advancedThemes.add(new Theme("cupertino", "cupertino.png"));
        advancedThemes.add(new Theme("trontastic", "trontastic.png"));

        themes = new TreeMap<>();
        themes.put("Aristo", "aristo");
        themes.put("Black-Tie", "black-tie");
        themes.put("Blitzer", "blitzer");
        themes.put("Bluesky", "bluesky");
        themes.put("Casablanca", "casablanca");
        themes.put("Cupertino", "cupertino");
        themes.put("Dark-Hive", "dark-hive");
        themes.put("Dot-Luv", "dot-luv");
        themes.put("Eggplant", "eggplant");
        themes.put("Excite-Bike", "excite-bike");
        themes.put("Flick", "flick");
        themes.put("Glass-X", "glass-x");
        themes.put("Hot-Sneaks", "hot-sneaks");
        themes.put("Humanity", "humanity");
        themes.put("Le-Frog", "le-frog");
        themes.put("Midnight", "midnight");
        themes.put("Mint-Choc", "mint-choc");
        themes.put("Overcast", "overcast");
        themes.put("Pepper-Grinder", "pepper-grinder");
        themes.put("Redmond", "redmond");
        themes.put("Rocket", "rocket");
        themes.put("Sam", "sam");
        themes.put("Smoothness", "smoothness");
        themes.put("South-Street", "south-street");
        themes.put("Start", "start");
        themes.put("Sunny", "sunny");
        themes.put("Swanky-Purse", "swanky-purse");
        themes.put("Trontastic", "trontastic");
        themes.put("UI-Darkness", "ui-darkness");
        themes.put("UI-Lightness", "ui-lightness");
        themes.put("Vader", "vader");
    }

    public void saveTheme() {
     /*   Cookie userCookie = new Cookie("ZCaltheme", theme);
        userCookie.setPath("/");
        HttpServletResponse response=(HttpServletResponse)FacesContext.getCurrentInstance()
                .getExternalContext()
                .getResponse();

        response.addCookie(userCookie);*/

        setTheme(theme);
    }

    public List<Theme> getAdvancedThemes() {
        return advancedThemes;
    }
}
