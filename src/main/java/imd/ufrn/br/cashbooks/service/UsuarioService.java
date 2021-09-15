package imd.ufrn.br.cashbooks.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import imd.ufrn.br.cashbooks.model.Usuario;
import imd.ufrn.br.cashbooks.repository.UsuarioRepository;
import imd.ufrn.br.cashbooks.service.exceptions.DatabaseException;
import imd.ufrn.br.cashbooks.service.exceptions.ResourceNotFoundException;
import imd.ufrn.br.cashbooks.service.exceptions.ValidationException;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository repository;
	
	public List<Usuario> findAll(){
		return repository.findAll();
	}
	
	public Usuario findById(Long id) {
		Optional<Usuario> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public Usuario findByEmail(String email) {
		Optional<Usuario> obj = repository.findByEmail(email);
		
		return obj.orElseThrow(() -> new ResourceNotFoundException(email));
	}
	
	public Usuario insert(Usuario obj) {
		ValidationException exception = new ValidationException("errors");
		
		if(obj.getNome() == null) {
			exception.addError("nome", "Campo vazio");
		}
				
		if(obj.getEmail() == null) {
			exception.addError("e-mail", "Campo vazio");
		}
		
		if(obj.getCnpj() == null ) {
			exception.addError("cnpj", "Campo vazio");		
		} else if(!CNPJService.validaCNPJ(obj.getCnpj())) {
			exception.addError("cnpj", "Valor inválido");
		}
		
		
				
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
        return repository.save(obj);
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
	
	public Usuario update(Long id, Usuario obj) {
		Usuario entity = null;
		try {
            entity = repository.getById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch(EntityNotFoundException e) {
        	throw new ResourceNotFoundException(id);
        }
    }

	private void updateData(Usuario entity, Usuario obj) {
		ValidationException exception = new ValidationException("errors");
		
		if(obj.getNome() == null) {
			exception.addError("nome", "Campo vazio");
		}
		
		
		if(obj.getEmail() == null) {
			exception.addError("e-mail", "Campo vazio");
		}
		
		if(obj.getCnpj() == null ) {
			exception.addError("cnpj", "Campo vazio");		
		} else if(!CNPJService.validaCNPJ(obj.getCnpj())) {
			exception.addError("cnpj", "Valor inválido");
		}
		
		
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
				
		entity.setNome(obj.getNome());
		entity.setEmail(obj.getEmail());
		entity.setCnpj(obj.getCnpj());		

	}

	public Usuario getUsuario() {
		Usuario user;
		if(repository.findById(1L).isEmpty()) {
			user = new Usuario();
			repository.save(user);
		} else {
			user = repository.findById(1L).get();
		}
		return user;
	}
	
	
}
