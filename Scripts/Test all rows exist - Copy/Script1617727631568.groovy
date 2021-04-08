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
oneBook = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/One_Book.csv'
epistleBooks = '/Users/cckozie/Documents/Sikuli/Files/Epistle_Books.csv'

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
mdType = 'tq'

repoOwner = 'manny_colon'

repo_en = repoOwner + '/en_' + mdType

repo_es = repoOwner + '/es-419_' + mdType

repo = repo_es

dcsRepo = dcsBaseURL + repo

fName = 'md2tsv_' + mdType + '.txt'

Date now = new Date()

String fName = 'md2tsv_tq-' + now.format('MMddyyhhmmss') + '.txt'

File oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/' + fName)
//======================================================================================================

type = 'rows'

bookErrors = false

first = true

WebUI.callTestCase(findTestCase('Login'), [:])

//for (book in testFiles) {
testFiles.each { book -> 
	
	msg = '\n Processing ' + book
	println(msg)
	GlobalVariable.output.add(msg)
	oFile.append(msg + '\n')
	
	
	println(repoOwner)
	
	WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])
	
	WebUI.scrollToPosition(0, 0)
	
	WebUI.delay(1)
	
	tRowText = getTableValues(targetPath, type)
	
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
	
	println(text)
	
	filePage = WebUI.verifyTextPresent('.md', false, FailureHandling.OPTIONAL)
	
	chapters = []
	
	if (!filePage) {
		chapters = getTableValues(bookFilesPath,'chapters')
	} else {
		chapters.add('01')
	}

	println(chapters)
	
//	for (chapter in chapters) {
		chapters.each { chapter -> 
		println('>>>>>>> Processing chapter ' + chapter)
		if (chapters.size() > 1) {
			WebUI.click(findTestObject('option_Chapter_Parmed', [('chapter') : chapter]))
			
			text = getTableValues(bookFilesPath,'files')
			
			println(text)
				
		}
		
//		for (file in text) {
		text.each { file ->
			println('>>>>>>> Processing file ' + file)
			
			WebUI.click(findTestObject('option_File_Parmed', [('file') : file]))
			
			(questions, answers) = getDivValues(fileTQsPath)
			
			println(questions)
			
			println(answers)
			
			println(chapter)
			
			chpt = chapter as Integer

			println(chpt)
			
			verse = file.replace('.md','') as Integer
			
			println(verse)
			
			ref = chpt + ':' + verse
						
			errFlag = false

//			println(tRowText)	
			
			mdRows = []
			
			for (int i : (0..questions.size()-1)) {
//				mdRows.add(ref + ' ' + questions[i] + ' ' + answers[i])
				mdRows.add(chpt + ' ' + verse + ' ' + questions[i] + ' ' + answers[i])
			}
			
//			mdRows.each { row ->
//				println(row)
//			}
			
			r = 0
			tRowText.each { row ->
				println(row)
			}
//			for (row in mdRows) {		
			mdRows.each { row ->
				println('markdown row:' + row)
//				println(tRowText[r])
				if (!tRowText.contains(row)) {
					msg = 'ERROR: ' + book + ' row [' + row + '] was not found in the app'
					println(msg)
					GlobalVariable.output.add(msg)
					oFile.append(msg + '\n')
					errFlag = true
					bookError = true
				} else {
					if (!errorsOnly) {
						msg = 'row [' + row + '] was found in the app'
						println(msg)
						GlobalVariable.output.add(msg)
						oFile.append(msg + '\n')
					}
				}
				r ++
			}
			return false
			if (!errFlag && !errorsOnly) {
				msg = 'No missing rows in ' + book + ' ' + ref
				println(msg)
				GlobalVariable.output.add(msg)
				oFile.append(msg + '\n')
			}
			
			WebUI.click(findTestObject('navLink_Chapter_Parmed', [('chapter') : chapter]))
			
		}
				
		WebUI.click(findTestObject('navLink_Book_Parmed', [('book') : book]))

	}
	
	if (!first && !bookErrors) {
		msg = '   No missing rows in ' + book
		println(msg)
		GlobalVariable.output.add(msg)
		oFile.append(msg + '\n')
	}
	
	first = false
	
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
			if (type == 'rows') {
			rowtext = rows.get(row).getText()
			text.add(rowtext)
		} else if (type == 'columns') {
			List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
			celltext = columns.get(0).getText()	
			text.add(celltext)
		} else if (type == 'files') {
			List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
			celltext = columns.get(0).getText()
			if (celltext.contains('.md')) {
				text.add(celltext)
			}
		} else if (type == 'chapters') {
			List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
			celltext = columns.get(0).getText()
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

