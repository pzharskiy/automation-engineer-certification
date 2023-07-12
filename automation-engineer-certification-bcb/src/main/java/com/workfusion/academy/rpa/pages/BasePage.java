package com.workfusion.academy.rpa.pages;


import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.automation.rpa.pages.AbstractPage;

public abstract class BasePage extends AbstractPage {

    public BasePage(DriverWrapper driver) {
        super(driver);
        waitForWebPageToLoad();
    }

    public DriverWrapper getWrapper() {
        return this.driver;
    }
}