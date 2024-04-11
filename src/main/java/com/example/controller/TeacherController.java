package com.example.controller;

import com.example.MyTelegramBot;
import com.example.entity.TeacherProfileEntity;
import com.example.enums.ProfileStep;
import com.example.repository.TeacherRepository;
import com.example.service.ExamService;
import com.example.service.TeacherService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class TeacherController {
    private MyTelegramBot myTelegramBot;
    private TeacherRepository teacherRepository;
    private TeacherService teacherService;
    private ExamService examService;
    public void handle(Message message,MainController mainController){
        if (message.getText().equals("\uD83D\uDC68\u200D\uD83C\uDFEB Imtihon o'tkazuvchi")){
            teacherService.helloTeacher(message);
        }
        else if (message.getText().equals("❌ Bekor qilish")) {
            TeacherProfileEntity entity = teacherRepository.findById(message.getChatId());
            entity.setExamFinishedStudentCount(0);
            entity.setStep(ProfileStep.DONE);
            entity.setGroup(null);
            entity.setVisible(Boolean.FALSE);
            entity.setFileName(null);
            entity.setLastMessageId(null);
            entity.setStudentListToString(null);
            entity.setExamId(null);
            entity.setStudentCount(null);
            teacherRepository.update(entity);
            message.setText("/start");
            mainController.handle(message);
        }
        else if (message.getText().equals("Imtihonni yakunlash.")) {
            examService.finishExam(message,message.getChatId());
        } else if (teacherRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_GROUP) ) {
            if (teacherRepository.findByGroup(message.getText()) == null && (message.getText().trim()).matches("^[A-Za-z`'_ 0-9]+$")){
                teacherService.enterExel(message);
            }else {
                myTelegramBot.sendMessage("Bunday guruh hozir mavjud yoki nom tog'ri shaklda emas qayta nom bering!",message.getChatId());
            }
        }
    }
}
