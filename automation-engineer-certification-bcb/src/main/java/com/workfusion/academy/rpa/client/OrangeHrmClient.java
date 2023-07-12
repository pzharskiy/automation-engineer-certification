package com.workfusion.academy.rpa.client;

import com.workfusion.academy.rpa.pages.LoginPage;
import com.workfusion.automation.rpa.driver.DriverType;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.automation.rpa.driver.RobotDriverWrapper;
import com.workfusion.rpa.helpers.RPA;
import org.slf4j.Logger;

public class OrangeHrmClient {

    private final DriverWrapper wrapper;

    private static final String ORANGE_HRM_URL = "https://train-orangehrm.workfusion.com/";

    public OrangeHrmClient(Logger logger) {
        this.wrapper = new RobotDriverWrapper(logger);
    }

    //Switches driver to ChromeDriver, opens url in ChromeBrowser
    //and Returns instance of class represents appropriate page object
    public LoginPage getLoginPage() {
        wrapper.switchDriver(DriverType.CHROME);
        RPA.openChrome(ORANGE_HRM_URL);
        return new LoginPage(wrapper);
    }

}
