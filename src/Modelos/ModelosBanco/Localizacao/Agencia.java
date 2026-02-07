package Modelos.ModelosBanco.Localizacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "agencia")
public class Agencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String nome;

    private Integer codigo;

    @OneToOne // Assumindo que várias agências podem estar no mesmo endereço ou 1 pra 1
    @JoinColumn(name = "idendereco")
    private Endereco endereco;
}