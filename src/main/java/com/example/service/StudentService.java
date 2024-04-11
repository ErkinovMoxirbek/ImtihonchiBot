package com.example.service;

import com.example.MyTelegramBot;
import com.example.entity.StudentProfileEntity;
import com.example.entity.TeacherProfileEntity;
import com.example.enums.ProfileStep;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.repository.TestRepository;
import com.example.util.InlineKeyBoardUtil;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@AllArgsConstructor
public class StudentService {

    private MyTelegramBot myTelegramBot;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private TestRepository testRepository;

    public void enterExamId(Message message) {
        StudentProfileEntity entity = new StudentProfileEntity();
        entity.setProfileId(message.getChatId());
        entity.setGrade(0.0);
        entity.setStep(ProfileStep.ENTER_EXAM_ID);
        entity.setVisible(Boolean.TRUE);
        if (studentRepository.findById(message.getChatId()) == null){
            studentRepository.save(entity);
        }else {
            studentRepository.update(entity);
        }
        myTelegramBot.sendMessage("Imtihon idsini kiriting:", message.getChatId(), ReplyKeyboardUtil.cancel());
    }

    public void enterName(Message message) {
        StudentProfileEntity dto = studentRepository.findById(message.getChatId());
        dto.setStep(ProfileStep.ENTER_NAME);
        dto.setExamId(Integer.valueOf(message.getText()));
        studentRepository.update(dto);
        myTelegramBot.sendMessage("Ismingizni kiriting(lotin harfida): ", message.getChatId(), ReplyKeyboardUtil.cancel());
    }

    public void enterSurname(Message message) {
        StudentProfileEntity entity = studentRepository.findById(message.getChatId());
        entity.setStep(ProfileStep.ENTER_SURNAME);
        entity.setName(message.getText());
        studentRepository.update(entity);
        myTelegramBot.sendMessage("Familiyangizni kiriting(lotin harfida): ", message.getChatId(), ReplyKeyboardUtil.cancel());
    }

    public void registerExam(Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StudentProfileEntity studentProfileEntity = studentRepository.findById(message.getChatId());
                TeacherProfileEntity teacherProfileEntity = teacherRepository.findByExamId(studentProfileEntity.getExamId());
                studentProfileEntity.setStep(ProfileStep.Question);
                studentProfileEntity.setSurname(message.getText());
                studentProfileEntity.setRandomTestId(randomTest(testRepository.getListExel(teacherProfileEntity.getFileName()).size()));
                Integer messageId = myTelegramBot.sendMessage("Tez orada imtihon boshlanadi kuting!", message.getChatId()).getMessageId();
                studentProfileEntity.setLastMessageId(messageId);
                studentRepository.update(studentProfileEntity);
                System.out.println(teacherRepository.findByExamId(studentProfileEntity.getExamId()));
                teacherProfileEntity.setStudentCount(teacherProfileEntity.getStudentCount() + 1);
                teacherProfileEntity.setStudentListToString(teacherProfileEntity.getStudentListToString() + teacherProfileEntity.getStudentCount() + ". " + studentProfileEntity.getName() + " " + studentProfileEntity.getSurname() + "; /");
                if (!teacherProfileEntity.getLastMessageId().equals(0)) {
                    try {
                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setMessageId(teacherProfileEntity.getLastMessageId());
                        deleteMessage.setChatId(teacherProfileEntity.getProfileId());
                        myTelegramBot.deleteMsg(deleteMessage);
                    } catch (Exception e) {
                        e.printStackTrace(); // Istisno haqida ma'lumot chiqarish
                        System.out.println("Message not found!"); // Xabar chiqarish
                        // Boshqa xodimlarga yoki foydalanuvchilarga xabar yuborish mumkin
                    }
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(teacherProfileEntity.getStudentListToString().replace('/', '\n'));
                sendMessage.setChatId(teacherProfileEntity.getProfileId());
                sendMessage.setReplyMarkup(InlineKeyBoardUtil.startExam(teacherProfileEntity.getExamId()));
                Message m = myTelegramBot.sendMsg(sendMessage);
                teacherProfileEntity.setLastMessageId(m.getMessageId());
                teacherRepository.update(teacherProfileEntity);
            }
        }).start();
    }

    public static String randomTest(Integer countRandom) {
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        List<Integer> checkList = new LinkedList<>();

        while (checkList.size() < countRandom) {
            int randomNum = random.nextInt(1, countRandom + 1);
            if (!checkList.contains(randomNum)) {
                checkList.add(randomNum);
            }
        }

        for (int i = 0; i < countRandom; i++) {
            str.append(checkList.get(i));
            if (i < checkList.size() - 1) {
                str.append("-");
            }
        }

        return str.toString();
    }
}
