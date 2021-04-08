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

WebUI.callTestCase(findTestCase('Login'), [:])

mdType = 'tn'

owner = 'manny_colon'

repo_en = owner + '/en_' + mdType

repo_es = owner + '/es-419_' + mdType

repo = repo_es

book = '1jn'


WebUI.callTestCase(findTestCase('Select Project'), [('owner') : owner, ('repo') : repo, ('book') : book])

WebUI.scrollToPosition(0, 0)

WebUI.delay(1)
	
referenceType = WebUI.getText(findTestObject('Object Repository/label_Book_or_Reference'))

println('ref=[' + referenceType + ']')

if (referenceType == 'Reference') {
	println('ref')
} else if (referenceType == 'Book') {
	println('Book')
}
