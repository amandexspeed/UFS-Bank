package Modelos.ModelosBanco.Cliente;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;
import Modelos.ModelosBanco.Enum.TipoPessoa;
import Modelos.ModelosBanco.Localizacao.Endereco;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 250)
    private String nome;

    @Column(name = "cpf_cnpj", length = 14)
    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa")
    private TipoPessoa tipoPessoa;

    @Column(length = 100)
    private String email;
    
    @Column(length = 14)
    private String telefone;
    
    @Column(name = "emailalternativo", length = 150)
    private String emailAlternativo;
    
    @Column(name = "telefonealternativo", length = 14)
    private String telefonealternativo;

    @ManyToOne
    @JoinColumn(name = "idendereco")
    private Endereco endereco;
    
    // Opcional: Lista de contas para acesso bidirecional
    @OneToMany(mappedBy = "cliente")
    private List<Conta> contas;
}