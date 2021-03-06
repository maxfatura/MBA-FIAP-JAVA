package telegram_bot.service;

import telegra_bot.machine.Mensagem;
import telegra_bot.machine.impl.Chuva;
import telegra_bot.machine.impl.FinalSemana;
import telegra_bot.machine.impl.NotFound;
import telegra_bot.machine.impl.Nublado;
import telegra_bot.machine.impl.Previsao;
import telegra_bot.machine.impl.Sol;
import telegra_bot.machine.impl.Temperatura;
import telegram_bot.dto.Forecast;
import telegram_bot.dto.TimeInformation;

public class ProcessadorMensagem {
	
	private Mensagem tipoMensagem;
	
	public Mensagem processar(String pergunta) {
		if(pergunta.matches("(?i).*ch.*v.*") && !(pergunta.matches("(?i).*fi.*") || pergunta.matches("(?i).*s.*bado.*"))) {
			 tipoMensagem = new Chuva();
		} else if(pergunta.matches("(?i).*sol.*")) { 
			tipoMensagem = new Sol();
		} else if(pergunta.matches("(?i).*prev.*") || pergunta.matches("(?i).*tempo.*")) {
			tipoMensagem = new Previsao();
		} else if(pergunta.matches("(?i).*tempe.*") || pergunta.matches("(?i).*grau.*")) {
			tipoMensagem = new Temperatura();
		} else if( pergunta.matches("(?i).*nubl.*")) {
			tipoMensagem = new Nublado();
		} else if( pergunta.matches("(?i).*fi.*") || pergunta.matches("(?i).*s.*bado.*")) {
			tipoMensagem = new FinalSemana();
		} else {
			tipoMensagem = new NotFound();
		}

		return tipoMensagem;
	}
	
	public String getMensagem(String pergunta, TimeInformation timeInformation) {
		String resposta = "";
		Mensagem mensagem = processar(pergunta);
		if(mensagem.isChuva()) {
			if(timeInformation.getResults().getDescription().matches("(?i).*ch.*v.*")) {
				resposta = "Sim, hoje esta chovendo e a Temperatura ? "+timeInformation.getResults().getTemp()+" Graus";
			} else {
				resposta = "N?o, hoje n?o esta chovendo, a condi??o do tempo agora ? "+timeInformation.getResults().getDescription()+" e a Temperatura ? "+timeInformation.getResults().getTemp()+" Graus";
			}
		}
		if(mensagem.isNublado()) {
			if(timeInformation.getResults().getDescription().matches("(?i).*nubl.*")) {
				resposta = "Sim, hoje esta nublado e a Temperatura ? "+timeInformation.getResults().getTemp()+" Graus";
			} else {
				resposta = "N?o, hoje n?o esta nublado, a condi??o do tempo agora ? "+timeInformation.getResults().getDescription()+" e a Temperatura ? "+timeInformation.getResults().getTemp()+" Graus";
			}
		}
		if(mensagem.isPrevisao()) {
			resposta = "Sim, a previs?o do tempo ? "+timeInformation.getResults().getDescription()+" e a temperatura esta em "+timeInformation.getResults().getTemp()+" Graus";
		}
		if(mensagem.isSol()) {
			if(timeInformation.getResults().getDescription().matches("(?i).*sol.*")) {
				resposta = "Sim, hoje esta sol e a Temperatura ? "+timeInformation.getResults().getTemp()+" Graus";
			} else {
				resposta = "N?o, hoje n?o esta sol, a condi??o do tempo agora ? "+timeInformation.getResults().getDescription()+" e a Temperatura ? "+timeInformation.getResults().getTemp()+" Graus";
			}
		}
		if(mensagem.isTemperatura()) {
			resposta = "Sim, a previs?o do tempo ? "+timeInformation.getResults().getDescription()+" e a temperatura esta em "+timeInformation.getResults().getTemp()+" Graus";
		}
		if(mensagem.isNotFound()) {
			resposta = "N?o entendi sua pergunta, mas vou te informar a previs?o do tempo: "+timeInformation.getResults().getDescription()+" e a temperatura esta em "+timeInformation.getResults().getTemp()+" Graus. Quer saber sobre chuva, tempo nublado, previs?o do tempo, tente outra pergunta";
		}
		if(mensagem.isFinalDeSemana()) {
			resposta = getFinalDeSemana(timeInformation);
		}
		
  	  return resposta;
	}
	
	public String getFinalDeSemana(TimeInformation timeInformation) {
		String mensagemRetorno = null;
		for(Forecast item : timeInformation.getResults().getForecast()) {
			if((item.getWeekday().equals("S?b") || item.getWeekday().equals("Sat")) && !timeInformation.getResults().getDate().contains(item.getDate().substring(3,5)+"/"+item.getDate().substring(1,3))) {
				mensagemRetorno = "Este final de semana dia "+item.getDate().substring(3,5)+"/"+item.getDate().substring(1,3)+" estar?: com a m?xima: "+item.getMax()+" m?nima: "+item.getMin();
			}
		}
		return mensagemRetorno;
	}

}
