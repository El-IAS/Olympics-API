package com.olympics.site.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteRelationship {
    private String site1;
    private String site2;
    private Double distance;
    private Double travelTimeByCar;
    private Double travelTimeByTransport;
}
