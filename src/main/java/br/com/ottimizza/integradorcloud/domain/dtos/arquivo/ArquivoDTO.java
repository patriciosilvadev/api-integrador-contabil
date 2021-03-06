package br.com.ottimizza.integradorcloud.domain.dtos.arquivo;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class ArquivoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String nome;

    private String cnpjEmpresa;

    private String cnpjContabilidade;

    private String labelComplemento01;

    private String labelComplemento02;
    
    private String labelComplemento03;
    
    private String labelComplemento04;

    private String labelComplemento05;

}