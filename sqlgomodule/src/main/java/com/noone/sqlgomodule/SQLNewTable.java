package com.noone.sqlgomodule;

public class SQLNewTable {
    private String name;
    private int sizes;

    public SQLNewTable(String name, int sizes) {
        this.name = name;
        this.sizes = sizes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSizes() {
        return sizes;
    }

    public void setSizes(int sizes) {
        this.sizes = sizes;
    }
}
