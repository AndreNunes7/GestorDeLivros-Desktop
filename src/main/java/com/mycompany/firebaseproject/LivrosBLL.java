package com.mycompany.firebaseproject;

public class LivrosBLL {
    public static void validaTitulo(char op, Livro umLivro) {
        Erro.setErro(false);
        if (umLivro.getTitulo().isEmpty()) {
            Erro.setErro("O campo TITULO é de preenchimento obrigatório...");
            return;
        }

        switch (op) {
            case 'c':
                LivrosDAL.consultaLivro(umLivro);
                break;
            case 'd':
                LivrosDAL.deletaLivro(umLivro);
                break;
            default:
                Erro.setErro("Operação inválida.");
        }
    }

    public static void validaDados(char op, Livro umLivro) {
        Erro.setErro(false);

        if (umLivro.getTitulo().isEmpty()) {
            Erro.setErro("O campo TITULO é de preenchimento obrigatório...");
            return;
        }
        if (umLivro.getAutor().isEmpty()) {
            Erro.setErro("O campo AUTOR é de preenchimento obrigatório...");
            return;
        }
        if (umLivro.getEditora().isEmpty()) {
            Erro.setErro("O campo EDITORA é de preenchimento obrigatório...");
            return;
        }
        if (umLivro.getAnoEdicao().isEmpty()) {
            Erro.setErro("O campo ANO EDICAO é de preenchimento obrigatório...");
            return;
        } else {
            try {
                Integer.parseInt(umLivro.getAnoEdicao());
            } catch (NumberFormatException e) {
                Erro.setErro("O campo ANO EDICAO deve ser numérico!");
                return;
            }
        }
        if (umLivro.getLocalizacao().isEmpty()) {
            Erro.setErro("O campo LOCALIZACAO é de preenchimento obrigatório...");
            return;
        }

        switch (op) {
            case 'i':
                LivrosDAL.insereLivro(umLivro);
                break;
            case 'a':
                LivrosDAL.alteraLivro(umLivro);
                break;
            default:
                Erro.setErro("Operação inválida.");
        }
    }
}