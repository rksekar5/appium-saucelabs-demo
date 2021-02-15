package mobile.utils;

import com.diconium.qa.testautomationframework.common.Logger;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Random;

import static com.diconium.qa.testautomationframework.common.Logger.logError;
import static com.diconium.qa.testautomationframework.common.Logger.logInfo;
import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofSeconds;
import static mobile.driverhandler.AppFactory.appiumDriver;
import static mobile.utils.Waiters.driver;
import static org.testng.Assert.*;

@Slf4j
public class MobileUtils {

  /**
   * Get attribute value from mobile element
   *
   * @param mobileElement
   */
  public static String getMobileAttributeValue(MobileElement mobileElement) {
    return mobileElement.getAttribute("value");
  }

  /**
   * Get mobile element checkbox status
   *
   * @param mobileElement
   * @return string
   */
  public static boolean getMobileElementCheckboxStatus(MobileElement mobileElement) {
    return Boolean.parseBoolean(mobileElement.getAttribute("checked"));
  }

  /**
   * Verify If the checkbox is checked
   *
   * @param mobileElement
   * @return string
   */
  public static boolean isCheckboxChecked(MobileElement mobileElement) {
    boolean checkboxStatus = getMobileElementCheckboxStatus(mobileElement);
    if (checkboxStatus) {
      logInfo("Check box: " + mobileElement + " is checked on");
      return true;
    } else {
      logInfo("Check box: " + mobileElement + " is checked off");
      return false;
    }
  }

  /**
   * check on the checkbox
   *
   * @param mobileElement
   * @return string
   */

  public static void checkOnCheckbox(MobileElement mobileElement) {
    if (! isCheckboxChecked(mobileElement)) {
      clickMobileElement(mobileElement);
      assertTrue(isCheckboxChecked(mobileElement));
      logInfo("Checked on the checkbox successfully");
    }
  }

  /**
   * Uncheck on the checkbox
   *
   * @param mobileElement
   * @return string
   */
  public static void uncheckOnCheckbox(MobileElement mobileElement) {
    if (isCheckboxChecked(mobileElement)) {
      clickMobileElement(mobileElement);
      assertFalse(isCheckboxChecked(mobileElement));
      logInfo("UnChecked the checkbox successfully");
    }
  }


  /**
   * Click on the mobile element
   *
   * @param mobileElement
   * @return string
   */
  public static void clickMobileElement(MobileElement mobileElement) {
    mobileElement.click();
    logInfo(String.format("%s clicked successfully", mobileElement));
  }

  /**
   * Type input keys to mobile element
   *
   * @param mobileElement
   * @return string
   */
  public static void sendKeysToMobileElement(MobileElement mobileElement, String input) {
    mobileElement.sendKeys(input);
    logInfo(String.format("%s is entered in %s", input, mobileElement));
  }

  /**
   * Get text from the specified mobile element
   *
   * @param mobileElement
   * @return string
   */
  public static String getTextFromMobileElement(MobileElement mobileElement) {
    return mobileElement.getText();
  }

  /**
   * Check if the mobile element is NOT displayed
   *
   * @param mobileElement
   * @return true or false
   */
  public boolean isMobileElementNotPresent(MobileElement mobileElement) {
    boolean isDisplayed = ! mobileElement.isDisplayed();
    if (isDisplayed) {
      logInfo("Element is displayed");
    } else {
      logInfo("Element is NOT displayed");
    }
    return isDisplayed;
  }

  /**
   * Check if the mobile element is displayed
   *
   * @param mobileElement
   * @return true or false
   */
  public boolean isMobileElementPresent(MobileElement mobileElement) {
    boolean isDisplayed = mobileElement.isDisplayed();
    if (isDisplayed) {
      logInfo("Element is displayed");
    } else {
      logInfo("Element is NOT displayed");
    }
    return isDisplayed;
  }

