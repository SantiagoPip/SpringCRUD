package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface IClienteSerivce {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	public Cliente findOne(Long id);
	public void delete(Long id);
	public Page<Cliente>findAll(Pageable pageable);//Se importa del data.domain.pageable

}
