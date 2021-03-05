package com.belar.fitquest;

public class ContentItem
{
    String dayNumber, dateAquired;
    int dayN;

    public ContentItem(String dayNumber, String dateAquired, int dayN) {
        this.dayNumber = dayNumber;
        this.dateAquired = dateAquired;
        this.dayN = dayN;
    }

    public ContentItem(String dayNumber, String dateAquired ) {
        this.dayNumber = dayNumber;
        this.dateAquired = dateAquired;
        this.dayN = Integer.parseInt(String.valueOf(dayNumber.charAt(dayNumber.length() -1)));
    }

    public String getDateAquired() {
        return dateAquired;
    }

    public void setDateAquired(String dateAquired) {
        this.dateAquired = dateAquired;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getDayN() {
        return dayN;
    }

    public void setDayN(int dayN) {
        this.dayN = dayN;
    }
}
