package pt.admedia.simples.model;

import com.google.gson.JsonObject;

import io.realm.RealmObject;

/**
 * Created by mrfreitas on 28/03/2016.
 */
public class SchedulesEntity extends RealmObject {

    private String day;
    private String start;
    private String end;

    public SchedulesEntity() {}

    public SchedulesEntity(JsonObject schedule) {
        if (!schedule.get("day").isJsonNull())
            this.day = schedule.get("day").getAsString();
        if (!schedule.get("start").isJsonNull())
            this.start = schedule.get("start").getAsString();
        if (!schedule.get("end").isJsonNull())
            this.end = schedule.get("end").getAsString();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
