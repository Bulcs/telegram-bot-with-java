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
		
		boolean search = false;

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
		
		controllLocations.register("ufrn", "ufrn");
		controllLocations.register("ifrn", "ifrn");
		controllCategories.register("teste", "teste", "teste");
		controllGoods.register("t1", "t1", "t1", "ifrn", "teste");
		controllGoods.register("t2", "t2", "t2", "ifrn", "teste");
		controllGoods.register("t3", "t3", "t3", "ufrn", "teste");
		
		/**
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
		
			/**
			 * Cada input do usuário através do bot resulta em uma ação, esse 
			 * 		for funciona para analisar essas ações e realizar a ação
			 * 		necessária.
			 */
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				
				
				/**
				 * Comando para cadastrar o BEM, move o STATE para onde se recebe o nome do bem
				 * a ser cadastrado
				 * */
				if(update.message().text().equals("/cadastrar_bem")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome do bem: "));
					status = STATE.WAITING_GOOD_NAME;
				}
					
				/**
				 * Comando para cadastrar LOCALIZAÇÃO, move o STATE para onde se recebe o nome da localização
				 * */
				else if(update.message().text().equals("/cadastrar_localizacao")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da localização: "));
					status = STATE.WAITING_LOCAL_NAME;
				}
					
				
				/**
				 * Comando para cadastrar CATEGORIA, move o STATE para onde se recebe o nome da categoria
				 * */
				else if(update.message().text().equals("/cadastrar_categoria")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da categoria: "));
					status = STATE.WAITING_CATEGORY_NAME;
				}
						
				/**
				 * Comando para listar BENS, pode capturar a exceção EmptyList que é chamada
				 * caso não existe nenhum bem cadastrado. 
				 * */
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
				 * Comando para listar LOCALIZAÇÕES, pode capturar a exceção EmptyList que é chamada
				 * caso não existe nenhum bem cadastrado. 
				 * É acessado tanto através do comando (/listar_localizacoes) como através do STATE,
				 * pois pode ser rechamado ao longo do sistema, ser ser necessária interação 
				 * direta com o usuário. 
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
				
				/**
				 * Comando para listar CATEGORIAS, pode capturar a exceção EmptyList que é chamada
				 * caso não existe nenhum bem cadastrado. 
				 * É acessado tanto através do comando (/listar_categorias) como através do STATE,
				 * pois pode ser rechamado ao longo do sistema, ser ser necessária interação 
				 * direta com o usuário. 
				 * */
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
				 * Comando para listar BENS de uma determinada LOCALIZAÇÃO.
				 * Muda o STATE de modo a voltar para a lista de localizações cadastradas,
				 * permitindo ao usuário escolher a localização que irá buscar.
				 * */
				else if(update.message().text().equals("/listar_bens_por_localizacao")){
					bot.execute(new SendMessage(update.message().chat().id(),
							"Aperte qualquer tecla para listar as localizações"));
					status = STATE.LIST_LOCATIONS;
					search = true;
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
					
					try {
						controllGoods.findByCode(code);
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Já existe um bem cadastrado com esse codigo, tente novamente."));
						status = STATE.NULL;
					} catch(OffTheList e) {
					
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizaçôes"));			
						status = STATE.LIST_LOCATIONS;
					}
					
				/**
				 * STATE utilizado para receber, no cadastro de BEM, qual a LOCALIZAÇÃO do bem que 
				 * se deseja cadastrar. Verifica, através da função findByName, se essa localização em 
				 * questão já existe. Se não existir, a exceção OffTheList é lançada. 
				 * */	
				} else if(status == STATE.WAITING_LOCATION) {
						
					location = update.message().text();		
					
					try{
						controllLocations.findByName(location);
						
						if(search) {
							status = STATE.LIST_GOODS_BY_LOCATION;
							bot.execute(new SendMessage(update.message().chat().id(),
									"Busca completa!\n Aperte qualquer tecla ver o resultado.")); 
						} else {
							bot.execute(new SendMessage(update.message().chat().id(),
									"Aperte qualquer tecla para listar as categorias")); 
							status = STATE.LIST_CATEGORIES;
						}

						
					} catch (OffTheList e) {
						
						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizaçôes"));
						
						status = STATE.LIST_LOCATIONS;
					}

				/**
				 * STATE utilizado para receber, no cadastro de BEM, qual a CATEGORIA do bem que 
				 * se deseja cadastrar. Verifica, através da função findByName, se essa localização em 
				 * questão já existe. Se não existir, a exceção OffTheList é lançada. 
				 * */		
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
				 * STATE utilizado para listar bens por localização
				 * */
				else if(status == STATE.LIST_GOODS_BY_LOCATION) {
					ArrayList <Good> goodsListByLocation = controllGoods.listByLocation(location);
					for (Good goods : goodsListByLocation){
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Nome: " + goods.getGoodsName() + 
								"\n| Descrição: " + goods.getGoodsDescription() + 
								"\n| Código: " + goods.getGoodsCode() + 
								"\n| Localização: " + goods.getGoodsLocation() + 
								"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
					}
					
					search = false;
				}
				
				/**
				 * Aqui começam os STATES de busca
				 * */
				else if (status == STATE.WAITING_GOOD_BY_CODE) {

					code = update.message().text();

					try{
						Good searchGood = controllGoods.findByCode(code);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Nome: " + searchGood.getGoodsName() +
								"\n|Localização: " + searchGood.getGoodsLocation())); 
					} catch (OffTheList e) {

						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
					}
					
					status = STATE.NULL;
				}

				else if (status == STATE.WAITING_GOOD_BY_NAME) {

					name = update.message().text();
					
					try {
							
						ArrayList <Good> goodsListByName = controllGoods.listByName(name);
						for (Good goods : goodsListByName){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Nome: " + goods.getGoodsName() + 
									"\n| Descrição: " + goods.getGoodsDescription() + 
									"\n| Código: " + goods.getGoodsCode() + 
									"\n| Localização: " + goods.getGoodsLocation() + 
									"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
						}
					} catch(EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
	
					}
					
					status = STATE.NULL;
				
				
				} else if (status == STATE.WAITING_GOOD_BY_DESCRIPTION) {

					description = update.message().text();
				
					try {
							
						ArrayList <Good> goodsListByDescription = controllGoods.listByDescription(description);
						for (Good goods : goodsListByDescription){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Nome: " + goods.getGoodsName() + 
									"\n| Descrição: " + goods.getGoodsDescription() + 
									"\n| Código: " + goods.getGoodsCode() + 
									"\n| Localização: " + goods.getGoodsLocation() + 
									"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
						}
					} catch(EmptyList e) {
						bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
	
					}
					
					status = STATE.NULL;
				}
				
				/**
				* Busca um bem pelo código e troca sua localização
				* */
				else if(update.message().text().equals("/trocar_localizacao_de_bem")) {

					bot.execute(new SendMessage(update.message().chat().id(),
						"Digite o codigo do bem que deseja buscar: "));
					status = STATE.WAITING_GOOD_BY_CODE_FOR_EXCHANCE;
				}

				else if (status == STATE.WAITING_GOOD_BY_CODE_FOR_EXCHANCE) {

					code = update.message().text();

					try{
						Good searchGood = controllGoods.findByCode(code);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Nome: " + searchGood.getGoodsName() +
								"\n|Localização atual: " + searchGood.getGoodsLocation() +
								"Digite a nova localização: "));
						status = STATE.WAITING_NEW_LOCATION;

					} catch (OffTheList e) {

						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
					}
					
					status = STATE.NULL;
				}

				else if (status == STATE.WAITING_NEW_LOCATION) {

					location = update.message().text();

					bot.execute(new SendMessage(update.message().chat().id(),"Localização do bem foi atualizado com sucesso!")); 
					controllGoods.register(name, description, code, location, category);
					status = STATE.NULL;
				}
			}
		}
	}
}


