package com.noone.sqlgo;

public class SQLGoTable<S> {
    private String keyName;
    private S data;

    public SQLGoTable(String keyName, S data) {
        this.keyName = keyName;
        this.data = data;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public S getData() {
        return data;
    }

    public void setData(S data) {
        this.data = data;
    }
}
