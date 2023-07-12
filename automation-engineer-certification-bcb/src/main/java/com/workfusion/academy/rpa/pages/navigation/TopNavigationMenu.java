package com.workfusion.academy.rpa.pages.navigation;

import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;
import com.workfusion.academy.rpa.pages.BasePage;
import com.workfusion.academy.rpa.pages.EmployeeListPage;
import com.workfusion.academy.rpa.pages.ImportPage;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.automation.rpa.pages.AbstractPage;
import com.workfusion.rpa.helpers.RPA;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface TopNavigationMenu {

    default TopNavigation topNavigation() {
        return new TopNavigation(getWrapper());
    }

    class TopNavigation {

        private static final String PIM_ID_SELECTOR="menu_pim_viewPimModule";
        private static final String CONFIGURATION_ID_SELECTOR="menu_pim_Configuration";
        private static final String IMPORT_ID_SELECTOR="menu_admin_pimCsvImport";
        private static final String WELCOME_ID_SELECTOR = "welcome";
        private static final String LOGOUT_XPATH_SELECTOR = "//*[contains(text(),'Logout')]";
        private static final String EMPLOYEE_LIST_ID_SELECTOR = "menu_pim_viewEmployeeList";

        DriverWrapper wrapper;

        public TopNavigation(DriverWrapper wrapper) {
            this.wrapper = wrapper;
        }

        public ImportPage importPage() {
            RPA.$(By.id(PIM_ID_SELECTOR)).hover();
            RPA.$(By.id(CONFIGURATION_ID_SELECTOR)).hover();
            RPA.$(By.id(IMPORT_ID_SELECTOR)).click();
            return new ImportPage(wrapper);
        }

        public EmployeeListPage employeeListPage(){
            RPA.$(By.id(PIM_ID_SELECTOR)).hover();
            RPA.$(By.id(EMPLOYEE_LIST_ID_SELECTOR)).click();
            return new EmployeeListPage(wrapper);
        }

        public void logout() {
            RPA.$(By.id(WELCOME_ID_SELECTOR)).click();
            WebDriverWait wait = new WebDriverWait(wrapper.getDriver(), 20);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LOGOUT_XPATH_SELECTOR)));
            RPA.$(By.xpath(LOGOUT_XPATH_SELECTOR)).click();
        }

        ;
    }

    DriverWrapper getWrapper();
}
