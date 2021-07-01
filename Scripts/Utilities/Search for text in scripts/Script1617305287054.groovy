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
import groovy.io.FileType as FileType
import org.apache.commons.io.FileUtils as FileUtils
import javax.swing.*

def prompt = { 
    JFrame jframe = new JFrame()

    answer = JOptionPane.showInputDialog(jframe, it)

    jframe.dispose()

    answer
}

system = CustomKeywords.'unfoldingWord_Keywords.GetTestingConfig.getOperatingSystem'()

searchText = prompt('Enter the text to search for:')

divider = '\n<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>'
count = 0

def list = []

scripts = [divider]

folders = WebUI.callTestCase(findTestCase('Utilities/Get script folders'), [:], FailureHandling.STOP_ON_FAILURE)

println(folders)

if (system.contains('Windows')) {
	slash = '\\'
} else {
	slash = '/'
}

folders.each {
	if (!it.contains('groovy')) {
		myDir = it
//		println('The dir is ' + myDir)
		dir = new File(it)
		dir.eachFileRecurse(FileType.FILES, { def file ->
		        list << file
		    })
		
		if (list.size > 0) {
		    list.each{ 
		        if (it.path.contains('groovy') && it.path.contains(myDir)) {
		            File file = new File(it.path)
//					println('The file is ' + file)
		            String text = FileUtils.readFileToString(file)
		
		            if (text.contains(searchText)) {
						
						if (it.path.contains('Keywords') || it.path.contains(/Test Listeners/)) {
							loc = it.path.lastIndexOf('groovy')
						} else {
						    loc = it.path.lastIndexOf(slash)
						}
						
		                script = it.path.substring(0, loc)
		
		                loc = script.lastIndexOf(slash)
		
		                temp = script.substring(0, loc)
		
		                loc = temp.lastIndexOf(slash)
		
		                script = script.substring(loc + 1)
						
//						println(it.path)
						
						if (!scripts.contains(script)) {
		
							scripts.add(script)
		
							count++
						}
		            }
		        }
			}
		}
	}
}
scripts.add(((((('\n"' + searchText) + '" was found in ') + count) + ' scripts.') + divider) + '\n')

//println(scripts)
scripts.sort()
scripts.each({ 
        println(it)
    })