  /**
   * Move file to the path
   *
   * @param scrFile
   * @param fileNamePath
   */
  public static void moveFileToPath(File scrFile, File fileNamePath) throws IOException {
    if (Files.exists(Paths.get(String.valueOf(fileNamePath)))) {
      Logger.logInfo("File exists already");
    } else {
      Path temp =
              Files.move(Paths.get(String.valueOf(scrFile)), Paths.get(String.valueOf(fileNamePath)));
      if (temp != null) {
        Logger.logInfo("File renamed and moved successfully");
      } else {
        Logger.logInfo("Failed to move the file");
      }
    }
  }

  /**
   * Switch the active screen to Landscape mode
   */
  public static void switchScreenToLandscape(){
    appiumDriver.rotate(ScreenOrientation.LANDSCAPE);
    ScreenOrientation orientation = appiumDriver.getOrientation();
    assertSame(orientation, ScreenOrientation.LANDSCAPE);
  }

  /**
   * Switch the active screen to Portrait mode
   */
  public static void switchScreenToPortrait(){
    appiumDriver.rotate(ScreenOrientation.PORTRAIT);
    ScreenOrientation orientation = appiumDriver.getOrientation();
    assertSame(orientation, ScreenOrientation.PORTRAIT);
  }

  /**
   * Set context to web view
   */
  public static void setContextToWebView(String packageName){
    appiumDriver.context("WEBVIEW_"+packageName);
  }

  /**
   * Set context to native app
   */
  public static void setContextToNativeApp(){
    appiumDriver.context("NATIVE_APP");
  }

  /**
   * Take a screenshot of the current viewport/window/page
   *
   * @param filename
   */
  public static void takeScreenshot(String filename) throws IOException {

    File scrFile = appiumDriver.getScreenshotAs(OutputType.FILE);

    Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
    Path filePath = Paths.get(root.toString(), "src", "main", "resources", "screenshots");
    File fileNamePath = new File(filePath.toString() + "/" + filename + ".png");

    if (appiumDriver.getCapabilities().getCapability("platformName").equals("iOS")) {
      moveFileToPath(scrFile, fileNamePath);
    } else if (appiumDriver.getCapabilities().getCapability("platformName").equals("Android")) {
      moveFileToPath(scrFile, fileNamePath);
    }
  }

  /**
   * Assert if mobile element is displayed
   *
   * @param mobileElement
   */
  public void assertMobileElementPresent(MobileElement mobileElement) {
    assertTrue(isMobileElementPresent(mobileElement));
  }

  /**
   * Assert if mobile element is NOT displayed
   *
   * @param mobileElement
   */
  public void assertMobileElementNotPresent(MobileElement mobileElement) {
    assertFalse(isMobileElementPresent(mobileElement));
  }

  /**
   * Clear the input field of the mobile element
   *
   * @param mobileElement
   */
  public static void clearInputField(MobileElement mobileElement) {
    try {
      mobileElement.clear();
      logInfo(String.format("%s cleared successfully", mobileElement));
    } catch (TimeoutException e) {
      e.getMessage();
    }
  }

