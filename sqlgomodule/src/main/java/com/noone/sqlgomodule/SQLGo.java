package com.noone.sqlgomodule;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLGo<S> {
    private static final String TAG = "SQLGo";
    private Context context;
    private String dbName;
    private int VERSION = 1;
    private SQLNew sqlOpen;
    private SQLiteDatabase db;
    private List<SQLGoTable> setDataList = new ArrayList<>();

    public SQLGo() {
    }

    /**
     * @param context
     * @param dbName  数据库名
     */
    public SQLGo selectDataBase(Context context, String dbName) {
        this.context = context;
        this.dbName = dbName;
        sqlOpen = new SQLNew().openHelper(context, dbName, null, VERSION);
        db = sqlOpen.getWritableDatabase();
        return this;
    }

    /**
     * 存储方法最后要用setData方法执行
     *
     * @param sqlGoTable
     */
    public SQLGo put(SQLGoTable... sqlGoTable) {
        setDataList.addAll(Arrays.asList(sqlGoTable));
        return this;
    }

    /**
     * 数据存储方法
     *
     * @param tableName 表名
     */
    public void commit(String tableName) {
        for (int i = 0; i < setDataList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            SQLGoTable table = setDataList.get(i);
            if (table.getData() instanceof String) {
                contentValues.put(table.getKeyName(), (String) table.getData());
            } else {
                if (table.getData() instanceof Intent) {
                    contentValues.put(table.getKeyName(), (Integer) table.getData());
                } else {
                    if (table.getData() instanceof Double)
                        contentValues.put(table.getKeyName(), (Double) table.getData());
                }
            }
            float ic = db.insert(tableName, null, contentValues);
            if (ic == -1) {
                Log.i(TAG, "commit: 暂时发现这里会抛出没有键值的异常");
            }
        }
    }

    /**
     * 获取一个数据
     *
     * @param tableName     表名
     * @param keyName       键值
     * @param sqlType       键值属性
     * @param selection     约束
     * @param selectionArgs 约束值
     * @return
     */
    public Object getDataOne(String tableName, String keyName, SQLType sqlType, String selection, String[] selectionArgs) {
        Cursor cursor = db.query(tableName, new String[]{keyName}, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        switch (sqlType) {
            case INTEGER:
                return cursor.getInt(cursor.getColumnIndex(keyName));
            case DOUBLE:
                return cursor.getDouble(cursor.getColumnIndex(keyName));
            case STRING:
                return cursor.getString(cursor.getColumnIndex(keyName));
            default:
                return null;
        }
    }

    /**
     * 获取一串数据
     *
     * @param tableName     表名
     * @param keyName       键值
     * @param sqlType       键值属性
     * @param selection     约束
     * @param selectionArgs 约束值
     * @return
     */
    public List<Object> getDataList(String tableName, String keyName, SQLType sqlType, String selection, String[] selectionArgs) {
        List<Object> list = new ArrayList<>();
        Cursor cursor = db.query(tableName, new String[]{keyName}, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                switch (sqlType) {
                    case INTEGER:
                        list.add(cursor.getInt(cursor.getColumnIndex(keyName)));
                    case DOUBLE:
                        list.add(cursor.getDouble(cursor.getColumnIndex(keyName)));
                    case STRING:
                        list.add(cursor.getString(cursor.getColumnIndex(keyName)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 更新数据
     *
     * @param tableName     表名
     * @param keyName       键值
     * @param data          键值数据
     * @param selection     更改时的约束
     * @param selectionArgs 约束值
     */
    public void upData(String tableName, String keyName, String upKeyName, S data, String selection, String[] selectionArgs) {
        Cursor cursor = db.query(tableName, new String[]{keyName}, null, null, null, null, null);
        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()) {
            do {
                if (data != null) {
                    if (data instanceof String)
                        values.put(upKeyName, (String) data);
                    else if (data instanceof Intent)
                        values.put(upKeyName, (Integer) data);
                    else if (data instanceof Double)
                        values.put(upKeyName, (Double) data);
                }
                db.update(tableName, values, selection, selectionArgs);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * 删除方法
     *
     * @param tableName     表名
     * @param keyName       键值
     * @param selection     约束
     * @param selectionArgs 约束值
     */
    public void deleteData(String tableName, String keyName, String selection, String[] selectionArgs) {
        Cursor cursor = db.query(tableName, new String[]{keyName}, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                db.delete(tableName, selection, selectionArgs);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public void dropTable(String tableName) {
        db.execSQL("drop table " + tableName);
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public void deleteTable(String tableName) {
        db.execSQL("delete from " + tableName);
    }

    /**
     * 可支持据类型的枚举
     */
    public enum SQLType {
        INTEGER, DOUBLE, STRING
    }
}