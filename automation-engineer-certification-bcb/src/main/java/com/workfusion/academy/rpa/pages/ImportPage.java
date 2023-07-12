package com.workfusion.academy.rpa.pages;

import com.workfusion.academy.rpa.pages.navigation.TopNavigationMenu;
import com.workfusion.academy.rpa.popup.OpenFilePage;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.rpa.helpers.RPA;
import org.openqa.selenium.By;

public class ImportPage extends BasePage implements TopNavigationMenu {

    private static final String CHOOSE_FILE_ID_SELECTOR = "pimCsvImport_csvFile";
    private static final String UPLOAD_BUTTON_ID_SELECTOR = "btnSave";


    public ImportPage(DriverWrapper driver) {
        super(driver);
    }

    public ImportPage upload(String filePath){
        /*RPA.$(By.id(CHOOSE_FILE_ID_SELECTOR)).click();
        new OpenFilePage(driver).open(filePath);
        RPA.$(By.id(UPLOAD_BUTTON_ID_SELECTOR)).click();*/
        RPA.$(By.id(CHOOSE_FILE_ID_SELECTOR)).sendKeys(filePath);
        RPA.$(By.id(UPLOAD_BUTTON_ID_SELECTOR)).click();
        return this;
    }
}
