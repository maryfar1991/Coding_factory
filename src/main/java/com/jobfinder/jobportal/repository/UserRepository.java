package com.jobfinder.jobportal.repository;

import com.jobfinder.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Αν θέλεις custom queries, τα προσθέτεις εδώ π.χ. findByUsername
    User findByUsername(String username);
}

