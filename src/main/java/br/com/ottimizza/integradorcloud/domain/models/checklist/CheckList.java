package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class CheckList implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<CheckListObservacoes> observacoes;
	
	private CheckListPerguntas[] perguntas;
}
