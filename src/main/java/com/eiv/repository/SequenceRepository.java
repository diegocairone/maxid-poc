package com.eiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eiv.entity.SequenceEntity;

public interface SequenceRepository extends JpaRepository<SequenceEntity, String> {

}
