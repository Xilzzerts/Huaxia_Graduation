package com.zirconi.entity;

public class StatusEntity {
	boolean is_wrong_password;
	boolean is_stuinfo_ok;
	boolean is_syserror;
	boolean is_transcript_ok;
	boolean is_timetable_ok;
	boolean is_timeout;

	public StatusEntity() {
		this.is_wrong_password = true;
		this.is_syserror = true;
		this.is_stuinfo_ok = false;
		this.is_timetable_ok = false;
		this.is_transcript_ok = false;
		this.is_timeout = true;
	}

	public boolean isIs_wrong_password() {
		return is_wrong_password;
	}

	public void setIs_wrong_password(boolean is_wrong_password) {
		this.is_wrong_password = is_wrong_password;
	}

	public boolean isIs_stuinfo_ok() {
		return is_stuinfo_ok;
	}

	public void setIs_stuinfo_ok(boolean is_stuinfo_ok) {
		this.is_stuinfo_ok = is_stuinfo_ok;
	}

	public boolean isIs_syserror() {
		return is_syserror;
	}

	public void setIs_syserror(boolean is_syserror) {
		this.is_syserror = is_syserror;
	}

	public boolean isIs_transcript_ok() {
		return is_transcript_ok;
	}

	public void setIs_transcript_ok(boolean is_transcript_ok) {
		this.is_transcript_ok = is_transcript_ok;
	}

	public boolean isIs_timetable_ok() {
		return is_timetable_ok;
	}

	public void setIs_timetable_ok(boolean is_timetable_ok) {
		this.is_timetable_ok = is_timetable_ok;
	}

	public boolean isIs_timeout() {
		return is_timeout;
	}

	public void setIs_timeout(boolean is_timeout) {
		this.is_timeout = is_timeout;
	}
}
