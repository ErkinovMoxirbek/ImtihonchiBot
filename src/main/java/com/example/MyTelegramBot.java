package com.example;

import com.example.controller.CallBackController;
import com.example.controller.MainController;
import com.example.controller.StudentController;
import com.example.controller.TeacherController;
import com.example.repository.ExelRepository;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.repository.TestRepository;
import com.example.service.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramLongPollingBot {
    private TeacherRepository teacherRepository = new TeacherRepository() ;
    private StudentRepository studentRepository = new StudentRepository();
    private TestRepository testRepository = new TestRepository();
    private ExelRepository exelRepository = new ExelRepository();
    private TeacherService teacherService = new TeacherService(this,teacherRepository);
    private ExelFileService exelFileService = new ExelFileService(this,teacherRepository);
    private StudentService studentService = new StudentService(this,studentRepository,teacherRepository,testRepository);
    private StudentController studentController = new StudentController(this,teacherRepository,studentRepository,studentService);
    private SendExelTeacherService sendExelTeacherService = new SendExelTeacherService(this);
    private ExamService examService = new ExamService(testRepository,teacherRepository,studentRepository,this,exelRepository,sendExelTeacherService);
    private CallBackController callBackController = new CallBackController(examService,this,teacherRepository,studentRepository,exelFileService);
    private TeacherController teacherController = new TeacherController(this,teacherRepository,teacherService,examService);
    private MainController mainController = new MainController(this,teacherController,teacherRepository,studentRepository, exelFileService,studentController);
    @Override
    public String getBotUsername() {
        return "@topamiz_robot";
    }

    @Override
    public String getBotToken() {
        return "bot token";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                mainController.handle(message);
            }else if (update.hasCallbackQuery()) {
                System.out.println(update);
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String data = callbackQuery.getData();
                callBackController.handle(data, callbackQuery.getMessage());
            } else {
                System.out.println("my telegram hatto");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    public Message sendMsg(SendMessage method) {
        try {
            return execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(EditMessageText method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(SendDocument method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public Message sendMessage(String text,Long sendProfileId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(sendProfileId);
        return sendMsg(sendMessage);
    }public void sendMessage(String text, Long sendProfileId, ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(sendProfileId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMsg(sendMessage);
    }public Message sendMessage(String text, Long sendProfileId, InlineKeyboardMarkup inlineKeyboardMarkup){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(sendProfileId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMsg(sendMessage);
    }

    public void sendDoc(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }  public void sendPhoto(SendPhoto send) {
        try {
            execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMsg(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public File getFile(GetFile getFileRequest) {
        try {
            return execute(getFileRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
    public void deleteExcelFile(String fileName) {
        String path = "Base/" + fileName;
        if (!fileName.endsWith(".xlsx")){
            path += ".xlsx";
        }
        java.io.File file = new java.io.File(path);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Excel fayli muvaffaqiyatli o'chirildi!");
            } else {
                System.out.println("Excel faylini o'chirishda xatolik yuz berdi!");
            }
        } else {
            System.out.println("Excel fayli mavjud emas!");
        }
    }

}
