package com.example.repository;

import com.example.dto.TestDTO;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
public class TestRepository {
    public List<TestDTO> getListExel(String fileName){
        // faylni yuklash
        File file = new File("Base/" + fileName);
        FileInputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fayl o'qish uchun mo'ljal
        Sheet sheet = workbook.getSheetAt(0);
        int a = 0;
        DataFormatter dataFormatter = new DataFormatter();
        List<TestDTO> list = new LinkedList<>();
        // har bir qator bo'yicha ma'lumotlarni o'qish
        for (Row row : sheet) {
            TestDTO dto = new TestDTO();
            if(a > 0) {
                for (Cell cell : row) {
                    int columnIndex = cell.getColumnIndex();
                    String cellValue = dataFormatter.formatCellValue(cell);

                    switch (columnIndex) {
                        case 0 -> {
                            if (!cellValue.isEmpty()) {
                                dto.setId(Integer.valueOf(cellValue));
                            }
                        }
                        case 1 -> dto.setQuestion(cellValue);
                        case 2 -> dto.setAOption(cellValue);
                        case 3 -> dto.setBOption(cellValue);
                        case 4 -> dto.setCOption(cellValue);
                        case 5 -> dto.setDOption(cellValue);
                        case 6 -> dto.setAnswer(cellValue);
                        case 7 -> {
                            if (!cellValue.isEmpty() && cellValue.trim().matches("^[0-99]+$")) {
                                dto.setGrade(Double.valueOf(cellValue));
                            }else {
                                dto.setGrade(1.0);
                            }
                        }
                    }
                }
                if (dto.getId() != null){
                    list.add(dto);
                }
            }
            a++;
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public TestDTO getInfoExelByCode(Integer id,String fileName){
        List<TestDTO> list = getListExel(fileName);
        for (TestDTO e : list){
            if (e.getId() != null && e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

}
