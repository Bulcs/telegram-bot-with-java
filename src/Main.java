import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {

	public static void main(String[] args) {

		//Criação do objeto bot com as informações de acesso
		TelegramBot bot = TelegramBotAdapter.build("909350681:AAHgxlxiLrG7oZtaC6EwyBUrPEbMjVILUCA");

		//objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;
		//objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;
		//objeto responsável por gerenciar o envio de ações do chat
		BaseResponse baseResponse;
		
		//testando
		GetUpdatesResponse usersResponse;
		
		//controle de off-set, isto é, a partir deste ID serão lido as mensagens pendentes na fila
		int m=0;
		//criacao do controlador
		Controller controll = new Controller();
		//loop infinito pode ser alterado por algum timer de intervalo curto
		while (true){

			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			//lista de mensagens
			List<Update> updates = updatesResponse.updates();
			
			//análise de cada ação da mensagem
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				
				//System.out.println("Recebendo mensagem:"+ update.message().text());
				
	
				if(update.message().text().equals("/cadastrar_bem")) {
							bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome do bem: "));
							//AINDA NAO TO CONSEGUINDO PEGAR A MENSAGEM, TA PEGANDO APENAS O /CADASTRAR_BEM E IMPLEMENTANDO EM NOME, DESCRICAO E CODE
							String nome = update.message().text();
							
							bot.execute(new SendMessage(update.message().chat().id(),"Digite a descrição do bem: "));
							String descricao = update.message().text();
							
							bot.execute(new SendMessage(update.message().chat().id(),"Digite o codigo do bem: "));
							String code = update.message().text();
							
							
							controll.registerGood("nome", "desc", "code");
				}
				else if(update.message().text().equals("/listar_bens")) {
					try {
						ArrayList <Goods> goodsList = controll.listGoods();
						for (Goods goods : goodsList){
							String name = goods.getGoodsName();
							String description = goods.getGoodsDescription();
							String code = goods.getGoodsCode();
							
							bot.execute(new SendMessage(update.message().chat().id(), 
									name + " " + description + " " + code));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				/*
				System.out.println("mensagem enviada: " + update.message().text());
				//envio de "Escrevendo" antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				//verificação de ação de chat foi enviada com sucesso
				System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
				
				//envio da mensagem de resposta
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Teste"));
				//verificação de mensagem enviada com sucesso
				System.out.println("Mensagem Enviada?" +sendResponse.isOk());
				*/
			}

		}

	}

}
