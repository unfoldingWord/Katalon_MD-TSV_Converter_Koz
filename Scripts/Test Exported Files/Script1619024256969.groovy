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

import org.apache.commons.lang3.StringUtils

import internal.GlobalVariable

dirName = (('/Users/' + GlobalVariable.pcUser) + '/Downloads')

filesPath = '/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/Reference/'

allBooks = filesPath + 'Bible_Books.csv'
ntBooks = filesPath + 'NT_Books.csv'
otBooks = filesPath + 'OT_Books.csv'
someBooks = filesPath + 'Some_Books.csv'
oneBook = filesPath + 'One_Book.csv'
epistleBooks = filesPath + 'Epistle_Books.csv'

myBooks = oneBook

rowLimit = 0

tab = '\t'

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

mdType = 'tn'

repo = repoOwner + '/' + langCode + '_' + mdType

dcsRepo = dcsBaseURL + repo

Date now = new Date()

String fName = 'md2tsv_' + mdType + '-Export-' + now.format('MMddyyhhmmss') + '.txt'

File oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/Logs/' + fName)

msg = 'Testing ' + repo
oFile.append(msg + '\n')

//======================================================================================================

type = 'rows'

bookErrors = false

WebUI.callTestCase(findTestCase('Login'), [:])

prtDetails = false

//for (book in testFiles) {
testFiles.each { book -> 
	
	msg = '\n Processing ' + book
	println(msg)
	oFile.append(msg + '\n')
	
	WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])
	
	WebUI.scrollToPosition(0, 0)
	
	WebUI.delay(1)
	
	WebUI.click(findTestObject('input_ID'))
	
	WebUI.click(findTestObject('input_SupportReference'))
	
	WebUI.click(findTestObject('input_OrigQuote'))
	
	WebUI.click(findTestObject('input_Occurrence'))

	WebUI.delay(1)
	
// Get the source and target rows
//	Source columns are: Book, Chapter, Verse, ID, SupportReference, OrigQuote, Occurrence, GLQuote, OccurrenceNote
//  Target columns are: Chapter, Verse, GLQuote, OccurrenceNote
	sRowText = getTableValues(sourcePath)
	if (prtDetails) {
		println('sRows = ' + sRowText.size())
		sRowText.each { 
			println(it)
		}
		println(sRowText)
	}
	tRowText = getTableValues(targetPath)
	if (prtDetails) {
		println('tRows = ' + tRowText.size())
		tRowText.each { 
			println(it)
		}
	}
	
	row = 0
	
//	for (text in sRowText) {
	sRowText.each { text ->
		
		String [] sFields = text.split(tab)
		if (prtDetails) {
			println('the source has ' + sFields.size() + ' fields')
		}
		bk = sFields[0]
		ch = sFields[1]
		vs = sFields[2]
		
		if (sFields.size() > 3) {
			id = sFields[3]
		} else {
			id = ''
		}
		if (sFields.size() > 4) {
			sr = sFields[4]
		} else {
			sr = ''
		}
		if (sFields.size() > 5) {
			oq = sFields[5]
		} else {
			oq = ''
		}
		if (sFields.size() > 6) {
			oc = sFields[6]
		} else {
			oc = ''
		}
		if (sFields.size() > 7) {
			glq = sFields[7]
		} else {
			glq = ''
		}
		if (sFields.size() > 8) {
			on = sFields[8]
		} else {
			on = ''
		}

		if (prtDetails) {
			println('tRow = ' + tRowText[row])
		}
		String [] tFields = tRowText[row].split(tab)
		if (prtDetails) {
			println('the target has ' + tFields.size() + ' fields')
			tFields.each {
				println(it)
			}
		}
		
		if (tFields.size() > 2 && tFields[2] != '') {
			glq = tFields[2]
		}
		
		if (tFields.size() > 3 && tFields[3] != '') {
			on = tFields[3]
		}
		
		tRowText[row] = bk + tab + ch + tab + vs + tab + id + tab + sr + tab + oq + tab + oc + tab + glq + tab + on
		
		if (prtDetails) {
			println(tRowText[row])
		}
		
		row ++
	}
	
