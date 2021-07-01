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

baseDir = (GlobalVariable.projectPath + '/Data Files/')

goodFile = ('en_tn_57-TIT-SAVE.tsv')

errFile = ('en_tn_57-TIT-both_errors.tsv')

//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
saveFile = baseDir + goodFile
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

myURL = 'https://qa.door43.org/translate_test/en_tn/src/branch/tcc001-tc-create-1/en_tn_57-TIT.tsv'

user = GlobalVariable.validateUser

password = GlobalVariable.validatePassword

CustomKeywords.'unfoldingWord_Keywords.WorkWithRepo.replaceRepoContent'(myURL, saveFile, user, password)

GlobalVariable.scriptRunning = false
