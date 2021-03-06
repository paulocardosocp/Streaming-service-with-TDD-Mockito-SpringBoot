package com.streaming.streaming.servico.impl;

import java.util.Calendar;
import java.util.Optional;

import com.streaming.streaming.modelo.Genero;
import com.streaming.streaming.modelo.Producao;
import com.streaming.streaming.repositorio.ProducaoRepository;
import com.streaming.streaming.servico.ProducaoService;
import com.streaming.streaming.servico.exception.AnoFuturoProducaoException;
import com.streaming.streaming.servico.exception.DuplicidadeProducaoException;
import com.streaming.streaming.servico.exception.ProducaoNotFoundException;

public class ProducaoServiceImpl implements ProducaoService {

	private ProducaoRepository producaoRepository;

	public ProducaoServiceImpl(ProducaoRepository producaoRepository) {
		this.producaoRepository = producaoRepository;
	}

	@Override
	public Producao salvar(Producao producao) throws DuplicidadeProducaoException, AnoFuturoProducaoException {
		Optional<Producao> optional = producaoRepository.findByTituloAndAno(producao.getTitulo(), 
				producao.getAno());
		if (optional.isPresent()) {
			throw new DuplicidadeProducaoException();
		}
		
		if (this.isAnoFuturoProducao(producao)) {
			throw new AnoFuturoProducaoException();
		}
		
		return producaoRepository.save(producao);
	}

	
	@Override
	public boolean isAnoFuturoProducao(Producao producao) {
		return (producao.getAno() > Calendar.getInstance().get(Calendar.YEAR));
	}


	@Override
	public Producao buscarPorGenero(String genero) throws ProducaoNotFoundException {	
		Optional<Producao> optional = producaoRepository.findByDescricaoGenero(genero);
		return optional.orElseThrow(() -> new ProducaoNotFoundException());
	}

}
