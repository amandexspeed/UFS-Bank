package Modelos.ModelosBanco.Cliente;

import lombok.Data;
import jakarta.persistence.*;
import Modelos.ModelosBanco.Localizacao.Agencia;

@Data
@Entity
@Table(name = "conta")
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float saldo;
    
    @Column(precision = 8) // Ajustar conforme precisão do decimal
    private Double numero;

    @ManyToOne
    @JoinColumn(name = "idAgencia")
    private Agencia agencia;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;
}