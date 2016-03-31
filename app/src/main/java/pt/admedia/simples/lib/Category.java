package pt.admedia.simples.lib;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by mrfreitas on 12/03/2016.
 */
public class Category implements PartnersCategories {

    private String seo;
    private String title;
    private String description = "";
    boolean isActive;
    private ArrayList<PartnersCategories> subCategories = new ArrayList<>();


    public Category(JsonObject categorias){
        this.seo = seo;
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<PartnersCategories> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<PartnersCategories> subCategories) {
        this.subCategories = subCategories;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setSubCatState(){
        for (PartnersCategories pc : subCategories){
            pc.setState(isActive);
        }
    }

    public void setState(boolean state){
        isActive = state;
        for (PartnersCategories pc : subCategories){
            pc.setState(state);
        }
    }
}
