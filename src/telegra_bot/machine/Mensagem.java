package telegra_bot.machine;

public interface Mensagem {
	
	Boolean isTemperatura();
	Boolean isChuva();
	Boolean isSol();
	Boolean isNublado();
	Boolean isPrevisao();
	Boolean isNotFound();
	String getMensagem();
	Boolean isFinalDeSemana();
	void setMensagem(String mensagem);
}
