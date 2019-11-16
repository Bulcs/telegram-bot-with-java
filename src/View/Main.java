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
import Exceptions.EmptyList;
import Exceptions.OffTheList;
import Model.Category;
import Model.Good;
import Model.Location;
import Model.STATE;

public class Main {
	public static void main(String[] args) {
		
		/*
		 *Declara��o de vari�veis que ser�o utilizadas para receber os valores
		 *que o usu�rio digita.
		 * */
		STATE status = STATE.NULL;
		String name = "null";
		String description = "null";
		String code = "null";
		String location = "null";
		String category = "null";

		//Cria��o do objeto bot com as informa��es de acesso
		TelegramBot bot = TelegramBotAdapter.build("909350681:AAHgxlxiLrG7oZtaC6EwyBUrPEbMjVILUCA");

		/*
		 * GetUpdatesResponse: objeto respons�vel por receber as mensagens
		 * SendResponse: objeto respons�vel por gerenciar o envio de respostas
		 * BaseResponse: respons�vel por gerenciar o envio de a��es do chat
		 * */
		GetUpdatesResponse updatesResponse;
		//SendResponse sendResponse:
		//BaseResponse baseResponse;
		
		
		//controle de off-set, isto �, a partir deste ID ser�o lido as mensagens pendentes na fila
		int m=0;
		
		/*
		 * Controladores para cada uma das classes
		 * */
		Goods controllGoods = new Goods();
		Categories controllCategories = new Categories();
		Locations controllLocations = new Locations();
		
		/*
		 * Loop infinito, pode ser alterado por algum timer de intervalo curto,
		 * 				funciona para receber tudo o que acontece de intera��o bot-usu�rio
		 * 
		 * updatesResponse: executa comando no Telegram para obter as mensagens
		 * 				pendentes a partir de um off-set (limite inicial)
		 * 
		 * updates: lista de mensagens/updates do usu�rio
		 */
		while (true){

			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			List<Update> updates = updatesResponse.updates();
		
			/*
			 * Cada input do usu�rio atrav�s do bot resulta em uma a��o, esse 
			 * 		for funciona para analisar essas a��es e realizar a a��o
			 * 		necess�ria.
			 */
			for (Update update : updates) {
				
				//atualiza��o do off-set
				m = update.updateId()+1;
				
				if(update.message().text().equals("/cadastrar_bem")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome do bem: "));
					status = STATE.WAITING_GOOD_NAME;
				}
					
				else if(update.message().text().equals("/cadastrar_localizacao")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da localiza��o: "));
					status = STATE.WAITING_LOCAL_NAME;
				}
					
				else if(update.message().text().equals("/cadastrar_categoria")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da categoria: "));
					status = STATE.WAITING_CATEGORY_NAME;
				}
						
				else if(update.message().text().equals("/listar_bens")) {
						
					try {
						controllGoods.sizeOfList();
						ArrayList <Good> goodsList = controllGoods.list();
						for (Good goods : goodsList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Nome: " + goods.getGoodsName() + 
									"\n| Descri��o: " + goods.getGoodsDescription() + 
									"\n| C�digo: " + goods.getGoodsCode() + 
									"\n| Localiza��o: " + goods.getGoodsLocation() + 
									"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
						}
					} catch (EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
						status = STATE.NULL;
					}
				}
					
				else if(update.message().text().equals("/listar_localizacoes")  || status == STATE.LIST_LOCATIONS) {		
					
					try {
						controllLocations.sizeOfList();
						ArrayList <Location> locationList = controllLocations.list();
						for (Location local: locationList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Localiza��o: " + local.getLocationName() + 
									"\n| Descri��o: " + local.getLocationDescription()));
							}
					} catch (EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
						
						if(status == STATE.LIST_LOCATIONS) {
							bot.execute(new SendMessage(update.message().chat().id(), "Por favor, cadastre uma localiza��o antes "
									+ "de tentar cadastrar um bem."));
						}

						status = STATE.NULL;
					}
	
					if(status == STATE.LIST_LOCATIONS){
							
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite o nome da localiza��o que est� associada ao bem que voc� deseja cadastrar:"));
						status = STATE.WAITING_LOCATION;
					} else {
						status = STATE.NULL;
					}
				}
				
				else if(update.message().text().equals("/listar_categorias")  || status == STATE.LIST_CATEGORIES) {		
					try {
						
						controllCategories.sizeOfList();
						ArrayList <Category> categoriesList = controllCategories.list();
						for (Category gCategory: categoriesList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Categoria: " + gCategory.getCategoryName() + 
									"\n| Descri��o: " +	gCategory.getCategoryDescription() +
									"\n| C�digo: " + gCategory.getCategoryCode()));
							}
					} catch (EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
						
						if(status == STATE.LIST_CATEGORIES) {
							bot.execute(new SendMessage(update.message().chat().id(), "Por favor, cadastre uma categoria antes "
									+ "de tentar cadastrar um bem."));
						}
						
						status = STATE.NULL;
					}
	
