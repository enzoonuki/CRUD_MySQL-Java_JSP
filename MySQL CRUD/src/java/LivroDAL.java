/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Luna
 */

public class LivroDAL {
    public static String driverName = "com.mysql.jdbc.Driver";
    // Configurando a nossa conexão com um banco de dados//
    public static String serverName = "localhost:3306";    //caminho do servidor do BD
    public static String db = "desafio";        //nome do seu banco de dados
    public static String url = "jdbc:mysql://" + serverName + "/" + db;
    public static String nome = "root";        //nome de um usuário de seu BD
    public static String senha = "senhafoda123";      //sua senha de acesso

    public static Connection con = null;
    
    public static void conecta() {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url + "?user=" + nome + "&password="
                    + senha + "&useSSL=false&allowPublicKeyRetrieval=true");
        }
        catch (SQLException e)
        {
            Erro.setErro("SQLException: " + e.getMessage() + 
                "<br>SQLState: " + e.getSQLState() +
                "<br>VendorError: " + e.getErrorCode());
        }
        catch (Exception e)
        {
            Erro.setErro("Erro de driver");
        }
    }
    
    public static void desconecta() {
        try
        {
            con.close();
        }
        catch (SQLException e)
        {
            Erro.setErro("Erro ao desconectar");
        }
    }

    public static void consultaLivro()
    {
        ResultSet rs;
        String aux = "SELECT * FROM " + db + ".tablivro WHERE codigo=" + Livro.getCodigo();
        
        Erro.setErro(false);
        conecta();
        if (Erro.getErro()) return;
        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(aux);
            if (rs.next())
            {
                Erro.setErro(false);
                Livro.setTitulo(rs.getString(2));
                Livro.setAutor(rs.getString(3));
                Livro.setEditora(rs.getString(4));
                Livro.setAno(rs.getString(5));
            }
            else
            {
                Erro.setErro("Livro não encontrado.");
            }
            st.close();
        }
        catch (Exception e)
        {
            Erro.setErro(e.getMessage());
        }
        desconecta();
    }

    public static void insereLivro() {
        String aux = "INSERT INTO " + db + ".tablivro (codigo,titulo,autor,editora,ano) VALUES (" + Livro.getCodigo() + ",'" + Livro.getTitulo() +
                "','" + Livro.getAutor() + "','" + Livro.getEditora() + "'," + Livro.getAno() + ")";
    
     Erro.setErro(false);
        consultaLivro();
        if (!Erro.getErro()) {
            Erro.setErro("Código de livro já cadastrado.");
            return;
        }
        Erro.setErro(false);
        conecta();
        if (Erro.getErro()) return;
        try {
            Statement st = con.createStatement();
            st.executeUpdate(aux);
            st.close();
        }
        catch (Exception e)
        {
            Erro.setErro("Livro não cadastrado.");
        }
        desconecta();
    }

 public static void deletaLivro() {
        String aux = "DELETE FROM " + db + ".tablivro WHERE codigo=" + Livro.getCodigo();
        Erro.setErro(false);
        consultaLivro();
        if (con == null) return;
        if (Erro.getErro()) {
            Erro.setErro("Código de livro não cadastrado.");
            return;
        }
        Erro.setErro(false);
        conecta();
        if (Erro.getErro()) return;
        try {
            Statement st = con.createStatement();
            st.executeUpdate(aux);
            st.close();
        }
        catch (Exception e)
        {
            Erro.setErro(e.getMessage());
        }
        desconecta();
    }

    public static void atualizaLivro()
    {
        String aux = "UPDATE " + db + ".tablivro SET titulo='" + Livro.getTitulo() + "',autor='" + Livro.getAutor() + "',editora='" +
            Livro.getEditora() + "',ano=" + Livro.getAno() + " WHERE codigo=" + Livro.getCodigo();

        Erro.setErro(false);
        consultaLivro();
        if (con == null) return;
        if (Erro.getErro()) {
            Erro.setErro("Código de livro não cadastrado.");
            return;
        }
        Erro.setErro(false);
        conecta();
        if (Erro.getErro()) return;
        try {
            Statement st = con.createStatement();
            st.executeUpdate(aux);
            st.close();
        }
        catch (Exception e)
        {
            Erro.setErro(e.getMessage());
        }
        desconecta();
    }
    public static void geraTabela()
    {
        ResultSet rs;
        String aux = "SHOW TABLES LIKE 'tablivro'";
        try
        {
            conecta();
            Statement st = con.createStatement();
            rs = st.executeQuery(aux);
            if (rs.next())
            {
                desconecta();
                Erro.setErro("Tabela já existe.");
                return;
            }
            Erro.setErro(false);
            aux = "CREATE TABLE tablivro (codigo int, titulo varchar(45), autor varchar(45), editora"
                + " varchar(45), ano int)";
            st.executeUpdate(aux);
        }
        catch (Exception e)
        {
            Erro.setErro(e.getMessage());
        }
        desconecta();
    }
    public static void deleTabela()
    {
        try
        {
            conecta();
            String aux = "DROP TABLE tablivro";
            Statement st = con.createStatement();
            st.executeUpdate(aux);
        }
        catch (Exception e)
        {
            Erro.setErro("Tabela não existe.");
        }
        desconecta();
    }
}