package com.example.service;

import com.example.MyTelegramBot;
import com.example.entity.TeacherProfileEntity;
import com.example.enums.ProfileStep;
import com.example.repository.TeacherRepository;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@AllArgsConstructor
public class ExelFileService {
    private MyTelegramBot myTelegramBot;
    private TeacherRepository teacherRepository;

    public void handleDocument(Message message, Document document) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileId = document.getFileId();
                String filePath = getFilePath(fileId);
                if (filePath != null) {
                    try {
                        File excelFile = downloadFile(filePath);
                        if (excelFile != null) {
                            Message message1 = sendMessageToUser("Fayl yuklanmoqda...", message.getChatId());
                            saveFile(excelFile, message,"Base", document.getFileName());
                            myTelegramBot.deleteMsg(new DeleteMessage(message1.getChatId().toString(), message1.getMessageId()));
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setText("Hammasi tayor, Imtihonga kirish uchun id: " + teacherRepository.findById(message.getChatId()).getExamId() + "\n\nO'quvchilar qo'shilishi kutilmoqda...");
                            sendMessage.setChatId(message.getChatId());
                            myTelegramBot.sendMsg(sendMessage);
                            SendMessage sendMessage1 = new SendMessage();
                            sendMessage1.setText("Imtihonni boshlashingiz yoki yakunlashingiz mumkin!");
                            sendMessage1.setReplyMarkup(ReplyKeyboardUtil.endExam());
                            sendMessage1.setChatId(message.getChatId());
                            myTelegramBot.sendMsg(sendMessage1);
                            TeacherProfileEntity entity = teacherRepository.findById(message.getChatId());
                            entity.setStep(ProfileStep.EXAM);
                            teacherRepository.update(entity);
                        } else {
                            sendMessageToUser("Fayl yuklanmadi.", message.getChatId());
                        }
                    } catch (IOException e) {
                        sendMessageToUser("Xato yuz berdi: " + e.getMessage(), message.getChatId());
                    }
                } else {
                    sendMessageToUser("Fayl haqida ma'lumot olishda xato yuz berdi. \n " + document.getFileSize(), message.getChatId());
                }
            }
        }).start();
    }

    private String getFilePath(String fileId) {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        org.telegram.telegrambots.meta.api.objects.File file = myTelegramBot.getFile(getFileRequest);
        String filePath = file.getFilePath();
        if (filePath != null) {
            return "https://api.telegram.org/file/bot" + myTelegramBot.getBotToken() + "/" + filePath;
        }
        return null;
    }

    private File downloadFile(String filePath) throws IOException {
        File downloadedFile;
        try (InputStream inputStream = new URL(filePath).openStream()) {
            downloadedFile = File.createTempFile("excel_", ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(downloadedFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return downloadedFile;
    }

    private void saveFile(File excelFile, Message message, String path, String fileName) throws IOException {
        if (!excelFile.exists() || !excelFile.isFile()) {
            sendMessageToUser(" fayli topilmadi yoki fayl emas.", message.getChatId());
            return;
        }
        File directory = new File("Base");
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                fileName = UUID.randomUUID().toString() + ".xlsx";
            }
        }
        File targetFolder = new File(path);
        if (!targetFolder.exists() || !targetFolder.isDirectory()) {
            sendMessageToUser("Hedef papka mavjud emas yoki papka emas.", message.getChatId());
            return;
        }
        Path sourcePath = excelFile.toPath();
        Path targetPath = Path.of(path, fileName);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private Message sendMessageToUser(String text, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return myTelegramBot.sendMsg(sendMessage);
    }
    public void infoExel(Message message){
        TeacherProfileEntity dto = teacherRepository.findById(message.getChatId());
        if (dto.getLastMessageId() != null){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(message.getChatId());
                deleteMessage.setMessageId(dto.getLastMessageId());
                myTelegramBot.deleteMsg(deleteMessage);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Message not found!");
            }
        }else {
            System.out.println("LastMessageId Null");
        }
        myTelegramBot.sendMessage("Biz uchun exel fayl bilan ishlash qulay, keling shuning uchun buni qanday ishlatishni o'rgataman.",message.getChatId());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setPhoto(new InputFile(new File("Base/infoBotBase/infoExel.png")));
        sendPhoto.setCaption("""
                Rasmga qarang, uni tartib raqamlab chiqdik va bu raqamlar har bir qatorda turibdi, keling bu qatorlar nimaga javobgarligini tanishtiraman :\s
                                
                 1) Bu qatorda savollar tartib raqami bo'lishi kerak;
                 2) Bu qatorda savollar bo'lishi kerak;
                 3, 4, 5, 6) Savol uchun A,B,C,D variantlar kiritishingiz kerak. Agar sizni variantingiz kam bo'lsa, ya'ni 3ta yoki 2ta bo'lsa, siz kerakli qatorga variantingizni kiriting va variant eng kamida 2ta yoki eng ko'pida 4ta bo'lishi kerak;
                 7) Bu qatorda to'g'ri bo'lgan variantni kiritib ketishingiz kerak;
                 8) Bu qatorda to'g'ri topilgan savolga necha ball berilishini kiritib ketish kerak;
                 9) 8-qatordan o'n tomonda qatorlar siz uchun eslatmalar yozish uchun.              
                                
                 ❗️Eslatma, bu ma'lumot o'zgarib turadi, chunki bu platforma rivojlanmoqda. Agar imloviy xato yoki taklifingiz bo'lsa @mohirbek_erkinov ga aloqaga chiqing. Biz albatta javob beramiz!
                """);
        myTelegramBot.sendPhoto(sendPhoto);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId());
        sendDocument.setDocument(new InputFile(new File("Base/infoBotBase/Namuna.xlsx")));
        sendDocument.setCaption("Siz shu namunadan foydalanishingiz mumkin!");
        myTelegramBot.sendDoc(sendDocument);
        myTelegramBot.sendMessage("Test mavjud bo'lgan exel fileni jo'nating!",message.getChatId(),ReplyKeyboardUtil.cancel());
    }

}
