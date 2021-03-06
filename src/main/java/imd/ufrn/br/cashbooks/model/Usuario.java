package imd.ufrn.br.cashbooks.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	protected Long id;

	protected String nome;
	protected String cnpj;

	protected String email;
	protected double saldo=0;
	//protected String senha;


	protected Usuario(Long id, String nome, String email, double saldo, String cnpj ) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.saldo = saldo;
		this.cnpj = cnpj;
		//this.senha = senha;
	}

	public Usuario() {
		
	}
	
	@JsonIgnore
	@OneToMany(mappedBy = "usuario")
	protected List<Movimentacao> movimentacoes = new ArrayList<>();
	
	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}
	

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public double getSaldo() {
		return saldo;
	}


	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
