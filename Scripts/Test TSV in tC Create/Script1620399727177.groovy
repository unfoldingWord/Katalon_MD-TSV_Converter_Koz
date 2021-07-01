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
import org.openqa.selenium.Keys as Keys

println('File is ' + myFile)

println('Book number is ' + bookNumber)

fields = myFile.split('/')

file = fields[fields.size()-1]

f1 = file.substring(3,6)

println(f1)

repoFile = 'en_tn_' + bookNumber + '-' + f1 + '.tsv'

repo = 'https://qa.door43.org/translate_test/es-419_tn/src/branch/tc01-tc-create-1/' + repoFile

println('repo is' + repo)

// Load the tN project to create the repo if it doesn't already exist
url = 'https://develop--tc-create-app.netlify.app/'

WebUI.callTestCase(findTestCase('Login'), [('url') : url])

WebUI.click(findTestObject('Page_tC Create/listOrg_translate_test'))

WebUI.click(findTestObject('Page_tC Create/resource_Parmed', [('resource') : 'unfoldingWord/en_tn']))

WebUI.click(findTestObject('Page_tC Create/combo_Select Language'))

WebUI.click(findTestObject('Page_tC Create/listOption_Language_Parmed', [('lang_code') : 'es-419']))

WebUI.click(findTestObject('/Page_tC Create/resource_Parmed', [('resource') : repoFile]))

WebUI.delay(10)

WebUI.closeBrowser()

// Load the new tsv file into the repo
msg = 'Loading file to ' + repo

println(msg)

CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendInfoMessage'(msg)

fileText = CustomKeywords.'unfoldingWord_Keywords.WorkWithRepo.replaceRepoContent'(repo, myFile, 'tc01', 'tc01')

// Reload the project to get the new repo content
WebUI.callTestCase(findTestCase('Login'), [('url') : url])

WebUI.click(findTestObject('Page_tC Create/listOrg_translate_test'))

WebUI.click(findTestObject('Page_tC Create/resource_Parmed', [('resource') : 'unfoldingWord/en_tn']))

WebUI.click(findTestObject('Page_tC Create/combo_Select Language'))

WebUI.click(findTestObject('Page_tC Create/listOption_Language_Parmed', [('lang_code') : 'es-419']))

WebUI.click(findTestObject('/Page_tC Create/resource_Parmed', [('resource') : repoFile]))

if (fileText.length() > 0) {
	println(fileText)
		
	columns = ['Book', 'Chapter', 'Verse', 'ID', 'OrigQuote', 'Occurrence', 'GLQuote']
	
	CustomKeywords.'unfoldingWord_Keywords.ManageTNColumns.toggleColumn'(columns)
	
	WebUI.click(findTestObject('Object Repository/Page_tCC translationNotes/button_Preview'))
	
	WebUI.click(findTestObject('Page_tCC translationNotes/list_RowsPerPage'))
	
	WebUI.click(findTestObject('Page_tCC translationNotes/option_RowsPerPage_parmned', [('rows') : 10]))
	
	WebUI.click(findTestObject('Page_tCC translationNotes/button_Search'))
	
	books = []
	
	chapters = []
	
	verses = []
	
	ids = []
	
	sRefs = []
	
	oQuotes = []
	
	occurs = []
	
	gQuotes = []
	
	oNotes = []
	
	fileText.splitEachLine('\t', { def fields ->
	        books.add(fields[0])
	
	        chapters.add(fields[1])
	
	        verses.add(fields[2])
	
	        ids.add(fields[3])
	
	        sRefs.add(fields[4])
	
	        oQuotes.add(fields[5])
	
	        occurs.add(fields[6])
	
	        gQuotes.add(fields[7])
			
			oNote = fields[8].replace('<br>','\n')
	        oNotes.add(oNote)
	    })
	
	errorCount = 0
	r = 0
	ids.each { id ->
	//for (def id : ids) {
	    if (id != 'ID') {
	        WebUI.setText(findTestObject('Page_tCC translationNotes/input_Search'), id)
	
	        myBook = WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_Book_SearchId'))
	        if (books[r] != myBook) {
	            msg = 'Book does not match on ID ' + id + '. File [' + books[r] + '] - Screen [' + myBook + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        myChapter= WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_Chapter_SearchId')) 
	        if (chapters[r] != myChapter) {
	            msg = 'Chapter does not match on ID ' + id + '. File [' + chapters[r] + '] - Screen [' + myChapter + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        myVerse = WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_Verse_SearchId'))
	        if (verses[r] != myVerse) {
	            msg = 'verse does not match on ID ' + id + '. File [' + verses[r] + '] - Screen [' + myVerse + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        mySRef = WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_SupportReference_SearchId'))
	        if (sRefs[r] != mySRef) {
	            msg = 'SupportReference does not match on ID ' + id + '. File [' + sRefs[r] + '] - Screen [' + mySRef + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        myOQuote= WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_OrigQuote_SearchId'))
	        if (oQuotes[r] != myOQuote) {
	            msg = 'OrigQuote does not match on ID ' + id + '. File [' + oQuotes[r] + '] - Screen [' + myOQuote + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        myOccur= WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_Occurrence_SearchId'))
	        if (occurs[r] != myOccur) {
	            msg = 'Occurrence does not match on ID ' + id + '. File [' + occurs[r] + '] - Screen [' + myOccur + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        myGQuote= WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_GLQuote_SearchId'))
	        if ((gQuotes[r]) != myGQuote) {
	            msg = 'GLQuote does not match on ID ' + id + '. File [' + gQuote[r] + '] - Screen [' + myGQuote + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
	        myONote= WebUI.getText(findTestObject('Page_tCC translationNotes/text_Preview_OccurrenceNote_SearchId'))
	        if (oNotes[r] != myONote) {
	            msg = 'OccurrenceNote does not match on ID ' + id + '.\nFile [' + oNotes[r] + ']\nScrn [' + myONote + ']'
				println(msg)
				CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
				errorCount ++
	        }
			
			WebUI.sendKeys(findTestObject('Page_tCC translationNotes/input_Search'), Keys.chord(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, 
			        Keys.BACK_SPACE))
	    }
		
		r ++
	}
	if (errorCount > 0) {
		msg = errorCount + ' mismatached field errors were detected in the tN project.'
		println(msg)
		CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
	} else {
		msg = 'No mismatached field errors were detected in the tN project.'
		println(msg)
		CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendInfoMessage'(msg)
	}
	
} else {
	msg = 'ERROR: Unable to commit the new target TSV file because it generated no changes,'
	println(msg)
	CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
}

WebUI.closeBrowser()
