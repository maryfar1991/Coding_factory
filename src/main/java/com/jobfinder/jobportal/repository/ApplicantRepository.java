package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}

