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

class Test_Suite_Teardown {
	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def sampleAfterTestSuite(TestSuiteContext testSuiteContext) {
//		Date now = new Date()
		String msg = ""
//		String fName = 'test_suite-' + now.format('MMddyyhhmmss') + '.txt'
//		File oFile = new File('/Users/' + GlobalVariable.pcUser + '/Katalon Studio/Files/' + fName)
		String separator = "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv"
		println(separator)
		String prefix = ">>>>>>>>>"
		boolean hdr = false
		boolean first = true
		boolean error = false
		for (def i : (0..GlobalVariable.tsMessages.size()-1)) {
			if (GlobalVariable.tsMessages[i] != null && GlobalVariable.tsMessages[i].length() > 2) {
				if (GlobalVariable.tsMessages[i][0..1] == "##") {
					if (!first) {
						if (hdr) {
							if (!error) {
								msg = "+++++++++++++++++++ PASSED +++++++++++++++++++++"
							}
							println(msg)
//							oFile.append(msg + '\n')
							error = false
						}
						if (error) {
							msg = "******************* FAILED *********************"
							println(msg)
//							oFile.append(msg + '\n\n')
							error = false
						}
						msg = "\n        ----------------------------------------\n"
						println(msg)
//						oFile.append(msg)
					}
					msg = prefix + GlobalVariable.tsMessages[i][2..GlobalVariable.tsMessages[i].length()-1]
					println(msg)
//					oFile.append(msg + '\n')
					hdr = true
					first = false
				} else {
					msg = GlobalVariable.tsMessages[i]
					println(msg)
//					oFile.append(msg + '\n')
					hdr = false
					error = true
				}
			} else {
				msg = GlobalVariable.tsMessages[i]
				println(msg)
//				oFile.append(msg + '\n')
				hdr = false
			}
		}
		if (error) {
			msg = "******************* FAILED *********************"
			println(msg)
//			oFile.append(msg + '\n\n')
			error = false
		}
		if (hdr && !error) {
			msg = "+++++++++++++++++++ PASSED +++++++++++++++++++++"
			println(msg)
//			oFile.append(msg + '\n\n')
		} 
		
		
	msg = "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
	println(msg)
	}
}