package com.zirconi.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


import android.util.Log;

import com.zirconi.entity.DomEntity;
import com.zirconi.entity.StatusEntity;
import com.zirconi.entity.StuInfoEntity;
import com.zirconi.entity.TimetableEntity;
import com.zirconi.entity.TranscriptEntity;

public class XMLParser {

	public static DomEntity ParseXML(String body) {
		if (body == null || body.equals("")) {
			Log.w("XMLParser", "body is empty!");
			return null;
		}
		DomEntity dom = new DomEntity();
		StatusEntity statusEntity = new StatusEntity();
		StuInfoEntity stuinfoEntity = new StuInfoEntity();
		List<TimetableEntity> ltime = new ArrayList<TimetableEntity>();
		List<TranscriptEntity> ltrans = new ArrayList<TranscriptEntity>();

		// SAXReader sax = new SAXReader();
		try {
			// Document document = sax.read(body);
			Document document = DocumentHelper.parseText(body);
			Element root = document.getRootElement();
			if (root == null) {
				Log.w("XMLParser", "root is null!");
				return null;
			}
			Element serverstatus = root.element("serverstatus");
			if (serverstatus == null) {
				Log.w("XMLParser", "serverstatus is null!");
				return null;
			}
			String sstatus = serverstatus.attributeValue("value");
			Log.d("XMLParser", sstatus);

			if (sstatus.equalsIgnoreCase("ok")) {
				dom.setIs_server_full(false);
			}
			Element estatus = root.element("status");
			if (estatus == null) {
				Log.d("XMLParser", "estatus is null");
				return dom;
			}
			// maybe need to check has those attributes?
			if (estatus.attributeValue("timeout").equalsIgnoreCase("false")) {
				statusEntity.setIs_timeout(false);
			}
			if (estatus.attributeValue("password").equalsIgnoreCase("false")) {
				statusEntity.setIs_wrong_password(false);
			}
			if (estatus.attributeValue("syserror").equalsIgnoreCase("false")) {
				statusEntity.setIs_syserror(false);
			}
			if (estatus.attributeValue("stuinfo").equalsIgnoreCase("true")) {
				statusEntity.setIs_stuinfo_ok(true);
			}
			if (estatus.attributeValue("stuinfo").equalsIgnoreCase("true")) {
				statusEntity.setIs_stuinfo_ok(true);
			}
			if (estatus.attributeValue("timetable").equalsIgnoreCase("true")) {
				statusEntity.setIs_timetable_ok(true);
			}
			if (estatus.attributeValue("transcript").equalsIgnoreCase("true")) {
				statusEntity.setIs_transcript_ok(true);
			}
			dom.setStatusEntity(statusEntity);

			// student information
			Element estuinfo = root.element("stuinfo");
			if (estuinfo == null) {
				Log.d("XMLParser", "estuinfo is null");
				return dom;
			}
			stuinfoEntity.setStr_class(estuinfo.attributeValue("class"));
			stuinfoEntity.setStr_id(estuinfo.attributeValue("id"));
			stuinfoEntity.setStr_major(estuinfo.attributeValue("major"));
			stuinfoEntity.setStr_name(estuinfo.attributeValue("name"));
			dom.setStuinfoEntity(stuinfoEntity);

			// timetable
			Element etimetable = root.element("timetable_root");
			if (etimetable == null) {
				Log.d("XMLParser", "etimetable is null");
				return dom;
			}

			List<Element> let = etimetable.elements("timetable");
			for (Element x : let) {
				TimetableEntity temp = new TimetableEntity();
				temp.setStr_classname(x.attributeValue("classname"));
				temp.setStr_classroom(x.attributeValue("classroom"));
				temp.setStr_teacher(x.attributeValue("teacher"));
				temp.setStr_times(x.attributeValue("times"));
				ltime.add(temp);
			}
			dom.setTimetableEntity(ltime);

			// Transcript
			Element etranscript = root.element("transcript_root");
			if (etranscript == null) {
				Log.d("XMLParser", "etranscript is null");
				return dom;
			}

			List<Element> ltr = etranscript.elements("transcript");
			for (Element x : ltr) {
				TranscriptEntity temp = new TranscriptEntity();
				temp.setAssessment_methods(x
						.attributeValue("assessment_methods"));
				temp.setCourse_name(x.attributeValue("course_name"));
				temp.setCourse_teacher(x.attributeValue("course_teacher"));
				temp.setCourse_type(x.attributeValue("course_type"));
				temp.setCredit(x.attributeValue("credit"));
				temp.setGrade_point(x.attributeValue("grade_point"));
				temp.setMakeup_mark(x.attributeValue("makeup_mark"));
				temp.setRebuild_mark(x.attributeValue("rebuild_mark"));
				temp.setSemester(x.attributeValue("semester"));
				temp.setStr_academic_year(x.attributeValue("academic_year"));
				temp.setTotal_mark(x.attributeValue("total_mark"));
				ltrans.add(temp);
			}
			dom.setTranscriptEntity(ltrans);

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dom;
	}
}
