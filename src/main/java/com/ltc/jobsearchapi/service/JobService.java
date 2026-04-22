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
}