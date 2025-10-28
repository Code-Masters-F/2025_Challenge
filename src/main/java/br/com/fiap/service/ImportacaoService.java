package br.com.fiap.service;

import br.com.fiap.dao.PequenoVarejoDao;
import br.com.fiap.dao.VendaDao;
import br.com.fiap.model.PequenoVarejo;
import br.com.fiap.model.UnidadeDeMedida;
import br.com.fiap.model.Venda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static utils.ImportacaoUtils.*;

public class ImportacaoService {
    private static final String BASE_PATH = System.getProperty("user.dir")
            + File.separator + "Sales Archive" + File.separator;


    public static void importarCSV(String nomeArquivo) {
        String caminhoCompleto = BASE_PATH + nomeArquivo;

        File file = new File(caminhoCompleto);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Arquivo não encontrado em: " + caminhoCompleto);
            return;
        }

        String nome = nomeArquivo.toLowerCase();
        if (nome.contains("comercio")) {
            importarComercios(caminhoCompleto);
        } else if (nome.contains("venda")) {
            importarVendas(caminhoCompleto);
        } else {
            System.out.println("Tipo de arquivo não reconhecido: " + nomeArquivo);
        }
    }

    private static void importarComercios(String caminho) {
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

    private static void importarVendas(String caminho) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminho))) {
            VendaDao vendaDao = new VendaDao();
            PequenoVarejoDao varejoDao = new PequenoVarejoDao();
            String linha;
            bufferedReader.readLine();
            int contador = 0;

            while ((linha = bufferedReader.readLine()) != null) {
                String[] dados = linha.split(",");

                String nomeComercio = dados[0].trim();
                String nomeProduto = dados[1].trim();
                double tamanhoEmbalagem = parseDoubleSafe(dados[2]);
                UnidadeDeMedida unidadeDeMedida = UnidadeDeMedida.fromString(dados[3].trim());
                double quantidade = parseDoubleSafe(dados[4]);
                BigDecimal precoUnitario = parseBigDecimalSafe(dados[5]);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dados[6].trim(), formatter);

                Instant dataHora = localDateTime.atZone(ZoneId.of("UTC")).toInstant();

                Integer idComercio = varejoDao.buscarIdPorNome(nomeComercio);

                if (idComercio != null) {
                    Venda venda = new Venda();
                    venda.setIdVarejo(idComercio);
                    venda.setNomeProduto(nomeProduto);
                    venda.setTamanhoEmbalagem(tamanhoEmbalagem);
                    venda.setUnidadeDeMedida(unidadeDeMedida);
                    venda.setQuantidade(quantidade);
                    venda.setPrecoUnitario(precoUnitario);
                    venda.setDataHora(dataHora);

                    vendaDao.salvar(venda);
                    contador++;
                }
            }
            System.out.println(contador + " vendas importadas com sucesso!");
        } catch (IOException | SQLException e) {
            System.out.println("Falha ao importar vendas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
