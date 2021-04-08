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


allBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/Bible_Books.csv'
ntBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/NT_Books.csv'
otBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/OT_Books.csv'
someBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/Some_Books.csv'

myBooks = someBooks

testFiles = []

new File(myBooks).splitEachLine(',', { def fields ->
		bookNum = (fields[0])

		if (bookNum.length() < 2) {
			bookNum = ('0' + bookNum)
		}
		
		bookAbrv = (fields[1])

		testFiles.add(bookAbrv.toLowerCase())
	})

sourcePath = "/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[1]/table/tbody"

targetPath = "/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[2]/table/tbody"

repoOwner = 'manny_colon'

repo_en = 'manny_colon/en_tq'

repo_es = 'manny_colon/es-419_tq'

repo = repo_es

WebUI.callTestCase(findTestCase('Login'), [:])

for (book in testFiles) {

	WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])
	
	WebUI.scrollToPosition(0, 0)
	
	WebUI.delay(3)
	
	sRowText = getTableValues(sourcePath)
	
	tRowText = getTableValues(targetPath)
	
	errFlag = false
	
	n = 0
	
	println('Processing ' + book)
	sRowText.each { it ->
		row = n + 1
		if(tRowText[n] != it) {
			println('mismatched row ' + row + '\n[' + sRowText[n] + ']\n[' + tRowText[n] + ']\n')
			GlobalVariable.output.add(book + ' mismatched row ' + row + '[' + sRowText[n] + '] [' + tRowText[n] + ']')
			errFlag = true
		}
		n++
	}
	
	if (!errFlag) {
		println('No mismatching rows')
		GlobalVariable.output.add('No mismatched rows in ' + book)
//		WebUI.closeBrowser()
	}
	
	WebUI.clickOffset(findTestObject('button_NewProject'), 30, 0)
	
}

println('\n\n')
GlobalVariable.output.each { line ->
	println(line)
}
println('\n\n')

WebUI.closeBrowser()

def getTableValues(xPath) {
	WebDriver driver = DriverFactory.getWebDriver()
	WebElement Table = driver.findElement(By.xpath(xPath))
	List<WebElement> rows = Table.findElements(By.tagName('tr'))
	int rows_count = rows.size()
	def rowText = []
	
	for (int row = 0; row < rows_count; row++) {
		rowtext = rows.get(row).getText()
		rowText.add(rowtext)
	}
	return rowText
}