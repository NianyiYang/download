package com.yny.downloadlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.HashMap;
import java.util.Map;

public class FileService {
	private DownloadDBHelper openHelper;

	public FileService(Context context) {
		openHelper = new DownloadDBHelper(context);
	}

	@SuppressLint("UseSparseArrays") 
	public Map<Integer, Integer> getData(String path) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownload where downpath=?", new String[] { path });
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		while (cursor.moveToNext()) {
			data.put(cursor.getInt(0), cursor.getInt(1));
			data.put(cursor.getInt(cursor.getColumnIndexOrThrow("threadid")),cursor.getInt(cursor.getColumnIndexOrThrow("downlength")));
		}
		cursor.close();
		db.close();
		return data;
	}

	public void save(String path, Map<Integer, Integer> map) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
				db.execSQL("insert into filedownload(downpath, threadid, downlength) values(?,?,?)", new Object[] { path, entry.getKey(), entry.getValue() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}


	public void update_tyc(String path, int threadId, int pos) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("update filedownload set downlength=? where downpath=? and threadid=?", new Object[] { pos, path, threadId });
		db.close();
	}

	public void update(String path, Map<Integer, Integer> map) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
				db.execSQL("update filedownload set downlength=? where downpath=? and threadid=?", new Object[] { entry.getValue(), path, entry.getKey() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}

	public void delete(String path) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from filedownload where downpath=?", new Object[] { path });
		db.close();
	}
	
	public Boolean check() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Boolean isFinished = false;
		try {
			SQLiteStatement statement = db.compileStatement("select count(*) from filedownload");
		    long count = statement.simpleQueryForLong();
			if(count == 0)
				isFinished = true;
		} catch (Exception e) {
			e.printStackTrace();
			isFinished = false;
		} finally {
			db.close();
		}
		return isFinished;
	}
}
