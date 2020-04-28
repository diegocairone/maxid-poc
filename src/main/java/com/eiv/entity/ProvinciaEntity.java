package com.eiv.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.eiv.util.HashKeyGenerator;

@Entity
@Table(name = "provincias")
public class ProvinciaEntity {

    @EmbeddedId
    @GeneratedValue(generator = "provincia-generator")
    @GenericGenerator(
            name = "provincia-generator", 
            strategy = "com.eiv.util.HashKeyGenerator",
            parameters = { 
                    @Parameter(name = HashKeyGenerator.COMPOSITE_KEY, value = "true"),
                    @Parameter(name = HashKeyGenerator.ID_FIELD, value = "provinciaId"),
                    @Parameter(name = HashKeyGenerator.USE_HASH, value = "true")})
    private ProvinciaPkEntity pk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "pais_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PaisEntity pais;
    
    @Column(name = "provincia_id", insertable = false, updatable = false)
    private Long id;
    
    private String nombre;
    
    public ProvinciaEntity() {
        this.pk = new ProvinciaPkEntity();
    }

    public ProvinciaEntity(PaisEntity pais, Long id) {
        super();
        this.pais = pais;
        this.id = id;
        this.pk = new ProvinciaPkEntity(pais.getId(), id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.pk.setProvinciaId(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PaisEntity getPais() {
        return pais;
    }

    public void setPais(PaisEntity pais) {
        this.pais = pais;
        this.pk.setPaisId(pais.getId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
        ProvinciaEntity other = (ProvinciaEntity) obj;
        if (pk == null) {
            if (other.pk != null) {
                return false;
            }
        } else if (!pk.equals(other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProvinciaEntity [pk=" + pk + "]";
    }    
}
