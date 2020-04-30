package com.eiv.frm;

public class PaisFrmDto implements PaisFrm {

    private long id;
    private String nombre;
    
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null || nombre.trim().isEmpty() 
                ? null : nombre.trim().toUpperCase();
    }
}
