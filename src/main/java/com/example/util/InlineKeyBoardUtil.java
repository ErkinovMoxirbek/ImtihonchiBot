package com.example.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

public class InlineKeyBoardUtil {
    public static InlineKeyboardButton button(String text, String callBack) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callBack);
        return button;
    }

    public static InlineKeyboardMarkup startExam(Integer examId) {

        List<InlineKeyboardButton> row = new LinkedList<>();
        InlineKeyboardButton button1 = InlineKeyBoardUtil.button("Imtihoni boshlash", "start/" + examId);
        row.add(button1);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }public static InlineKeyboardMarkup infoExel() {

        List<InlineKeyboardButton> row = new LinkedList<>();
        InlineKeyboardButton button1 = InlineKeyBoardUtil.button("Exel haqida ma'lumot olish", "infoExel/");
        row.add(button1);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup examOptions(Integer examId, Integer optionId, Integer optionCount) {
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        if (optionCount != null && optionCount == 4){
            InlineKeyboardButton button1 = InlineKeyBoardUtil.button(" A ", "option/A/" + examId + "/" + optionId );
            row1.add(button1);
            InlineKeyboardButton button2 = InlineKeyBoardUtil.button(" B ", "option/B/" + examId + "/" + optionId );
            row1.add(button2);
            InlineKeyboardButton button3 = InlineKeyBoardUtil.button(" C ", "option/C/" + examId + "/" + optionId );
            row2.add(button3);
            InlineKeyboardButton button4 = InlineKeyBoardUtil.button(" D ", "option/D/" + examId + "/" + optionId );
            row2.add(button4);
        }if (optionCount != null && optionCount == 3){
            InlineKeyboardButton button1 = InlineKeyBoardUtil.button(" A ", "option/A/" + examId + "/" + optionId );
            row1.add(button1);
            InlineKeyboardButton button2 = InlineKeyBoardUtil.button(" B ", "option/B/" + examId + "/" + optionId );
            row1.add(button2);
            InlineKeyboardButton button3 = InlineKeyBoardUtil.button(" C ", "option/C/" + examId + "/" + optionId );
            row2.add(button3);
        }if (optionCount != null && optionCount == 2){
            InlineKeyboardButton button1 = InlineKeyBoardUtil.button(" A ", "option/A/" + examId + "/" + optionId );
            row1.add(button1);
            InlineKeyboardButton button2 = InlineKeyBoardUtil.button(" B ", "option/B/" + examId + "/" + optionId );
            row1.add(button2);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row1);
        rowList.add(row2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
