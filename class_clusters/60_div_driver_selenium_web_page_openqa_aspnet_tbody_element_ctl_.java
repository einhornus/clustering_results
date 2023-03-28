package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waits {
	
	
	
	
	public static void waitForVisibilityOfElement(WebDriver driver,WebElement element){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(element));
		
	}
	
	public static void waitForSomeSeconds(int seconds){
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {}
	
	}
	
	public static void waitForElementToBeClickable(WebDriver driver, WebElement element){
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	
	
}

--------------------

package com.example.fw;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public abstract class WebDriverHelperBase extends HelperBase {

	protected WebDriver driver;
	public boolean acceptNextAlert = true;
	
	public WebDriverHelperBase(ApplicationManager manager) {
	  super(manager);
	  this.driver = manager.getDriver();
	}

	protected void type(By locator, String text) {
		if (text != null) {
			findElement(locator).clear();
			findElement(locator).sendKeys(text);
		}
	}
	
	protected void click(By locator) {
		findElement(locator).click();
	}

	protected void selectByText(By locator, String text) {
		if (text != null) {
			new Select(findElement(locator)).selectByVisibleText(text);
		}
	}

	protected WebElement findElement(By by) {
    	return driver.findElement(by);
    }

    protected List<WebElement> findElements(By by) {
    	return driver.findElements(by);
    }

	protected boolean isElementPresent(By by) {
	    try {
	    	findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	private boolean isAlertPresent() {
	    try {
	    	driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	    	acceptNextAlert = true;
	    }
	  }

}

--------------------



import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import static org.hamcrest.Matchers.*;
import com.thoughtworks.selenium.DefaultSelenium;


public class TestTable {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
private DefaultSelenium selenium;

  @Before
  public void setUp() throws Exception {
	  selenium = new DefaultSelenium("localhost", 5555, "*firefox C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe ", "http://localhost:8752/Vaadinjpa/");
		selenium.start();
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8752/Vaadinjpa/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testUntitled2() throws Exception {
    driver.get(baseUrl);// + "/Vaadinjpa/");
    selenium.open("/Vaadinjpa/");
    
   // driver.findElement(By.xpath("//div[@id='Vaadinjpa-416572120']/div/div[2]/div/div/div/div/div/div/div/div[7]/div/div")).click();
    
    driver.findElement(By.cssSelector("textarea.v-textarea.v-textarea-focus")).click();
   // driver.findElement(By.cssSelector("textarea.v-textarea.v-textarea-focus")).clear();
    driver.findElement(By.cssSelector("textarea.v-textarea.v-textarea-focus")).sendKeys("test2");
    driver.findElement(By.xpath("//div[@id='VAADIN_COMBOBOX_OPTIONLIST']/div/div[2]/table/tbody/tr[2]/td")).click();
    driver.findElement(By.cssSelector("span.v-button-wrap")).click();
    driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
    driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("aaa@wp.pl");
    //driver.findElement(By.cssSelector("span.v-button-wrap")).click();
    //driver.findElement(By.xpath("//div[@id='Vaadinjpa-416572120']/div/div[2]/div/div/div/div/div/div/div/div[15]/div/div[2]/div/table/tbody/tr[3]/td/div")).click();
    //driver.findElement(By.xpath("//div[@id='Vaadinjpa-416572120']/div/div[2]/div/div/div/div/div/div/div/div[15]/div/div[2]/div/table/tbody/tr[4]/td/div")).click();
   // System.out.println(selenium.getText("//div[@id='Vaadinjpa-416572120']/div/div[2]/div/div/div/div/div/div/div/div[15]/div/div[2]/div/table/tbody/tr[4]/td/div"));
   // "//div[@id='Vaadinjpa-416572120']/div/div[2]/div/div/div/div/div/div/div/div[15]/div/div[2]/div/table/tbody/tr[4]/td/div"
    


  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    selenium.close();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

--------------------

