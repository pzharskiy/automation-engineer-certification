package com.workfusion.academy.service;

import com.workfusion.academy.model.Entity;
import com.workfusion.academy.model.HrmDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HrmFileGenerator {

    List<String[]> dataLines = new ArrayList<>();
    private final String[] headers;
    private String automationAcademyId;
    private final String CSV_POSTFIX = ".csv";

    public HrmFileGenerator() {
        headers = new String[]{"first_name", "middle_name", "last_name", "employee_id", "other_id", "driver's_license_no", "license_expiry_date", "gender", "marital_status", "nationality", "date_of_birth", "address_street_1", "address_street_2", "city", "state/province", "zip/postal_code", "country", "home_telephone", "mobile", "work_telephone", "work_email", "other_email"};
        dataLines.add(headers);
    }

    public String generateFile(List<Entity> employees) {
        employees.forEach(employee -> {
            HrmDao hrmDao = new HrmDao();

            String[] names = employee.getName().split(" ");

            hrmDao.setEmployee_id(automationAcademyId + "-"+ employee.getEntityId());

            if (names.length == 2) {
                hrmDao.setFirst_name(names[0]);
                hrmDao.setLast_name(names[1]);
            }
            if (names.length == 3) {
                hrmDao.setFirst_name(names[0]);
                hrmDao.setMiddle_name(names[1]);
                hrmDao.setLast_name(names[2]);
            }

            dataLines.add(hrmDao.getAsInputRow());
        });

        return saveToFile().getAbsolutePath();
    }

    public void setAutomationAcademyId(String automationAcademyId) {
        this.automationAcademyId = automationAcademyId;
    }

    private File saveToFile() {
        File csvOutputFile = new File(UUID.randomUUID() + CSV_POSTFIX);

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return csvOutputFile;
    }
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}
