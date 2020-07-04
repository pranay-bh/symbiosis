package com.solvedunsolved.plantsymbiosis.Model;

public class Plantpair {
    private String name1;
    private String name2;
    private String category;
    private String relationship;
    private byte[] img1;
    private byte[] img2;
    private  int id;

    public Plantpair() {
    }

    public Plantpair(String name1, String name2, String category, String relationship, byte[] img1,byte[] img2, int id) {
        this.name1 = name1;
        this.name2 = name2;
        this.category = category;
        this.relationship = relationship;
        this.img1 = img1;
        this.img2 = img2;
        this.id = id;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public byte[] getImg1() {
        return img1;
    }

    public void setImg1(byte[] img1) {
        this.img1 = img1;
    }

    public byte[] getImg2() {
        return img2;
    }

    public void setImg2(byte[] img2) {
        this.img2 = img2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
