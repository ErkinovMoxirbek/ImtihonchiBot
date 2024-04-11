package com.example.controller;

import com.example.MyTelegramBot;
import com.example.entity.StudentProfileEntity;
import com.example.entity.TeacherProfileEntity;
import com.example.enums.ProfileStep;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.service.StudentService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
@AllArgsConstructor
public class StudentController {
    private MyTelegramBot myTelegramBot;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private StudentService studentService;
    public void handle(Message message, MainController mainController){
        if (message.getText().equals("\uD83C\uDF93 Imtihon topshiruvchi")){
            studentService.enterExamId(message);
        } else if (message.getText().equals("❌ Bekor qilish")) {
            StudentProfileEntity studentProfileEntity = studentRepository.findById(message.getChatId());
            studentProfileEntity.setStep(ProfileStep.DONE);
            studentProfileEntity.setVisible(Boolean.FALSE);
            studentRepository.update(studentProfileEntity);
            message.setText("/start");
            mainController.handle(message);
        }else if (studentRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_EXAM_ID)) {
            TeacherProfileEntity teacherProfileEntity = teacherRepository.findByExamId(Integer.valueOf(message.getText()));
            if (teacherProfileEntity != null && !teacherProfileEntity.getStep().equals(ProfileStep.DONE) && message.getText().matches("^[0-9]+$")){
                studentService.enterName(message);
            }else {
                myTelegramBot.sendMessage("Bunday id imtihon bo'lmayabdi yoki imtihon boshlangan!\n\nID xato bo'lishi mumkin qayta urinib ko'ring!",message.getChatId());
            }
        } else if (studentRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_NAME)) {
            if (message.getText() != null && message.getText().length() > 2 && (message.getText().trim()).matches("^[A-Za-z`']+$")){
                studentService.enterSurname(message);
            }else {
                myTelegramBot.sendMessage("Ism tog'ri shaklda kiritilmadi qayta urining!",message.getChatId());
            }
        }else if (studentRepository.findById(message.getChatId()).getStep().equals(ProfileStep.ENTER_SURNAME)&& (message.getText().trim()).matches("^[A-Za-z`']+$")) {
            if (message.getText() != null && message.getText().length() > 4){
                studentService.registerExam(message);
            }else {
                myTelegramBot.sendMessage("Familiya tog'ri shaklda kiritilmadi qayta urining!",message.getChatId());
            }
        }
    }
}
