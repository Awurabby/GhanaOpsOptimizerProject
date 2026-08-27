package com.team.smartops;

import java.util.List;

import com.team.smartops.structures.Graph;

public class AppState {
    public List<String> locations;
    public List<String> roads;
    public List<String> requests;
    public List<String> resources;
    public Graph graph;
    public boolean dataLoaded = false;
    public boolean graphLoaded = false;
}