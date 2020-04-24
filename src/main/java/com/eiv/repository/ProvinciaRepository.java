package com.eiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eiv.entity.ProvinciaEntity;
import com.eiv.entity.ProvinciaPkEntity;

public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity, ProvinciaPkEntity> {

}
