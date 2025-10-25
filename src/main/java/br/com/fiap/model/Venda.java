package br.com.fiap.model;

import java.time.Instant;

public class Venda {
    private int id;
    private int idVarejo;
    private String produto;
    Instant dataHora;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVarejo() {
        return idVarejo;
    }

    public void setIdVarejo(int idVarejo) {
        this.idVarejo = idVarejo;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }
}




