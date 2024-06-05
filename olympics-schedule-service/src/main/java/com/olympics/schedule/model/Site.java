package com.olympics.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Site {

    private Long id;

    private String nom;

    private String infoGeographique;

    private boolean siteParalympique;
}
