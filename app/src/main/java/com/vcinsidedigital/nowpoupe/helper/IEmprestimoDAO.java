package com.vcinsidedigital.nowpoupe.helper;

import com.vcinsidedigital.nowpoupe.model.Emprestimo;

import java.util.List;

public interface IEmprestimoDAO
{
    public boolean salvar(Emprestimo emprestimo);
    public boolean atualizar(Emprestimo emprestimo);
    public boolean deletar(Emprestimo emprestimo);
    public List<Emprestimo> listar();
}