  /**
   * Get random numbers
   */
  public static int getRandomNumber(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min + 1) + min;
  }

  /**
   * Check If the expected text is present
   *
   * @param mobileElement
   * @param text
   */
  public static boolean isTextPresent(MobileElement mobileElement, String text) {
    try {
      return mobileElement.getText().contains(text);
    } catch (TimeoutException var3) {
      Logger.logError(String.format("The Text %s is not present", text));
      var3.getCause();
      return false;
    }
  }

  /**
   * Check If the expected text is NOT present
   *
   * @param mobileElement
   * @param text
   */
  public static boolean isTextNotPresent(MobileElement mobileElement, String text) {
    return ! isTextPresent(mobileElement, text);
  }

  /**
   * Verify If the text is present
   *
   * @param mobileElement
   * @param text
   */
  public static void verifyTextPresent(MobileElement mobileElement, String text) {
    boolean textPresent = isTextPresent(mobileElement, text);
    if (textPresent) {
      Logger.logInfo(String.format("Text: '%s' is displayed as expected", text));
    } else {
      Logger.logError(String.format("Text '%s' is not displayed", text));
    }
  }

  /**
   * Verify If the text is NOT present
   *
   * @param mobileElement
   * @param text
   */
  public static void verifyTextNotPresent(MobileElement mobileElement, String text) {
    boolean textNotPresent = isTextNotPresent(mobileElement, text);
    if (! textNotPresent) {
      Logger.logInfo(String.format("Text: '%s' is still displayed", text));
    } else {
      Logger.logError(String.format("Text '%s' is not displayed as expected ", text));
    }
  }

  /**
   * Assert If the text is present
   *
   * @param mobileElement
   * @param text
   */
  public static void assertTextPresent(MobileElement mobileElement, String text) {
    Assert.assertTrue(mobileElement.getText().contains(text));
    Logger.logInfo(String.format("Text: '%s' is displayed as expected", text));
  }

  /**
   * Assert If the value is present
   *
   * @param mobileElement
   * @param value
   */
  public static void assertValuePresent(MobileElement mobileElement, String value) {
    Assert.assertTrue(mobileElement.getAttribute("value").contains(value));
  }

  /**
   * Assert If the text is NOT present
   *
   * @param mobileElement
   * @param text
   */
  public static void assertTextNotPresent(MobileElement mobileElement, String text) {
    Assert.assertFalse(mobileElement.getText().contains(text));
    Logger.logInfo(String.format("Text: '%s' is not displayed as expected", text));
  }

  /**
   * Verify If the element is selected
   *
   * @param mobileElement
   */
  public static boolean isElementSelected(MobileElement mobileElement) {
    return mobileElement.isSelected();
  }

  /**
   * Get the size of the mobile element
   *
   * @param mobileElement
   */
  public static Dimension getElementSize(MobileElement mobileElement) {
    return mobileElement.getSize();
  }


  /**
   * Get the active mobile element
   */
  public static void getActiveElement() {
    driver.switchTo().activeElement();
  }


  /**
   * Start an Android Activity
   *
   * @param activityName
   */

  public static void startAnAndroidActivity(Activity activityName) {
    try {
      ((AndroidDriver) driver).startActivity(activityName);
    } catch (Exception e) {
      e.printStackTrace();
      e.getMessage();
      e.getCause();
    }
  }


  /**
   * Get current Android Activity
   */
  public static String getCurrentAnAndroidActivity() {
    String activity = ((AndroidDriver) driver).currentActivity();
    return activity;
  }


  /**
   * Get current Android Package
   */
  public static String getCurrentAnAndroidPackage() {
    String packageName = ((AndroidDriver) driver).getCurrentPackage();
    return packageName;
  }

  /**
   * Get Device Time on the device
   */
  public static String getDeviceTime() {
    return driver.getDeviceTime();
  }

  /**
   * Open the Keyboard
   */
  public static void getKeyboard() {
    driver.getKeyboard();
  }

  /**
   * Long Press on the mobile element
   *
   *  @param mobileElement
   */
  public static void longPressOnTheElement(MobileElement mobileElement) throws InterruptedException {

    TouchAction action = new TouchAction(driver);

    MobileElement element = mobileElement;

    action.longPress(PointOption.point(element.getLocation().getX(), element.getLocation().getY()));
    action.waitAction(WaitOptions.waitOptions(Duration.ofMillis(10000)));
    action.release().perform();
  }

  /**
   * Long Press on the mobile element and release
   *
   *  @param mobileElement
   */
  public void longPressAndRelease(MobileElement mobileElement) {
    logInfo("Long press and release the element");
    new TouchAction<>(appiumDriver)
            .longPress(longPressOptions()
                    .withElement(element(mobileElement))
                    .withDuration(ofSeconds(1)))
            .release().perform();
  }

  /**
   * Tap on the mobile element by coordinates
   */
  public static void tapByCoordinates(int xOffset, int yOffset) {
    TouchAction action = new TouchAction(driver);
    action.tap(PointOption.point(xOffset, yOffset))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3))).perform();
  }

  /**
   * Double tap on the mobile element by coordinates
   */
  public static void doubleTapByCoordinates(int x, int y) {
    TouchAction action = new TouchAction(driver);
    Point point = new Point(x, y);
    int xcord = point.getX();
    int ycord = point.getY();

    action.tap(PointOption.point(xcord, ycord))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)));
    action.perform();

    action.tap(PointOption.point(xcord, ycord))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)));
    action.perform();
  }


  /**
   * Double tap on the mobile element
   * @param mobileElement
   */
  public void doubleTapOnMobileElement(MobileElement mobileElement){
    logInfo("Double tap on the element");
    new TouchActions(appiumDriver).doubleTap(mobileElement).perform();
  }

  /**
   * Perform touch
   */
  public static void performTouch(int xOffset, int y1Offset, int y2xOffset) {
    TouchAction action = new TouchAction(driver);
    action.press(PointOption.point(xOffset, y1Offset));
    action.moveTo(PointOption.point(xOffset, y2xOffset));
    action.release();
    action.perform();
  }

  /**
   * Double tap on the touch screen using finger motion events
   *
   *  @param mobileElement
   */
  public static void doubleTapAction(MobileElement mobileElement) {
    TouchAction action = new TouchAction(driver);
    MobileElement element = mobileElement;
    assert element != null;
    Point point = element.getLocation();
    int xcord = point.getX();
    int ycord = point.getY();

    action.press(PointOption.point(xcord, ycord))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)));
    action.release().perform();

    action.press(PointOption.point(xcord, ycord))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(50)));
    action.release().perform();
  }


  /**
   * Finger move on the screen
   */
  public static void moveToScreen(int startx, int starty, int endx, int endy) {
    TouchAction action = new TouchAction(driver);

    action.press(PointOption.point(startx, starty))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
            .moveTo(PointOption.point(endx, endy));
    action.release().perform();

  }

  /**
   * Perform multi touch on the screen
   */
  public static void multiTouchPerform(Point point1, Point point2) {
    TouchAction actionOne = new TouchAction(driver);
    actionOne.press(PointOption.point(point1.getX(), point1.getY()));
    actionOne.moveTo(PointOption.point(point1.getX(), point1.getY()));
    actionOne.release();

    TouchAction actionTwo = new TouchAction(driver);
    actionTwo.press(PointOption.point(point2.getX(), point2.getY()));
    actionTwo.moveTo(PointOption.point(point2.getX(), point2.getY()));
    actionTwo.release();

    MultiTouchAction multiAction = new MultiTouchAction(driver);
    multiAction.add(actionOne);
    multiAction.add(actionTwo);
    multiAction.perform();
  }

  /**
   * Multi touch on the mobile element
   *
   * @param mobileElement
   */
  public static void multiTouchByElement(MobileElement mobileElement) {
    TouchAction action = new TouchAction(driver);

    MobileElement element = mobileElement;
    action.press(PointOption.point(element.getLocation().getX(), element.getLocation().getY()))
            .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).release();
    MultiTouchAction multiAction = new MultiTouchAction(driver);
    multiAction.add(action).perform();
  }


  /**
   * Vertical scroll down on the screen
   */
  public static void verticalScrollDown() {
    TouchAction action = new TouchAction(driver);
    Dimension size = driver.manage().window().getSize();
    // Down area points
    int start_x_down = (int) (size.width * 0.5);
    int end_x_down = (int) (size.height * 0.95);

    // Up area points
    int start_x_up = start_x_down;
    int end_x_up = (int) (size.height * 0.35);

    action.press(PointOption.point(start_x_down, end_x_down));
    action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)));
    action.moveTo(PointOption.point(start_x_up, end_x_up));
    action.release().perform();
  }

  /**
   * Vertical scroll up on the screen
   */
  public static void verticalScrollUp() {
    TouchAction action = new TouchAction(driver);

    Dimension size = driver.manage().window().getSize();

    // Area points
    int start_x = (int) (size.width / 2);
    int start_y = (int) (size.height * 0.8);

    int end_y = (int) (size.height * 0.2);

    action.press(PointOption.point(start_x, end_y));
    action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)));
    action.moveTo(PointOption.point(start_x, start_y));
    action.release().perform();

  }

  /**
   * Horizontal swipe on Seekbar (SeekBar is an extension of ProgressBar that adds a draggable thumb.
   * The user can touch the thumb and drag left or right to set the current progress level or use the arrow keys)
   * @param mobileElement
   */
  public static void swipeOnSeekbar(MobileElement mobileElement, int percentage) {
    MobileElement element = mobileElement;
    String newValue = String.valueOf(percentage / 100);
    element.setValue(newValue);
    Logger.logInfo(String.format("Element %s is moved to %s value", element,
            element.getAttribute("value").toString()));
  }



  /**
   * Single tap on the touch enabled device
   *
   * @param mobileElement
   */
  public static void singleTapAction(MobileElement mobileElement) {
    TouchAction action = new TouchAction(driver);
    MobileElement element = mobileElement;

    action.tap(PointOption.point(element.getLocation().getX(), element.getLocation().getY()));
    action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
    action.perform();
  }

  /**
   * Single tap on the mobile element of a touch enabled device
   *
   * @param mobileElement
   */
  public void tapOnMobileElement(MobileElement mobileElement) {
    logInfo("Tap on the mobile element");
    new TouchActions(appiumDriver).singleTap(mobileElement).perform();
  }


  /**
   * Launch the app under test on the device
   */
  public static void launchApp() {
    try {
      driver.launchApp();
      Logger.logInfo(String.format("The App is launched on the device"));
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Send the currently running app for this session to the background
   */
  public static void runAppInBackground(long time) {
    try {
      Duration duration = Duration.ofSeconds(time);
      driver.runAppInBackground(duration);
      Logger.logInfo(String.format("The App was running on Background of the device"));
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Close the app on device
   */
  public static void closeAnApp() {
    try {
      driver.closeApp();
      Logger.logInfo(String.format("The App is closed on the device"));
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Reset the currently running app for this session
   */
  public static void resetApp() {
    try {
      driver.resetApp();
      Logger.logInfo(String.format("The App is reset for this session"));
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Lock the Android/iOS device
   */
  public static void lockDevice() {
    try {
      if (driver.getPlatformName().equals("ios")) {
        ((IOSDriver<MobileElement>) driver).lockDevice();
        Logger.logInfo(String.format("The iOS device is locked"));
      } else if (driver.getPlatformName().equals("android")) {
        ((AndroidDriver<MobileElement>) driver).lockDevice();
        Logger.logInfo(String.format("The Android device is locked"));
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }

  /**
   * Check whether the device is locked or not
   */
  public static boolean isDeviceLocked() {
    boolean isLocked;
    try {
      if (driver.getPlatformName().equals("ios")) {
        isLocked = ((IOSDriver<MobileElement>) driver).isDeviceLocked();
        return isLocked;
      } else if (driver.getPlatformName().equals("android")) {
        isLocked = ((AndroidDriver<MobileElement>) driver).isDeviceLocked();
        return isLocked;
      }
      Logger.logInfo(String.format("The device is unlocked"));
    } catch (TimeoutException var2) {
      var2.getMessage();
      return false;
    }
    return false;
  }


  /**
   * Unlock the Android device
   */
  public static void unlockAndroidDevice() {
    try {
      if (driver.getPlatformName().equals("android")) {
        if (isDeviceLocked()) {
          ((AndroidDriver<MobileElement>) driver).unlockDevice();
          Logger.logInfo(String.format("The Android device is unlocked"));
        } else {
          ((AndroidDriver<MobileElement>) driver).lockDevice();
          ((AndroidDriver<MobileElement>) driver).unlockDevice();
          Logger.logInfo(String.format("The Android device is unlocked"));
        }
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Rotate the device in three dimensions only
   */
  public static void rotateDevice() {
    try {
      driver.rotate(new DeviceRotation(10, 10, 10));
      Logger.logInfo(String.format("The Device is rotated in three dimensions"));
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Toggle airplane mode on Android device
   */
  public static void toggleAirplaneMode() {
    if (driver.getPlatformName().equals("android")) {
      ((AndroidDriver<MobileElement>) driver).toggleAirplaneMode();
      Logger.logInfo(String.format("The Device is in Airplane mode"));
    } else {
      Logger.logInfo(String.format("The Device is not in Airplane mode"));
    }
  }


  /**
   * Switch the state of the wifi service
   */
  public static void toogleWifi() {
    if (driver.getPlatformName().equals("android")) {
      ((AndroidDriver<MobileElement>) driver).toggleWifi();
      Logger.logInfo(String.format("Toggle Wifi"));
    }
  }

  /**
   * Switch the state of the location service
   */
  public static void toggleLocationServices() {
    if (driver.getPlatformName().equals("android")) {
      ((AndroidDriver<MobileElement>) driver).toggleLocationServices();
      Logger.logInfo(String.format("Toggle Location service"));
    }
  }


  /**
   * Simulate an SMS message (Emulator only)
   */
  public static void sendSMS(String phoneNumber, String message) {
    try {
      if (driver.getPlatformName().equals("android")) {
        ((AndroidDriver<MobileElement>) driver).sendSMS(phoneNumber, message);
        Logger.logInfo(String.format("A SMS Message is sent"));
      } else {
        Logger.logInfo(String.format("Not possible to send a SMS Message"));
      }
    } catch (TimeoutException var2) {
      var2.getMessage();
    }
  }


  /**
   * Open Android notification Bar (Emulator only)
   */
  public static void openNotifications() {
    if (driver.getPlatformName().equals("android")) {
      ((AndroidDriver<MobileElement>) driver).openNotifications();
    }
  }


  /**
   * Open a url on the browser
   */
  public static void openBrowserUrl(String url) {
    driver.get(url);
  }


  /**
   * Switch to Alert
   */
  public static void switchToAlert() {
    try {
      driver.switchTo().alert();
    } catch (Exception e) {
      e.getCause();
      e.getMessage();
    }
  }

  /**
   * Switch to Frame
   */
  public static void switchToFrame(MobileElement mobileElement) {
    MobileElement element = mobileElement;
    driver.switchTo().frame(element);
  }

  /**
   * Reload the current page.
   */
  public static void reload() {
    driver.navigate().refresh();
  }

  /**
   * Delete all cookies
   */
  public static void deleteAllCookies() {
    driver.manage().deleteAllCookies();
  }

  /**
   * Perform drag and drop
   * @param fromElement
   * @param toElement
   */
  public static void dragAndDrop(MobileElement fromElement, MobileElement toElement) {
    logInfo(String.format("Drag element from %s to %s", fromElement, toElement));
    new TouchAction<>(appiumDriver)
            .longPress(longPressOptions()
                    .withElement(element(fromElement))
                    .withDuration(ofSeconds(1)))
            .moveTo(element(toElement))
            .release().perform();
  }

  /**
   * Click and hold on the mobile element
   * @param mobileElement
   */
  public void clickAndHold(MobileElement mobileElement){
    logInfo("Click and hold on the element");
    new Actions(appiumDriver).clickAndHold(mobileElement).release().perform();
  }

  /**
   * Swipe from top to bottom
   */
  public void swipeFromUpToBottomWithJavascriptExecutor()
  {
    try {
      JavascriptExecutor js = appiumDriver;
      HashMap<String, String> scrollObject = new HashMap<>();
      scrollObject.put("direction", "up");
      js.executeScript("mobile: swipe", scrollObject);
      log.debug("Swipe up was Successfully done");
    }
    catch (Exception e)
    {
      logError("swipe up was not successfull: "+e);
    }
  }

  /**
   * Swipe from bottom to top
   */
  public void swipeFromBottomToUpWithJavascriptExecutor()
  {
    try  {
      JavascriptExecutor js = appiumDriver;
      HashMap<String, String> scrollObject = new HashMap<>();
      scrollObject.put("direction", "down");
      js.executeScript("mobile: swipe", scrollObject);
      log.debug("Swipe down was Successfully done");
    }
    catch (Exception e)
    {
      logError("swipe up was not successfull: "+e);
    }
  }

  /**
   * Scroll from top to bottom
   */
  public void scrollFromUpToBottomWithJavascriptExecutor()
  {
    try {
      JavascriptExecutor js = appiumDriver;
      HashMap<String, String> scrollObject = new HashMap<>();
      scrollObject.put("direction", "up");
      js.executeScript("mobile: scroll", scrollObject);
      log.debug("Swipe up was Successfully done");
    }
    catch (Exception e)
    {
      logError("swipe up was not successful: "+e);
    }
  }

  /**
   * Scroll from bottom to top
   */
  public void scrollFromBottomToUpWithJavascriptExecutor()
  {
    try  {
      JavascriptExecutor js = appiumDriver;
      HashMap<String, String> scrollObject = new HashMap<>();
      scrollObject.put("direction", "down");
      js.executeScript("mobile: scroll", scrollObject);
      log.debug("Swipe down was Successfully done");
    }
    catch (Exception e)
    {
      logError("swipe up was not successful: "+e);
    }
  }

  /**
   * Swipe right
   */
  public void swipeRight(){
    swipe("right");
  }

  /**
   * Swipe left
   */
  public void swipeLeft(){
    swipe("right");
  }

  /**
   * Swipe up
   */
  public static void swipeUp(){
    swipe("up");
  }

  /**
   * Swipe down
   */
  public static void swipeDown(){
    swipe("down");
  }

  /**
   * Swipe on the given direction
   */
  private static void swipe(String direction) {
    Dimension size = appiumDriver.manage().window().getSize();

    int startX;
    int endX;
    int startY;
    int endY;

    switch (direction) {
      case "right":
        startY = size.height / 2;
        startX = (int) (size.width * 0.90);
        endX = (int) (size.width * 0.05);
        new TouchAction<>(appiumDriver)
                .press(point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
                .moveTo(point(endX, startY))
                .release()
                .perform();
        break;

      case "left":
        startY = size.height / 2;
        startX = (int) (size.width * 0.05);
        endX = (int) (size.width * 0.90);
        new TouchAction<>(appiumDriver)
                .press(point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
                .moveTo(point(endX, startY))
                .release()
                .perform();
        break;

      case "up":
        endY = (int) (size.height * 0.70);
        startY = (int) (size.height * 0.30);
        startX = (size.width / 2);
        new TouchAction<>(appiumDriver)
                .press(point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
                .moveTo(point(startX, endY))
                .release()
                .perform();
        break;

      case "down":
        startY = (int) (size.height * 0.70);
        endY = (int) (size.height * 0.30);
        startX = (size.width / 2);
        new TouchAction<>(appiumDriver)
                .press(point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
                .moveTo(point(startX, endY))
                .release()
                .perform();
        break;
    }
  }

  /**
   * Retrieves the device location
   */
  public static void getDeviceLocation(){
    final Location location = appiumDriver.location();
    logInfo(String.format("Device location is: %s", location.toString()));
  }

  /**
   * Assign the device location
   *
   * @param latitude
   * @param longitude
   * @param altitude
   */
  public static void setDeviceLocation(long latitude, long longitude, long altitude){
    appiumDriver.setLocation(new Location(latitude,longitude,altitude));
    logInfo(String
            .format("Location set for devices successfully to %s %s %s", latitude, longitude, altitude));
  }

  /**
   * Scroll to the mobile element
   *
   * @param mobileElement
   */
  public void scrollToElement(MobileElement mobileElement){
    new TouchActions(appiumDriver).scroll(mobileElement, 10, 100).perform();
  }

  public static MobileElement getMobileElementWithXpath(String locator){
    return appiumDriver.findElementByXPath(locator);
  }

  public static MobileElement getMobileElementWithAccessibilityId(String locator){
    return appiumDriver.findElementByAccessibilityId(locator);
  }

  public MobileElement getMobileElementWithId(String locator){
    return appiumDriver.findElementById(locator);
  }

}
