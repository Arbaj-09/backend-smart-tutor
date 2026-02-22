package com.smarttutor.repository;

import com.smarttutor.entity.Hod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HodRepository extends JpaRepository<Hod, Long> {
    Optional<Hod> findByEmail(String email);
    boolean existsByEmail(String email);
}
