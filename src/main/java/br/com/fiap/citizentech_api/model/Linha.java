package br.com.fiap.citizentech_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "T_CT_LINHA")
@Getter @Setter @NoArgsConstructor
public class Linha {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_linha")
    @SequenceGenerator(name = "seq_linha", sequenceName = "SEQ_CT_LINHA",
            allocationSize = 1)
    @Column(name = "id_linha")
    private Long id;
    @Column(name = "nm_linha", nullable = false, length = 50)
    private String nome;
    @Column(name = "ds_linha", nullable = false, length = 100)
    private String descricao;
    @Column(name = "nr_intervalo_minutos", nullable = false)
    private Integer intervaloMinutos;
    @OneToMany(mappedBy = "linha", cascade = CascadeType.ALL, orphanRemoval =
            true)
    private List<LinhaPonto> pontos = new ArrayList<>();
}