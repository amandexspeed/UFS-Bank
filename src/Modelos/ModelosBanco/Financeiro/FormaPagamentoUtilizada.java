package Modelos.ModelosBanco.Financeiro;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "formas_pagamento_utilizada")
public class FormaPagamentoUtilizada {

    @EmbeddedId
    private FormaPagamentoUtilizadaId id = new FormaPagamentoUtilizadaId();

    @ManyToOne
    @MapsId("idRegistroFinanceiro") // Vincula parte da chave composta a este objeto
    @JoinColumn(name = "idregistro_financeiro")
    private RegistroFinanceiro registroFinanceiro;

    @ManyToOne
    @MapsId("idFormaPagamento") // Vincula a outra parte
    @JoinColumn(name = "idforma_pagamento")
    private FormaPagamento formaPagamento;

    private Float valor; // O atributo extra que obrigou a criar essa classe
}