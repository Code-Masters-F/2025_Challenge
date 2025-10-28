package br.com.fiap.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Venda {
    private int id;
    private int idVarejo;
    private String nome_produto;
    private double tamanhoEmbalagem;
    Instant dataHora;
    private BigDecimal preco;
    private UnidadeDeMedida unidadeDeMedida;
    private double quantidade;

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

    public double getTamanhoEmbalagem() {
        return tamanhoEmbalagem;
    }

    public void setTamanhoEmbalagem(double tamanhoEmbalagem) {
        this.tamanhoEmbalagem = tamanhoEmbalagem;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    public BigDecimal getPrecoUnitario() {
        return preco;
    }

    public void setPrecoUnitario(BigDecimal preco) {
        this.preco = preco;
    }

    public UnidadeDeMedida getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    public void setUnidadeDeMedida(UnidadeDeMedida unidadeDeMedida) {
        this.unidadeDeMedida = unidadeDeMedida;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

}




