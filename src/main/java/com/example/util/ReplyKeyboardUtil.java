package com.example.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.LinkedList;
import java.util.List;

public class ReplyKeyboardUtil {
    public static KeyboardButton button(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }
    public static ReplyKeyboardMarkup menuKeyboard() {
        KeyboardButton teacher = button("\uD83D\uDC68\u200D\uD83C\uDFEB Imtihon o'tkazuvchi");
        KeyboardButton student = button("\uD83C\uDF93 Imtihon topshiruvchi");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(teacher);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(student);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        rowList.add(row2);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup cancel() {
        KeyboardButton butn = button("❌ Bekor qilish");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(butn);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }   public static ReplyKeyboardMarkup endExam() {
        KeyboardButton butn = button("Imtihonni yakunlash.");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(butn);
        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
