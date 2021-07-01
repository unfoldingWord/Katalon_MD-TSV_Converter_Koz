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
import org.openqa.selenium.By as By
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.apache.commons.lang3.StringUtils as StringUtils
import org.openqa.selenium.interactions.Actions as Actions
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper

import javax.swing.*
import java.awt.Frame

dirName = (('/Users/' + GlobalVariable.pcUser) + '/Downloads')

filesPath = (('/Users/' + GlobalVariable.pcUser) + '/Katalon Studio/Files/Reference/')

allBooks = (filesPath + 'Bible_Books.csv')

ntBooks = (filesPath + 'NT_Books.csv')

otBooks = (filesPath + 'OT_Books.csv')

someBooks = (filesPath + 'Some_Books.csv')

oneBook = (filesPath + 'One_Book.csv')

epistleBooks = (filesPath + 'Epistle_Books.csv')

myBooks = someBooks

rowLimit = 0

tab = '\t'

testFiles = []

new File(myBooks).splitEachLine(',', { def fields ->
        bookNum = (fields[0])

        if (bookNum.length() < 2) {
            bookNum = ('0' + bookNum)
        }
        
        bookAbrv = (fields[1])

        testFiles.add(bookAbrv.toLowerCase())
    })

errorsOnly = true

dcsBaseURL = 'https://git.door43.org/'

sourcePath = '/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[1]/table/tbody'

targetPath = '/html/body/div[1]/div/div/div[2]/div[2]/div/div/div[2]/table/tbody'

bookFilesPath = '/html/body/div[1]/div[2]/div[2]/table/tbody'

fileTQsPath = '/html/body/div[1]/div[2]/div[2]/div[5]/div/div'

//======================================================================================================
repoOwner = 'manny_colon'

langCode = 'es-419'

mdType = 'tn'

repo = ((((repoOwner + '/') + langCode) + '_') + mdType)

dcsRepo = (dcsBaseURL + repo)

Date now = new Date()

String fName = ((('md2tsv_' + mdType) + '-Export-') + now.format('MMddyyhhmmss')) + '.txt'

File oFile = new File((('/Users/' + GlobalVariable.pcUser) + '/Katalon Studio/Files/Logs/') + fName)

msg = ('Testing ' + repo)
println(msg)

oFile.append(msg + '\n')

//======================================================================================================
type = 'rows'

bookErrors = false

WebUI.callTestCase(findTestCase('Login'), [:])

prtDetails = false

