package pt.admedia.simples.lib;

/**
 * Created by mrfreitas on 12/03/2016.
 */
public class SubCategory implements PartnersCategories {

    private String seo;
    private String title;
    private String Description;
    private boolean isActive;


    public SubCategory(String niu, String title){
        this.seo = niu;
        this.title = title;
    }

    public String getSeo() {
        return seo;
    }
    public void setSeo(String seo) {
        this.seo = seo;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setState(boolean isActive) {
        this.isActive = isActive;
    }
}
