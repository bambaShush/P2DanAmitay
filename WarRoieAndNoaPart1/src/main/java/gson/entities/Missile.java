package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Missile {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("launchTime")
    @Expose
    private long launchTime;
    @SerializedName("flyTime")
    @Expose
    private int flyTime;
    @SerializedName("damage")
    @Expose
    private double damage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public int getFlyTime() {
        return flyTime;
    }

    public void setFlyTime(int flyTime) {
        this.flyTime = flyTime;
    }

    public Double getDamage() {
        return damage;
    }

    public void setDamage(Double damage) {
        this.damage = damage;
    }
}
