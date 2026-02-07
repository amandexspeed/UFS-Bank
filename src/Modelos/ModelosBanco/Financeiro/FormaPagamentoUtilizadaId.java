package Modelos.ModelosBanco.Financeiro;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class FormaPagamentoUtilizadaId implements Serializable {
    @Column(name = "idregistro_financeiro")
    private Integer idRegistroFinanceiro;

    @Column(name = "idforma_pagamento")
    private Integer idFormaPagamento;
}