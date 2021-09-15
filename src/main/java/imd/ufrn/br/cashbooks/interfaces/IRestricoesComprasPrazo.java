package imd.ufrn.br.cashbooks.interfaces;

import java.time.LocalDate;

import imd.ufrn.br.cashbooks.model.Movimentacao;
import imd.ufrn.br.cashbooks.model.Usuario;

public interface IRestricoesComprasPrazo {
	public LocalDate calcularDataLimite(Movimentacao mov);
	public boolean validarMovimentacao(Movimentacao mov);
	public Integer getLimite();
}