//for (book in testFiles) {
testFiles.each{ def book ->
        msg = ('\n Processing ' + book)

        println(msg)

        oFile.append(msg + '\n')

        WebUI.callTestCase(findTestCase('Select Project'), [('owner') : repoOwner, ('repo') : repo, ('book') : book])

        WebUI.scrollToPosition(0, 0)

        WebUI.delay(1)
		
        sourceText = WebUI.getText(findTestObject('table_Source'))

        sourceLines = []

        sourceText.eachLine({ def text ->
                sourceLines.add(text)
            })

        rowCount = sourceLines.size()

        println(('The source has ' + rowCount) + ' rows')
		
		// Move rows
		
		// If there is only 1 file to process, give the operator the option to drag target rows around		
		if (testFiles.size() == 1) {
			JOptionPane.showMessageDialog(new Frame('Message'), 'Drag the rows as desired, then click OK')
		}

        WebUI.click(findTestObject('input_ID'))

        WebUI.click(findTestObject('input_SupportReference'))

        WebUI.click(findTestObject('input_OrigQuote'))

        WebUI.click(findTestObject('input_Occurrence'))

        WebUI.delay(1)

        // Get the source and target rows
        //	Source columns are: Book, Chapter, Verse, ID, SupportReference, OrigQuote, Occurrence, GLQuote, OccurrenceNote
        //  Target columns are: Chapter, Verse, GLQuote, OccurrenceNote
		println('Retrieving source values')
        sRowText = getTableValues(sourcePath)

        rowCount = sRowText.size()

        if (prtDetails) {
            println('sRows = ' + rowCount)

            sRowText.each({ 
                    println(it)
                })

            println(sRowText)
        }
        
		println('Retrieving target values')
        tRowText = getTableValues(targetPath)

        if (prtDetails) {
            println('tRows = ' + tRowText.size())

            tRowText.each({ 
                    println(it)
                })
        }
        
        row = 0
		nullRows = []
        for (text in sRowText) {
 ////       sRowText.each({ def text ->
            String[] sFields = text.split(tab)

            if (prtDetails) {
                println(('the source has ' + sFields.size()) + ' fields')
            }
            
            bk = (sFields[0])

            ch = (sFields[1])

            vs = (sFields[2])

            if (sFields.size() > 3) {
                id = (sFields[3])
            } else {
                id = ''
            }
            
            if (sFields.size() > 4) {
                sr = (sFields[4])
            } else {
                sr = ''
            }
            
            if (sFields.size() > 5) {
                oq = (sFields[5])
            } else {
                oq = ''
            }
            
            if (sFields.size() > 6) {
                oc = (sFields[6])
            } else {
                oc = ''
            }
            
            if (sFields.size() > 7) {
                glq = (sFields[7])
            } else {
                glq = ''
            }
            
            if (sFields.size() > 8) {
                on = (sFields[8])
            } else {
                on = ''
            }
            
            if (prtDetails) {
                println('tRow = ' + (tRowText[row]))
            }
            
            String[] tFields = (tRowText[row]).split(tab)

            if (prtDetails) {
                println(('the target has ' + tFields.size()) + ' fields')

                tFields.each({ 
                        println(it)
                    })
            }
            
            if (tFields.size() > 2) {
				if (tFields[2] != '') {
                	glq = (tFields[2])
				}
            }
            
            if (tFields.size() > 3) {
				if (tFields[3] != '') {
                	on = (tFields[3])
				}	
            }
			
			if (sr + oq + oc + glq + on != '') {
				tRowText[row] = bk + tab + ch + tab + vs + tab + id + tab + sr + tab + oq + tab + oc + tab + glq + tab + on
			} else { 
				tRowText[row] = null
			}
			
            if (prtDetails) {
                println(tRowText[row])
            }
            
            row++
        }

		// Remove all empty app rows
		tRowText.removeAll([null])
		
        // Get a list of existing download TSV files
        oldFiles = getTSVFiles(book.toUpperCase())

        if (prtDetails) {
            println(oldFiles)
        }
        
        initSize = vFiles.size

        // Get the just exported file
        newFiles = runExport(initSize, book.toUpperCase())

        if (prtDetails) {
            println(newFiles)
        }
        
        myFile = (newFiles.minus(oldFiles)[0])

        if (prtDetails) {
            println('myFile:' + myFile)

            println('dirName2:' + dirName)
        }
        
        myFile = ((dirName + '/') + myFile)

        // Count the tabs in each row
        tabErrors = 0

        tabs = []

        if (prtDetails) {
            println(myFile)
        }
        
        new File(myFile).eachLine({ def line ->
                count = StringUtils.countMatches(line, '\t')

                tabs.add(count)

                if (count != 8) {
                    println(((('#### ERROR: Line has ' + count) + ' tabs:') + line) + '\n')

                    tabErrors++
                }
            })

        //Parse the exported file
        exportLines = []

        new File(myFile).eachLine({ def line ->
                exportLines.add(line)
            })

		if (prtDetails) {
			println('\n\n############################')
			println('tRowText has ' + tRowText.size() + ' rows.')
			tRowText.each { row ->
				println(row)
			}
			println('############################\n\n')
	
			println('\n\n############################')
			println('exportLines has ' + exportLines.size() + ' rows.')
			exportLines.each { row ->
				println(row)
			}
			println('############################\n\n')
		}
		
		rowErrors = 0

        r = 0

        ids = []

        idErrors = 0

		for (row in tRowText) {
//        tRowText.each({ def row ->
                exportRow = (exportLines[(r + 1)])

                String[] expRow = exportRow.split(tab)

                expId = (expRow[3])

                if (expId in ids) {
		 			msg = 'ID ' + expId + ' is duplicated in the TSV file.'
					println('######## ERROR: ' + msg + '\n')
					CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'('Test failed because ' + msg)
                    idErrors++
                } else {
                    ids.add(expId)
                }
 
				if (prtDetails) {
	                println('The row in the app is      ' + row)
	                println('The row in the TSV file is ' + exportRow) + '\n'
				}
				
                if (row != exportRow) {
                    String[] appRow = row.split(tab)

                    if (((appRow[3]) != '') || ((expRow[3]) == '')) {
						msg = 'These rows do not match:'
						CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
						println('################ ' + msg)
						msg = 'The row in the app is      '  + row
						println(msg)
						CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
						msg = 'The row in the TSV file is ' + exportRow + '\n'
						println(msg)
						CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'(msg)
	
	                    rowErrors++
							
					}
                }
                
                r++
            }

        println(tRowText.size() + ' rows were processed.')

        msg = rowErrors + ' mismatched rows were found.'
		println(msg)
		if (rowErrors > 0) {
			CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'('Test failed because ' + msg)
		}
		
        msg = idErrors + ' duplicate IDs were found.'
		println(msg)
		if (idErrors > 0) {
			CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'('Test failed because ' + msg)
		}
		
        msg = tabErrors + ' tab errors were found.'
		println(msg)
		if (tabErrors > 0) {
			CustomKeywords.'unfoldingWord_Keywords.SendMessage.SendFailMessage'('Test failed because ' + msg)
		}
    } 

