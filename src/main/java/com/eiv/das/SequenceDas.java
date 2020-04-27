package com.eiv.das;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eiv.entity.SequenceEntity;
import com.eiv.repository.SequenceRepository;

@Service
public class SequenceDas {

    @Autowired private SequenceRepository sequenceRepository;
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long nextValue(String id) {
        
        SequenceEntity sequenceEntity = sequenceRepository.findById(id)
                .orElse(new SequenceEntity(id, 0L));
        
        sequenceEntity.setValor(sequenceEntity.getValor() + 1L);
        sequenceRepository.save(sequenceEntity);
        
        return sequenceEntity.getValor();
    }
}
