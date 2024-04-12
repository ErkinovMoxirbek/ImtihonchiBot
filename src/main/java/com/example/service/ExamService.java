package com.example.service;

import com.example.MyTelegramBot;
import com.example.dto.TestDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStep;
import com.example.repository.ExelRepository;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import com.example.repository.TestRepository;
import com.example.util.InlineKeyBoardUtil;
import com.example.util.ReplyKeyboardUtil;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@AllArgsConstructor
public class ExamService {
    private TestRepository testRepository;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;
    private MyTelegramBot myTelegramBot;
    private ExelRepository exelRepository;
    private SendExelTeacherService sendExelTeacherService;

    public void sendStudentTest(Message message, Integer examId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ProfileEntity> studentProfileEntityList = studentRepository.findAll();
                for (ProfileEntity s : studentProfileEntityList) {
                    String[] randomArr = s.getRandomTestId().split("-");
                    if (s.getStep().equals(ProfileStep.Question)) {
                        if (s.getExamId().equals(examId) &&
                                s.getFinishedOptionCount() < testRepository.getListExel(teacherRepository.findByExamId(examId).getFileName()).size()
                                && s.getStep().equals(ProfileStep.Question)) {
                            TestDTO t = testRepository.getInfoExelByCode(Integer.valueOf(randomArr[s.getFinishedOptionCount()]), teacherRepository.findByExamId(examId).getFileName());
                            Message message1 = myTelegramBot.sendMessage(t.getQuestion() + "\n" + t.getAOption() + "\n" + t.getBOption() + "\n" + t.getCOption() + "\n" + t.getDOption(), s.getProfileId(), InlineKeyBoardUtil.examOptions(examId, t.getId(), optionCount(t)));
                            s.setStep(ProfileStep.Question_Answer);
                            s.setLastMessageId(message1.getMessageId());
                            studentRepository.update(s);
                        }
                    }
                    if (s.getFinishedOptionCount().equals(s.getRandomTestId().split("-").length) && !s.getStep().equals(ProfileStep.DONE)) {
                        myTelegramBot.sendMessage("Savollar yakunlandi.", s.getProfileId());
                        ProfileEntity entity = teacherRepository.findByExamId(examId);
                        entity.setExamFinishedStudentCount(entity.getExamFinishedStudentCount() + 1);
                        teacherRepository.update(entity);
                        s.setStep(ProfileStep.DONE);
                        studentRepository.update(s);
                    }
                }
                if (teacherRepository.findByExamId(examId).getExamFinishedStudentCount().equals(teacherRepository.findByExamId(examId).getStudentCount())) {
                    finishExam(message, examId);
                }
            }
        }).start();
    }
    private Integer optionCount(TestDTO t){
        int count = 4;
        if (t.getDOption().isEmpty() || t.getDOption() == null){
            count--;
        }if (t.getCOption().isEmpty() || t.getCOption() == null){
            count--;
        }if (t.getBOption().isEmpty() || t.getBOption() == null){
            count--;
        }if (t.getAOption().isEmpty() || t.getAOption() == null){
            count--;
        }
        if (count < 2)return null;
        return count;
    }
    public void sendStudentEndExam(ProfileEntity s, String group){
        myTelegramBot.sendMessage("Ism: " + s.getName() + "; \n" + "Familiya: " + s.getSurname() + "; \n" + "Guruh: " + group + "; \n" + "Umumiy ball: " + s.getGrade() + "; ",s.getProfileId());
        myTelegramBot.sendMessage("Ajoyib! \nImtihon tugadi.",s.getProfileId(), ReplyKeyboardUtil.menuKeyboard());
        s.setStep(ProfileStep.DONE);
        studentRepository.update(s);
    }


    public void checkOption(Message message, String answer, String examId, String optionId) {
        ProfileEntity studentProfileEntity = studentRepository.findById(message.getChatId());
        if (!studentProfileEntity.getStep().equals(ProfileStep.Question)){
            return;
        }
        TestDTO testDTO = testRepository.getInfoExelByCode(Integer.parseInt(optionId),teacherRepository.findByExamId(Integer.valueOf(examId)).getFileName());
        studentProfileEntity.setFinishedOptionCount(studentProfileEntity.getFinishedOptionCount() + 1);
        if (testDTO != null && testDTO.getAnswer().equals(answer)){
            studentProfileEntity.setGrade(studentProfileEntity.getGrade() + testDTO.getGrade());
        }
        if (studentProfileEntity.getFinishedOptionCount() < testRepository.getListExel(teacherRepository.findByExamId(Integer.valueOf(examId)).getFileName()).size()){
            answer +="-";
        }if (studentProfileEntity.getFinishedOptionCount() <= testRepository.getListExel(teacherRepository.findByExamId(Integer.valueOf(examId)).getFileName()).size()){
            studentProfileEntity.setOptions(studentProfileEntity.getOptions() + answer);
        }
        studentRepository.update(studentProfileEntity);
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message.getMessageId());
            myTelegramBot.deleteMsg(deleteMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Message not found!");
        }
        sendStudentTest(message, Integer.valueOf(examId));

    }
    public void finishExam(Message message, Long teacherId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProfileEntity teacherProfileEntity = teacherRepository.findById(teacherId);
                teacherProfileEntity.setExamFinishedStudentCount(teacherProfileEntity.getStudentCount());
                teacherRepository.update(teacherProfileEntity);
                List<ProfileEntity> studentList = studentRepository.findAll();
                for (ProfileEntity s : studentList) {
                    if (s.getExamId().equals(teacherProfileEntity.getExamId())) {
                        Integer arrCount = s.getRandomTestId().split("-").length;
                        String str = "";
                        System.out.println(s);
                        System.out.println(!s.getFinishedOptionCount().equals(arrCount));
                        System.out.println(!s.getStep().equals(ProfileStep.DONE));
                        if (!s.getFinishedOptionCount().equals(arrCount) && !s.getStep().equals(ProfileStep.DONE)) {
                            try {
                                DeleteMessage deleteMessage = new DeleteMessage();
                                deleteMessage.setChatId(s.getProfileId());
                                deleteMessage.setMessageId(s.getLastMessageId());
                                myTelegramBot.deleteMsg(deleteMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Message not found!");
                            }
                        }
                        if (!s.getFinishedOptionCount().equals(arrCount)) {
                            System.out.println(arrCount + " | " + s.getFinishedOptionCount());
                            for (int j = 0; j < arrCount - s.getFinishedOptionCount(); j++) {
                                str += "javob yo'q";
                                if (j + 1 < arrCount - s.getFinishedOptionCount()) {
                                    str += "-";
                                }
                            }
                        }
                        s.setOptions(s.getOptions() + str);
                        studentRepository.update(s);
                    }
                }
                finishExam(message, teacherProfileEntity.getExamId());
            }
        }).start();
        }
    public void finishExam(Message message,Integer examId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean bool = true;
                System.out.println(teacherRepository.findByExamId(examId).getStudentCount() > 0);
                List<TestDTO> list = null;
                if (teacherRepository.findByExamId(examId).getStudentCount() > 0) {
                    try {
                        list = testRepository.getListExel( teacherRepository.findByExamId(examId).getFileName());
                    } catch (Exception e) {
                        e.printStackTrace(); // Istisno haqida ma'lumot chiqarish
                        System.out.println("Message not found!"); // Xabar chiqarish
                        // Boshqa xodimlarga yoki foydalanuvchilarga xabar yuborish mumkin
                    }

                    exelRepository.saveStudentExel(studentRepository.findAll(), teacherRepository.findByExamId(examId), teacherRepository.findByExamId(examId).getGroup(),list);
                    bool = sendExelTeacherService.sendDoc(teacherRepository.findByExamId(examId).getGroup(), teacherRepository.findByExamId(examId).getProfileId(), teacherRepository.findByExamId(examId).getFileName());
                }
                for (ProfileEntity s : studentRepository.findAll()) {
                    if (s.getExamId().equals(examId)) {
                        sendStudentEndExam(s, teacherRepository.findByExamId(examId).getGroup());
                    }
                }
                myTelegramBot.sendMessage("Imtihon tugadi.", teacherRepository.findByExamId(examId).getProfileId(), ReplyKeyboardUtil.menuKeyboard());
                if (bool) {
                    myTelegramBot.deleteExcelFile(teacherRepository.findByExamId(examId).getGroup() + "_Exammer.xlsx");
                    myTelegramBot.deleteExcelFile(teacherRepository.findByExamId(examId).getFileName());
                }
                for (ProfileEntity s : studentRepository.findAll()) {
                    if (s.getExamId().equals(examId)) {
                        s.setStep(ProfileStep.DONE);
                        s.setGrade(0.0);
                        s.setExamId(null);
                        s.setOptions(null);
                        s.setRole(ProfileRole.DONE);
                        s.setFinishedOptionCount(0);
                        s.setLastMessageId(null);
                        s.setRandomTestId(null);
                        s.setVisible(Boolean.FALSE);
                        studentRepository.update(s);
                    }
                }
                ProfileEntity entity = teacherRepository.findByExamId(examId);
                entity.setExamFinishedStudentCount(0);
                entity.setStep(ProfileStep.DONE);
                entity.setGroup(null);
                entity.setVisible(Boolean.FALSE);
                entity.setFileName(null);
                entity.setLastMessageId(null);
                entity.setRole(ProfileRole.DONE);
                entity.setStudentListToString(null);
                entity.setExamId(null);
                entity.setStudentCount(null);
                teacherRepository.update(entity);
                System.out.println("Exam the end");
            }
        }).start();
    }

}
