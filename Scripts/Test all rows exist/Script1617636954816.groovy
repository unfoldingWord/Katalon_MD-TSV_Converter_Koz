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

// TEST TO VERIFY THAT ALL "ROWS" IN THE MD FILE ARE REPRESENTED IN THE TARGET ON THE SCREEN

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

errorsOnly = true

dcsBaseURL = 'https://git.door43.org/'

sourcePath = "/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[1]/table/tbody"

targetPath = "/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[2]/table/tbody"

bookFilesPath = "/html/body/div[1]/div[2]/div[2]/table/tbody"

fileTQsPath= "/html/body/div[1]/div[2]/div[2]/div[5]/div/div"

//======================================================================================================

repoOwner = 'manny_colon'

langCode = 'es-419'

mdType = 'tq'

repo = repoOwner + '/' + langCode + '_' + mdType

dcsRepo = dcsBaseURL + repo

Date now = new Date()

String fName = 'md2tsv_' + mdType + '-' + now.format('MMddyyhhmmss') + '.txt'

File oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/Logs/' + fName)

msg = 'Testing ' + repo
GlobalVariable.tcMessages.add(msg)
oFile.append(msg + '\n')

//======================================================================================================

bookErrors = false

WebUI.callTestCase(findTestCase('Login'), [:])

for (book in testFiles) {
//testFiles.each { book -> 
	
	msg = '\n Processing ' + book
	println(msg)
	GlobalVariable.tcMessages.add(msg)
	oFile.append(msg + '\n')
	
	WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])
	
	WebUI.scrollToPosition(0, 0)
	
	WebUI.delay(1)
	
	referenceType = WebUI.getText(findTestObject('Object Repository/label_Book_or_Reference'))
	
	tRowText = getTableValues(targetPath, 'rows')
	
	tRowText2 = []
	
	tRowText.each { row ->
		newRow = row.replace('[[','')
		newRow = newRow.replace(']]','')
		tRowText2.add(newRow)
	}
	
	// Open a second browser tab
	WebUI.executeJavaScript('window.open();', [])
	
	currentWindow = WebUI.getWindowIndex()
	
	//Go to new tab
	WebUI.switchToWindowIndex(currentWindow + 1)
	
	// Navigate to dcs repo
	WebUI.navigateToUrl(dcsRepo)
	
	//Select the book file
	WebUI.click(findTestObject('Object Repository/option_Book_Parmed', [('book') : book]))
	
	WebUI.delay(3)
	
	text = getTableValues(bookFilesPath,'files')
	
//	println(text)
	
	filePage = WebUI.verifyTextPresent('.md', false, FailureHandling.OPTIONAL)
	
	chapters = []
	
	if (!filePage) {
		chapters = getTableValues(bookFilesPath,'chapters')
	} else {
		chapters.add('01')
	}

//	println(chapters)
	
