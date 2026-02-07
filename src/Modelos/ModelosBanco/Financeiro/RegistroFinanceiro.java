package Modelos.ModelosBanco.Financeiro;

import java.time.LocalDateTime;
import java.util.List;

import Modelos.ModelosBanco.Cliente.Conta;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;

@Data
@Entity
@Table(name = "registro_financeiro")
public class RegistroFinanceiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    private Float valor;

    @ManyToOne
    @JoinColumn(name = "idconta")
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "idtipo_movimentacao")
    private TipoMovimentacao tipoMovimentacao;

    // Relacionamento com a tabela associativa
    @OneToMany(mappedBy = "registroFinanceiro", cascade = CascadeType.ALL)
    private List<FormaPagamentoUtilizada> formasPagamento;
}