import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MainClass {

    public static void main(String[] args) {


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new GoodsManagerBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        
        Controller controll = new Controller();
        controll.registerLocation("UFRN", "Faculdade");
        controll.registerLocation("IFRN", "Escola");

        controll.registerCategory("Mobilia", "Móveis", "1122-XMB");
        controll.registerCategory("Eletrônicos", "Computadores e Impressoras",
                "1433-YET");

        controll.listLocations();
        controll.listCategories();

    }
}
