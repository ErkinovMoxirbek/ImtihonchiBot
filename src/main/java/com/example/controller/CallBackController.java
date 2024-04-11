package com.example.controller;

import com.example.MyTelegramBot;
import com.example.entity.StudentProfileEntity;
import com.example.entity.TeacherProfileEntity;
import com.example.enums.ProfileStep;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.service.ExamService;
import com.example.service.ExelFileService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class CallBackController {
    private ExamService examService;
    private MyTelegramBot myTelegramBot;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private ExelFileService exelFileService;

    public void handle(String text,Message message){
        if (text.startsWith("start/")){
            TeacherProfileEntity entity = teacherRepository.findById(message.getChatId());
            entity.setStep(ProfileStep.DONE);
            teacherRepository.update(entity);
            Integer examId = Integer.valueOf(text.split("/")[1]);
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message.getMessageId());
            myTelegramBot.deleteMsg(deleteMessage);
            for (StudentProfileEntity s : studentRepository.findAll()){
                if (s.getExamId().equals(examId)){
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setChatId(s.getProfileId());
                    editMessageText.setMessageId(s.getLastMessageId());
                    editMessageText.setText("Imtihon boshlandi.");
                    myTelegramBot.sendMsg(editMessageText);
                }
            }
            examService.sendStudentTest(message,examId);
        } else if (text.startsWith("option/")) {
            String [] arr = text.split("/");
            StudentProfileEntity entity = studentRepository.findById(message.getChatId());
            entity.setStep(ProfileStep.Question);
            studentRepository.update(entity);
            examService.checkOption(message,arr[1],arr[2],arr[3]);
        }else if (text.startsWith("infoExel/")){
            exelFileService.infoExel(message);
        }
    }
}