// Get a list existing download TSV files
	oldFiles = getTSVFiles(book.toUpperCase())
	
	if (prtDetails) {
		println(oldFiles)
	}
	
	initSize = vFiles.size
	
// Get the just exported file
	
	newFiles = runExport(initSize, book.toUpperCase())
	
	if (prtDetails) {
		println(newFiles)
	}
	
	myFile = newFiles.minus(oldFiles)[0]
	
	if (prtDetails) {
		
		println('myFile:' + myFile)
	
		println('dirName2:' + dirName)
	}
	
	myFile = ((dirName + '/') + myFile)
	
// Count the tabs in each row
	tabErrors = 0
	tabs = []
	
	if (prtDetails) {
		println(myFile)
	}
	
	new File(myFile).eachLine { line ->
		
		count = StringUtils.countMatches(line, '\t')
		
		tabs.add(count)
		
		if (count != 8) {
			println('#### ERROR: Line has ' + count + ' tabs:' + line + '\n')
			tabErrors ++
		}
	}
		
//Parse the exported file
	
	exportLines = []
	
	new File(myFile).eachLine({ def line ->
		exportLines.add(line)
	})

	rowErrors = 0
	r = 0
	ids = []
	idErrors = 0
	tRowText.each { row ->
		exportRow = exportLines[r+1]
		String [] expRow = exportRow.split(tab)
		expId = expRow[3]
		if (expId in ids) {
			msg = 'ID ' + expId + ' is duplicated in the TSV file.'
			println('######## ERROR: ' + msg + '\n')
			CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'('Test failed because ' + msg)
			idErrors ++
		} else {
			ids.add(expId)
		}
		if (row != exportRow) {
			String [] appRow = row.split(tab)
			if (appRow[3] != '' || expRow[3] == '') {
				msg = 'These rows do not match:'
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'('Test failed because ' + msg)
				println('################ ' + msg)
				msg = 'The row in the app is      '  + row
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				msg = 'The row in the TSV file is ' + exportRow + '\n'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				rowErrors ++
			}
		}
		r ++
	}
	
	println(tRowText.size() + ' rows were processed.')
	println(rowErrors + ' mismatched rows were found.')
	println(idErrors + ' duplicate IDs were found.')
	println(tabErrors + ' tab errors were found.')

	
}

WebUI.closeBrowser()

def getTableValues(xPath) {
	WebDriver driver = DriverFactory.getWebDriver()
	WebElement Table = driver.findElement(By.xpath(xPath))
	List<WebElement> rows = Table.findElements(By.tagName('tr'))
	int rows_count = rows.size()
	def text = []
	if (rowLimit > 0) {
		rows.subList(rowLimit, rows_count).clear();
		rows_count = rows.size()
	}
	row = 0
	rows.each {
//	for (int row = 0; row < rows_count; row++) {
		rowText = ''
		List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))
		for (def column : (0..columns.size()-1)) {
			celltext = columns.get(column).getText()
			if (prtDetails) {
				println('row ' + row + ', column ' + column + ' is [' + celltext + ']')
			}
			rowText = rowText + celltext + '\t'
		}
		text.add(rowText)
	row ++
	}
	return text
}


def getTSVFiles(def testFile) {
	if (prtDetails) {
		println('dirName1:' + dirName)
	}

	List files = new File(dirName).list()

	vFiles = []

	files.each({ def file ->
			if (file.contains(mdType + '_' + testFile) && file.substring(file.length()-3) == 'tsv') {
				vFiles.add(file)
			}
		})

	return vFiles
}

def runExport(def initSize, def testFile) {
// Export the file
	WebUI.click(findTestObject('Object Repository/button_3-Dot'))
	
	WebUI.delay(1)
	
	WebUI.click(findTestObject('Object Repository/option_Export_To_TSV'))
	
	WebUI.delay(1)
	
	vSize = initSize

	myFile = ''

	newContent = ''

	while ((vSize <= initSize)) {
		vFiles = getTSVFiles(testFile)

		vSize = vFiles.size()

	}
	
	return vFiles
	
}

