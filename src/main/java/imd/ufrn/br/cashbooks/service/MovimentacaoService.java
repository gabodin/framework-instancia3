package imd.ufrn.br.cashbooks.service;



import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import imd.ufrn.br.cashbooks.extensions.CategorizarEmpresas;
import imd.ufrn.br.cashbooks.extensions.RegraEmpresas;
import imd.ufrn.br.cashbooks.extensions.RelatorioHTML;
import imd.ufrn.br.cashbooks.interfaces.ICategorizarAutomaticamente;
import imd.ufrn.br.cashbooks.interfaces.IGerarRelatorio;
import imd.ufrn.br.cashbooks.interfaces.IRestricoesComprasPrazo;
import imd.ufrn.br.cashbooks.model.Cliente;
import imd.ufrn.br.cashbooks.model.Movimentacao;
import imd.ufrn.br.cashbooks.model.Usuario;
import imd.ufrn.br.cashbooks.model.enums.Categoria;
import imd.ufrn.br.cashbooks.model.enums.MovimentacaoStatus;
import imd.ufrn.br.cashbooks.repository.MovimentacaoRepository;
import imd.ufrn.br.cashbooks.repository.UsuarioRepository;
import imd.ufrn.br.cashbooks.service.exceptions.DatabaseException;
import imd.ufrn.br.cashbooks.service.exceptions.ResourceNotFoundException;
import imd.ufrn.br.cashbooks.service.exceptions.ValidationException;

@Service
public class MovimentacaoService {
	
	@Autowired
	private MovimentacaoRepository repository;
	
	@Autowired
	private ClienteService serviceCliente;
	
	
	@Autowired
	private UsuarioRepository proprietarioRepository;
	
	private ICategorizarAutomaticamente categoriaStrategy;
	
	private IGerarRelatorio relatorioStategy;
	
	private IRestricoesComprasPrazo prazoStrategy;
	
	@Autowired
	private RegraEmpresas regra;
	
	@Autowired
	private CategorizarEmpresas categorizar;
	
	public String getRelatorio() {
		if(relatorioStategy == null) this.setRelatorioStrategy(new RelatorioHTML());
			
		return relatorioStategy.gerarRelatorio(this.findAll());
	}
	
	public void setStrategy (ICategorizarAutomaticamente strategy) {
		this.categoriaStrategy = strategy;
	}
	
	public void setRelatorioStrategy(IGerarRelatorio strategy) {
		this.relatorioStategy = strategy;
	}
	
	public void setPrazoStrategy(IRestricoesComprasPrazo strategy) {
		this.prazoStrategy = strategy;
	}
	
	public boolean validarMovimentacao(Movimentacao mov) {
		return prazoStrategy.validarMovimentacao(mov);
	}
	
	public List<Movimentacao> findAll(){
		return repository.findAll();
	}
	
