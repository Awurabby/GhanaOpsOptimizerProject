package com.team.smartops;

import com.team.smartops.model.Location;
import com.team.smartops.model.Resource;
import com.team.smartops.model.Road;
import com.team.smartops.model.ServiceRequest;
import com.team.smartops.structures.Graph;

import java.util.List;
import java.util.Map;

public class AppState {
    public List<Location> locations;
    public List<Road> roads;
    public List<ServiceRequest> requests;
    public List<Resource> resources;
    public Map<Integer, String> locationNameIndex;
    public Graph graph;
    public boolean dataLoaded = false;
    public boolean graphLoaded = false;
}