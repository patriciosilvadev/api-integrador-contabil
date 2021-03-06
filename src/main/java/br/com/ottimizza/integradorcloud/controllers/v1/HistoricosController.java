package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.HistoricoService;

@RestController
@RequestMapping("/api/v1/historicos") // @formatter:off
public class HistoricosController {

    @Inject
    private HistoricoService historicoService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody HistoricoDTO historicoDTO, 
                                                     OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
            historicoService.salvar(historicoDTO, authentication)
        ));
    }

    @GetMapping("/sf")
    public ResponseEntity<?> buscaHistoricosProSF(@Valid HistoricoDTO filter, 
                                                  OAuth2Authentication authentication) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<>(
    		historicoService.buscaHistoricosProSF(filter, authentication)
    	));
    }
    
    @GetMapping
    public ResponseEntity<?> buscarPorContaMovimento(@Valid PageCriteria pageCriteria,
                                                     @Valid HistoricoDTO historicoDTO,
                                                     OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<HistoricoDTO>(
            historicoService.buscar(historicoDTO, pageCriteria, authentication)
        ));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePorId(@PathVariable("id") BigInteger id,
                                         OAuth2Authentication authentication) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<>(
    		historicoService.deletaPorId(id, authentication)
    	));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizaHistorico(@PathVariable("id") BigInteger id, 
    										   @RequestBody HistoricoDTO historico,
                                               OAuth2Authentication authentication) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<>(
    		historicoService.atualizar(id, historico, authentication)
    	));
    }
}