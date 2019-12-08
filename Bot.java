import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Bot extends TelegramLongPollingBot {
    Book book = new Book();
    private long chat_id;


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());

        chat_id = update.getMessage().getChatId();

        sendMessage.setText(input(update.getMessage().getText()));

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

    }

    private String input(String msg) {
        if(msg.contains("Информация о книге"))
            return getInfoBook();
        if(msg.contains("Hi") || msg.contains("Hello") || msg.contains("Привет"))
            return "Привет дружище";

        return msg;
    }

    @Override
    public String getBotUsername() {
        return "@test14Java_bot";
    }

    @Override
    public String getBotToken() {
        return "1008855537:AAELeFD2GuALL3kl2alaXBKWKI0oexwgMv4";
    }

    public String getInfoBook(){
        SendPhoto sendPhotoRequest = new SendPhoto();
        try (InputStream in = new URL(book.getImage()).openStream()){

            Files.copy(in, Paths.get("/home/vadislav/Pictures"));
            sendPhotoRequest.setChatId(chat_id);
            sendPhotoRequest.setPhoto(new File("/home/vadislav/Pictures"));
            execute(sendPhotoRequest);

        }
        catch (IOException e) {
            System.out.println("File not found");
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }

        String info = book.getTitle()

                + "\nАвтор " + book.getAuthorName()
                + "\nЖанр " + book.getGeners()
                + "\n\nОписание\n" + book.getDescription()
                + "\n\nКоличество лайков " + book.getLikes();

        return info;
    }
}
