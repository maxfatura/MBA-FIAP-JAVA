package telegram_bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import telegram_bot.dto.TimeInformation;
import telegram_bot.service.ProcessadorMensagem;

public class Main {
	
	private static final int HTTP_COD_SUCESSO = 200;
	private static TimeInformation timeInformation;


	public static void main(String[] args) throws IOException {
		
		
		// Criacao do objeto bot com as informacoes de acesso.
		TelegramBot bot = new TelegramBot("1757162368:AAHWusQarLBDDO2Q7mlA-Iugg_nArJlUjG8");

		// Objeto responsavel por receber as mensagens.
		GetUpdatesResponse updatesResponse;

		// Objeto responsavel por gerenciar o envio de respostas.
		SendResponse sendResponse;

		// Objeto responsavel por gerenciar o envio de acoes do chat.
		BaseResponse baseResponse;
		
		ProcessadorMensagem processadorMensagem = new ProcessadorMensagem();

		// Controle de off-set, isto e, a partir deste ID sera lido as mensagens
		// pendentes na fila.
		int m = 0;
		
		// Loop infinito pode ser alterado por algum timer de intervalo curto.
		while (true) {
			// Executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial).
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// Lista de mensagens.
			List<Update> updates = updatesResponse.updates();

			// Analise de cada acao da mensagem.
			for (Update update : updates) {

				// Atualizacao do off-set.
				m = update.updateId() + 1;
				if(update.message().text().equals("/start")) {
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Olá, vamos falar sobre previsão do tempo ? qual a sua dúvida ?"));
					continue;
				} 
				// Carregar previsão do tempo
				loadTimeInformation();
				//Processar Mensagem
				String mensagem = processadorMensagem.getMensagem(update.message().text(), timeInformation);
				// Envio da mensagem de resposta.
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), mensagem));

				System.out.println("Recebendo mensagem: " + update.message().text());

				// Envio de "Escrevendo" antes de enviar a resposta.
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

				// Verificacao de acao de chat foi enviada com sucesso.
				System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

				
				// Verificacao de mensagem enviada com sucesso.
				System.out.println("Mensagem Enviada? " + sendResponse.isOk());
			}
		}
	}
	
	public static void loadTimeInformation() throws IOException {
		URL url = new URL("https://api.hgbrasil.com/weather?key=c71a8c2d");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

		if (con.getResponseCode() != HTTP_COD_SUCESSO) {
			throw new RuntimeException("HTTP error code : "+ con.getResponseCode());
		}
		
		JSONObject json = null;
		JSONParser parser=new JSONParser();
		try {
			json = (JSONObject) parser.parse(br);
		} catch (IOException | ParseException e) {
			throw new RuntimeException("HTTP error code : "+ e.getMessage());
		}
		
		Gson gson = new Gson(); 
		timeInformation = gson.fromJson(json.toJSONString(), TimeInformation.class);
	}
}
