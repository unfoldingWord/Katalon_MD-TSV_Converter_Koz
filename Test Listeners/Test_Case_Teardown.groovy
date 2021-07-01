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
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.io.File as File

// Modified 11/06/20	Print the browser used on summary

class Test_Case_Teardown {
	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def sampleAfterTestCase(TestCaseContext testCaseContext) {
		def suite = false
		String fileName = GlobalVariable.testSuiteLogFile
		if (fileName != null && fileName.length() > 1) {
			suite = true
			println('test case teardown in suite')
			println('log file is at ' + fileName)
		}
		
		String browser = CustomKeywords.'unfoldingWord_Keywords.GetTestingConfig.getBrowserAndVersion'()
		String separator = '==================================================================================================================================================='
		String msg = separator
		println(msg)
		if (suite) {
			File oFile = new File(fileName)
			oFile.append(msg + '\n')
		}
		
		String prefix = ' +++++++++++ '
		String testCaseStatus = testCaseContext.getTestCaseStatus()
		if (GlobalVariable.scriptRunning) {
			testCaseStatus = 'FAILED'
		}
		if (testCaseStatus != 'PASSED'  ) {
			prefix = ' ---------------------- '
		}
		
		msg = prefix + testCaseContext.getTestCaseId() + ' on ' + GlobalVariable.version + ' running on ' + browser + ' ' + testCaseStatus + prefix
		println(msg)
		if (suite) {
			File oFile = new File(fileName)
			oFile.append(msg + '\n')
		}
		
		GlobalVariable.tsMessages.add('##' + testCaseContext.getTestCaseId() )
		
		if (GlobalVariable.scriptRunning) {
			msg = '\\\\\\\\\\\\\\\\\\\\ SCRIPT ABENDED //////////'
//			println(msg)
			GlobalVariable.tcMessages.add(msg)
//			GlobalVariable.tsMessages.add(msg)
		}
		
		if (GlobalVariable.tcMessages.size() > 0) {
//			println('\n')
			for (def i : (0..GlobalVariable.tcMessages.size()-1)) {
				msg = GlobalVariable.tcMessages[i]
				println(msg)
				if (suite) {
					File oFile = new File(fileName)
					oFile.append(msg + '\n')
				}
				GlobalVariable.tsMessages.add(GlobalVariable.tcMessages[i])
			}
		}
		
		if (GlobalVariable.scriptRunning && 1 == 2) {
			msg = '\\\\\\\\\\\\\\\\\\\\ SCRIPT ABENDED //////////'
			println(msg)
			if (suite) {
				File oFile = new File(fileName)
				oFile.append(msg + '\n')
			}
			GlobalVariable.tsMessages.add(msg)
		}
		
		if (GlobalVariable.testCount > 0) {
			msg = '\n' + GlobalVariable.testCount + ' tests were run. ' + GlobalVariable.passCount + ' tests passed. ' + GlobalVariable.errorCount + ' tests failed with ' + GlobalVariable.expectedErrors + ' expected.'
			println(msg)
			if (suite) {
				File oFile = new File(fileName)
				oFile.append(msg + '\n')
				GlobalVariable.testCount = ''
				GlobalVariable.passCount = ''
				GlobalVariable.errorCount = ''
				GlobalVariable.expectedErrors = ''
			}
		}
		
		if (GlobalVariable.timings.size() > 0) {
			def first = true
			GlobalVariable.timings.each ({ def et ->
				msg = et
				if (first) {
					msg = '\n## Processing Times ##\n' + msg
					first = false
				}
				println(msg)
				if (suite) {
					File oFile = new File(fileName)
					oFile.append(msg + '\n')
				}
			})
			GlobalVariable.timings = ''
		}
		msg = separator
		println(msg)
		if (suite) {
			File oFile = new File(fileName)
			oFile.append(msg + '\n\n')
		}
	}
}