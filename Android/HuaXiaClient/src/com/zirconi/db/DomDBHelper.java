package com.zirconi.db;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DomDBHelper extends SQLiteOpenHelper {

	static final String DB_NAME = "dom.db";
	static final int DB_VERSION = 1;

	public DomDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// three tables
		Log.d("DBHelper", "onCreate");
		String table1 = "CREATE TABLE STUINFO(key INTEGER PRIMARY KEY,"
				+ "class VARCHAR(30), " + "ID VARCHAR(20), "
				+ "major VARCHAR(50), " + "name VARCHAR(15))";
		// <timeable classname="土力学与基础工程I" classroom="2-407"
		// teacher="苏明会z[01-15]" times="2节/周"/>
		String table2 = "CREATE TABLE TIMETABLE(" + "key INTEGER PRIMARY KEY, "
				+ "classname VARCHAR(60), " + "classroom VARCHAR(10), "
				+ "teacher VARCHAR(20), " + "times VARCHAR(15))";
		// <transcript academic_year="2011-2012" assessment_methods="考查"
		// course_name="中国近现代史纲要" course_teacher="刘娟z" course_type="必修课"
		// credit="2" grade_point="3.9" makeup_mark="" rebuild_mark=""
		// semester="1" total_mark="89"/>
		String table3 = "CREATE TABLE TRANSCRIPT(key INTEGER PRIMARY KEY,"
				+ "academic_year VARCHAR(20),"
				+ "assessment_methods VARCHAR(8)," + "course_name VARCHAR(60),"
				+ "course_teacher VARCHAR(20), " + "course_type VARCHAR(20), "
				+ "credit VARCHAR(8), " + "grade_point VARCHAR(8),"
				+ " makeup_mark VARCHAR(8)," + "rebuild_mark VARCHAR(8),"
				+ "semester VARCHAR(8)," + "total_mark VARCHAR(8))";
		db.execSQL(table1);
		db.execSQL(table2);
		db.execSQL(table3);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS STUINFO");
		db.execSQL("DROP TABLE IF EXISTS TIMETABLE");
		db.execSQL("DROP TABLE IF EXISTS TRANSCRIPT");
		this.onCreate(db);
	}
}
