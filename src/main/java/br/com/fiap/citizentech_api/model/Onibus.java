package br.com.fiap.citizentech_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "T_CT_ONIBUS")
@Getter @Setter @NoArgsConstructor
public class Onibus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
            "seq_onibus")
    @SequenceGenerator(name = "seq_onibus", sequenceName = "SEQ_CT_ONIBUS",
            allocationSize = 1)
    @Column(name = "id_onibus")
    private Long id;
    @Column(name = "nr_placa", nullable = false, length = 10, unique = true)
    private String placa;
    @Enumerated(EnumType.STRING)
    @Column(name = "st_onibus", nullable = false, length = 20)
    private StatusOnibus status;
}