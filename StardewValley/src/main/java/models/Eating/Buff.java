package models.Eating;

public class Buff {
    private String mainString;
    private int hour;
    private String skill;

    public int getHour() {
        return hour;
    }

    public String getMainString() {
        return mainString;
    }

    public String getSkill() {
        return skill;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMainString(String mainString) {
        this.mainString = mainString;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
