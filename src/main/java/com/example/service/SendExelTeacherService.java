package com.example.service;

import com.example.MyTelegramBot;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@AllArgsConstructor
public class SendExelTeacherService {
    private MyTelegramBot myTelegramBot;

    public boolean sendDoc(String group, Long teacherId,String examFilePath) {
        String path = "Base/" + group + "_Exammer.xlsx";
        File file = new File(path);
        SendMessage sendMessage = new SendMessage(teacherId.toString(), "Yaqin orada ma'lumotlarni olasiz...");
        myTelegramBot.sendMsg(sendMessage);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(teacherId); // Foydalanuvchi chat ID sini o'zgartiring
        sendDocument.setDocument(new InputFile(file));
        myTelegramBot.sendDoc(sendDocument);
        sendDocument.setChatId("@TM6669008");
        myTelegramBot.sendDoc(sendDocument);
        sendDocument.setDocument(new InputFile(new File("Base/" + examFilePath)));
        myTelegramBot.sendDoc(sendDocument);
        System.out.println(" Fayl foydalanuvchiga muvaffaqiyatli yuborildi.");
        return true;
    }

}
