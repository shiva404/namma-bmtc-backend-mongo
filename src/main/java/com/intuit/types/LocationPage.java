package com.intuit.types;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class LocationPage {
    private List<Location> locations;
    private int offset;
    private int size;

    public int getThanksCount() {
        return thanksCount;
    }

    public void setThanksCount(int thanksCount) {
        this.thanksCount = thanksCount;
    }

    private int thanksCount;

    public List<Location> getLocations() {
        if(locations == null)
            locations = new LinkedList<>();
        return locations;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
