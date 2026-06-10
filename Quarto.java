//Quarto.java
package model;

import java.io.Serializable;

public class Quarto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int numero;
    private String tipo; // Ex: Single, Double, Suite
    private double precoDiaria;
    private boolean ocupado;
    private String cpfCliente; // Mantém o CPF do cliente se estiver ocupado

    public Quarto(int numero, String tipo, double precoDiaria) {
        this.numero = numero;
        this.tipo = tipo;
        this.precoDiaria = precoDiaria;
        this.ocupado = false;
        this.cpfCliente = "";
    }

    // Getters e Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecoDiaria() { return precoDiaria; }
    public void setPrecoDiaria(double precoDiaria) { this.precoDiaria = precoDiaria; }

    public boolean isOcupado() { return ocupado; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }

    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }

    @Override
    public String toString() {
        return "Quarto Nº " + numero + " [" + tipo + "] - Preço/Noite: " + precoDiaria + "€ - Status: " + (ocupado ? "Ocupado por CPF: " + cpfCliente : "Disponível");
    }
}