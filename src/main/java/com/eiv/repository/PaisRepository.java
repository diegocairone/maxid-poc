package com.eiv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eiv.entity.PaisEntity;

public interface PaisRepository extends JpaRepository<PaisEntity, Long> {

    @Query("SELECT MAX(e.id) FROM PaisEntity e")
    public Optional<Long> getMax();
}
