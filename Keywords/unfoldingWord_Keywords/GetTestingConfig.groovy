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
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration

import org.openqa.selenium.Capabilities
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory
import java.awt.*

public class GetTestingConfig {
	@Keyword
	def static getOperatingSystem () {
		return(System.getProperty('os.name'))
	}

	@Keyword
	def static getBrowserAndVersion() {
		WebDriver driver = DriverFactory.getWebDriver()
		String browserName = driver.capabilities['browserName']
		return browserName
	}

	@Keyword
	def getScreenResolution() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
		Integer screenHeight = screenSize.height
		Integer screenWidth = screenSize.width
		return [screenWidth, screenHeight]
	}

	@Keyword
	def getTestCaseName() {
		String path1 = RunConfiguration.getExecutionSource()
		int pos1 = path1.lastIndexOf("/")
		if (pos1 < 0) {
			pos1 = path1.lastIndexOf($/\/$)
		}
		String x1 = path1.substring(pos1+1 , path1.length()-3);
		return(x1)
	}
}