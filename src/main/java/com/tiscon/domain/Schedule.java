package com.tiscon.domain;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String scheduleId;

    private String scheduleValue;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleValue() {
        return scheduleValue;
    }

    public void setScheduleValue(String scheduleValue) {
        this.scheduleValue = scheduleValue;
    }
}
