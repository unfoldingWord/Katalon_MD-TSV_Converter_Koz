import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webui.driver.DriverFactory

import internal.GlobalVariable

WebUI.callTestCase(findTestCase('Open new project'), [:])

WebUI.scrollToPosition(0, 0)

WebDriver driver = DriverFactory.getWebDriver()
WebElement Table = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[1]/table/tbody"))
List<WebElement> rows_table = Table.findElements(By.tagName('tr'))
int rows_count = rows_table.size()
int columns_count = 3
def references = []
def questions = []
def responses = []

for (int row = 0; row < rows_count; row++) {
	List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName('td'))
	for (int column = 0; column < columns_count; column++) {
		String celltext = Columns_row.get(column).getText()
		if (column == 0) {
			references.add(celltext)
		} else if (column == 1) {
			questions.add(celltext)
		} else {
			responses.add(celltext)
		}
	}
}
println(references)
println(questions)
println(responses)
