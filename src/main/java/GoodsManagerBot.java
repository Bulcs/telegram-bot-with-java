import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class GoodsManagerBot extends TelegramLongPollingBot {

    Message nome_do_bem;

        public void onUpdateReceived(Update update) {

            System.out.println(update.getMessage().getText()); //escreve a mensagem
            System.out.println(update.getMessage().getFrom().getFirstName() ); //pega o nome do user no telegram

            String command=update.getMessage().getText();

            SendMessage message = new SendMessage();

            //COMANDOS CRIADOS NO TELEGRAM ABAIXO

            if(command.equals("/myname")){

                System.out.println(update.getMessage().getFrom().getFirstName());

                message.setText(update.getMessage().getFrom().getFirstName());
            }

            //if(command.equals("/cadastrar_bem")){
            //    message.setText("Digite o nome do bem");
            //    nome_do_bem = update.getMessage();
            //    message.setText(update.getMessage.get);
            //}

            if (command.equals("/mylastname")){

                System.out.println(update.getMessage().getFrom().getLastName());
                message.setText(update.getMessage().getFrom().getLastName());
            }

            if (command.equals("/myfullname")){
                System.out.println(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getLastName());
           message.setText(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getLastName());
            }

            message.setChatId(update.getMessage().getChatId());


            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }

        public String getBotUsername() {
            return "Goods Manager";
        }

        public String getBotToken() {
            return "909350681:AAHgxlxiLrG7oZtaC6EwyBUrPEbMjVILUCA";
        }
    }
