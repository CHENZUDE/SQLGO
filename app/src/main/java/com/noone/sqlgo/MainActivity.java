package com.noone.sqlgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SQLNew().openHelper(this, "my.db", null, 1)
                .putTable(new SQLNewTable("asd", 20),
                        new SQLNewTable("qwe", 20),
                        new SQLNewTable("zxc", 20))
                .commit("user");

        new SQLGo<>().selectDataBase(this, "my.db")
                .put(new SQLGoTable("asd", "123")
                        , new SQLGoTable("qwe", "456")
                        , new SQLGoTable("zxc", "789"))
                .commit("user");
    }

    private static final String TAG = "MainActivity";
}
