import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.configuration.RunConfiguration as RC
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.io.File as File

class Test_Suite_Setup {
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def sampleBeforeTestSuite(TestSuiteContext testSuiteContext) {
		println('running test suite ' + testSuiteContext.getTestSuiteId())
//		def dirName = RC.getProjectDir()
//		GlobalVariable.projectPath = dirName
//		def slash2 = dirName.indexOf('/',2)
//		def slash3 = dirName.indexOf('/',slash2+1)
//		def user = dirName.substring(slash2+1,slash3)
//		GlobalVariable.pcUser = user
		def dirName = RC.getProjectDir()
		println ('Project path is ' + dirName)
		GlobalVariable.projectPath = dirName
		def loc = dirName.indexOf('/Users/')
		def git = dirName.indexOf('/git')
		def user = dirName.substring(loc+7,git)
		GlobalVariable.pcUser = user
		def suite = testSuiteContext.getTestSuiteId()
		loc = suite.indexOf('/')
		def suiteName = suite.substring(loc+1,suite.length())
		Date now = new Date()
		String fName = suiteName + '-' + now.format('yyMMddhhmmss') + '.txt'
		fName = fName.replace(' ','_')
		String logsDir = '/Users/' + GlobalVariable.pcUser + GlobalVariable.logsDir
		File lDir = new File(logsDir);
		if (!lDir.isDirectory()) {
			lDir.mkdirs()
		}
		String fileName = logsDir + '/' + fName
		println('file is ' + fileName)
//		File oFile = new File(fileName)
//		oFile.append('this is a log file')
		GlobalVariable.testSuiteLogFile = fileName
		println('suite is ' + suiteName)
		println('log file is at ' + GlobalVariable.testSuiteLogFile)
	}
}