//	for (chapter in chapters) {
		chapters.each { chapter -> 
		println('>>>>>>> Processing chapter ' + chapter)
		if (chapters.size() > 1) {
			WebUI.click(findTestObject('option_Chapter_Parmed', [('chapter') : chapter]))
			
			text = getTableValues(bookFilesPath,'files')
			
//			println(text)
				
		}
		
//		for (file in text) {
		text.each { file ->
			println('>>>>>>> Processing file ' + file)
			
			WebUI.click(findTestObject('option_File_Parmed', [('file') : file]))
			
			//questions and answers are really GLQuote and OccurrenceNote in tN files
			
			(questions, answers) = getDivValues(fileTQsPath)
			
//			println(questions)
			
//			println(answers)
			
//			println(chapter)
			
			chpt = chapter as Integer

//			println(chpt)
			
			verse = file.replace('.md','') as Integer
			
//			println(verse)
			
			ref = chpt + ':' + verse
						
			errFlag = false

//			println(tRowText)	
			
			mdRows = []
			
			for (int i : (0..questions.size()-1)) {
				if (referenceType == 'Reference') {
					mdRows.add(ref + ' ' + questions[i] + ' ' + answers[i])
				} else {
					mdRows.add(chpt + ' ' + verse + ' ' + questions[i] + ' ' + answers[i])
				}
			}
			
			if (!errorsOnly) {
				println('\n========= mdRows')
				mdRows.each { row ->
					println(row)
				}
				println('\n========= tRowText')
				tRowText.each { row ->
					println(row)
				}
			}
			
			r = 0
			
//			for (row in mdRows) {		
			mdRows.each { row ->
				if (!errorsOnly) {
					println('markdown row ' + r + ' [' + row + ']')
					println(tRowText[r])
				}
				if (!tRowText.contains(row) && !tRowText2.contains(row)) {
					msg = 'ERROR: ' + book + ' row [' + row + '] was not found in the app'
					println(msg)
					GlobalVariable.tcMessages.add(msg)
					oFile.append(msg + '\n')
					errFlag = true
					bookError = true
//					return false
				} else {
					if (!errorsOnly) {
						msg = 'row [' + row + '] was found in the app'
						println(msg)
						GlobalVariable.tcMessages.add(msg)
						oFile.append(msg + '\n')
					}
				}
				r ++
			}
			
			if (!errFlag) {
				if (!errorsOnly) {
					msg = 'No missing rows in ' + book + ' ' + ref
					println(msg)
					GlobalVariable.tcMessages.add(msg)
					oFile.append(msg + '\n')
				}
			}
			
			WebUI.click(findTestObject('navLink_Chapter_Parmed', [('chapter') : chapter]))
			
		}
				
		WebUI.click(findTestObject('navLink_Book_Parmed', [('book') : book]))

	}
	
	if (!bookErrors) {
		msg = '   No missing rows in ' + book
		println(msg)
		GlobalVariable.tcMessages.add(msg)
		oFile.append(msg + '\n')
	}
	
	bookErrors = false

		//Switch back to original tab
	WebUI.switchToWindowIndex(currentWindow)
	
	WebUI.delay(1)
	
	WebUI.scrollToPosition(0, 0)
	
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

def getTableValues(xPath, type) {
	WebDriver driver = DriverFactory.getWebDriver()
	WebElement Table = driver.findElement(By.xpath(xPath))
	List<WebElement> rows = Table.findElements(By.tagName('tr'))
	int rows_count = rows.size()
	def text = []
	def rFields = []
	for (int row = 0; row < rows_count; row++) {
			if (type == 'rows') {
			rowtext = rows.get(row).getText()
			rowtext = rowtext.replace('\n',' ')
			text.add(rowtext)
			println(rowtext)
		} else if (type == 'columns') {
			List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
			celltext = columns.get(0).getText()	
			text.add(celltext)
		} else if (type == 'files') {
			rowtext = rows.get(row).getText()
			rowtext = rowtext.replace('\n',' ')
			println('files row = [' + rowtext + ']')
			rFields = rowtext.split(' ')
			celltext = rFields[0]
//			List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
//			celltext = columns.get(0).getText()
			if (celltext.contains('.md')) {
				text.add(celltext)
			}
		} else if (type == 'chapters') {
			rowtext = rows.get(row).getText()
			rowtext = rowtext.replace('\n',' ')
			println('chapters row = [' + rowtext + ']')
			rFields = rowtext.split(' ')
			celltext = rFields[0]
//			List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
//			celltext = columns.get(0).getText()
			if (!celltext.contains('..')) {
				text.add(celltext)
			}
		}
	}
	return text
}

def getDivValues(xPath) {
	questions = []
	answers = []
	
	WebDriver driver = DriverFactory.getWebDriver()
	WebElement Table = driver.findElement(By.xpath(xPath))
	List<WebElement> rowsq = Table.findElements(By.tagName('h1'))
	rows_count = rowsq.size()
	for (int row = 0; row < rows_count; row++) {
		rowtext = rowsq.get(row).getText()
		questions.add(rowtext)
	}
	List<WebElement> rowsa = Table.findElements(By.tagName('p'))
	rows_count = rowsa.size()
	for (row = 0; row < rows_count; row++) {
		rowtext = rowsa.get(row).getText()
		answers.add(rowtext)
	}
	return [questions, answers]
}

