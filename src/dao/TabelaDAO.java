/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.Conection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Tabela;

/**
 *
 * @author Junnio
 */
public class TabelaDAO {

    private Connection con;
    private PreparedStatement pstm;
    private ResultSet rs;       
    
    public TabelaDAO(){
        con = Conection.getConexao();
    }
   
    public List<Tabela> listarTabelas(String nomeTabela) {
       
        List<Tabela> tabelas = new ArrayList<>();        
                
        String sql = "SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS "
                + "WHERE TABLE_SCHEMA LIKE '"+nomeTabela+"';";

        try {
            
            pstm = con.prepareStatement(sql);
            //pstm.setString(1,"%"+ banco.getNome());
            rs = pstm.executeQuery(sql);
            while (rs.next()) {
                Tabela tabela = new Tabela();
                tabela.setNome(rs.getString("TABLE_NAME"));
                tabelas.add(tabela);
            }
        } catch (Exception e) {
            System.err.println("Erro encotrado   001T, causa:" + e.getMessage());
        }
        return tabelas;
    }
    
    public List<Tabela> referenciadas(String nomeBanco, String nomeTabela){
        
        List<Tabela> tabelasReferenciadas = new ArrayList<>();
        
        String sql ="SELECT TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, "
                + "REFERENCED_COLUMN_NAME  FROM information_schema.KEY_COLUMN_USAGE WHERE "
                + "TABLE_SCHEMA = '"+nomeBanco+"' AND TABLE_NAME = '"+nomeTabela+"' AND "
                + "REFERENCED_TABLE_NAME IS NOT NULL;";
        try {
            
            pstm = con.prepareStatement(sql);
            //pstm.setString(1,"%"+ banco.getNome());
            rs = pstm.executeQuery(sql);
            while (rs.next()) {
                Tabela tabela = new Tabela();
                tabela.setNomeReferenciada(rs.getString("REFERENCED_TABLE_NAME"));
                tabela.setNome(rs.getString("TABLE_NAME"));
                tabela.setNomeColuna(rs.getString("COLUMN_NAME"));
                tabela.setNomeColunaReferenciada(rs.getString("REFERENCED_COLUMN_NAME"));
                tabelasReferenciadas.add(tabela);
            }
        } catch (Exception e) {
            System.err.println("Erro encotrado   001T, causa:" + e.getMessage());
        }
        return tabelasReferenciadas;
    }

}
