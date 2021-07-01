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
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

import internal.GlobalVariable


public class ManageTNColumns {
	@Keyword
	// Could not figure out how to test the state, so this is strictly a toggle function
	// Also, using the 'columns_Parmed' element does not work, so the actual element names need to be passed in the array
	def toggleColumn(columns) {
		def modalOpen = WebUI.verifyElementPresent(findTestObject('Page_tCC translationNotes/btnX_CloseColumns'), 1, , FailureHandling.OPTIONAL)
		if (!modalOpen) {
			WebUI.click(findTestObject('Page_tCC translationNotes/button_ViewColumns'))
			WebUI.delay(1)
		}
		for (column in columns) {
			WebUI.click(findTestObject('Page_tCC translationNotes/columns_Parmed', [('column') : column]))
		}
		if (!modalOpen) {
			WebUI.click(findTestObject('Page_tCC translationNotes/btnX_CloseColumns'))
		}
	}

}