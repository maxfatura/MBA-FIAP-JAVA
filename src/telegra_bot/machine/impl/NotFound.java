package telegra_bot.machine.impl;

import telegra_bot.machine.Mensagem;

public class NotFound implements Mensagem {

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
		return false;
	}

	@Override
	public Boolean isNotFound() {
		return true;
	}

	@Override
	public Boolean isFinalDeSemana() {
		return false;
	}

}
