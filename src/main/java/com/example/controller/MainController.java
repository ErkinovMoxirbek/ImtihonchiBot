package com.example.controller;

import com.example.MyTelegramBot;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStep;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.service.ExelFileService;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
@AllArgsConstructor
public class MainController {
    private MyTelegramBot myTelegramBot;
    private TeacherController teacherController;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private ExelFileService exelFileService;
    private StudentController studentController;
    public void handle(Message message){
        if (message.getText() == null ){
            if (message.hasDocument()&& teacherRepository.findById(message.getChatId())!=null && teacherRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_EXEL)){
                ProfileEntity entity = teacherRepository.findById(message.getChatId());
                String [] fileName = message.getDocument().getFileName().split("\\.");
                if(teacherRepository.findByFileName(message.getDocument().getFileName()) != null){
                    Document document = message.getDocument();
                    document.setFileName(fileName[0] + "_." + fileName[1]);
                    handle(message);
                    System.out.println(fileName[0] + "_." + fileName[1]);
                }else {
                    entity.setFileName(message.getDocument().getFileName());
                    teacherRepository.update(entity);
                    exelFileService.handleDocument(message,message.getDocument());
                }
            }
            else {
                myTelegramBot.sendMessage("Siz birinchi guruhga nom bering!", message.getChatId());
            }
        }
        if ( !message.getText().isEmpty() && message.getText().equals("/start")){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Botga \nname: " + message.getChat().getFirstName() + ", \nSurname: " + message.getChat().getLastName() + ", \nSizning loyihangizga qiziqmoqda. " + "\n\nUsername: @" + message.getChat().getUserName() + "\nDate/Time: " + LocalDateTime.now());
            sendMessage.setChatId("@TM6669008");
            myTelegramBot.sendMsg(sendMessage);
            myTelegramBot.sendMessage("Assalomu alaykum bo'limlarni birini tanlang!",message.getChatId(), ReplyKeyboardUtil.menuKeyboard());
        } else if (message.getText().equals("\uD83D\uDC68\u200D\uD83C\uDFEB Imtihon o'tkazuvchi")) {
            teacherController.handle(message,this);
        }else if (teacherRepository.findByIdAndRole(message.getChatId(), ProfileRole.TEACHER) != null){
            if (teacherRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_GROUP) ||
                    teacherRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_EXEL) ||
                    teacherRepository.findById(message.getChatId()).getStep().equals(ProfileStep.EXAM)){
                teacherController.handle(message,this);
            }
        }else if (message.getText().equals("\uD83C\uDF93 Imtihon topshiruvchi")){
            studentController.handle(message,this);
        } else if (studentRepository.findByIdAndRole(message.getChatId(),ProfileRole.STUDENT) != null) {
            if (studentRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_EXAM_ID)){
                studentController.handle(message,this);
            } else if (studentRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_NAME)) {
                studentController.handle(message,this);
            } else if (studentRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_SURNAME)) {
                studentController.handle(message,this);
            }
        }
    }

}
