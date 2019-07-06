package com.noone.sqlgo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLNew {
    private SQLNewN sqlNewN;

    public SQLNew() {

    }

    public SQLNew openHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        sqlNewN = new SQLNewN(context, dbName, factory, version);
        return this;
    }

    public SQLNew putTable(SQLNewTable... sqlNewTables) {
        sqlNewN.putTable(sqlNewTables);
        return this;
    }

    public void commit(String tableName) {
        sqlNewN.commit(tableName);
    }


    public SQLiteDatabase getWritableDatabase() {
        return sqlNewN.getWritableDatabase();
    }

    private class SQLNewN extends SQLiteOpenHelper {
        private List<SQLNewTable> list = new ArrayList<>();

        public SQLNewN(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, dbName, factory, version);
        }

        public void putTable(SQLNewTable... sqlNewTable) {
            for (SQLNewTable table : sqlNewTable) {
                list.add(table);
            }
        }

        public void commit(String tableName) {
            String sql = "create table " + tableName + "(id integer primary key autoincrement" + "";
            for (int i = 0; i < list.size(); i++) {
                sql += "," + list.get(i).getName() + " varchar(" + list.get(i).getSizes() + ")";
            }
            sql += ")";
            Log.i("建表", sql);
            try {
                getWritableDatabase().execSQL(sql);
            } catch (Exception e) {
                Log.e("建表错误", e.toString());
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table userLogIn(id integer primary key autoincrement,text varchar(10000),img varchar(100),addTime varchar(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
        }
    }
}