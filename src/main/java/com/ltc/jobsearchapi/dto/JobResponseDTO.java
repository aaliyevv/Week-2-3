package com.ltc.jobsearchapi.dto;

import com.ltc.jobsearchapi.model.JobType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JobResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private JobType jobType;
    private LocalDateTime createdAt;
}