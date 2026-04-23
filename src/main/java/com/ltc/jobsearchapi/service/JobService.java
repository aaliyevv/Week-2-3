package com.ltc.jobsearchapi.service;

import com.ltc.jobsearchapi.dto.JobResponseDTO;
import com.ltc.jobsearchapi.exception.BadRequestException;
import com.ltc.jobsearchapi.model.Job;
import com.ltc.jobsearchapi.model.JobType;
import com.ltc.jobsearchapi.repo.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (size > 50) {
            throw new BadRequestException("Page size cannot be greater than 50");
        }

        if (page < 0) {
            throw new BadRequestException("Page cannot be negative");
        }

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

    public List<JobResponseDTO> getSuggestions(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Keyword must not be empty");
        }

        Pageable pageable = PageRequest.of(0, 5); // limit suggestions

        List<Job> jobs = jobRepository.findSuggestions(keyword, pageable);

        return jobs.stream()
                .map(this::mapToDTO)
                .toList();
    }
}