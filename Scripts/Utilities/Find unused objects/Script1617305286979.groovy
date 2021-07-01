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

import groovy.io.FileType
import org.apache.commons.io.FileUtils

import javax.swing.*

// THIS NEEDS TO POINT TO THE TOP LEVEL OBJECT REPOSITORY
//              vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
dir = new File(GlobalVariable.projectPath + '/Object Repository')
//			    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

divider = '\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
def list = []
scripts = [divider]


// WEB ELEMENT OBJECTS
objects = [divider]

dir.eachFileRecurse (FileType.FILES) { file ->
  list << file
}

count = 0
if(list.size > 0) {
	list.each {
		if (it.path.contains('.rs')) {
			len = it.path.length()
			obj = it.path.substring(0,len-3)
			loc = obj.lastIndexOf('/')
			t1 = obj.substring(0,loc)
			loc = t1.lastIndexOf('/')
			loc = t1.lastIndexOf('/')
			obj = obj.substring(loc + 1)
			objects.add(obj)
		}
	}
}
//println(objects)

list1 = []
usedObjects = []

folders = WebUI.callTestCase(findTestCase('Utilities/Get script folders'), [:], FailureHandling.STOP_ON_FAILURE)

// ITERATE THROUGH ALL OF THE OBJECTS IN EACH SCRIPT
folders.each {
	
	dir = new File(it)
	dir.eachFileRecurse (FileType.FILES) { file ->
	  list1 << file
	}
	
	list1.each {
		if (it.path.contains('groovy')) {
			File file = new File(it.path)
			String text = FileUtils.readFileToString(file)
			objects.each {
				if (text.contains(it) && usedObjects.indexOf(it) < 0) {
					usedObjects.add(it)
				}
			}
		}
	}
}
totalObjs = (objects.size - 1)
println('There are ' + totalObjs + ' defined objects')
usedObjs = usedObjects.size
println(usedObjs + ' are used in scripts')
unused = totalObjs - usedObjs
objects.add('\nThere are ' + unused + ' unused objects. (archive folder was not searched)' + divider + '\n')
objects.sort()
objects.each {
	idx = usedObjects.indexOf(it)
//	println(it + ':' + idx)
	if (idx < 0) {
		println(it)
	}
}