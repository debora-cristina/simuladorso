package com.educarso.simulador.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.educarso.simulador.domain.ConfigurarPolitica;
import com.educarso.simulador.domain.Processo;
import com.educarso.simulador.enumerador.Estado;
import com.educarso.simulador.view.IPolitica;
import com.educarso.simulador.view.MapaProcessos;

public class SJF extends PoliticaImpl implements IPolitica {

	private int tempo = 0;
	private boolean troca = false;
	private boolean escalonar = true;
	private int posicao = 0;


	public void analisar() {

		if (getPosicao() < getFila().size() - 1) {		
				setPosicao(getPosicao()+1);
		} else {
			getMapa().remove(Estado.EXECUTANDO);
			getMapa().remove(Estado.PRONTO);
			escalonar = false;
		}
	}

	@Override
	public List<MapaProcessos> executar(List<Processo> fila, ConfigurarPolitica config) {

		inserirFila(fila);
		List<Processo> filaProcessos = new ArrayList<Processo>();
		filaProcessos = getFila();
		setPosicao(0);
		setConfiguracoes(config);
		setFila(ordenar(getFila(), 2));
		int tempoProcessamento = filaProcessos.get(0).getTempoCpu();
		addProcessoPronto();
		while (escalonar) {

			if (getTempo() == tempoProcessamento) {
				troca = true;
				addProcessoFinalizado(getFila().get(getPosicao()).getNomeProcesso(), escalonar);
				analisar();
				getMapaProcessos().add(new MapaProcessos(getTempo(), copia()));
				tempoProcessamento = tempoProcessamento + getFila().get(getPosicao()).getTempoCpu();

			} else if (analisa(Estado.ENCERRADO) == getFila().size()) {
				escalonar = false;
			} else {

				if (analisa(Estado.ENCERRADO) != getFila().size()) {
					addProcessoExecutando(getFila().get(getPosicao()).getNomeProcesso(), escalonar);
				}
				getMapaProcessos().add(new MapaProcessos(getTempo(), copia()));
				setTempo(getTempo() + 1);

			}

		}


		return getMapaProcessos();

	}

}
