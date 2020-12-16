package br.com.ottimizza.integradorcloud.services;



import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.checklist.CheckListRespostasDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfempresa.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.mappers.checklist.CheckListMapper;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckList;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListPerguntas;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListPerguntasRepository;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListRespostasRepository;

@Service
public class CheckListService {
	
	@Inject
	CheckListPerguntasRepository perguntasRepository;
	
	@Inject
	CheckListRespostasRepository respostasRepository;
	
	@Value("${salesforce.service.url}")
    private String SF_SERVICE_URL;

	public CheckList buscaCheckList(String tipoRoteiro) throws Exception {
		CheckList checkList = new CheckList();
		checkList.setPerguntas(perguntasRepository.buscaPorTipo(tipoRoteiro));
		checkList.setObservacoes(perguntasRepository.buscaObservacoes());
		return checkList;
	}
	
	// PERGUNTAS 
	
	public CheckListPerguntas salvaPerguntas(CheckListPerguntas pergunta) throws Exception {
		return perguntasRepository.save(pergunta);
	}
	
	// RESPOSTAS
	
	public CheckListRespostasDTO salvaResposta(CheckListRespostasDTO respostaDTO, String authorization) throws Exception {
		CheckListRespostas respostaDb = null;
		
		//List<BigInteger> idsPerguntasContato = perguntasRepository.getIdsPerguntasContanto();
		try {
			respostaDb = respostasRepository.checkResposta(respostaDTO.getRoteiroId(), respostaDTO.getPerguntaId());
		}catch(Exception ex) { } 
		if(respostaDb != null) {
			CheckListRespostas resposta = respostasRepository.save(respostaDTO.patch(respostaDb));
			atualizaEmpresaCRM(respostaDTO, authorization);
			return CheckListMapper.respostasFromEntity(resposta);
		}
		else {
			//if(idsPerguntasContato.contains(respostaDTO.getPerguntaId())) {
			CheckListRespostas resposta = respostasRepository.save(CheckListMapper.respostasFromDTO(respostaDTO));
			atualizaEmpresaCRM(respostaDTO, authorization);
			return CheckListMapper.respostasFromEntity(resposta);
		}
	}
	
	public Page<CheckListRespostasDTO> getRespostas(CheckListRespostasDTO filtro, PageCriteria pageCriteria) throws Exception {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<CheckListRespostas> example = Example.of(CheckListMapper.respostasFromDTO(filtro), matcher);
		
        return respostasRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize()))
        										.map(CheckListMapper::respostasFromEntity);
	}
	
	public void atualizaEmpresaCRM(CheckListRespostasDTO respostaDTO, String authorization) throws Exception {
		SFEmpresa empresaCrm = null;
		ObjectMapper mapper = new ObjectMapper();
		if(respostaDTO.getPerguntaId() == BigInteger.valueOf(7)) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.Nome_de_quem_faz_o_fechamento(respostaDTO.getResposta())
					.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString,authorization);
		}
		if(respostaDTO.getPerguntaId() == BigInteger.valueOf(37)) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.Email_de_quem_faz_o_fechamento(respostaDTO.getResposta())
				.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString, authorization);
		}
	}
	
	private String defaultPatch(String url, String body, String authentication) {
    	RestTemplate template = new RestTemplate();
    	
    	HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    	requestFactory.setConnectTimeout(15000);
    	requestFactory.setReadTimeout(15000);
    	
    	template.setRequestFactory(requestFactory);
    	
    	HttpHeaders headers =  new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("Authorization", authentication);
    	
    	return template.patchForObject(url, new HttpEntity<String>(body, headers), String.class);
    }

}