package com.jobfinder.jobportal.controller;

import com.jobfinder.jobportal.entity.Job;
import com.jobfinder.jobportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyJobController {

    private final JobService jobService;

    public CompanyJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<?> createJob(@RequestBody Job job, Authentication auth) {
        String email = auth.getName();
        System.out.println("📨 Job creation request from user: " + email);

        Job savedJob = jobService.createJob(job);
        return ResponseEntity.ok(savedJob);
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}


