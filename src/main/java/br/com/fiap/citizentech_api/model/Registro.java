package br.com.fiap.citizentech_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Table(name = "T_CT_REGSITRO")
@Getter @Setter @NoArgsConstructor
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
            "seq_registro")
    @SequenceGenerator(name = "seq_registro", sequenceName =
            "SEQ_CT_REGSITRO", allocationSize = 1)
    @Column(name = "id_registro")
    private Long id;
    @Column(name = "dt_chegada", nullable = false)
    private LocalDateTime dataChegada;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "T_CT_PONTO_ONIBUS_id_ponto", nullable = false)
    private PontoOnibus ponto;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "T_CT_OPERACAO_id_operacao", nullable = false)
    private Operacao operacao;
}