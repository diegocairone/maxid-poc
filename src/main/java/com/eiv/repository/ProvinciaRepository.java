package com.eiv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eiv.entity.PaisEntity;
import com.eiv.entity.ProvinciaEntity;
import com.eiv.entity.ProvinciaPkEntity;

public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity, ProvinciaPkEntity> {

    @Query("SELECT MAX(e.id) FROM ProvinciaEntity e WHERE e.pais = :pais")
    public Optional<Long> getMax(PaisEntity pais);
}
