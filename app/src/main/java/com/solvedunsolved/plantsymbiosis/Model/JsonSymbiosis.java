package com.solvedunsolved.plantsymbiosis.Model;

import java.io.Serializable;

public class JsonSymbiosis implements Serializable {
    private static  final long id = 1L;
    private String plantid;
    private String name;
    private String helps;
    private String helpedby;
    private String avoid;
    private String attracts;
    private String repels;
    private String comment;
    private String photoURL;

    public JsonSymbiosis() {
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelps() {
        return helps;
    }

    public void setHelps(String helps) {
        this.helps = helps;
    }

    public String getHelpedby() {
        return helpedby;
    }

    public void setHelpedby(String helpedby) {
        this.helpedby = helpedby;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }

    public String getAttracts() {
        return attracts;
    }

    public void setAttracts(String attracts) {
        this.attracts = attracts;
    }

    public String getRepels() {
        return repels;
    }

    public void setRepels(String repels) {
        this.repels = repels;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
