package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportManager implements ITestListener {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        logger.info("Initializing ExtentReport Manager for Test Suite: {}", context.getSuite().getName());
        
        String reportFolderPath = System.getProperty("user.dir") + "/target/reports";
        File reportFolder = new File(reportFolderPath);
        
        // Ensure target/reports directory exists
        if (!reportFolder.exists()) {
            reportFolder.mkdirs();
        }

        String reportPath = reportFolderPath + "/ExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("API Test Automation Suite");
        sparkReporter.config().setReportName("REST Assured Execution Results");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Safe retrieval to prevent listener crashing during initialization
        String env = "QA";
         try {
        String baseURI = ConfigReader.getProperty("baseURI");
        if (baseURI != null) env = baseURI;
         } catch (Exception e) {
        logger.warn("Could not load baseURI from ConfigReader during report setup: {}", e.getMessage());
         }
        extent.setSystemInfo("Environment", env);
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info(">>> STARTING TEST: {}", testName);
        
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("<<< PASSED TEST: {}", result.getMethod().getMethodName());
        extentTest.get().log(Status.PASS, "Test Executed Successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("<<< FAILED TEST: {} | Exception: {}", result.getMethod().getMethodName(), result.getThrowable().getMessage());
        extentTest.get().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("<<< SKIPPED TEST: {}", result.getMethod().getMethodName());
        extentTest.get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Flushing ExtentReports to target/reports/ExtentReport.html");
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * Dual Logging Utility: Logs to Log4j2 (.log file) and ExtentReport HTML simultaneously
     */
    public static void logInfo(String message) {
        logger.info(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.INFO, message);
        }
    }

    public static void logError(String message) {
        logger.error(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.FAIL, message);
        }
    }
}