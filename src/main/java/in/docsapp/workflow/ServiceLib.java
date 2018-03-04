package in.docsapp.workflow;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import in.docsapp.generics.BasePage;
import in.docsapp.generics.ExcelLibrary;
import in.docsapp.generics.GenericMethods;
import in.docsapp.generics.Wait;
import in.docsapp.pages.AddNewCasePagePO;
import in.docsapp.pages.DoctorsDashboardPagePO;
import in.docsapp.pages.OpsDashboardPagePO;
import in.docsapp.pages.SigninPagePagePO;


/**
 * 
 * <p>
 * <b>Note:</b> The ServiceLib class has inbuilt methods which performs complete tasks<br><br>
 * For example: <br>
 * loginService: This method can be used to login to the application directly using email and password<br>
 * initService: This method can be used to login to the application using type of user and case name ex: doctor, case 1<br>
 * exit: <br>
 * actionOnQuestion: <br>
 * createCaseAsOps: <br>
 * searchAppID: <br>
 * assignDoctor: <br>
 * viewCaseAsOps: <br>
 * waitForCaseTOLoad: <br>
 * addCaseFlow: <br>
 * diagnoseCaseFlow: <br>
 * verifyCaseFlow: <br>
 * </p>
 *
 * @author Vinayak
 */