	public Movimentacao findById(Long id) {
		Optional<Movimentacao> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public void categorizar(Movimentacao mov) {
		this.categoriaStrategy.categorizar(mov);
	}
	
	public Movimentacao insert(Movimentacao obj) {		
		//System.out.println(obj.getCategoria());
		Optional<Usuario> op = proprietarioRepository.findById(1L);
		Usuario prop = op.get();
		
		this.setStrategy(categorizar);
		categorizar(obj);
		
		this.setPrazoStrategy(regra);
		
		
		ValidationException exception = new ValidationException("errors");
		
		if(obj.getCliente().getId() == null) {//Movimentação sem cliente
			obj.setCliente(null);
		}
		
		if(obj.getDataCobranca() == null) {
			exception.addError("dataCobranca", "campo vazio");
		}
		
		if(obj.getDataMovimentacao() == null) {
			exception.addError("dataMovimentacao", "campo vazio");
		}
		
		if(obj.getDataCobranca() != null && obj.getDataMovimentacao() != null) {
			if(obj.getDataCobranca().isBefore(obj.getDataMovimentacao())) {
				exception.addError("datas", "A data de cobranca não pode ser anterior a data de movimentacao");
			}
		}
		
		if(obj.getDescricao() == null) {
			exception.addError("descricao", "campo vazio");
		}
		
		if(obj.getStatus() == null) {
			exception.addError("status", "campo vazio");
		}
		
		if(obj.getValor() == null) {
			exception.addError("valor", "campo vazio");
		}
		
		if(!validarMovimentacao(obj)) {
			exception.addError("prazo", "movimentacao invalida");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		
		if(obj.getStatus() == MovimentacaoStatus.ENTRADA) {
			prop.setSaldo(prop.getSaldo() + obj.getValor());
        } else if(obj.getStatus() == MovimentacaoStatus.SAIDA){
			prop.setSaldo(prop.getSaldo() - obj.getValor());
        }
		
		Movimentacao mov = repository.save(obj);
		
        return mov;
    }
	
	public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
        	throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
	
	public Movimentacao update(Long id, Movimentacao obj) {
		Movimentacao entity = null;
		try {
            entity = repository.getById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch(EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

	private void updateData(Movimentacao entity, Movimentacao obj) {
		System.out.println(obj.getCategoria());
		//TODO
		ValidationException exception = new ValidationException("errors");
		
		if(obj.getCliente().getId() == null) {//Movimentação sem cliente
			obj.setCliente(null);
		}
		
		if(obj.getCliente() == null) {//Movimentação sem cliente
			obj.setCliente(null);
		}
		
		if(obj.getDataCobranca() == null) {
			exception.addError("dataCobranca", "campo vazio");
		}
		
		if(obj.getDataMovimentacao() == null) {
			exception.addError("dataMovimentacao", "campo vazio");
		}
		
		if(obj.getDataCobranca() != null && obj.getDataMovimentacao() != null) {
			if(obj.getDataCobranca().isBefore(obj.getDataMovimentacao())) {
				exception.addError("datas", "A data de cobranca não pode ser anterior a data de movimentacao");
			}
		}
		
		if(!validarMovimentacao(obj)) {
			exception.addError("prazo", "movimentacao invalida");
		}
		
		if(obj.getDescricao() == null) {
			exception.addError("descricao", "campo vazio");
		}
		
		if(obj.getStatus() == null) {
			exception.addError("status", "campo vazio");
		}
		
		if(obj.getValor() == null) {
			exception.addError("valor", "campo vazio");
		}
		
		if(obj.getCategoria() == null) {
			exception.addError("categoria", "campo vazio");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}	
		
		entity.setDataCobranca(obj.getDataCobranca());
		entity.setValor(obj.getValor());
		entity.setDescricao(obj.getDescricao());
		entity.setCliente(obj.getCliente());
		entity.setStatus(obj.getStatus());
		entity.setCategoria(obj.getCategoria());
		
	}
	
	public Double getBalancoDiario() {	
		return getBalancoDoDia(LocalDate.now());
	}

	public List<Double> getBalancoRetroativamente(int diasAVer) {
		List<Double> saldos = new ArrayList<>();

		for(LocalDate date = LocalDate.now().minusDays(diasAVer); date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()); date = date.plusDays(1)) {
			saldos.add(getBalancoDoDia(date));
		}
		
		return saldos;
	}
	
	public List<Movimentacao> getMovimentacoesRetroativamente(int diasAVer) {
		List<Movimentacao> movimentacoes = new ArrayList<>();
		List<Movimentacao> movimentacoesDoDia = new ArrayList<>();

		for(LocalDate date = LocalDate.now().minusDays(diasAVer); date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()); date = date.plusDays(1)) {
			movimentacoesDoDia = repository.findAllByDataMovimentacao(date);
			for(Movimentacao m : movimentacoesDoDia) {
				if(m.isPago()) 
					movimentacoes.add(m);
			}
		}
		
		return movimentacoes;
	}
	
	public Double getBalancoDoMes() {
		LocalDate dataAtual = LocalDate.now();
		int mesAtual = dataAtual.getMonthValue();
		Double balanco = 0.0;
		List<Movimentacao> movimentacoes = repository.findAll();
		for(Movimentacao m : movimentacoes) {
			if(	m.getDataMovimentacao().getYear() == dataAtual.getYear() && m.getDataMovimentacao().getMonthValue() == mesAtual && m.isPago()) {
				if(m.getStatus() == MovimentacaoStatus.SAIDA)
					balanco -= m.getValor();
				if(m.getStatus() == MovimentacaoStatus.ENTRADA)
					balanco += m.getValor();
			}
		}
		return balanco;
	}
	
	public Double getGastoDoMes() {
		LocalDate dataAtual = LocalDate.now();
		int mesAtual = dataAtual.getMonthValue();
		Double gasto = 0.0;
		List<Movimentacao> movimentacoes = repository.findAll();
		for(Movimentacao m : movimentacoes) {
			if(	m.getDataMovimentacao().getYear() == dataAtual.getYear() && m.getDataMovimentacao().getMonthValue() == mesAtual) {
				if(m.getStatus() == MovimentacaoStatus.SAIDA)
					gasto += m.getValor();
			}
		}
		return gasto;
	}
	
	public Double getBalancoDaSemana() {
		LocalDate dataAtual = LocalDate.now();
		LocalDate ultimoDiaSemana = dataAtual;
		List<Movimentacao> movimentacoes = repository.findAll(); 
		Double balanco = 0.0;
		while(ultimoDiaSemana.getDayOfWeek() != DayOfWeek.SATURDAY) {
			ultimoDiaSemana = ultimoDiaSemana.plusDays(1);
		}
		LocalDate antesSemana = ultimoDiaSemana.minus(7, ChronoUnit.DAYS);
		
		for(Movimentacao m : movimentacoes) {
			if(	m.getDataMovimentacao().isAfter(antesSemana) && m.getDataMovimentacao().isBefore(ultimoDiaSemana.plusDays(1)) && m.isPago()) {
				if(m.getStatus() == MovimentacaoStatus.SAIDA)
					balanco -= m.getValor();
				if(m.getStatus() == MovimentacaoStatus.ENTRADA)
					balanco += m.getValor();
			}
		}
		
		return balanco;
	}
	
	public Double getGastoDaSemana() {
		LocalDate dataAtual = LocalDate.now();
		LocalDate ultimoDiaSemana = dataAtual;
		List<Movimentacao> movimentacoes = repository.findAll(); 
		Double gasto = 0.0;
		while(ultimoDiaSemana.getDayOfWeek() != DayOfWeek.SATURDAY) {
			ultimoDiaSemana = ultimoDiaSemana.plusDays(1);
		}
		LocalDate antesSemana = ultimoDiaSemana.minus(7, ChronoUnit.DAYS);
		
		for(Movimentacao m : movimentacoes) {
			if(	m.getDataMovimentacao().isAfter(antesSemana) && m.getDataMovimentacao().isBefore(ultimoDiaSemana.plusDays(1)) && m.isPago()) {
				if(m.getStatus() == MovimentacaoStatus.SAIDA)
					gasto += m.getValor();

			}
		}
		
		return gasto;
	}
		
	public Double getBalancoDoDia(LocalDate data) {

		List<Movimentacao> movimentacoes = repository.findAllByDataMovimentacao(data);
		Double saldo = 0.0;
		
		for(Movimentacao mov : movimentacoes) {
			if(mov.getStatus() == MovimentacaoStatus.ENTRADA && mov.isPago())
				saldo += mov.getValor();
	        else if (mov.getStatus() == MovimentacaoStatus.SAIDA && mov.isPago())
	        	saldo -= mov.getValor();
		
		}
		
		return saldo;
	}
	
	public List<Movimentacao> getMovimentacoesFiadoHoje() {
		
		
		LocalDate dataLocal = LocalDate.now();
		

		List<Movimentacao> movimentacoes = repository.findAllByDataCobranca(dataLocal);
		List<Movimentacao> movimentacoesFiado = new ArrayList<Movimentacao>();
		
		for(Movimentacao mov : movimentacoes) {
			if(!mov.isPago()) {
				movimentacoesFiado.add(mov);
			}
		}
		
		return movimentacoesFiado;
	}
	
	public List<Movimentacao> getMovimentacoesFiado(){
		
		List<Movimentacao> movimentacoes = repository.findAll();
		List<Movimentacao> movimentacoesFiado = new ArrayList<Movimentacao>();
		
		for(Movimentacao mov : movimentacoes) {
			if(!mov.isPago()) {
				movimentacoesFiado.add(mov);
			}
		}
		
		return movimentacoesFiado;
	}
	
	public List<Movimentacao>findAllByCliente(Cliente cliente){
		return repository.findAllByCliente(cliente);
	}
	
	public Movimentacao pagarMovimentacao(Long id) {
		Movimentacao mov = findById(id);
		
		mov.setPago(true);
		update(id, mov);
		
		return mov;
	}
	
	public List<Movimentacao> getDividas(){
		List<Movimentacao> movimentacoesSaida = repository.findAll();
		List<Movimentacao> movimentacoesDivida = new ArrayList<>();
		
		for(Movimentacao mov : movimentacoesSaida) {
			if((!mov.isPago()) && mov.getStatus() == MovimentacaoStatus.SAIDA) {
				movimentacoesDivida.add(mov);
			}
		}
		
		return movimentacoesDivida;
	}
	
	public Double getGastosPorCategoria(Categoria cat) {
		List<Movimentacao> movimentacoes = getMovimentacoesRetroativamente(30);
		Double gastos = 0.0;
		
		for(Movimentacao mov : movimentacoes) {
			if(mov.isPago() && mov.getStatus() == MovimentacaoStatus.SAIDA && mov.getCategoria() == cat) {
				gastos += mov.getValor();
			}
		}
		return gastos;
	}
	
	public Map<String, Double> getGastosCategoricamente() {
		Map<String, Double> map = new HashMap<String, Double>();
		
		for (Categoria cat : Categoria.values()) { 
			map.put(cat.name(), getGastosPorCategoria(cat));
		}
				
		return map;
	}
	
}
