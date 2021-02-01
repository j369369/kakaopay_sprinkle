package com.kakaopay.sprinkle.domain.sprinkle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SprinkleRepository extends JpaRepository<Sprinkle,Long> {

    Optional<Sprinkle> findByTokenAndCreatedAtGreaterThan(String token, LocalDateTime createdAt);

}
