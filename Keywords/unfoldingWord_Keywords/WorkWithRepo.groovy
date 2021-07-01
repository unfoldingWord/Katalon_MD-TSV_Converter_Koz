package unfoldingWord_Keywords

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.Keys as Keys
import groovy.io.FileType as FileType
import org.apache.commons.io.FileUtils as FileUtils

import java.awt.datatransfer.Clipboard as Clipboard
import java.awt.datatransfer.Transferable as Transferable
import java.awt.datatransfer.DataFlavor as DataFlavor
import java.awt.Toolkit as Toolkit
import java.awt.datatransfer.StringSelection;

public class WorkWithRepo {
	@Keyword
	// 02/10/21 	Modified to use the HotKeys CustomKeyword instead of hard-coded select-all and paste

	def replaceRepoContent(repo,file,user,password) {
		println('Loading file ' + file + ' into repo ' + repo)

		def tab = '   '

		// Open a firefox browser so that sendKeys will work
		if (GlobalVariable.systemOS.contains('Windows')) {
			System.setProperty("webdriver.gecko.driver","C:\\Users\\cckoz\\Katalon\\Katalon_Studio_Windows_64-7.9.0\\configuration\\resources\\drivers\\firefox_win64\\geckodriver.exe")
		} else {
			System.setProperty("webdriver.gecko.driver","/Applications/Katalon Studio.app/Contents/Eclipse/configuration/resources/drivers/firefox_mac/geckodriver");
		}
		WebDriver driver = new FirefoxDriver()
		DriverFactory.changeWebDriver(driver)

		WebUI.navigateToUrl(repo)

		if (WebUI.verifyElementPresent(findTestObject('Page_Git Repo/icon_UserSignIn'), 1)) {
			WebUI.click(findTestObject('Page_Git Repo/icon_UserSignIn'))

			WebUI.setText(findTestObject('Page_Git Repo/input_Username'), user)

			WebUI.setText(findTestObject('Page_Git Repo/input_Password'), password)

			WebUI.click(findTestObject('Page_Git Repo/button_SignIn'))
		}

		WebUI.click(findTestObject('Page_Git Repo/icon_Edit'))

		WebUI.waitForElementClickable(findTestObject('Page_Git Repo/span_ProjectTextHeader'), 15)

		while (!WebUI.getText(findTestObject('Page_Git Repo/span_ProjectTextHeader'), FailureHandling.OPTIONAL).contains(tab)) {
			WebUI.delay(1)
		}

		WebUI.delay(2)

		WebUI.click(findTestObject('Page_Git Repo/span_ProjectTextHeader'))

		WebUI.delay(1)

		HotKeys.sendKeys(null, 'all')

		WebUI.delay(1)

		File iFile = new File(file)

		String fileText = FileUtils.readFileToString(iFile)

		//Copy the file text into the clipboard
		StringSelection stringSelection = new StringSelection(fileText)

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()

		clipboard.setContents(stringSelection, null)

		HotKeys.sendKeys(null, 'paste')

		WebUI.delay(1)

		// Cancel rather than commit if the commit button is not enabled (there was not change to the content)

		def enabled = WebUI.verifyElementClickable(findTestObject('Object Repository/Page_Git Repo/button_CommitChanges'), FailureHandling.OPTIONAL)

		println('enabled is ' + enabled)

		if (enabled) {
			WebUI.click(findTestObject('Page_Git Repo/button_CommitChanges'))
		} else {
			WebUI.click(findTestObject('Page_Git Repo/button_Cancel'))
		}

		WebUI.closeBrowser()

		return fileText

	}
}