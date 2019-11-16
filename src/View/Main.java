package View;
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

import Controller.Categories;
import Controller.Goods;
import Controller.Locations;
import Model.Category;
import Model.Good;
import Model.Location;
import Model.STATE;

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
		String category = "null";

		//Criação do objeto bot com as informações de acesso
		TelegramBot bot = TelegramBotAdapter.build("909350681:AAHgxlxiLrG7oZtaC6EwyBUrPEbMjVILUCA");

		/*
		 * GetUpdatesResponse: objeto responsável por receber as mensagens
		 * SendResponse: objeto responsável por gerenciar o envio de respostas
		 * BaseResponse: responsável por gerenciar o envio de ações do chat
		 * */
		GetUpdatesResponse updatesResponse;
		
		//SendResponse sendResponse:
		//BaseResponse baseResponse;
		
		
		//controle de off-set, isto é, a partir deste ID serão lido as mensagens pendentes na fila
		int m=0;
		
		//criacao do controlador
		//Controller controll = new Controller();
		Goods controllGoods = new Goods();
		Categories controllCategories = new Categories();
		Locations controllLocations = new Locations();
		
		/*
		 * Loop infinito, pode ser alterado por algum timer de intervalo curto,
		 * 				funciona para receber tudo o que acontece de interação bot-usuário
		 * 
		 * updatesResponse: executa comando no Telegram para obter as mensagens
		 * 				pendentes a partir de um off-set (limite inicial)
		 * 
		 * updates: lista de mensagens/updates do usuário
		 */
		while (true){

			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			List<Update> updates = updatesResponse.updates();
		
			/*
			 * Cada input do usuário através do bot resulta em uma ação, esse 
			 * 		for funciona para analisar essas ações e realizar a ação
			 * 		necessária.
			 */
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				

				/* @TOFIX
				 * Tem que mudar aqui essa definição de entrada (porque nao
				 * da pra ficar botando um OU pra cada estado de STATE)
				 * */

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
						ArrayList <Good> goodsList = controllGoods.list();
						for (Good goods : goodsList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									goods.getGoodsName() + " " +
									goods.getGoodsDescription() + " " +
									goods.getGoodsCode() + " " +
									goods.getGoodsLocation() + " " +
									goods.getGoodsCategory()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					
				else if(update.message().text().equals("/listar_localizacoes")  || status == STATE.LIST_LOCATIONS) {		
					try {
						ArrayList <Location> locationList = controllLocations.list();
						for (Location local: locationList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									local.getLocationName() + " " +
									local.getLocationDescription()));
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					if(status == STATE.LIST_LOCATIONS){
							
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite o nome da localização que está associada ao bem que você deseja cadastrar:"));
						status = STATE.WAITING_LOCATION;
					} else {
						status = STATE.NULL;
					}
				}
				
				else if(update.message().text().equals("/listar_categorias")  || status == STATE.LIST_CATEGORIES) {		
					try {
						ArrayList <Category> categoriesList = controllCategories.list();
						for (Category gCategory: categoriesList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									gCategory.getCategoryName() + " " +
									gCategory.getCategoryDescription() +
									" " + gCategory.getCategoryCode()));
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					if(status == STATE.LIST_CATEGORIES){
							
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite o nome da categoria que está associada ao bem que "
								+ "você deseja cadastrar:"));
						status = STATE.WAITING_CATEGORY;
					} else {
						status = STATE.NULL;
					}
				}
				
				
				/*
				 * Aqui começam os STATES utilizados para registrar um novo bem
				 * */		
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
						
					status = STATE.LIST_LOCATIONS;
					
				/*@TOFIX*/	
				} else if(status == STATE.WAITING_LOCATION) {
						
					location = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Aperte qualquer tecla para listar as categorias")); 
					//aqui precisa verificar se a pessoa digitou algo que existe
					status = STATE.LIST_CATEGORIES;
					
				} else if(status == STATE.WAITING_CATEGORY) {
					category = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Bem cadastrado com sucesso!")); 
					//aqui precisa verificar se a pessoa digitou algo que existe
					
					controllGoods.register(name, description, code, location, category);
					status = STATE.NULL;
				}
					
				
				/*
				 * Aqui começam os STATES utilizados para registrar uma nova localização
				 * */
				else if(status == STATE.WAITING_LOCAL_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descrição do local: "));
						
					status = STATE.WAITING_LOCAL_DESCRIPTION;
						
				} else if (status == STATE.WAITING_LOCAL_DESCRIPTION) {
						
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Cadastrado com sucesso!"));	
						
					controllLocations.register(name, description);
					
					status = STATE.NULL;
				}
				
				
				/*
				 * Aqui começam os STATES utilizados para registrar uma nova categoria
				 * */	
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
					
					controllCategories.register(name, description, code);
						
					status = STATE.NULL;
				}
			}
		}
	}
}


