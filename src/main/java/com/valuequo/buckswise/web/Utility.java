package com.valuequo.buckswise.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.valuequo.buckswise.service.dto.AmfiDTO;

import org.apache.poi.sl.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class Utility {

    //For Dev, Comment this line and uncomment below line before code push.
      public static final String FILE_PATH = "src/main/resources/NAVAll.xlsx";
    //For Prod
    // public static final String FILE_PATH = "NAVAll.xlsx"; 
    Cell cel;

    @Autowired
    private AmfiDTO amfiDTO;

    @Autowired
    private com.valuequo.buckswise.service.AmfiService amfiService;

    // @Autowired
    // private GoogledriveService googledrive;

    /**
     * author - Sandeep Pote Fire Trigger Every Night at 12:00 AM
     */
    // @Scheduled(cron = "0/20 * * * * ?")

    //@Scheduled(cron = "0 0 0 * * *")
    @GetMapping("/uploadNav")
    public void textToJson() throws FileNotFoundException {
        try {
            System.out.println("UploadNav Service Called");
            ArrayList<AmfiDTO> al = new ArrayList<AmfiDTO>();
            Workbook workbook = WorkbookFactory.create(new File(FILE_PATH));
            System.out.println("NAV File created");

            Iterator<org.apache.poi.ss.usermodel.Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                org.apache.poi.ss.usermodel.Sheet sheet = sheetIterator.next();
            }
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            DataFormatter formatter = new DataFormatter();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                // create the object of DTO class
                AmfiDTO json1 = new AmfiDTO();
                cel = row.getCell(5);
                if (cel == null || cel.getCellType() == CellType.BLANK) {
                } else {
                    if (row.getRowNum() == 0) {
                    } else {
                        // set value to DTO object
                        json1.setSchemeCode(formatter.formatCellValue(row.getCell(0)));
                        json1.setISINDivPayoutISINGrowth(formatter.formatCellValue(row.getCell(1)));
                        json1.setISINDivReinvestment(formatter.formatCellValue(row.getCell(2)));
                        json1.setSchemeName(formatter.formatCellValue(row.getCell(3)));
                        json1.setDay1(formatter.formatCellValue(row.getCell(4)));
                        json1.setDate(formatter.formatCellValue(row.getCell(5)));
                        al.add(json1);
                    }
                }

            }
            System.out.println("Before NAV Data save");
            amfiService.save(al);
            System.out.println("After NAV Data save");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(al);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * author - Sandeep Pote Mapping the AMC_Code to NAV table
     */
    @Scheduled(cron = "0 15 0 * * *")
    @GetMapping("/putAmcCode")
    public void amfiData() {
        amfiService.getAmfiCode();
    }

    // @PostMapping("/saveFile")
    // public void saveFile(@RequestParam("file") MultipartFile file) throws IOException {
    //    googledrive.convert(file);
    // }
}