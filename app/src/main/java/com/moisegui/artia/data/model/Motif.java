package com.moisegui.artia.data.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Motif {

    private String motifID;
    private String motifName;
    private String motifDescription;
    private String motifImageSrc;

    public Motif() {
    }

    public Motif(String motifID, String motifName, String motifDescription, String motifImageSrc) {
        this.motifID = motifID;
        this.motifName = motifName;
        this.motifDescription = motifDescription;
        this.motifImageSrc = motifImageSrc;
    }

    public String getMotifID() {
        return motifID;
    }

    public void setMotifID(String motifID) {
        this.motifID = motifID;
    }

    public String getMotifName() {
        return motifName;
    }

    public void setMotifName(String motifName) {
        this.motifName = motifName;
    }

    public String getMotifDescription() {
        return motifDescription;
    }

    public void setMotifDescription(String motifDescription) {
        this.motifDescription = motifDescription;
    }

    public String getMotifImageSrc() {
        return motifImageSrc;
    }

    public void setMotifImageSrc(String motifImageSrc) {
        this.motifImageSrc = motifImageSrc;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("motifID", motifID);
        result.put("author", motifName);
        result.put("title", motifDescription);
        result.put("body", motifImageSrc);

        return result;
    }
}
