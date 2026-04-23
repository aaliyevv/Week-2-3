package com.ltc.jobsearchapi.repo;

import com.ltc.jobsearchapi.model.Job;
import com.ltc.jobsearchapi.model.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
        SELECT j FROM Job j
        WHERE 
        (:keyword IS NULL OR 
            LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (:location IS NULL OR LOWER(j.location) = LOWER(:location))
        AND (:jobType IS NULL OR j.jobType = :jobType)
    """)    // use lower for case-insensitive

    Page<Job> searchJobs(
            // use parameter to prevent injection
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") JobType jobType,
            Pageable pageable
    );

    @Query("""
    SELECT j FROM Job j
    WHERE 
        LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    ORDER BY j.createdAt DESC
""")
    List<Job> findSuggestions(@Param("keyword") String keyword, Pageable pageable);

}