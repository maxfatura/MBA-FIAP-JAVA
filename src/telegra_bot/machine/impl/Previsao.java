package telegra_bot.machine.impl;

import telegra_bot.machine.Mensagem;

public class Previsao implements Mensagem {

	private String mensagem;
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public Boolean isTemperatura() {
		return false;
	}

	@Override
	public Boolean isChuva() {
		return false;
	}

	@Override
	public Boolean isSol() {
		return false;
	}

	@Override
	public Boolean isNublado() {
		return false;
	}

	@Override
	public Boolean isPrevisao() {
		return true;
	}

	@Override
	public Boolean isNotFound() {
		return false;
	}

}