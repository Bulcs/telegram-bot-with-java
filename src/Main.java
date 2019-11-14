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
		
		/*
		 *Declaração de variáveis que serão utilizadas para receber os valores
		 *que o usuário digita.
		 * */
		STATE status = STATE.NULL;
		String name = "null";
		String description = "null";
		String code = "null";
		String location = "null";

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

				/* @TOFIX
				 * Tem que mudar aqui essa definição de entrada (porque nao
				 * da pra ficar botando um OU pra cada estado de STATE)
				 * */
				if(status == STATE.NULL || status == STATE.LIST) {
					
					
					if(update.message().text().equals("/cadastrar_bem")) {
						bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome do bem: "));
						status = STATE.WAITING_GOOD_NAME;
					}
					
					else if(update.message().text().equals("/cadastrar_localizacao")) {
						bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da localização: "));
						status = STATE.WAITING_LOCAL_NAME;
					}
					
					else if(update.message().text().equals("/cadastrar_categoria")) {
						bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da categoria: "));
						status = STATE.WAITING_CATEGORY_NAME;
					}
						
					else if(update.message().text().equals("/listar_bens")) {
						
						try {
							ArrayList <Goods> goodsList = controll.listGoods();
							for (Goods goods : goodsList){
								bot.execute(new SendMessage(update.message().chat().id(), 
										goods.getGoodsName() + " " +
										goods.getGoodsDescription() +
										" " + goods.getGoodsCode()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					else if(update.message().text().equals("/listar_localizacoes")  || status == STATE.LIST) {		
						try {
							ArrayList <Location> locationList = controll.listLocations();
							for (Location local: locationList){
								bot.execute(new SendMessage(update.message().chat().id(), 
										local.getLocationName() + " " +
										local.getLocationDescription()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(status == STATE.LIST){
							
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Digite o nome da localização que está associada ao bem que você deseja cadastrar:"));
							status = STATE.WAITING_LOCATION;
						} else {
							status = STATE.NULL;
						}
					}
				}
				
						
				else if(status == STATE.WAITING_GOOD_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descrição do bem: "));
					
					status = STATE.WAITING_GOOD_DESCRIPTION;
					
				} else if (status == STATE.WAITING_GOOD_DESCRIPTION) {
					
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Digite o código do bem:"));
					
					status = STATE.WAITING_GOOD_CODE;
					
				} else if(status == STATE.WAITING_GOOD_CODE) {
					
					code = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Aperte qualquer tecla para listar as localizaçôes"));	
					
					controll.registerGood(name, description, code);
					
					status = STATE.LIST;
				/*@TOFIX*/	
				} else if(status == STATE.WAITING_LOCATION) {
					
					location = update.message().text();
					 
					//aqui precisa verificar se a pessoa digitou algo que existe
					status = STATE.NULL;
				}
				
				else if(status == STATE.WAITING_LOCAL_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descrição do local: "));
					
					status = STATE.WAITING_LOCAL_DESCRIPTION;
					
				} else if (status == STATE.WAITING_LOCAL_DESCRIPTION) {
					
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Cadastrado com sucesso!"));	
					
					controll.registerLocation(name, description);
					
					status = STATE.NULL;
				}
				
				else if(status == STATE.WAITING_CATEGORY_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descrição da categoria: "));
					
					status = STATE.WAITING_CATEGORY_DESCRIPTION;
					
				} else if (status == STATE.WAITING_CATEGORY_DESCRIPTION) {
					
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Digite o código da categoria:"));
					
					status = STATE.WAITING_CATEGORY_CODE;
					
				} else if(status == STATE.WAITING_CATEGORY_CODE) {
					
					code = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Cadastrado com sucesso!"));			
					controll.registerCategory(name, description, code);
					
					status = STATE.NULL;
				}

			}

		}

	}

}
