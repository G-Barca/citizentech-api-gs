package br.com.fiap.citizentech_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "T_CT_LINHA_PONTO")
@Getter @Setter @NoArgsConstructor
public class LinhaPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
            "seq_linha_ponto")
    @SequenceGenerator(name = "seq_linha_ponto", sequenceName =
            "SEQ_CT_LINHA_PONTO", allocationSize = 1)
    @Column(name = "id_linha_ponto")
    private Long id;
    @Column(name = "nr_sequencia", nullable = false)
    private Integer sequencia;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "T_CT_LINHA_id_linha", nullable = false)
    private Linha linha;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "T_CT_PONTO_ONIBUS_id_ponto", nullable = false)
    private PontoOnibus ponto;
}