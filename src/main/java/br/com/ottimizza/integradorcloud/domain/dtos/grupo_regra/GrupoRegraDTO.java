package br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import br.com.ottimizza.integradorcloud.domain.models.Regra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GrupoRegraDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private Integer posicao;
    
    private String contaMovimento;

    private Short tipoLancamento;

    private String idRoteiro;

    private String cnpjEmpresa;

    private String cnpjContabilidade;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;
    
    private Integer contagemRegras;

    private List<Regra> regras;

    private Boolean ativo;

    private String usuario;
    
}