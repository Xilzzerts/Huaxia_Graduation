package com.zirconi.entity;

import java.util.List;

public class DomEntity {
	boolean is_server_full;
	StatusEntity statusEntity;
	StuInfoEntity stuinfoEntity;
	List<TimetableEntity> timetableEntity;
	List<TranscriptEntity> transcriptEntity;

	public DomEntity() {
		this.is_server_full = true;
		this.statusEntity = null;
		this.stuinfoEntity = null;
		this.timetableEntity = null;
		this.transcriptEntity = null;
	}

	public boolean getIs_server_full() {
		return is_server_full;
	}

	public void setIs_server_full(boolean is_server_full) {
		this.is_server_full = is_server_full;
	}

	public StatusEntity getStatusEntity() {
		return statusEntity;
	}

	public void setStatusEntity(StatusEntity statusEntity) {
		this.statusEntity = statusEntity;
	}

	public StuInfoEntity getStuinfoEntity() {
		return stuinfoEntity;
	}

	public void setStuinfoEntity(StuInfoEntity stuinfoEntity) {
		this.stuinfoEntity = stuinfoEntity;
	}

	public List<TimetableEntity> getTimetableEntity() {
		return timetableEntity;
	}

	public void setTimetableEntity(List<TimetableEntity> timetableEntity) {
		this.timetableEntity = timetableEntity;
	}

	public List<TranscriptEntity> getTranscriptEntity() {
		return transcriptEntity;
	}

	public void setTranscriptEntity(List<TranscriptEntity> transcriptEntity) {
		this.transcriptEntity = transcriptEntity;
	}

}
