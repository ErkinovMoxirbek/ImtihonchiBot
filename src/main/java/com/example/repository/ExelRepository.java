package com.example.repository;

import com.example.dto.TestDTO;
import com.example.entity.StudentProfileEntity;
import com.example.entity.TeacherProfileEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class ExelRepository {
    public Workbook createTeacherExel(TeacherProfileEntity teacherProfileEntity){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Jadval");

        // Exam ID, Group, Student Count ma'lumotlarini yaratish
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Exam ID");
        headerRow.createCell(1).setCellValue("Group");
        headerRow.createCell(2).setCellValue("Student Count");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(teacherProfileEntity.getExamId());
        dataRow.createCell(1).setCellValue(teacherProfileEntity.getGroup());
        dataRow.createCell(2).setCellValue(teacherProfileEntity.getStudentCount());

        // N, Name Surname, Score, Answer headerlarini yaratish
        Row scoreHeaderRow = sheet.createRow(2);
        scoreHeaderRow.createCell(0).setCellValue(" N ");
        scoreHeaderRow.createCell(1).setCellValue("Name Surname");
        scoreHeaderRow.createCell(2).setCellValue("Score");
        scoreHeaderRow.createCell(3).setCellValue("Answer");
        scoreHeaderRow.createCell(4).setCellValue("number of correct answers");

        return workbook;
    }

    public void saveStudentExel(List<StudentProfileEntity> list, TeacherProfileEntity teacherProfileEntity, String group, List<TestDTO>testDTOS) {
        int row = 3;
        String fileName = "Base/" + group + "_Exammer.xlsx";
        Workbook workbook = createTeacherExel(teacherProfileEntity);
        Sheet sheet = workbook.getSheet("Jadval");

        for (StudentProfileEntity studentProfileEntity : list) {
            if (studentProfileEntity.getExamId().equals(teacherProfileEntity.getExamId())){
                Row dataRow = sheet.createRow(row);

                dataRow.createCell(0).setCellValue(row - 2); // -2, chunki headerlar (1-qator, 2-qator) mavjud

                String fullName = studentProfileEntity.getName() + " " + studentProfileEntity.getSurname();
                dataRow.createCell(1).setCellValue(fullName);

                dataRow.createCell(2).setCellValue(studentProfileEntity.getGrade());

                String[] arrInt = studentProfileEntity.getRandomTestId().split("-");
                String[] arrStr = studentProfileEntity.getOptions().split("-");
                StringBuilder answers = new StringBuilder("Javoblar: ");
                Integer answer = 0;
                for (int i = 1; i <= arrInt.length; i++) {
                    for (int j = 0; j < arrInt.length; j++) {
                        if (arrInt[j].equals(String.valueOf(i))){
                            if (testDTOS != null && testDTOS.get(i-1).getAnswer().equals(arrStr[j+1])){
                               answer++;
                            }
                            String option = arrStr[j+1];
                            answers.append((i)).append(". ").append(option).append(", ");
                        }
                    }
                }
                dataRow.createCell(3).setCellValue(answers.toString());
                dataRow.createCell(4).setCellValue(answer.toString());
                row++;
            }
        }
        for (int i = 0; i < 4 ; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
            System.out.println("Excel fayl muvaffaqiyatli yaratildi!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
