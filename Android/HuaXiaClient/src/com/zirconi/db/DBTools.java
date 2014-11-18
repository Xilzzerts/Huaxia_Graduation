package com.zirconi.db;

import java.util.ArrayList;
import java.util.List;

import com.zirconi.entity.DomEntity;
import com.zirconi.entity.StatusEntity;
import com.zirconi.entity.StuInfoEntity;
import com.zirconi.entity.TimetableEntity;
import com.zirconi.entity.TranscriptEntity;
import com.zirconi.etc.GlobalApplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBTools {
	public static void SaveDomToDB(SQLiteOpenHelper s, DomEntity d) {
		delAll(s);
		SQLiteDatabase db = s.getWritableDatabase();

		if (d == null)
			return;
		ContentValues values = new ContentValues();
		values.put("class", d.getStuinfoEntity().getStr_class());
		values.put("id", d.getStuinfoEntity().getStr_id());
		values.put("major", d.getStuinfoEntity().getStr_major());
		values.put("name", d.getStuinfoEntity().getStr_name());
		Log.d("DBTools", "INSERT");
		db.insert("STUINFO", "key", values);
		// timetable

		values.clear();
		for (TimetableEntity x : d.getTimetableEntity()) {
			values.put("classname", x.getStr_classname());
			values.put("classroom", x.getStr_classroom());
			values.put("teacher", x.getStr_teacher());
			values.put("times", x.getStr_times());
			db.insert("TIMETABLE", "key", values);
			values.clear();
		}
		// transcript
		for (TranscriptEntity t : d.getTranscriptEntity()) {
			values.put("academic_year", t.getStr_academic_year());
			values.put("assessment_methods", t.getAssessment_methods());
			values.put("course_name", t.getCourse_name());
			values.put("course_teacher", t.getCourse_teacher());
			values.put("course_type", t.getCourse_type());
			values.put("credit", t.getCredit());
			values.put("grade_point", t.getGrade_point());
			values.put("makeup_mark", t.getMakeup_mark());
			values.put("rebuild_mark", t.getRebuild_mark());
			values.put("semester", t.getSemester());
			values.put("total_mark", t.getTotal_mark());
			db.insert("TRANSCRIPT", "key", values);
			values.clear();
		}
		db.close();
	}

	public static Cursor GetCursor(SQLiteOpenHelper s, String table) {
		SQLiteDatabase db = s.getReadableDatabase();
		return db.query(table, null, null, null, null, null, "key ASC");
	}

	public static DomEntity GetDom(SQLiteOpenHelper s) {
		SQLiteDatabase db = s.getReadableDatabase();
		DomEntity dom = new DomEntity();
		StuInfoEntity stuinfoEntity = new StuInfoEntity();
		List<TimetableEntity> ltime = new ArrayList<TimetableEntity>();
		List<TranscriptEntity> ltrans = new ArrayList<TranscriptEntity>();

		Cursor cursorA = db.query("STUINFO", null, null, null, null, null,
				"key ASC");
		if (cursorA == null)
			return null;
		// read from db, so no status
		dom.setStatusEntity(null);

		if (cursorA.moveToFirst()) {
			stuinfoEntity.setStr_class(cursorA.getString(cursorA
					.getColumnIndex("class")));
			stuinfoEntity.setStr_id(cursorA.getString(cursorA
					.getColumnIndex("ID")));
			stuinfoEntity.setStr_major(cursorA.getString(cursorA
					.getColumnIndex("major")));
			stuinfoEntity.setStr_name(cursorA.getString(cursorA
					.getColumnIndex("name")));
			dom.setStuinfoEntity(stuinfoEntity);
		}
		cursorA = db
				.query("TIMETABLE", null, null, null, null, null, "key ASC");
		while (cursorA.moveToNext()) {
			TimetableEntity temp = new TimetableEntity();
			temp.setStr_classname(cursorA.getString(cursorA
					.getColumnIndex("classname")));
			temp.setStr_classroom(cursorA.getString(cursorA
					.getColumnIndex("classroom")));
			temp.setStr_teacher(cursorA.getString(cursorA
					.getColumnIndex("teacher")));
			temp.setStr_times(cursorA.getString(cursorA.getColumnIndex("times")));
			ltime.add(temp);
		}
		dom.setTimetableEntity(ltime);
		cursorA = db.query("TRANSCRIPT", null, null, null, null, null,
				"key ASC");
		while (cursorA.moveToNext()) {
			TranscriptEntity t = new TranscriptEntity();
			t.setStr_academic_year(cursorA.getString(cursorA
					.getColumnIndex("academic_year")));
			t.setAssessment_methods(cursorA.getString(cursorA
					.getColumnIndex("assessment_methods")));
			t.setCourse_name(cursorA.getString(cursorA
					.getColumnIndex("course_name")));
			t.setCourse_teacher(cursorA.getString(cursorA
					.getColumnIndex("course_teacher")));
			t.setCourse_type(cursorA.getString(cursorA
					.getColumnIndex("course_type")));
			t.setCredit(cursorA.getString(cursorA.getColumnIndex("credit")));
			t.setGrade_point(cursorA.getString(cursorA
					.getColumnIndex("grade_point")));
			t.setMakeup_mark(cursorA.getString(cursorA
					.getColumnIndex("makeup_mark")));
			t.setRebuild_mark(cursorA.getString(cursorA
					.getColumnIndex("rebuild_mark")));
			t.setSemester(cursorA.getString(cursorA.getColumnIndex("semester")));
			t.setTotal_mark(cursorA.getString(cursorA
					.getColumnIndex("total_mark")));
			ltrans.add(t);
		}
		dom.setTranscriptEntity(ltrans);
		cursorA.close();
		db.close();
		return dom;
	}

	public static void delAll(SQLiteOpenHelper s) {
		SQLiteDatabase db = s.getWritableDatabase();
		db.delete("STUINFO", "1", null);
		db.delete("TIMETABLE", "1", null);
		db.delete("TRANSCRIPT", "1", null);
		db.close();
	}
}
