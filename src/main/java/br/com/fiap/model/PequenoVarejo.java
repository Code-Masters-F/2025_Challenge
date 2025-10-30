package br.com.fiap.model;

import java.security.PrivateKey;

public class PequenoVarejo {
    private int id;
    private String nome;
    private String cnpj;
    private String cidade;
    private String estado;
    private String endereco;
    private String cep;

    public PequenoVarejo(int id, String nome, String cnpj, String cidade, String estado, String endereco, String cep) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.estado = estado;
        this.endereco = endereco;
        this.cep = cep;
    }

    public PequenoVarejo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
