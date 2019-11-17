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
		int m = 0;
		
		/*
		 * Controladores para cada uma das classes
		 * */
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
						controllGoods.sizeOfList();
						ArrayList <Good> goodsList = controllGoods.list();
						for (Good goods : goodsList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Nome: " + goods.getGoodsName() + 
									"\n| Descrição: " + goods.getGoodsDescription() + 
									"\n| Código: " + goods.getGoodsCode() + 
									"\n| Localização: " + goods.getGoodsLocation() + 
									"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
						}
					} catch (EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
						status = STATE.NULL;
					}
				}

				
				/**
				 * States e verificações de listagem
				 * */
				else if(update.message().text().equals("/listar_localizacoes")  || status == STATE.LIST_LOCATIONS) {		
					
					try {
						controllLocations.sizeOfList();
						ArrayList <Location> locationList = controllLocations.list();
						for (Location local: locationList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Localização: " + local.getLocationName() + 
									"\n| Descrição: " + local.getLocationDescription()));
							}
					} catch (EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
						
						if(status == STATE.LIST_LOCATIONS) {
							bot.execute(new SendMessage(update.message().chat().id(), "Por favor, cadastre uma localização antes "
									+ "de tentar cadastrar um bem."));
						}

						status = STATE.NULL;
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
						
						controllCategories.sizeOfList();
						ArrayList <Category> categoriesList = controllCategories.list();
						for (Category gCategory: categoriesList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Categoria: " + gCategory.getCategoryName() + 
									"\n| Descrição: " +	gCategory.getCategoryDescription() +
									"\n| Código: " + gCategory.getCategoryCode()));
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
								"Digite o nome da categoria que está associada ao bem que "
								+ "você deseja cadastrar:"));
						status = STATE.WAITING_CATEGORY;
					} else {
						status = STATE.NULL;
					}
				}
				
				/**
				 * States e verificações de comandos de busca 
				 * */
				else if(update.message().text().equals("/buscar_bem_por_codigo")) {

					bot.execute(new SendMessage(update.message().chat().id(),
						"Digite o codigo do bem que deseja buscar: "));
					status = STATE.WAITING_GOOD_BY_CODE;		
				}

				else if(update.message().text().equals("/buscar_bem_por_nome")) {

					bot.execute(new SendMessage(update.message().chat().id(),
						"Digite o nome do bem que deseja buscar: "));
					status = STATE.WAITING_GOOD_BY_NAME;
				}

				else if(update.message().text().equals("/buscar_bem_por_descricao")) {

					bot.execute(new SendMessage(update.message().chat().id(),
						"Digite o descricao do bem que deseja buscar: "));
					status = STATE.WAITING_GOOD_BY_DESCRIPTION;
				}
				
				
				/**
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
					
					try{
						controllLocations.findByName(location);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as categorias")); 
					
						status = STATE.LIST_CATEGORIES;
						
					} catch (OffTheList e) {
						
						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizaçôes"));
						
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
				
				/**
				 * Aqui começam os STATES utilizados para registrar uma nova localização
				 * */
				else if(status == STATE.WAITING_LOCAL_NAME) {
					name = update.message().text();
					
					try {
						controllLocations.findByName(name);
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Já existe uma localização cadastrada com esse nome, tente novamente."));
						status = STATE.NULL;
					} catch(OffTheList e) {
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite a descrição do local: "));
							
						status = STATE.WAITING_LOCAL_DESCRIPTION;
					}

						
				} else if (status == STATE.WAITING_LOCAL_DESCRIPTION) {
			
					description = update.message().text();
					bot.execute(new SendMessage(update.message().chat().id(),
							"Cadastrado com sucesso!"));	
						
					controllLocations.register(name, description);
					
					status = STATE.NULL;
				}
				
				
				/***
				 * Aqui começam os STATES utilizados para registrar uma nova categoria
				 * */	
				else if(status == STATE.WAITING_CATEGORY_NAME) {
					name = update.message().text();
					
					try {
						controllCategories.findByName(name);
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Já existe uma categoria cadastrada com esse nome, tente novamente."));
						status = STATE.NULL;
					} catch(OffTheList e) {
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite a descrição da categoria: "));
							
						status = STATE.WAITING_CATEGORY_DESCRIPTION;
					}

						
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
				
				/**
				 * Aqui começam os STATES de busca
				 * */
				else if (status == STATE.WAITING_GOOD_BY_CODE) {

					code = update.message().text();

					try{
						controllGoods.findByName(code);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar a localizacao")); 

						status = STATE.LIST_LOCATIONS;
					} catch (OffTheList e) {

						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));

						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizaçôes"));

						status = STATE.LIST_LOCATIONS;
					}
				}

				else if (status == STATE.WAITING_GOOD_BY_NAME) {

					name = update.message().text();

					try{
						controllGoods.findByName(name);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar a localizacao")); 

						status = STATE.LIST_LOCATIONS;
					} catch (OffTheList e) {

						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));

						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizaçôes"));

						status = STATE.LIST_LOCATIONS;
					}
				} else if (status == STATE.WAITING_GOOD_BY_DESCRIPTION) {

					description = update.message().text();

					try{
						controllGoods.findByName(description);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar a localizacao")); 

						status = STATE.LIST_LOCATIONS;
					} catch (OffTheList e) {

						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));

						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizaçôes"));

						status = STATE.LIST_LOCATIONS;
					}
				}	
			}
		}
	}
}


