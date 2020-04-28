package com.eiv.das;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eiv.entity.PaisEntity;
import com.eiv.entity.ProvinciaEntity;
import com.eiv.frm.ProvinciaFrm;
import com.eiv.repository.PaisRepository;
import com.eiv.repository.ProvinciaRepository;

@Service
public class ProvinciaDas {

    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaDas.class);

    @Autowired private PaisRepository paisRepository;
    @Autowired private ProvinciaRepository provinciaRepository;
    
    @Transactional(readOnly = true)
    public List<ProvinciaEntity> findAll() {
        return provinciaRepository.findAll();
    }
    
    @Transactional
    public ProvinciaEntity create(ProvinciaFrm pais) {
        return create(pais, 0);
    }
    
    @Transactional
    public ProvinciaEntity create(ProvinciaFrm provincia, long delay) {

        PaisEntity paisEntity = paisRepository.findById(provincia.getPaisId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("No se encuentra un PAIS con ID %s", provincia.getPaisId())));
        
        if (delay > 0) {
            try {
                LOG.info("X-DELAY {} - Comenzando ....", delay);
                TimeUnit.SECONDS.sleep(delay);
                LOG.info("X-DELAY {} - Terminado!!!", delay);
            } catch (InterruptedException ex) {
                LOG.error("DELAY ERROR: {}", ex);
            } 
        }
           
        ProvinciaEntity provinciaEntity = new ProvinciaEntity();
        provinciaEntity.setPais(paisEntity);
        provinciaEntity.setNombre(provincia.getNombre());
        
        return provinciaRepository.save(provinciaEntity);
    }
}