GlobalVariable.scriptRunning = false	
	
WebUI.closeBrowser()

def getTableValues(def xPath) {
    WebDriver driver = DriverFactory.getWebDriver()

    WebElement Table = driver.findElement(By.xpath(xPath))

    List<WebElement> rows = Table.findElements(By.tagName('tr'))

    int rows_count = rows.size()

    def text = []

    if (rowLimit > 0) {
        rows.subList(rowLimit, rows_count).clear()

        rows_count = rows.size()
    }
    
	count = rows.size()
	row = 0
	println( '-'.multiply(count))
    rows.each{
		
        rowText = ''
		
        List<WebElement> columns = rows.get(row).findElements(By.tagName('td'))

        for (def column : (0..columns.size() - 1)) {
            celltext = columns.get(column).getText()

            if (prtDetails) {
                println(((((('row ' + row) + ', column ') + column) + ' is [') + celltext) + ']')
            }
            
            rowText = ((rowText + celltext) + '\t')
        }
        
        text.add(rowText)
		
		print('-')

        row++
    }

    return text
}

def getTSVFiles(def testFile) {
    if (prtDetails) {
        println('dirName1:' + dirName)
    }
    
    List<WebElement> files = new File(dirName).list()

    vFiles = []

    files.each({ def file ->
            if (file.contains((mdType + '_') + testFile) && (file.substring(file.length() - 3) == 'tsv')) {
                vFiles.add(file)
            }
        })

    return vFiles
}

def runExport(def initSize, def testFile) {
    WebUI.click(findTestObject('Object Repository/button_3-Dot'))

    WebUI.delay(1)

    WebUI.click(findTestObject('Object Repository/option_Export_To_TSV'))

    WebUI.delay(1)

    vSize = initSize

    myFile = ''

    newContent = ''

    while (vSize <= initSize) {
        vFiles = getTSVFiles(testFile)

        vSize = vFiles.size()
    }
    
    return vFiles
}

def dragRows(moves, rowCount) {
    rows = (rowCount + 1)
    for (def i : (0..moves)) {
        from = Math.abs(new Random().nextInt()%rowCount) + 1

        to = Math.abs(new Random().nextInt()%rowCount) + 1

        println((('Moving row ' + from) + ' to row ') + to)

        CustomKeywords.'html5.dnd.DragAndDropHelper.dragAndDrop'(findTestObject('icon_Drag_Parmed', [('row') : from]), findTestObject(
               'icon_Drag_Parmed', [('row') : to]))
    }
}

def dragIt(def from, def to) {
	def ofset = WebUI.getElementWidth(findTestObject(origQuote[from]))

	Integer x = WebUI.getElementWidth(findTestObject(origQuote[from])) / -(2)

	WebDriver driver = DriverFactory.getWebDriver()

	Actions builder = new Actions(driver)

	if (from != to) {
		ofset = ((WebUI.getElementLeftPosition(findTestObject(origQuote[to])) + WebUI.getElementWidth(findTestObject(origQuote[
				to]))) - WebUI.getElementLeftPosition(findTestObject(origQuote[from])))
	}
	
	WebUI.mouseOverOffset(findTestObject(origQuote[from]), x, 0)

	builder.clickAndHold()

	builder.moveByOffset(ofset, 0).release().build().perform()	
}

def dragRow() {
	
	WebDriver driver = DriverFactory.getWebDriver()
	Actions builder=new Actions(driver)
	WebElement sourceElement = WebUiCommonHelper.findWebElement(findTestObject('icon_Drag_Parmed', [('row') : 3]), 2)
	WebElement destinationElement = WebUiCommonHelper.findWebElement(findTestObject('icon_Drag_Parmed', [('row') : 5]), 2)
	builder.clickAndHold(sourceElement)
	builder.moveToElement(destinationElement)
	builder.release()
	builder.perform()
	
}