package com.example.service;

import com.example.MyTelegramBot;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStep;
import com.example.repository.TeacherRepository;
import com.example.util.InlineKeyBoardUtil;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Random;


@AllArgsConstructor
public class TeacherService {
    private MyTelegramBot myTelegramBot;
    private TeacherRepository teacherRepository;
    public void helloTeacher(Message message){
        ProfileEntity entity = new ProfileEntity();
        entity.setProfileId(message.getChatId());
        entity.setStep(ProfileStep.ENTER_GROUP);
        entity.setRole(ProfileRole.TEACHER);
        Random random = new Random();
        entity.setExamId(random.nextInt(100000,999999));
        entity.setStudentCount(0);
        entity.setExamFinishedStudentCount(0);
        entity.setLastMessageId(0);
        entity.setVisible(Boolean.TRUE);

        if (teacherRepository.findByIdAndRole(message.getChatId(),ProfileRole.DONE) == null
                && teacherRepository.findByIdAndRole(message.getChatId(),ProfileRole.TEACHER) == null
                && teacherRepository.findByIdAndRole(message.getChatId(),ProfileRole.STUDENT) == null){
            teacherRepository.save(entity);
        }else {
            teacherRepository.update(entity);
        }
        myTelegramBot.sendMessage("Guruhni kiriting!",message.getChatId(), ReplyKeyboardUtil.cancel());
    }
    public void enterExel(Message message){
        ProfileEntity entity = teacherRepository.findById(message.getChatId());
        entity.setGroup(message.getText());
        entity.setStep(ProfileStep.ENTER_EXEL);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Test mavjud bo'lgan exel fileni jo'nating!");
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(InlineKeyBoardUtil.infoExel());
        Integer messageId = myTelegramBot.sendMsg(sendMessage).getMessageId();
        entity.setLastMessageId(messageId);
        teacherRepository.update(entity);
    }
}
