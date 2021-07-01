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

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.io.File as File

// THIS SCRIPT RUNS VALIDATION ON ALL BOOKS IN THE BIBLE TO GET ALL CURRENT UNIQUE VALIDATION PRIORITY CODES
// THIS IS USEFUL TO DEVELOP AND MAINTAIN SCRIPTS THAT DO THE VALIDATION TESTING

WebUI.callTestCase(findTestCase('tCC Components/tCC tN Open For Edit'), [('$username') : '', ('$password') : '', ('file') : ''], 
    FailureHandling.STOP_ON_FAILURE)

testFiles = []

allBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/Bible_Books.csv'
ntBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/NT_Books.csv'
someBooks = '/Users/' + GlobalVariable.pcUser + '/Documents/Sikuli/Files/Some_Books.csv'

myBooks = someBooks

new File(myBooks).splitEachLine(',', { def fields ->
        bookNum = (fields[0])

        if (bookNum.length() < 2) {
            bookNum = ('0' + bookNum)
        }
        
        bookAbrv = (fields[1])

        bookName = (((('en_tn_' + bookNum) + '-') + bookAbrv) + '.tsv')

        testFiles.add(bookName)
    })

dirName = '/Users/' + GlobalVariable.pcUser + '/Downloads'

myPrioritys = []

errorRows = []

now = new Date()

fName = 'Unique_Validation_Errors-' + now.format('MMddyyhhmmss') + '.csv'
oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/' + fName)

//for (def testFile in testFiles) {
testFiles.each({ def testFile ->
    println('Opening ' + testFile)
	
	retCode = CustomKeywords.'unfoldingWord_Keywords.HamburgerFunctions.chooseValidationLevel'('low')
	
	vFiles = getValidationFiles(testFile)
	
	initSize = vFiles.size()
	
    CustomKeywords.'unfoldingWord_Keywords.HamburgerFunctions.chooseFile'(testFile)

    WebUI.delay(2)
	
	println('processing ' + testFile)
	
    (vSize, newContent, myFile) = runValidation(initSize, testFile)
	
	println(vSize)
	println(newContent)
	println(myFile)

	// Read the file into field lists
	prioritys = []
	if (myFile.length() > 1) {
	
		new File(myFile).splitEachLine(',', { def fields ->
				prioritys.add(fields[0])
			})
		
		newLines = []
		
		new File(myFile).eachLine({ def line ->
			newLines.add(line)
		})
	
		for (def row : (0..prioritys.size()-1) ) {
			if (!myPrioritys.contains(prioritys[row])) {
				myPrioritys.add(prioritys[row])
				errorRows.add(newLines[row])
				oFile.append(testFile + ',' + newLines[row] + '\n')
			}
		}
		
		println(myPrioritys)
		
		println(errorRows)	
	}

})

GlobalVariable.scriptRunning = false

WebUI.closeBrowser()


def getValidationFiles(def testFile) {
	List files = new File(dirName).list()

	vFiles = []

	files.each({ def file ->
			if (file.contains('Validation-' + testFile)) {
				vFiles.add(file)
			}
		})

	return vFiles
}

def runValidation(def initSize, def testFile) {
	WebUI.scrollToPosition(0, 0)

	WebUI.waitForElementClickable(findTestObject('Page_tCC translationNotes/button_validate'), 30)

	WebUI.click(findTestObject('Page_tCC translationNotes/button_validate'))

	WebUI.waitForElementPresent(findTestObject('Object Repository/Page_tCC translationNotes/alert_ValidationRunning'), 5)

	vSize = initSize

	myFile = ''

	newContent = ''

	noFile = false

	while ((vSize <= initSize) && !(noFile)) {
		vFiles = getValidationFiles(testFile)

		vSize = vFiles.size()
		
		if (!(WebUI.verifyAlertNotPresent(2, FailureHandling.OPTIONAL))) {
			noFile = true
		}
	}
	
	if (noFile) {
		WebUI.acceptAlert(FailureHandling.OPTIONAL)
	} else {
		vFiles = vFiles.toSorted()

		myFile = (vFiles[(vFiles.size() - 1)])

		println('myFile:' + myFile)

		println('dirName2:' + dirName)

		myFile = ((dirName + '/') + myFile)
		
	}
	
    return [vSize, newContent, myFile]
}

	