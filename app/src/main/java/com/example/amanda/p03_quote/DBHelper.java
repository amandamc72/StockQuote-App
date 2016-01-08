package com.example.amanda.p03_quote;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "myQuote.db";
	public static final String TABLE_NAME = "mySymbol";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(
				"CREATE TABLE mySymbol " +
						"(SYMBOL TEXT PRIMARY KEY NOT NULL)"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public boolean dbf_initRows() {
		if ( dbf_numberOfRows() == 0 ) {
			SQLiteDatabase db = this.getWritableDatabase();

			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('AAPL');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('AMZN');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('FB');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('GOOG');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('MSFT');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('INTC');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('AMD');");
			db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('QCOM');");

			db.close();
			return true;
		}
		else {
			return false;
		}
	}

	public int dbf_numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
		db.close();
		return numRows;
	}

	public String dbf_getLastRowSymbol () {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery("select * from " + TABLE_NAME, null);
		res.moveToLast();

		return res.getString(res.getColumnIndex("SYMBOL"));
	}

	public ArrayList<ListData> dbf_getAllRecords() {
		ArrayList<ListData> lv_symbolList = new ArrayList<ListData>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ListData data = new ListData();
				data.setSymbol(cursor.getString(0));
				lv_symbolList.add(data);
			} while (cursor.moveToNext());
		}
		db.close();

		return lv_symbolList;
	}

	public void dbf_deletePart(String symbol) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE SYMBOL = '" + symbol + "'");
		db.close();
	}

	public void dbf_appendSymbol(String symbol) {
		//String last = dbf_getLastRowSymbol();
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('" + (symbol) + "');");
		db.close();
	}
}
