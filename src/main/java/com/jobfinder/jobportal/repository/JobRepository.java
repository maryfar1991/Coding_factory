package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // Αν θέλεις custom queries, τα προσθέτεις εδώ
}


