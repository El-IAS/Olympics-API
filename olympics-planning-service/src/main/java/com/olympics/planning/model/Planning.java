package com.olympics.planning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "plannings")
public class Planning {
    @Id
    private String id;
    private String username; // Spectator or journalist
    private List<PlanEvent> events;
}
