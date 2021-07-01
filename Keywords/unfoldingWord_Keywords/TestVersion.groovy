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


public class TestVersion {
	@Keyword
	// 02/16/21	Modified to allow for the build number being added to the app version
	//This method tests the current version number to see if it's greater than the one passed in
	//Used to bypass test for features not yet in the build
	def isVersionGreater(testVersion) {
		//		println(testVersion)
		//convert the version number to alpha to make sorting easier (e.g. '1.0.4' = 'A D'; '1.1.6' = 'AAF')
		def vNum = ''
		def thisVersion = GlobalVariable.version
		//		println('version:' + thisVersion)
		def versions = [thisVersion, testVersion]
		def alphaVersions = []
		for (def x : (0..1)) {
			def version = versions[x]
			if (x == 0) {
				def v = version.indexOf('v')
				//				println('v:'+v)
				def rc = version.indexOf('-rc')
				//				println('rc:'+ rc)
				def sp = version.indexOf(' ',v)
				//				println('sp:'+sp)
				if (rc > 0) {
					version = version[0..rc-1]
				} else if (sp > 0) {
					version = version[0..sp-1]
				}
				def l = version.length() -1
				vNum = version[v+1..l]
			} else {
				vNum = testVersion
			}
			String aNum = ''
			int a
			String sa = ''
			for (def c : (0..vNum.length()-1)) {
				if (vNum[c] != '.') {
					if (vNum[c] == '0') {
						aNum += ' '
					} else {
						a = vNum[c].toInteger()
						a += 64
						sa = (char)a
						aNum += sa
					}
				}
			}
			alphaVersions += aNum
		}
		//		println('a=[' + alphaVersions[0] + '] b=[' + alphaVersions[1] + ']')
		if (alphaVersions[0] > alphaVersions[1]) {
			return true
		} else {
			return false
		}
	}
}