package com.eiv.frm;

public class PaisFrmDto implements PaisFrm {

    private String nombre;
    
    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null || nombre.trim().isEmpty() 
                ? null : nombre.trim().toUpperCase();
    }
}
