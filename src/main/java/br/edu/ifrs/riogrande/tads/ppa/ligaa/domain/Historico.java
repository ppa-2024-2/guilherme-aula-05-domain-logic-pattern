package br.edu.ifrs.riogrande.tads.ppa.ligaa.domain;

import java.util.List;

import br.edu.ifrs.riogrande.tads.ppa.ligaa.domain.Matricula.Situacao;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.service.DomainException;

// Value Object => Objeto de Valor 
// DTO => VO => DTO
public record Historico(Aluno aluno, List<Turma> turmas) {

    public void aprovadoEm(Disciplina disciplina, String cpf, Aluno aluno) {
        if (turmas.stream().flatMap(t -> t.getMatriculas().stream())
        .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.APROVADO))) {
            throw new DomainException("Aluno " + cpf + " já aprovado na disciplina " + disciplina.getNome());
        }
        // FIXME: considerar a disciplina
    }

    public void vagaParaReprovados(Turma turma, Historico historico, String codigoTurma){
        int qtdAlunosTurma = turma.getMatriculas().size();
        
        int qtdVagas = turma.getVagas();

        // há vagas?
        if (qtdAlunosTurma >= qtdVagas) { // não

            // se nunca foi reprovado, não pode matricular-se
            if (!historico.jaReprovado(turma)) {
                throw new DomainException("Não há vagas na turma " + codigoTurma);
            }
        }
    }

    public boolean jaReprovado(Turma turma) {

        var turmasAnterioresDaDisciplina = turmas.stream()
        .filter(t -> t.getDisciplina().equals(turma.getDisciplina()))
        .toList();

        boolean reprovadoAnteriormente = turmasAnterioresDaDisciplina.stream().flatMap(t -> t.getMatriculas().stream())
                    .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.REPROVADO));
        System.out.println(reprovadoAnteriormente);
        return reprovadoAnteriormente;       
    }
}
