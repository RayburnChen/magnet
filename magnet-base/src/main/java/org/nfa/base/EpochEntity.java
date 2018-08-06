package org.nfa.base;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

public class EpochEntity extends BaseEntity {

	private static final long serialVersionUID = 4901029049264741126L;

	@Field
	@Indexed
	private long epochSecond;
	@Field
	@Indexed
	private long epochDay;
	@Field
	private String dateTime;// 2011-12-03T10:15:30

	public long getEpochSecond() {
		return epochSecond;
	}

	public void setEpochSecond(long epochSecond) {
		this.epochSecond = epochSecond;
	}

	public long getEpochDay() {
		return epochDay;
	}

	public void setEpochDay(long epochDay) {
		this.epochDay = epochDay;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
