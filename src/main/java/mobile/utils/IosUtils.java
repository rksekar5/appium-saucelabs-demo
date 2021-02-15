package mobile.utils;

import com.diconium.qa.testautomationframework.common.Logger;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.TimeoutException;

import static com.diconium.qa.testautomationframework.common.Logger.logInfo;
import static mobile.driverhandler.AndroidFactory.appiumDriver;
import static mobile.utils.MobileUtils.isDeviceLocked;
import static mobile.utils.Waiters.driver;

public class IosUtils {

  /**
   * Check If the keyboard is displayed
   */
  public boolean isKeyboardDisplayed(){
    System.out.println(((IOSDriver<MobileElement>)appiumDriver).isKeyboardShown());
    return ((IOSDriver<MobileElement>)appiumDriver).isKeyboardShown();
  }

  /**
   * Hide the iOS keyboard
   */
  public void hideIOSKeyboard(){
    if(isKeyboardDisplayed()) {
      try {
        appiumDriver.hideKeyboard();
        logInfo("Keyboard minimized on the screen");
      } catch (Exception e) {
        logInfo("Unable to minimize the keyboard");
        e.printStackTrace();
      }
    }
  }

  /**
   * Check whether the specified app is installed on the iOS device
   *
   * @param app
   * @param bundleId
   */
  public static boolean checkIfIOSAppInstalled(String app, String bundleId) {
    try {
      if (driver.isAppInstalled(bundleId)) {
        return true;
      } else {
        Logger.logInfo(String.format("App '%s' with AppPackage '%s' is not installed into Device ",
                app, bundleId));
        return false;
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
      return false;
    }
  }

  /**
   * Install the given app on the iOS device
   *
   * @param app
   * @param bundleId
   */
  public static void installIOSApp(String app, String bundleId) {
    try {
      if (checkIfIOSAppInstalled(app, bundleId)) {
        Logger.logInfo(
                String.format("App '%s' with AppPackage '%s' is already present", app, bundleId));
      } else {
        driver.installApp(app);
        Logger.logInfo(String.format("App '%s' with AppPackage '%s' is now installed into Device ",
                app, bundleId));
      }

    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Remove an app from iOS device
   *
   * @param app
   * @param bundleId
   * bundleId == "com.example.AppName"
   */
  public static void removeAppFromIOSDevice(String app, String bundleId) {

    try {
      if (checkIfIOSAppInstalled(app, bundleId)) {
        driver.removeApp(bundleId);
        Logger.logInfo(String.format("App '%s' with AppPackage '%s' is removed from iOS device",
                app, bundleId));
      } else {
        Logger.logInfo(String.format(
                "App '%s' with AppPackage '%s' is not removed from iOS device", app,
                bundleId));
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Terminate the given app onto iOS device
   *
   * @param bundleId
   * bundleId == "com.example.AppName"
   */
  public static void terminateAppOnIOSDevice(String bundleId) {
    try {
      if (driver.isAppInstalled(bundleId)) {
        driver.terminateApp(bundleId);
        Logger.logInfo(String.format("The App is terminated on iOS device"));
      } else {
        Logger.logInfo(String.format("The App is not terminated on iOS device"));
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Get the state of the app on the iOS device
   * @param bundleId
   * bundleId == "com.example.AppName"
   */
  public static void getIOSAppStatus(String bundleId) {
    try {
      if (driver.isAppInstalled(bundleId)) {
        driver.queryAppState(bundleId);
        Logger.logInfo(String.format("The given app status on iOS device is: %s ",
                driver.queryAppState(bundleId)));
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Perform a shake action on iOS device
   */
  public static void shakeIOSDevice() {
    try {
      if (driver.getPlatformName().equals("ios")) {
        ((IOSDriver<MobileElement>) driver).shake();
        Logger.logInfo(String.format("The iOS device has been shaken"));
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Unlock the iOS device
   */
  public static void unlockIOSDevice() {
    try {
      if (driver.getPlatformName().equals("ios")) {
        if (isDeviceLocked()) {
          ((IOSDriver<MobileElement>) driver).unlockDevice();
          Logger.logInfo(String.format("The iOS device is unlocked"));
        } else {
          ((IOSDriver<MobileElement>) driver).lockDevice();
          ((IOSDriver<MobileElement>) driver).unlockDevice();
          Logger.logInfo(String.format("The iOS device is unlocked"));
        }
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Toggle the simulator being enrolled to accept touchId (iOS Simulator only)
   */
  public static void toggleTouchIDEnrollment(boolean enrolled) {
    try {
      if (driver.getPlatformName().equals("ios")) {
        ((IOSDriver<MobileElement>) driver).toggleTouchIDEnrollment(enrolled);
        Logger.logInfo(String.format("Toggle Touch ID has been enrolled"));
      } else {
        Logger.logInfo(String.format("Toggle Touch ID has not been enrolled because of incorrect platform name"));
      }

    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Perform Touch ID (iOS Simulator only)
   *
   * @param enrolled
   */
  public static void performTouchID(boolean enrolled) {
    try {
      if (driver.getPlatformName().equals("ios")) {
        if (enrolled == true) {
          ((IOSDriver<MobileElement>) driver).performTouchID(true);
          Logger.logInfo(String.format("Touch ID has been simulated successfully"));
        } else {
          ((IOSDriver<MobileElement>) driver).performTouchID(false);
          Logger.logInfo(String.format("Touch ID simulation is failed"));
        }
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
      Logger.logInfo(String.format("Touch ID simulation is failed because of incorrect platform name"));
    }
  }


}
