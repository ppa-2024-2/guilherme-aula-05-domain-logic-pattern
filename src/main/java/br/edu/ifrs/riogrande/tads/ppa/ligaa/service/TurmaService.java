package br.edu.ifrs.riogrande.tads.ppa.ligaa.service;

import org.springframework.stereotype.Service;

import br.edu.ifrs.riogrande.tads.ppa.ligaa.domain.Historico;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.domain.Matricula;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.domain.Turma;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.domain.Matricula.Situacao;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.AlunoRepository;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.DisciplinaRepository;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.ProfessorRepository;
import br.edu.ifrs.riogrande.tads.ppa.ligaa.repository.TurmaRepository;
import jakarta.annotation.PostConstruct;

@Service
public class TurmaService {

    int numero;

    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    
    public TurmaService
        (
            TurmaRepository turmaRepository,
            DisciplinaRepository disciplinaRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository
        ) {
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
    }

    public Matricula matricular(String cpf, String codigoTurma) {
        // turma existe?
        var turma = turmaRepository.findByCodigo(codigoTurma)
            .orElseThrow(() -> new NotFoundException());

        // turma já terminou o ciclo?
        turma.isFechada(codigoTurma);

        // aluno existe?
        var aluno = alunoRepository.findByCpf(cpf)
            .orElseThrow(() -> new NotFoundException());

        // code smell => mau cheiro no código

        // aluno já matriculado?
        // if (turma.getMatriculas().stream().anyMatch(m -> m.getAluno().equals(aluno))) {
        turma.estáMatriculado(aluno, cpf, codigoTurma);

        // todas as turmas do aluno
        // var turmas = turmaRepository.findByAluno(aluno);

        Historico historico = turmaRepository.findHistorico(aluno);

        // aluno já fez essa disciplina?
        // if (turmas.stream().flatMap(t -> t.getMatriculas().stream())
        //     .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.APROVADO))) {
        // if (turmas.stream().flatMap(t -> t.getMatriculas().stream())
        //     .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.APROVADO))) {
        historico.aprovadoEm(turma.getDisciplina(), cpf, aluno);

        historico.vagaParaReprovados(turma, historico, codigoTurma);

        var matricula = new Matricula();
        matricula.setNumero(++numero);
        matricula.setSituacao(Situacao.REGULAR);
        matricula.setAluno(aluno);
        turma.getMatriculas().add(matricula);
        // persistir
        System.out.println(matricula);
        return matricula;
    }


    @PostConstruct
    void popular() { // seed
        var can = alunoRepository.findByCpf("11122233344").orElseThrow();
        var ppa = disciplinaRepository.findByCodigo("ppa").orElseThrow();
        var marcio = professorRepository.findBySiape("1810497").orElseThrow();
        
        var ppa20232 = new Turma();
        ppa20232.setCodigo("ppa-2023-2");
        ppa20232.setDisciplina(ppa);
        ppa20232.setProfessor(marcio);
        ppa20232.setSemestre("2023-2");
        ppa20232.setVagas(10);

        var ppa20242 = new Turma();
        ppa20242.setCodigo("ppa-2024-2");
        ppa20242.setDisciplina(ppa);
        ppa20242.setProfessor(marcio);
        ppa20242.setSemestre("2024-2");
        ppa20242.setVagas(1);

        var mat = new Matricula();
        mat.setAluno(can);
        mat.setNumero(++numero);
        mat.setSituacao(Situacao.REPROVADO);
        ppa20232.getMatriculas().add(mat);

        turmaRepository.save(ppa20232);
        turmaRepository.save(ppa20242);

        System.out.println(ppa20232);
        System.out.println(ppa20242);
    }

    
}
