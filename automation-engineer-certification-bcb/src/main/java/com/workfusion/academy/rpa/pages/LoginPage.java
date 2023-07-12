package com.workfusion.academy.rpa.pages;

import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.rpa.helpers.RPA;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final String USERNAME_ID_SELECTOR = "txtUsername";
    private static final String PASSWORD_ID_SELECTOR = "txtPassword";
    private static final String LOGIN_ID_SELECTOR = "btnLogin";

    public LoginPage(DriverWrapper driver) {
        super(driver);
    }

    public LoginPage username(String username) {
        RPA.$(By.id(USERNAME_ID_SELECTOR)).sendKeys(username);
        return this;
    }

    public LoginPage password(String password) {
        RPA.$(By.id(PASSWORD_ID_SELECTOR)).sendKeys(password);
        return this;
    }

    public DashboardPage login() {
        RPA.$(By.id(LOGIN_ID_SELECTOR)).click();
        return new DashboardPage(getWrapper());
    }

    public DashboardPage login(String username, String password) {
        username(username);
        password(password);
        RPA.$(By.id(LOGIN_ID_SELECTOR)).click();
        return new DashboardPage(getWrapper());
    }

}
