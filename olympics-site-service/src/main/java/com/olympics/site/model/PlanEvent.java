package com.olympics.site.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public
class PlanEvent {
    private String site;
    private String sport;
    private String eventName;
    private String startTime;
    private String endTime;
}