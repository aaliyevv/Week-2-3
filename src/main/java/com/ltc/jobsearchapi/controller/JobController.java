package com.ltc.jobsearchapi.controller;

import com.ltc.jobsearchapi.dto.JobResponseDTO;
import com.ltc.jobsearchapi.model.JobType;
import com.ltc.jobsearchapi.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public Page<JobResponseDTO> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return jobService.searchJobs(keyword, location, jobType, page, size);
    }

    // required = false: the request not included we get an error
}