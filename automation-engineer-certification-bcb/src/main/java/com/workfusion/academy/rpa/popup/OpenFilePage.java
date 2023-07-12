package com.workfusion.academy.rpa.popup;

import com.workfusion.automation.rpa.driver.DriverType;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.automation.rpa.pages.AbstractPage;
import com.workfusion.rpa.helpers.RPA;

import static com.workfusion.rpa.helpers.RPA.pressEnter;
import static com.workfusion.rpa.helpers.RPA.switchTo;

public class OpenFilePage extends AbstractPage {

    private final String OPEN_POPUP_WINDOW_SELECTOR = "[CLASS:#32770; NAME:Open]";

    private final String BUTTON_OPEN_SELECTOR = ".Button[text='Open']";

    private final String FIELD_FILE_NAME_SELECTOR=  "[CLASS:Edit; NAME:File name:]";

    private final String BUTTON_CANCEL_SELECTOR = ".Button[text=\"Cancel\"]";


    public OpenFilePage(DriverWrapper driver) {
        super(driver);
        driver.switchDriver(DriverType.DESKTOP);
        switchTo().window(OPEN_POPUP_WINDOW_SELECTOR);
    }

    public void open(String filePath){
        String currentWindowHandle = driver.getWindowHandle();
        RPA.$(FIELD_FILE_NAME_SELECTOR).sendKeys(filePath);
        pressEnter();
        driver.waitForWindowToClose(currentWindowHandle, driver.getDriverSettings().getWindowSwitchWaitSeconds());
        driver.switchDriver(DriverType.CHROME);
    }

}
