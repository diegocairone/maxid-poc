package com.eiv.ctrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eiv.das.PaisDas;
import com.eiv.entity.PaisEntity;
import com.eiv.frm.PaisFrmDto;

@RestController
@RequestMapping("/paises")
public class PaisCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(PaisCtrl.class);
    
    @Autowired private PaisDas paisDas;
    
    @Autowired private PlatformTransactionManager tm;
    
    @GetMapping("/")
    public ResponseEntity<List<PaisEntity>> listar() {
        List<PaisEntity> paisEntities = paisDas.findAll();
        return ResponseEntity.ok(paisEntities);
    }
    
    @PostMapping("/")
    public ResponseEntity<PaisEntity> nuevo(
            @RequestBody PaisFrmDto pais, 
            @RequestHeader(value = "x-delay", required = false, defaultValue = "0") long delay) {
        LOG.info("Header X-DELAY: {}", delay);
        
        TransactionTemplate template = new TransactionTemplate(tm);
        
        PaisEntity paisEntity = template.execute(new TransactionCallback<PaisEntity>() {

            @Override
            public PaisEntity doInTransaction(TransactionStatus status) {
                
                PaisEntity paisEntity = paisDas.create(pais, delay);
                status.setRollbackOnly();
                
                return paisEntity;
            }
        });
        
        return ResponseEntity.ok(paisEntity);
    }
}
