package imd.ufrn.br.cashbooks.extensions;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import imd.ufrn.br.cashbooks.interfaces.IRestricoesComprasPrazo;
import imd.ufrn.br.cashbooks.model.Movimentacao;
import imd.ufrn.br.cashbooks.model.enums.MovimentacaoStatus;
import imd.ufrn.br.cashbooks.service.UsuarioService;

@Component
public class RegraEmpresas implements IRestricoesComprasPrazo{
	
	
	

	@Override
	public LocalDate calcularDataLimite(Movimentacao mov) {
		LocalDate data = mov.getDataMovimentacao();
		while(data.getMonth() == mov.getDataMovimentacao().getMonth()) {
			data = data.plusDays(1);
		}
		data = data.minusDays(1);
		return data;
	}

	@Override
	public boolean validarMovimentacao(Movimentacao mov) {
		if(mov.isPago() && mov.getStatus() == MovimentacaoStatus.ENTRADA) {
			return true;
		}
		else if(mov.getStatus() == MovimentacaoStatus.SAIDA) {
			if(!mov.getDataCobranca().isAfter(calcularDataLimite(mov))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer getLimite() {//Não tem limite
		return null;
	}
	
}
