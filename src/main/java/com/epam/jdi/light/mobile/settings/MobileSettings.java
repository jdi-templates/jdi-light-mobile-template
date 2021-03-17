package com.epam.jdi.light.mobile.settings;

import com.epam.jdi.light.common.ElementArea;
import com.epam.jdi.light.mobile.elements.base.MobileAppBaseElement;
import com.epam.jdi.light.mobile.elements.base.MobileAppUIElement;
import com.epam.jdi.light.mobile.elements.base.MobileBaseElement;
import com.epam.jdi.light.mobile.elements.base.MobileUIElement;
import com.epam.jdi.light.settings.JDISettings;
import com.epam.jdi.light.settings.WebSettings;

import static com.epam.jdi.light.driver.get.RemoteDriverInfo.SAUCE_LABS;
import static com.epam.jdi.light.driver.get.RemoteDriverInfo.appium;
import static com.epam.jdi.light.driver.get.RemoteDriverInfo.browserstack;
import static com.epam.jdi.light.driver.sauce.SauceSettings.sauceCapabilities;
import static com.epam.jdi.light.elements.base.JdiSettings.DEFAULT_CONTEXT;
import static com.epam.jdi.light.elements.init.PageFactory.STOP_INIT_CLASSES;
import static com.epam.jdi.light.mobile.driver.MobileDriverData.CAPABILITIES_FOR_ANDROID;
import static com.epam.jdi.light.mobile.driver.MobileDriverData.CAPABILITIES_FOR_IOS;
import static com.epam.jdi.light.mobile.driver.MobileDriverInfos.ANDROID_INFO;
import static com.epam.jdi.light.mobile.driver.MobileDriverInfos.IOS_INFO;
import static com.epam.jdi.light.settings.JDISettings.DRIVER;
import static com.epam.jdi.light.settings.JDISettings.ELEMENT;
import static com.epam.jdi.light.settings.WebSettings.loadCapabilities;
import static com.epam.jdi.tools.PropertyReader.fillAction;
import static java.util.Arrays.asList;

/**
 * Created by Roman Iovlev on 20.03.2019
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */
public class MobileSettings {
    public static void init() {
        setup();
        WebSettings.init();
    }

    public static void setup() {
        WebSettings.additionalInit = () -> {
            STOP_INIT_CLASSES.addAll(asList(MobileAppUIElement.class, MobileAppBaseElement.class,
                    MobileBaseElement.class, MobileUIElement.class));
            DRIVER.types.add("android", ANDROID_INFO);
            DRIVER.types.add("ios", IOS_INFO);
            DRIVER.remoteRun = true;
            ELEMENT.beforeSearch = el -> {
            };
            ELEMENT.clickType = ElementArea.CENTER;
            fillAction(p -> DRIVER.remoteUrl = getRemoteUrl(p), "remote.type");
            loadCapabilities("android.capabilities.path", "android.properties",
                    p -> p.forEach((key, value) -> CAPABILITIES_FOR_ANDROID.put(key.toString(), value.toString())));
            loadCapabilities("ios.capabilities.path", "ios.properties",
                    p -> p.forEach((key, value) -> CAPABILITIES_FOR_IOS.put(key.toString(), value.toString())));
            DEFAULT_CONTEXT = driver -> driver;
            JDISettings.DRIVER.setup = driver -> driver;
        };
    }

    private static String getRemoteUrl(String prop) {
        switch (prop.toLowerCase().replaceAll(" ", "")) {
            case "sauce":
            case "saucelabs":
                DRIVER.capabilities.common = sauceCapabilities();
                return SAUCE_LABS;
            case "browserstack":
                return browserstack();
            case "appium":
                return appium();
            default:
                return "";
        }
    }
}
