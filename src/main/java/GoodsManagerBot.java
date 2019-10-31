import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class GoodsManagerBot extends TelegramLongPollingBot {

    String nome_do_bem;
    private Object ChatAction;

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

            if(command.equals("/cadastrar_bem")){
                nome_do_bem = update.getMessage().getText();
                //nome_do_bem = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
                //nome_do_bem = update.getMessage();
                //nome_do_bem = update.getMessage();
                //message.setText(update.getMessage().getFrom().toString(nome_do_bem));
                System.out.println(nome_do_bem + " teste");
            }

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
