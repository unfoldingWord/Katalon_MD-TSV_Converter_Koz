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

repoOwner = 'manny_colon'

langCode = 'es-419'

mdType = 'tn'

repo = ((((repoOwner + '/') + langCode) + '_') + mdType)

WebUI.callTestCase(findTestCase('Login'), [:])

WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : '3jn'])

WebUI.scrollToPosition(0, 0)

WebUI.delay(1)

referenceType = WebUI.getText(findTestObject('Object Repository/label_Book_or_Reference'))

if (referenceType == 'Book') {
	checkboxes = ['ID', 'SupportReference', 'OrigQuote', 'Occurrence']
	idRow = 3
	tabCount = 8
} else {
	checkboxes = ['ID', 'Tags', 'Quote', 'Occurrence']
	idRow = 1
	tabCount = 6
}

sText = WebUI.getText(findTestObject('cell_BCV_Source_Parmed', [('row') : 4, ('column') : 5]))
println('sText:'+sText)

tText = WebUI.getText(findTestObject('cell_BCV_Target_Parmed', [('row') : 4, ('column') : 5]))
println('tText:'+tText)

myClass = WebUI.getAttribute(findTestObject('cell_Checkbox_Parmed', [('row') : 4, ('column') : 3]), 'class')
println('myClass:'+myClass)
if (myClass.indexOf('checked') >= 0) {
	println('Checked')
} else {
	println('Not checked')
}

WebUI.click(findTestObject('cell_BCV_Target_Parmed', [('row') : 4, ('column') : 3]))

WebUI.delay(3)

//myClass = WebUI.getAttribute(findTestObject('td_Checkbox_Span'), 'class')
myClass = WebUI.getAttribute(findTestObject('cell_Checkbox_Parmed', [('row') : 4, ('column') : 3]), 'class')
println('myClass:'+myClass)
if (myClass.indexOf('checked') >= 0) {
	println('Checked')
} else {
	println('Not checked')
}
return false

checked = WebUI.verifyElementChecked(findTestObject('cell_Checkbox_Parmed', [('row') : 4, ('column') : 3]), 1, FailureHandling.OPTIONAL)
println('checked:'+checked)

WebUI.click(findTestObject('cell_BCV_Target_Parmed', [('row') : 4, ('column') : 3]))

WebUI.delay(3)

checked = WebUI.verifyElementChecked(findTestObject('td_Checkbox_Span'), 1, FailureHandling.OPTIONAL)
println('checked:'+checked)


