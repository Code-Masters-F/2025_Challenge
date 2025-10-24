-- Comandos que deletam o banco. (para testes etc...)


-- 1. Deletar tabela filha (que contém a Foreign Key)
DROP TABLE Venda;

-- 2. Agora sim, deleta a tabela pai (que era referenciada)
DROP TABLE PequenoVarejo;