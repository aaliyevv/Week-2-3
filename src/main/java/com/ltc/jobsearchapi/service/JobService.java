package com.ltc.jobsearchapi.service;

import com.ltc.jobsearchapi.dto.JobResponseDTO;
import com.ltc.jobsearchapi.model.Job;
import com.ltc.jobsearchapi.model.JobType;
import com.ltc.jobsearchapi.repo.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Page<JobResponseDTO> searchJobs(
            String keyword,
            String location,
            JobType jobType,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Job> jobs = jobRepository.searchJobs(
                keyword,
                location,
                jobType,
                pageable
        );

        return jobs.map(this::mapToDTO);
    }

    private JobResponseDTO mapToDTO(Job job) {
        return JobResponseDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .createdAt(job.getCreatedAt())
                .build();
    }
}