package com.vcinsidedigital.nowpoupe.model;

import java.io.Serializable;

public class Emprestimo implements Serializable
{
    private Long id;
    private String cliente;
    private double valor;
    private double taxa;
    private String dataEntrada;

    private String dataSaida;
    private double totalAPagar;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String data) {
        this.dataEntrada = data;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public double getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }
}
