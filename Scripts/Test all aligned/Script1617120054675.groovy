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

// 	TEST TO VERIFY THAT THE SOURCE AND TARGET VERSE REFERENCES MATCH ON ALL ROWS

filesPath = '/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/Reference/'

allBooks = filesPath + 'Bible_Books.csv'
ntBooks = filesPath + 'NT_Books.csv'
otBooks = filesPath + 'OT_Books.csv'
someBooks = filesPath + 'Some_Books.csv'
oneBook = filesPath + 'One_Book.csv'
epistleBooks = filesPath + 'Epistle_Books.csv'

myBooks = oneBook

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

langCode = 'es-419'

mdType = 'tn'

repo = repoOwner + '/' + langCode + '_' + mdType

Date now = new Date()

String fName = 'rows_alligned_' + mdType + '-' + now.format('MMddyyhhmmss') + '.txt'

File oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/' + fName)
msg = 'Testing ' + repo
GlobalVariable.tcMessages.add(msg)
oFile.append(msg + '\n')

//Test book/chapter/verse only?
referenceOnly = true

space = ' '

WebUI.callTestCase(findTestCase('Login'), [:])

for (book in testFiles) {

	WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])
	
	WebUI.scrollToPosition(0, 0)
	
	WebUI.delay(3)

	referenceType = WebUI.getText(findTestObject('Object Repository/label_Book_or_Reference'))

	sRowText = getTableValues(sourcePath, 'source', referenceType)
		
	tRowText = getTableValues(targetPath, 'target', referenceType)
	
	errFlag = false
	
	n = 0
	
	msg = 'Processing ' + book
	println(msg)
	GlobalVariable.tcMessages.add(msg)
	oFile.append(msg + '\n')
	
	sRowText.each { it ->
		row = n + 1
		if(tRowText[n] != it) {
			msg = book + ' mismatched row ' + row + '[' + sRowText[n] + '] [' + tRowText[n] + ']'
			println(msg)
			GlobalVariable.tcMessages.add(msg)
			oFile.append(msg + '\n')
			errFlag = true
		}
		n++
	}
	
	if (!errFlag) {
		msg = 'No mismatched rows in ' + book
		println(msg)
		GlobalVariable.tcMessages.add(msg)
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
GlobalVariable.tcMessages.each { line ->
	println(line)
}
println('\n\n')

GlobalVariable.scriptRunning = false

WebUI.closeBrowser()

def getTableValues(xPath, type, refType) {
	WebDriver driver = DriverFactory.getWebDriver()
	WebElement Table = driver.findElement(By.xpath(xPath))
	List<WebElement> rows = Table.findElements(By.tagName('tr'))
	int rows_count = rows.size()
	String [] rFields
	def text = []
	
	for (int row = 0; row < rows_count; row++) {
		rowtext = rows.get(row).getText()
		if (type == 'source') {
			rFields = rowtext.split(' ')
			if (refType == 'Reference') {
				text.add(rFields[0])
			} else {
				text.add(rFields[1] + ':' + rFields[2])
			}
		} else { 
			rFields = rowtext.split('\n')
			if (refType == 'Reference') {
				text.add(rFields[0])
			} else {
				rFields = rFields[0].split(' ')
				text.add(rFields[0] + ':' + rFields[1])
			}
		}
	}
	return text
}