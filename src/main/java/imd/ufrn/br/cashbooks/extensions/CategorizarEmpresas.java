package imd.ufrn.br.cashbooks.extensions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import imd.ufrn.br.cashbooks.interfaces.ICategorizarAutomaticamente;
import imd.ufrn.br.cashbooks.model.Movimentacao;
import imd.ufrn.br.cashbooks.model.enums.Categoria;
import imd.ufrn.br.cashbooks.model.enums.MovimentacaoStatus;
import imd.ufrn.br.cashbooks.service.MovimentacaoService;

@Component
public class CategorizarEmpresas implements ICategorizarAutomaticamente{
	
	@Autowired
	private MovimentacaoService service;

	@Override 
	public void categorizar(Movimentacao mov) {
		
		List<Movimentacao> movimentacoes = service.findAll();
		
		String descricao = mov.getDescricao().toLowerCase();
		
		if(mov.getStatus() == MovimentacaoStatus.ENTRADA) {
			mov.setCategoria(Categoria.CAIXA);
			return;
		}
		
		for(Movimentacao m : movimentacoes) {
			if(m.getDescricao().equals(descricao)) {
				mov.setCategoria(m.getCategoria());
				return;
			}
		}
		
		
		if(descricao.contains("roubo") || descricao.contains("furto") || descricao.contains("assalto")) {
			mov.setCategoria(Categoria.ROUBO);
		}//ROUBO
		else if(descricao.contains("estoque") || descricao.contains("carga") || descricao.contains("carregamento") || descricao.contains("quilo") 
				|| descricao.contains("kg") || descricao.contains("reposiçao") || descricao.contains("reposicao") || descricao.contains("reposição") || descricao.contains("reca")) {
			mov.setCategoria(Categoria.ESTOQUE);
		}//ESTOQUE
		else {
			mov.setCategoria(Categoria.DESPESAS_DIVERSAS);
		}//DESPESAS_DIVERSAS
		
	}

}
