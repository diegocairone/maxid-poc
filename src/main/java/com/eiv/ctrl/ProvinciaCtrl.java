package com.eiv.ctrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eiv.das.ProvinciaDas;
import com.eiv.entity.ProvinciaEntity;
import com.eiv.frm.ProvinciaFrmDto;

@RestController
@RequestMapping("/provincias")
public class ProvinciaCtrl {
private static final Logger LOG = LoggerFactory.getLogger(ProvinciaCtrl.class);
    
    @Autowired private ProvinciaDas provinciaDas;
    
    @GetMapping("/")
    public ResponseEntity<List<ProvinciaEntity>> listar() {
        List<ProvinciaEntity> provinciaEntities = provinciaDas.findAll();
        return ResponseEntity.ok(provinciaEntities);
    }
    
    @PostMapping("/")
    public ResponseEntity<ProvinciaEntity> nuevo(
            @RequestBody ProvinciaFrmDto provincia, 
            @RequestHeader(value = "x-delay", required = false, defaultValue = "0") long delay) {
        LOG.info("Header X-DELAY: {}", delay);
        ProvinciaEntity provinciaEntity = provinciaDas.create(provincia, delay);
        return ResponseEntity.ok(provinciaEntity);
    }
}
