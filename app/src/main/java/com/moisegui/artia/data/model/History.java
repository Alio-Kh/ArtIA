package com.moisegui.artia.data.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class History {

    private String historyID;
    private String Historydate;
    private String user;
    private String motif;

    public History() {
    }

    public History(String historyID, String historydate, String user, String motif) {
        this.historyID = historyID;
        Historydate = historydate;
        this.user = user;
        this.motif = motif;
    }

    public History(String historydate, String user, String motif) {
        this.historyID = historyID;
        Historydate = historydate;
        this.user = user;
        this.motif = motif;
    }

    public String getHistoryID() {
        return historyID;
    }

    public void setHistoryID(String historyID) {
        this.historyID = historyID;
    }

    public String getHistorydate() {
        return Historydate;
    }

    public void setHistorydate(String historydate) {
        Historydate = historydate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("historyID", historyID);
        result.put("Historydate", Historydate);
        result.put("user", user);
        result.put("motif", motif);
        return result;
    }
}