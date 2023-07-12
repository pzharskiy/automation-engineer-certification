package com.workfusion.academy.rpa.pages;

import com.workfusion.academy.rpa.pages.navigation.TopNavigationMenu;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.rpa.helpers.RPA;
import org.openqa.selenium.By;

public class EmployeeListPage extends BasePage implements TopNavigationMenu {

    private static final String EMPLOYEE_ID_FIELD_ID_SELECTOR = "empsearch_id";
    private static final String SEARCH_BUTTON_ID_SELECTOR = "searchBtn";
    private static final String LAST_PAGE_LINK_XPATH_SELECTOR = "//ul[@class=\"paging top\"]//a[contains(text(),'Last')]";


    public EmployeeListPage(DriverWrapper driver) {
        super(driver);
    }

    public EmployeeListPage id(String id){
        RPA.$(By.id(EMPLOYEE_ID_FIELD_ID_SELECTOR)).sendKeys("%"+id+"%");
        return this;
    }

    public EmployeeListPage search(){
        RPA.$(By.id(SEARCH_BUTTON_ID_SELECTOR)).click();
        return this;
    }

    public EmployeeListPage last(){
        if(RPA.exists(By.id(LAST_PAGE_LINK_XPATH_SELECTOR))) {
            RPA.$(By.id(LAST_PAGE_LINK_XPATH_SELECTOR)).click();
        }
        return this;
    }
}
