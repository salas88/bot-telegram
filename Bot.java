import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    Book book = new Book();
    private long chat_id;
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());

        chat_id = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

    }

    public String getMessage(String msg){
        List keyboard = new ArrayList();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow =new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if(msg.equals("Привет") || msg.equals("Меню")){
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Популярное");
            keyboardFirstRow.add("Новости");
            keyboardSecondRow.add("Полезная информация");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Выбрать...";
        } if(msg.equals("Полезная Информация")){
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Инфомация о книге");
            keyboardFirstRow.add("/person bebosehum_");
            keyboardFirstRow.add("Меню");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Важно! для коректной работы раздела\" Популярное";

        }
    }

    @Override
    public String getBotUsername() {
        return "@test14Java_bot";
    }

    @Override
    public String getBotToken() {
        return "1008855537:AAELeFD2GuALL3kl2alaXBKWKI0oexwgMv4";
    }

    public String getInfoBook()  {
        SendPhoto sendPhotoRequest = new SendPhoto();


        try (InputStream in = new URL(book.getImage()).openStream()){

            Files.copy(in, Paths.get("/home/vadislav/Pictures"));
            sendPhotoRequest.setChatId(chat_id);
            sendPhotoRequest.setPhoto(new File("/home/vadislav/Pictures"));
            execute(sendPhotoRequest);
            Files.delete(Paths.get("/home/vadislav/Pictures"));

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

    public String getInfoPerson(String msg){
        Author aut = new Author(msg);
        SendPhoto sendPhotoRequest = new SendPhoto();
        try (InputStream in = new URL(aut.getImage()).openStream()){

            Files.copy(in, Paths.get("/home/vadislav/Pictures"));
            sendPhotoRequest.setChatId(chat_id);
            sendPhotoRequest.setPhoto(new File("/home/vadislav/Pictures"));
            execute(sendPhotoRequest);
            Files.delete(Paths.get("/home/vadislav/Pictures"));

        }
        catch (IOException e) {
            System.out.println("File not found");
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
        return aut.getInfoPerson();
    }
}
