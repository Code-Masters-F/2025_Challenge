package br.com.fiap.service;

import br.com.fiap.dao.PequenoVarejoDao;
import br.com.fiap.dao.VendaDao;
import br.com.fiap.model.PequenoVarejo;
import br.com.fiap.model.Venda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class ImportacaoService {
    public void importarCSV(String caminho) {
        if (caminho.endsWith("comercios.csv")) {
            importarComercios(caminho);
        } else if (caminho.endsWith("vendas.csv")) {
            importarVendas(caminho);
        } else {
            System.out.println("Tipo de arquivo não reconhecido!");
        }
    }

    private void importarComercios(String caminho) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))) {
            PequenoVarejoDao pequenoVarejoDao = new PequenoVarejoDao();
            String linha;
            bufferedReader.readLine(); // pula cabeçalho
            int contador = 0;

            while ((linha = bufferedReader.readLine()) != null) {
                String[] dados = linha.split(",");
                PequenoVarejo pequenoVarejo = new PequenoVarejo();
                pequenoVarejo.setNome(dados[0]);
                pequenoVarejo.setCnpj(dados[1]);
                pequenoVarejo.setEndereco(dados[2]);
                pequenoVarejo.setCidade(dados[3]);
                pequenoVarejo.setEstado(dados[4]);
                pequenoVarejo.setCep(dados[5]);
                pequenoVarejoDao.salvar(pequenoVarejo);
                contador++;
            }
            System.out.println(contador + " comércios importados com sucesso!");
        } catch (IOException | SQLException e) {
            System.out.println("Erro ao importar comércios: " + e.getMessage());
        }
    }

    private void importarVendas(String caminho) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))) {
            VendaDao dao = new VendaDao();
            PequenoVarejoDao varejoDao = new PequenoVarejoDao();
            String linha;
            bufferedReader.readLine();
            int contador = 0;

            while ((linha = bufferedReader.readLine()) != null) {
                String[] dados = linha.split(",");

                String nomeComercio = dados[0];
                String produto = dados[1];
                String dataHoraTexto = dados[2];

                Integer idComercio = varejoDao.buscarIdPorNome()
            }
        }
    }
}
