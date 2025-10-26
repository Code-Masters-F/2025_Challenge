package br.com.fiap.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Venda {
    private int id;
    private int idVarejo;
    private String nome_produto;
    Instant dataHora;
    private BigDecimal preco;
    private UnidadeDeMedida unidadeDeMedida;

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

    public String getNomeProduto() {
        return nome_produto;
    }

    public void setNomeProduto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public UnidadeDeMedida getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    public void setUnidadeDeMedida(UnidadeDeMedida unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }
}