public class ServiceLib extends BasePage{

	
	public ServiceLib(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * <p>
	 *  <b>Note:</b>The loginService method helps login to the application based on the usename and password
	 *  </p>
	 * @param userName
	 * @param password
	 * 
	 *
	 * 
	 * 
	 */
	public void loginService(String userName, String password)
	{
		try {
		SigninPagePagePO signin=new SigninPagePagePO(driver);
		GenericMethods methods=new GenericMethods();
		
		methods.type(driver, signin.getEleSigninUsername(), userName);
		methods.type(driver, signin.getEleSigninPassword(), password);
		methods.customDelay(5);
		methods.click(driver, signin.getEleSigninButton());
		}
		catch (Throwable e) {
			System.out.println("Unable to login to the application using "+userName+" "+ password);
		}
		
	}
	
	/**
	 * <p>
	 * <b>Note:</b> The initService method helps login to application using type of user(ops, doctor, vendor) and case name or user name 
	 * </p>
	 * 
	 * @param typeOfUser
	 * @param caseName
	 * 
	 * 
	 */
	public void initService(String typeOfUser, String caseOrUsername)
	{
		try {
		
		SigninPagePagePO signin=new SigninPagePagePO(driver);
		GenericMethods methods=new GenericMethods();
		
		String user=typeOfUser.toLowerCase();
		System.out.println(user);
		
		String userName="";
		String password="";
		String sheetName="";
		
		if(caseOrUsername.toLowerCase().contains("case"))
		{
			System.out.println("Entered Case Loop");
			int rowNum=ExcelLibrary.findRowNum(caseOrUsername, GenericMethods.getConfigProperty("sheet5"));
			System.out.println("Row count of "+caseOrUsername+" = "+rowNum);
			
			if(user.contains("ops"))
			{
				caseOrUsername="ops";
				System.out.println("Name = "+caseOrUsername);
			}
			else if(user.contains("doctor"))
			{
				caseOrUsername=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Doctor");
				System.out.println("Name = "+caseOrUsername);
			}
			
			else if (user.contains("vendor")) {
				caseOrUsername=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Vendor");
				System.out.println("Name = "+caseOrUsername);
			}
			
			else {
				System.out.println(typeOfUser+" user does not match");
			}
		}
		
		if(user.contains("ops"))
		{
			sheetName=GenericServices.getConfigProperty("sheet2");
		}
		else if (user.contains("doctor")) {
			sheetName=GenericServices.getConfigProperty("sheet3");
		}
		else if (user.contains("vendor")) {
			sheetName=GenericServices.getConfigProperty("sheet4");
		}
		else {
			System.out.println(typeOfUser+" user does not match");
		}
		
		System.out.println("Sheet name of login "+sheetName);
		int rowCount =ExcelLibrary.getLastRowCount(sheetName);
		
		first:
		for(int i=1;i<=rowCount;i++)
		{
			if(ExcelLibrary.getCellValue(sheetName, i, 0).contains(caseOrUsername))
			{
				userName=ExcelLibrary.getCellValue(sheetName, i, 1);
				password=ExcelLibrary.getCellValue(sheetName, i, 2);
				System.out.println(userName);
				System.out.println(password);
				break first;
			}
		}
		
		methods.type(driver, signin.getEleSigninUsername(), userName);
		methods.type(driver, signin.getEleSigninPassword(), password);
		methods.customDelay(5);
		methods.click(driver, signin.getEleSigninButton());
		}
		catch (Throwable e) {
			System.out.println("Unable to login based on "+ caseOrUsername);
		}
		
	}
	
	/**
	 * <p>
	 * <b>Note:</b> The exit method helps to logout from the application
	 * 
	 * </p>
	 */
	public void exit()
	{
		BasePage base=new BasePage(driver);
		GenericMethods methods=new GenericMethods();
		methods.customDelay(3);
		methods.click(driver, base.getElelogoutButton());
	}
	
	/**
	 * 
	 * @param caseName
	 * 
	 * <p>
	 *<b>Note:</b>The actionOnQuestion method is used to diagnose a case as doctor and verify it as a ops for max life 
	 * </p>
	 * 
	 */
	public void actionOnQuestion(String caseName)
	{
		GenericMethods methods=new GenericMethods();
		
		String sheetName="";
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		
		String vendor=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Vendor");
		
		String[] ch = vendor.toLowerCase().split(" ");
		vendor="";
		for(String s:ch)
		{
			vendor=vendor+s;
		}
		System.out.println(vendor);
		sheetName=ExcelLibrary.getSheetName(vendor);
		System.out.println(sheetName);
		
		int rowCount=ExcelLibrary.getLastRowCount(sheetName);
		System.out.println(rowCount);
		for(int i=1; i<=rowCount; i++)
		{
			String question=ExcelLibrary.getCellValue(sheetName, i, 0);
			
			String option=ExcelLibrary.getCellValue(sheetName, i, ExcelLibrary.findCellNum(caseName, sheetName));
			
			String answer=ExcelLibrary.getCellValue(sheetName, i, ExcelLibrary.findCellNum(caseName, sheetName)+1);
			
			
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//*[contains(text(),'"+question+"')]")));
			if(option.equalsIgnoreCase("yes"))
			{
				Wait.waitForElementClickable(driver, driver.findElement(By.xpath("//*[contains(text(),'"+question+"')]/../..//span[contains(text(),'Yes')]")));

				methods.click(driver, driver.findElement(By.xpath("//*[contains(text(),'"+question+"')]/../..//span[contains(text(),'Yes')]")));
				
				try {
					List<WebElement> ele = driver.findElements(By.xpath("//*[contains(text(),'"+question+"')]/../..//div[last()]//input"));
					for(WebElement we:ele)
					{
						methods.clearTextField(driver, we);
						methods.type(driver, we, answer);
					}
				}
				catch (Exception e) {
					System.out.println("No input text Field for question "+question);
				}
			}
			else if (option.equalsIgnoreCase("no")) {
				methods.click(driver, driver.findElement(By.xpath("//*[contains(text(),'"+question+"')]/../..//span[contains(text(),'No')]")));
			}
			else if (option.equalsIgnoreCase("NA")) {
				try {
					List<WebElement> ele = driver.findElements(By.xpath("//*[contains(text(),'"+question+"')]/../..//div[last()]//input"));
					for(WebElement we:ele)
					{
						methods.clearTextField(driver, we);
						methods.type(driver, we, answer);
					}
				}
				catch (Exception e) {
					System.out.println("No input text Field for question "+question);
				}
			}
				
			
		}
		
	}
	
	
	/**
	 * 
	 * @param caseName
	 * 
	 * <p>
	 * <b>Note: </b> The createCaseOps method is used to create a new case as ops
	 * 
	 * </p>
	 */
	public void createCaseOps(String caseName)
	{
		GenericMethods methods=new GenericMethods();
		
		AddNewCasePagePO addCase = new AddNewCasePagePO(driver);
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		
		methods.type(driver, addCase.getEleApplicationIDTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Application_ID"));

		methods.type(driver, addCase.getEleNameTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Name"));
		
		methods.type(driver, addCase.getElePhoneTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Phone_Number"));
		
		methods.type(driver, addCase.getEleAltPhoneTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Alt_Phone_Number"));
		
		String value=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Date_of_Birth(DD-MM-YYYY)");
		String[] DOB = value.split("-");
		
		methods.type(driver, addCase.getEleDayDOBTextField(), DOB[0]);
		
		methods.type(driver, addCase.getEleMonthDOBTextField(), DOB[1]);
		
		methods.type(driver, addCase.getEleYearDOBTextField(), DOB[2]);
		
		GenericMethods.selectbyVisibletext(addCase.getEleGenderDropdown(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Gender"));
		
		methods.type(driver, addCase.getEleAddressTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Address"));
		
		GenericMethods.selectbyVisibletext(addCase.getEleVendorDropdown(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Vendor"));
		
		methods.type(driver, addCase.getElePANTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "PAN"));
		
		methods.type(driver, addCase.getElePlanTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Plan_Details"));
		
		methods.type(driver, addCase.getEleNomineeNameTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Nominee"));
		
		methods.type(driver, addCase.getEleNomineeDOBTextField(), ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Nominee_DOB"));
		
		methods.click(driver, addCase.getEleSubmitButton());
	}
	
	/**
	 * 
	 * @param caseName
	 * 
	 * <p>
	 * <b>Note: </b>The searchAppID method is searches for the case based on the Application ID
	 * 
	 * 
	 * </p>
	 * 
	 */
	public void searchAppID(String caseName)
	{
		GenericMethods methods=new GenericMethods();
		
		OpsDashboardPagePO ops=new OpsDashboardPagePO(driver);
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		String data=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Application_ID");
		methods.type(driver, ops.getEleAppIDSearchTextField(), data);
	}
	
	
	/**
	 * 
	 * @param caseName
	 * 
	 * <p>
	 * <b>Note:</b>The assignDoctor method assigns a doctor to a case based on the case name
	 * 
	 * </p>
	 */
	public void assignDoctor(String caseName)
	{
		OpsDashboardPagePO ops=new OpsDashboardPagePO(driver);
		GenericMethods methods=new GenericMethods();
		
		waitForCasesToLoad();
		searchAppID(caseName);
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		String appID=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Application_ID");
		String doctorName=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Doctor");
		methods.click(driver, ops.getEleApplicationIDAssign(appID));
		methods.click(driver, ops.getEleDoctorsName(doctorName));
		try {
		driver.switchTo().alert().accept();
		}
		catch (Throwable e) {
		}
	}
	
	/**
	 * 
	 * @param caseName
	 * <p>
	 * <b>Note:</b>The viewCaseAsOps method clicks on view button for based on Application ID for a given case number
	 * 
	 * </p>
	 * 
	 */
	public void viewCaseAsOps(String caseName) {
		BasePage base=new BasePage(driver);
		
		GenericMethods methods=new GenericMethods();
		searchAppID(caseName);
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		String appID=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Application_ID");
		methods.click(driver, base.getEleforParticularCase(appID, "View"));
		
	}
	
	/**
	 * 
	 * 
	 * <p>
	 * <b>Note:</b>The waitForCasesToLoad method wait for the cases to load
	 * 
	 * </p>
	 */
	public void waitForCasesToLoad()
	{
		BasePage basePage=new BasePage(driver);
		GenericMethods genericMethods=new GenericMethods();
		
		genericMethods.customDelay(8);
		genericMethods.waitUntilElementISVisible(driver, basePage.getEleLoader());
	}
	
	/**
	 * 
	 * @param caseName
	 * 
	 * <p>
	 * <b>Note:</b>The addCaseFlow method has complete actions to add case from ops dashboard
	 * 
	 * </p>
	 */
	public void addCaseFlow(String caseName)
	{
		OpsDashboardPagePO ops=new OpsDashboardPagePO(driver);
		GenericMethods methods=new GenericMethods();
		
		waitForCasesToLoad();
		methods.click(driver, ops.getEleAddButton());
		createCaseOps(caseName);
		
	}
	
	/**
	 * @param caseName
	 * 
	 * <p>
	 * <b>Note:</b>The diagnoseCaseFlow method has complete actions to diagnose case based on the Application ID for a Case name from doctor dashboard
	 * 
	 * </p>
	 */
	public void diagnoseCaseFlow(String caseName)
	{
		BasePage basePage =new BasePage(driver);
		DoctorsDashboardPagePO doctor=new DoctorsDashboardPagePO(driver);
		
		GenericMethods methods=new GenericMethods();
		waitForCasesToLoad();
		searchAppID(caseName);
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		String appID=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Application_ID");
		methods.click(driver, basePage.getEleforParticularCase(appID, "Diagnosis"));
		
		actionOnQuestion(caseName);
		
		methods.click(driver, doctor.getEleDiagnosisSubmitButton());
	}
	
	
	/**
	 * @param caseName
	 * 
	 * <p>
	 * <b>Note:</b>The verifyCaseFlow method has complete actions to verify the case which is diagnosed by a doctor
	 * 
	 * </p>
	 */
	public void verifyCaseFlow(String caseName)
	{
		BasePage basePage =new BasePage(driver);
		OpsDashboardPagePO ops=new OpsDashboardPagePO(driver);
		
		GenericMethods methods=new GenericMethods();
		
		waitForCasesToLoad();
		searchAppID(caseName);
		int rowNum=ExcelLibrary.findRowNum(caseName, GenericMethods.getConfigProperty("sheet5"));
		String appID=ExcelLibrary.getSingleCell(GenericMethods.getConfigProperty("sheet5"), rowNum, "Application_ID");
		
		methods.click(driver, basePage.getEleforParticularCase(appID, "View"));
		actionOnQuestion(caseName);
		methods.click(driver, ops.getEleFormVerifyButton());
	}
}
