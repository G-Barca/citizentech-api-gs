package br.com.fiap.citizentech_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
@Entity
@Table(name = "T_CT_OPERACAO")
@Getter @Setter @NoArgsConstructor
public class Operacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
            "seq_operacao")
    @SequenceGenerator(name = "seq_operacao", sequenceName =
            "SEQ_CT_OPERACAO", allocationSize = 1)
    @Column(name = "id_operacao")
    private Long id;
    @Column(name = "dt_operacao", nullable = false)
    private LocalDate dataOperacao;
    @Column(name = "hr_inicio", nullable = false)
    private Integer horaInicio;
    @Column(name = "hr_fim", nullable = false)
    private Integer horaFim;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "T_CT_LINHA_id_linha", nullable = false)
    private Linha linha;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "T_CT_ONIBUS_id_onibus", nullable = false)
    private Onibus onibus;
}