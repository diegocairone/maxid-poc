package com.eiv.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProvinciaPkEntity implements Serializable {

    private static final long serialVersionUID = -1840807291170697822L;

    @Column(name = "pais_id")
    private long paisId;
    
    @Column(name = "provincia_id")
    private long provinciaId;
    
    public ProvinciaPkEntity() {
    }

    public ProvinciaPkEntity(long paisId, long provinciaId) {
        super();
        this.paisId = paisId;
        this.provinciaId = provinciaId;
    }

    public long getPaisId() {
        return paisId;
    }

    public void setPaisId(long paisId) {
        this.paisId = paisId;
    }

    public long getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(long provinciaId) {
        this.provinciaId = provinciaId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (paisId ^ (paisId >>> 32));
        result = prime * result + (int) (provinciaId ^ (provinciaId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProvinciaPkEntity other = (ProvinciaPkEntity) obj;
        if (paisId != other.paisId) {
            return false;
        }
        if (provinciaId != other.provinciaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProvinciaPkEntity [paisId=" + paisId + ", provinciaId=" + provinciaId + "]";
    }
    
}
