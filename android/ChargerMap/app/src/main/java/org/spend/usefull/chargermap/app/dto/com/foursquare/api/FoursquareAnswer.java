package org.spend.usefull.chargermap.app.dto.com.foursquare.api;

import java.util.LinkedList;
import java.util.List;

public class FoursquareAnswer {
    List<FoursquareGroup> groups;

    public List<FoursquareGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<FoursquareGroup> groups) {
        this.groups = groups;
    }

    public List<FoursquareItem> getAllItems() {
        LinkedList<FoursquareItem> items = new LinkedList<FoursquareItem>();
        for (FoursquareGroup group : groups) {
            items.addAll(group.getItems());
        }
        return items;
    }
}
