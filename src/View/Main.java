package View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;


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
		
		/**
		 * @author Carmem, Rafaela, Vinícius
	     * @param status state machine equal NULL
	     * @param name equal "null"
	     * @param description equal "null"
	     * @param code equal "null"
	     * @param location equal "null"
	     * @param category equal "null"
	     * @param searchGood equal null
	     * @param search equal false
	     * @param change equal false
	     */
		STATE status = STATE.NULL;
		String name = "null";
		String description = "null";
		String code = "null";
		String location = "null";
		String category = "null";
		Good searchGood = null;
		boolean search = false;
		boolean change = false;
		
<<<<<<< HEAD
		/** Creation of the bot object with access info*/
=======
		//Criação do objeto bot com as informações de acesso
>>>>>>> 1f1ae8160b69f44883869f21228390734babe710
		TelegramBot bot = TelegramBotAdapter.build("909350681:AAHgxlxiLrG7oZtaC6EwyBUrPEbMjVILUCA");

		/**
		 * GetUpdatesResponse: object who will receive the message
		 */
		GetUpdatesResponse updatesResponse;
		
		/** off-set control */
		int m = 0;
		
		/**
		 * Controladores para cada uma das classes
		 */
		Goods controllGoods = new Goods();
		Categories controllCategories = new Categories();
		Locations controllLocations = new Locations();
		/** Creating some locations, categories and goods */
		controllLocations.register("ufrn", "ufrn");
		controllLocations.register("ifrn", "ifrn");
		controllCategories.register("teste", "teste", "teste");
		controllGoods.register("t1", "t1", "t1", "ifrn", "teste");
		controllGoods.register("t2", "t2", "t2", "ifrn", "teste");
		controllGoods.register("t3", "t3", "t3", "ufrn", "teste");
		
		/**
		 * Infinite loop, can be changed by some short interval timer,
		 * works to receive everything that happens from bot-user interaction
		 *
		 * updatesResponse: run Telegram command to get messages
		 * pending from an off-set (initial limit)
		 *
		 * updates: list of messages / user updates
		 */
		while (true){

			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));
			
			List<Update> updates = updatesResponse.updates();
		
			/**
			 * Each user input through the bot results in an action, this
			 * for works to analyze these actions and perform the action
			 * required.
			 */
			for (Update update : updates) {
				
				//atualização do off-set
				m = update.updateId()+1;
				
				
				/**
				 * Command to register BEM, moves STATE to where the name of the property is received
				 * to be registered
				 * */
				if(update.message().text().equals("/cadastrar_bem")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome do bem: "));
					status = STATE.WAITING_GOOD_NAME;
				}
					
				/**
				 * Command to register LOCATION, moves STATE to the location name
				 * */
				else if(update.message().text().equals("/cadastrar_localizacao")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da localização: "));
					status = STATE.WAITING_LOCAL_NAME;
				}
					
				
				/**
				 * Command to register CATEGORY, moves STATE to where the category name is received
				 * */
				else if(update.message().text().equals("/cadastrar_categoria")) {
					bot.execute(new SendMessage(update.message().chat().id(),"Digite o nome da categoria: "));
					status = STATE.WAITING_CATEGORY_NAME;
				}
						
				/**
				 * Command to list GOOD can catch EmptyList exception that is called
				 * if there is no registered property.
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
				 * Command to list LOCATIONS, can catch the EmptyList exception that 
				 * is called if there are no registered properties. Accessed through 
				 * both the command (/ list_locations) and STATE, as it can be recalled 
				 * throughout the system, requiring direct user interaction.
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
						status = STATE.WAITING_LOCATION;
						bot.execute(new SendMessage(update.message().chat().id(), 
								"Digite o nome da localização que está associada a esse bem:"));
			

					} else {
						status = STATE.NULL;
					}
				}
				
				/**
				 * Command to list CATEGORIES, can catch the EmptyList exception
				 * that is called so there is no registered good. It is accessed 
				 * through both the command (/ list_categories) and through STATE, as 
				 * it can be recalled throughout the system, requiring direct user interaction.
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
				 * Command to list GOODS of a given LOCATION. Changes STATE 
				 * to return to the list of registered locations, allowing the 
				 * user to choose the location to search.
				 * */
				else if(update.message().text().equals("/listar_bens_por_localizacao")){
					bot.execute(new SendMessage(update.message().chat().id(),
							"Aperte qualquer tecla para listar as localizações"));
					status = STATE.LIST_LOCATIONS;
					search = true;
				}
				
				
				/**
				 * States and search commands verification 
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
				 * Here begins the STATES used to register a new asset.
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
				 * STATE used to receive, in the register of WELL, which is the 
				 * LOCATION of the good that you want to register. Checks with the 
				 * findByName function whether this location already exists. If it does not exist, 
				 * the OffTheList exception is thrown.
				 * */	
				} else if(status == STATE.WAITING_LOCATION) {
						
					location = update.message().text();		
					
					try{
						controllLocations.findByName(location);
						
						if(search) {
							status = STATE.LIST_GOODS_BY_LOCATION;
							bot.execute(new SendMessage(update.message().chat().id(),
									"Busca completa!\n Aperte qualquer tecla ver o resultado.")); 
						} else if(change){
							status = STATE.WAITING_NEW_LOCATION;
							bot.execute(new SendMessage(update.message().chat().id(),
									"Opa, tudo quase pronto! Aperte qualquer tecla para ver as mudanças.")); 
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
				 * STATE used to receive, in the register of WELL, which 
				 * CATEGORY of the good to be registered. Checks with the 
				 * findByName function whether this location already exists. 
				 * If it does not exist, the OffTheList exception is thrown. 
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
				 * Here begins the STATES used to register a new location.
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
				 * Here begins the STATES used to register a new category.
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
				 * STATE used to list assets by location
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
				 * Here begins the search STATES
				 * */
				else if (status == STATE.WAITING_GOOD_BY_CODE) {

					code = update.message().text();

					try{
						searchGood = controllGoods.findByCode(code);
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
				* Search for a good by code and change its location
				* */
				else if(update.message().text().equals("/trocar_localizacao_de_bem")) {

					bot.execute(new SendMessage(update.message().chat().id(),
						"Digite o codigo do bem que deseja buscar: "));
					status = STATE.WAITING_GOOD_BY_CODE_FOR_EXCHANCE;
				}

				else if (status == STATE.WAITING_GOOD_BY_CODE_FOR_EXCHANCE) {
					
					code = update.message().text();
				
					try{
						searchGood = controllGoods.findByCode(code);
						bot.execute(new SendMessage(update.message().chat().id(),
								"Nome: " + searchGood.getGoodsName() +
								"\n| Localização atual: " + searchGood.getGoodsLocation()));
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"Aperte qualquer tecla para listar as localizações disponíveis para troca: "));

						status = STATE.LIST_LOCATIONS;
						change = true;

					} catch (OffTheList e) {

						bot.execute(new SendMessage(update.message().chat().id(),
								e.getMessage()));
						status = STATE.NULL;
						
					}
					
				}

				else if (status == STATE.WAITING_NEW_LOCATION) {

					name = searchGood.getGoodsName();
					description = searchGood.getGoodsDescription();
					code = searchGood.getGoodsCode();
					category = searchGood.getGoodsCategory();
					
					controllGoods.deleteByCode(code);
					
					bot.execute(new SendMessage(update.message().chat().id(),"Localização do bem foi atualizado com sucesso!"));
					bot.execute(new SendMessage(update.message().chat().id(),
							"Nome: " + searchGood.getGoodsName() +
							"\n| Nova localização: " + location));
					
					controllGoods.register(name, description, code, location, category);
					status = STATE.NULL;
				}
				
			
				/**
				* Create a report
				* */
				else if (update.message().text().equals("/gerar_relatorio")) {
					try {
						controllGoods.sizeOfList();
						ArrayList <Good> goodsList = controllGoods.list();
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"BENS ORDENADOS POR LOCALIZAÇÃO: "));
							
						Collections.sort(goodsList,new Comparator(){
							public int compare(Object o1, Object o2) {
						        Good a1 = (Good)o1;
						        Good a2 = (Good)o2;
						        return a1.getGoodsLocation()
						                .compareToIgnoreCase(a2.getGoodsLocation());
						    }
						});
						
						for (Good goods : goodsList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Nome: " + goods.getGoodsName() + 
									"\n| Descrição: " + goods.getGoodsDescription() + 
									"\n| Código: " + goods.getGoodsCode() + 
									"\n| Localização: " + goods.getGoodsLocation() + 
									"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
						}
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"BENS ORDENADOS POR CATEGORIA: "));
							
						Collections.sort(goodsList,new Comparator(){
							public int compare(Object o1, Object o2) {
						        Good a1 = (Good)o1;
						        Good a2 = (Good)o2;
						        return a1.getGoodsCategory()
						                .compareToIgnoreCase(a2.getGoodsCategory());
						    }
						});
						
						for (Good goods : goodsList){
							bot.execute(new SendMessage(update.message().chat().id(), 
									"Nome: " + goods.getGoodsName() + 
									"\n| Descrição: " + goods.getGoodsDescription() + 
									"\n| Código: " + goods.getGoodsCode() + 
									"\n| Localização: " + goods.getGoodsLocation() + 
									"\n| Categoria: " + goods.getGoodsCategory() + "\n"));
						}
						
						bot.execute(new SendMessage(update.message().chat().id(),
								"BENS ORDEM ALFABÉTICA: "));
						
						Collections.sort(goodsList,new Comparator(){
							public int compare(Object o1, Object o2) {
						        Good a1 = (Good)o1;
						        Good a2 = (Good)o2;
						        return a1.getGoodsName()
						                .compareToIgnoreCase(a2.getGoodsName());
						    }
						});
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
			}
		}
	}
}