					if(status == STATE.LIST_CATEGORIES){
							
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite o nome da categoria que est� associada ao bem que "
								+ "voc� deseja cadastrar:"));
						status = STATE.WAITING_CATEGORY;
					} else {
						status = STATE.NULL;
					}
				}
				
				
				/*
				 * Aqui come�am os STATES utilizados para registrar um novo bem
				 * */		
				else if(status == STATE.WAITING_GOOD_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descri��o do bem: "));
					
					status = STATE.WAITING_GOOD_DESCRIPTION;
					
				} else if (status == STATE.WAITING_GOOD_DESCRIPTION) {
					
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
						"Digite o c�digo do bem:"));
				
					status = STATE.WAITING_GOOD_CODE;
					
				} else if(status == STATE.WAITING_GOOD_CODE) {
					
					code = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Aperte qualquer tecla para listar as localiza��es"));	
						
					status = STATE.LIST_LOCATIONS;
					
				/*@TOFIX*/	
				} else if(status == STATE.WAITING_LOCATION) {
						
					location = update.message().text();		
					
					try{
						controllLocations.findByName(location);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as categorias")); 
					
						status = STATE.LIST_CATEGORIES;
						
					} catch (OffTheList e) {
						
						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localiza��es"));
						
						status = STATE.LIST_LOCATIONS;
					}

					
				} else if(status == STATE.WAITING_CATEGORY) {
					category = update.message().text();
					
					try{
						controllCategories.findByName(category);
						bot.execute(new SendMessage(update.message().chat().id(),"Bem cadastrado com sucesso!")); 
						controllGoods.register(name, description, code, location, category);
						status = STATE.NULL;
						
					} catch(OffTheList e) {
						
						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as categorias"));
						
						status = STATE.LIST_CATEGORIES;
					}
				}
					
				
				/*
				 * Aqui come�am os STATES utilizados para registrar uma nova localiza��o
				 * */
				else if(status == STATE.WAITING_LOCAL_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descri��o do local: "));
						
					status = STATE.WAITING_LOCAL_DESCRIPTION;
						
				} else if (status == STATE.WAITING_LOCAL_DESCRIPTION) {
						
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Cadastrado com sucesso!"));	
						
					controllLocations.register(name, description);
					
					status = STATE.NULL;
				}
				
				
				/*
				 * Aqui come�am os STATES utilizados para registrar uma nova categoria
				 * */	
				else if(status == STATE.WAITING_CATEGORY_NAME) {
					name = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(), 
							"Digite a descri��o da categoria: "));
					
					status = STATE.WAITING_CATEGORY_DESCRIPTION;
						
				} else if (status == STATE.WAITING_CATEGORY_DESCRIPTION) {
						
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Digite o c�digo da categoria:"));
						
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


