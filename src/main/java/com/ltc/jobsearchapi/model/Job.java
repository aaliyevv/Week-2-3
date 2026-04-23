package com.ltc.jobsearchapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_location", columnList = "location"),
        @Index(name = "idx_job_type", columnList = "jobType"),
        @Index(name = "idx_title", columnList = "title")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDateTime createdAt;

    // With @Builder: no parameter order bugs, constructor doesn't force
    // you write null, just skip them
}