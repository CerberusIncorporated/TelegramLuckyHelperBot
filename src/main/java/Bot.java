import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            setButtons(sendMessage);
            sendMessage(sendMessage);

        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "Привет, я простой бот!\nЯ могу показать погоду и занять твоё свободное время развлечениями!");
                    break;
                case "/help":
                    sendMsg(message, "Я умею:\nпоказывать погоду;\n\nотправить ссылку на гитхаб с сериалами,мультсериалами,книгами;\n\n" +
                            "отправить ссылку на огромное количество материала по программированию, информационной безопансости.\n\n" +
                            "Для просмотра погоды просто введи название города, а для остального придётся воспользоваться кнопками!");
                    break;
                case "Материал":
                    sendMsg(message, "https://github.com/CerberusIncorporated/study");
                    break;
                case "Мультимедия":
                    sendMsg(message, "https://github.com/CerberusIncorporated/movie");
                    break;
//                case "Погода":
//                    sendMsg(message, "Окей, напишите название города");
//                    try{
//                        sendMsg(message,Weather.getWeather(message.getText(),model));
//                    }catch (IOException e){
//                        sendMsg(message, "Город не найден");
//                    }
                default:
                    try{
                        sendMsg(message,Weather.getWeather(message.getText(),model));
                    }catch (IOException e){
                        sendMsg(message, "Город не найден");
                    }

            }
        }
    }

    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));


        keyboardSecondRow.add(new KeyboardButton("Материал"));
        keyboardSecondRow.add(new KeyboardButton("Мультимедия"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


    public String getBotUsername() {
        return "LuckyHelperBot";
    }

    public String getBotToken() {
        return "1366434663:AAFlzsslNCsgAg-TK3lsevRd8pCPL5qxyCM";
    }
}
