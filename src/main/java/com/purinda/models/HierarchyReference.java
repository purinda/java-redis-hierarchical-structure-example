package com.purinda.models;

public class HierarchyReference {
    private int underageHierId;
    private int evClassId;
    private int evTypeId;
    private String status;

    public HierarchyReference(int underageHierId, int evClassId, int evTypeId, String status) {
        this.underageHierId = underageHierId;
        this.evClassId = evClassId;
        this.evTypeId = evTypeId;
        this.status = status;
    }

    public String toString() {
        return "HierarchyReference: [ID: " + underageHierId + ", EvClassId: " + evClassId + ", EvTypeId: " + evTypeId + ", Status: " + status + "]";
    }
}