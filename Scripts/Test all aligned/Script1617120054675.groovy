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
import groovy.time.*


allBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/Bible_Books.csv'
ntBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/NT_Books.csv'
otBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/OT_Books.csv'
someBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/Some_Books.csv'
oneBook = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/One_Book.csv'

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

langCode = 'ru'

mdType = 'tn'

repo = repoOwner + '/' + langCode + '_' + mdType

Date now = new Date()

String fName = 'rows_alligned_' + mdType + '-' + now.format('MMddyyhhmmss') + '.txt'

File oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/' + fName)
msg = 'Testing ' + repo
GlobalVariable.output.add(msg)
oFile.append(msg + '\n')

//Test book/chapter/verse only?
bcvOnly = true

space = ' '

WebUI.callTestCase(findTestCase('Login'), [:])

for (book in testFiles) {

	WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])
	
	WebUI.scrollToPosition(0, 0)
	
	WebUI.delay(3)

	sRowText = getTableValues(sourcePath, 'source')
		
	tRowText = getTableValues(targetPath, 'target')
	
	errFlag = false
	
	n = 0
	
	msg = 'Processing ' + book
	println(msg)
	GlobalVariable.output.add(msg)
	oFile.append(msg + '\n')
	
	sRowText.each { it ->
		row = n + 1
		if(tRowText[n] != it) {
			msg = book + ' mismatched row ' + row + '[' + sRowText[n] + '] [' + tRowText[n] + ']'
			println(msg)
			GlobalVariable.output.add(msg)
			oFile.append(msg + '\n')
			errFlag = true
		}
		n++
	}
	
	if (!errFlag) {
		msg = 'No mismatched rows in ' + book
		println(msg)
		GlobalVariable.output.add(msg)
		oFile.append(msg + '\n')
	}
	
//	WebUI.clickOffset(findTestObject('button_NewProject'), 30, 0)
	WebUI.waitForElementPresent(findTestObject('button_NewProject'), 5)
	
	width = WebUI.getElementWidth(findTestObject('Object Repository/button_NewProject'))
	
	Integer w = (width / 2) - 25
	
	println(w)
	
	WebUI.clickOffset(findTestObject('button_NewProject'), w, 0)

}

println('\n\n')
GlobalVariable.output.each { line ->
	println(line)
}
println('\n\n')

WebUI.closeBrowser()

def getTableValues(xPath, type) {
	WebDriver driver = DriverFactory.getWebDriver()
	WebElement Table = driver.findElement(By.xpath(xPath))
	List<WebElement> rows = Table.findElements(By.tagName('tr'))
	int rows_count = rows.size()
	def text = []
	
	for (int row = 0; row < rows_count; row++) {
		rowtext = rows.get(row).getText()
//		println(rowtext)
		if (bcvOnly) {
			spc1 = rowtext.indexOf(space)
//			println(spc1)
			spc2 = rowtext.indexOf(space, spc1+1)
//			println(spc2)
			spc3 = rowtext.indexOf(space, spc2+1)
//			println(spc3)
			if (type == 'source') {
				if (spc3 < 0) {
					spc3 = rowtext.length()
				}
				rowtext = rowtext.substring(spc1+1,spc3)
			} else {
				if (spc2 < 0) {
					spc2 = rowtext.length()
				}
				rowtext = rowtext.substring(0,spc2)
			}
		}
		text.add(rowtext)
	}
	return text
}