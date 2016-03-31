package pt.admedia.simples.lib;

import io.realm.RealmObject;

/**
 * Created by mrfreitas on 15/03/2016.
 */
public interface PartnersCategories {

    String seo = "";
    String title = "";
    String description = "";
    boolean isActive = false;

    String getSeo();
    void setSeo(String seo);

    String getTitle();
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);

    boolean isActive();
    void setState(boolean isActive);
}
