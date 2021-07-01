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

url = 'https://develop--tc-create-app.netlify.app/'

WebUI.callTestCase(findTestCase('Login'), [('url') : url])

WebUI.click(findTestObject('Page_tC Create/listOrg_translate_test'))

WebUI.click(findTestObject('Page_tC Create/resource_Parmed', [('resource') : 'unfoldingWord/en_tn']))

WebUI.click(findTestObject('Page_tC Create/combo_Select Language'))

WebUI.click(findTestObject('Page_tC Create/listOption_Language_Parmed', [('lang_code') : 'es-419']))

WebUI.click(findTestObject('/Page_tC Create/resource_Parmed', [('resource') : 'en_tn_65-3JN.tsv']))

WebUI.waitForElementClickable(findTestObject('Object Repository/Page_tCC translationNotes/buttonX_link_to_resources'),10)

WebUI.click(findTestObject('Object Repository/Page_tCC translationNotes/buttonX_link_to_resources'))

WebUI.click(findTestObject('Page_tC Create/resource_Parmed', [('resource') : 'unfoldingWord/en_tn']))

WebUI.click(findTestObject('/Page_tC Create/resource_Parmed', [('resource') : 'en_tn_65-3JN.tsv']))

