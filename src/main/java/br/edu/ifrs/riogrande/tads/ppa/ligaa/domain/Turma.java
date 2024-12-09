package br.edu.ifrs.riogrande.tads.ppa.ligaa.domain;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrs.riogrande.tads.ppa.ligaa.service.DomainException;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.service.ServiceException;

public class Turma {

    private String codigo; // ppa-2024-2

    private Disciplina disciplina;
    private Professor professor;

    private String semestre; // 2024-2
    private String sala;
    private int vagas;
    private boolean fechada;

    public void isFechada(String codigoTurma) {
        if (fechada) {
            throw new DomainException("Turma " + codigoTurma + " está fechada");
        }
    }

    public void setFechada(boolean fechada) {
        this.fechada = fechada;
    }

    private List<Matricula> matriculas = new ArrayList<>();

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }


    @Override
    public String toString() {
        return "Turma [codigo=" + codigo + ", disciplina=" + disciplina + ", professor=" + professor + ", semestre="
                + semestre + ", sala=" + sala + ", vagas=" + vagas + ", matriculas=" + matriculas + "]";
    }

    public void estáMatriculado(Aluno aluno, String cpf, String codigoTurma) {
        if (this.getMatriculas()
        .stream()
        .anyMatch(m -> m.getAluno().equals(aluno))) {
            throw new DomainException("Aluno " + cpf + " já está matriculado na turma " + codigoTurma);
        }
    }

    
}
