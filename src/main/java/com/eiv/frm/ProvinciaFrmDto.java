package com.eiv.frm;

public class ProvinciaFrmDto implements ProvinciaFrm {

    private long paisId;
    
    private String nombre;
    
    public ProvinciaFrmDto() {
    }
    
    public void setPaisId(long paisId) {
        this.paisId = paisId;
    }

    @Override
    public long getPaisId() {
        return paisId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

}
