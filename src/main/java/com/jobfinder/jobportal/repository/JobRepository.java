package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}

