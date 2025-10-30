-- ==========================================
-- 🔹 SCRIPT DE LIMPEZA GERAL DE DADOS
-- Banco: Oracle SQL Developer
-- Tabelas: venda, pequenovarejo
-- ==========================================

BEGIN
    -- Desabilita restrições de chave estrangeira temporariamente
    EXECUTE IMMEDIATE 'ALTER TABLE venda DISABLE CONSTRAINT ALL';
    EXECUTE IMMEDIATE 'ALTER TABLE pequenovarejo DISABLE CONSTRAINT ALL';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- ignora caso não existam constraints
END;
/

-- Remove os registros (mantém estrutura das tabelas)
BEGIN
    DELETE FROM venda;
    DELETE FROM pequenovarejo;
    COMMIT;
END;
/

-- (Opcional) Reinicia sequências se existirem
BEGIN
    EXECUTE IMMEDIATE 'ALTER SEQUENCE seq_venda RESTART START WITH 1';
    EXECUTE IMMEDIATE 'ALTER SEQUENCE seq_pequenovarejo RESTART START WITH 1';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- ignora se as sequências não existirem
END;
/

-- Reativa restrições
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE venda ENABLE CONSTRAINT ALL';
    EXECUTE IMMEDIATE 'ALTER TABLE pequenovarejo ENABLE CONSTRAINT ALL';
EXCEPTION
    WHEN OTHERS THEN
        NULL;
END;
/

COMMIT;
