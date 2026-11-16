package br.com.fiap.citizentech_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "T_CT_PONTO_ONIBUS")
@Getter @Setter @NoArgsConstructor
public class PontoOnibus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
            "seq_ponto")
    @SequenceGenerator(name = "seq_ponto", sequenceName =
            "SEQ_CT_PONTO_ONIBUS", allocationSize = 1)
    @Column(name = "id_ponto")
    private Long id;
    @Column(name = "nm_endereco", nullable = false, length = 50)
    private String endereco;
    @Column(name = "nr_endereco", nullable = false)
    private Integer numero;
}