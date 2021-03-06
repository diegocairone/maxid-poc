package com.eiv.das;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eiv.entity.PaisEntity;
import com.eiv.frm.PaisFrm;
import com.eiv.repository.PaisRepository;

@Service
public class PaisDas {

    private static final Logger LOG = LoggerFactory.getLogger(PaisDas.class);
    
    @Autowired private PaisRepository paisRepository;
    
    @Transactional(readOnly = true)
    public List<PaisEntity> findAll() {
        return paisRepository.findAll();
    }
    
    @Transactional
    public PaisEntity create(PaisFrm pais) {
        return create(pais, 0);
    }
    
    /**
     * mysql usa por defecto el isolation lever REPEATABLE READ y h2 usa READ COMMITED
     * 
     * @see <a href="https://medium.com/@elliotchance/sql-transaction-isolation-levels-explained-50d1a2f90d8f">SQL Transaction Isolation Levels Explained</a>
     * 
     * @param pais
     * @param delay
     * @return
     */
    @Transactional
    public PaisEntity create(PaisFrm pais, long delay) {
        
        //long id = paisRepository.getMax().orElse(0L) + 1L;
        //LOG.info("MAX ID CALCULADO: {} PARA {}", id, pais.getNombre());
        
        if (delay > 0) {
            try {
                LOG.info("X-DELAY {} - Comenzando ....", delay);
                TimeUnit.SECONDS.sleep(delay);
                LOG.info("X-DELAY {} - Terminado!!!", delay);
            } catch (InterruptedException ex) {
                LOG.error("DELAY ERROR: {}", ex);
            } 
        }
                
        PaisEntity paisEntity = new PaisEntity();
        paisEntity.setNombre(pais.getNombre());
        
        long id = paisRepository.getMax().orElse(0L) + 1L;
        paisEntity.setId(id);
        LOG.info("MAX ID CALCULADO: {} PARA {}", id, pais.getNombre());
        
        return paisRepository.save(paisEntity);
    }
}
