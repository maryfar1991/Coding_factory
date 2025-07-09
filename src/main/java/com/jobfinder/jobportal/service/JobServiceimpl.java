package com.jobfinder.jobportal.service;

import com.jobfinder.jobportal.entity.Job;
import com.jobfinder.jobportal.repository.JobRepository;
import com.jobfinder.jobportal.service.JobService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceimpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceimpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public Job createJob(Job job) {
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, Job job) {
        Optional<Job> existingJob = jobRepository.findById(id);
        if (existingJob.isPresent()) {
            Job updatedJob = existingJob.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setDescription(job.getDescription());
            updatedJob.setLocation(job.getLocation());
            updatedJob.setSalaryRange(job.getSalaryRange());
            updatedJob.setUpdatedAt(LocalDateTime.now());
            updatedJob.setCompany(job.getCompany());
            return jobRepository.save(updatedJob);
        } else {
            throw new RuntimeException("Job not found with id " + id);
        }
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}

