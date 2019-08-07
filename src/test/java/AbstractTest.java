import com.saucelabs.saucerest.SauceREST;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import util.ResultReporter;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by grago on 27.09.17.
 */
public class AbstractTest {

    private ResultReporter reporter;

    private ThreadLocal<AppiumDriver> webDriver = new ThreadLocal<>();
    private ThreadLocal<String> sessionId = new ThreadLocal<>();

    private String sauceURI = "@ondemand.eu-central-1.saucelabs.com";
    private String buildTag = System.getenv("BUILD_TAG");
    private String username = System.getenv("SAUCE_USERNAME");
    private String accesskey = System.getenv("SAUCE_ACCESS_KEY");
    private String rdcApiKey = System.getenv("RDC_API_KEY");
    private String extendedDebugging = System.getenv("EXT_DEBUGGING");
    private SauceREST sauceRESTClient = new SauceREST(username, accesskey);

    @DataProvider(name = "hardCodedBrowsers", parallel = true)
    public static Object[][] sauceBrowserDataProvider(Method testMethod) {
        return new Object[][]{

                // Real devices
                new Object[]{"real", "Android", "7", "", ""}

        };
    }

    public AppiumDriver getWebDriver() {
        return webDriver.get();
    }

    public String getSessionId() {
        return sessionId.get();
    }

    public void setup(String deviceType, String platformName, String platformVersion, String deviceName,
                      String browserName, Method method) throws MalformedURLException {

        // Silence Selenium logger
        Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);

        String testName = method.getName();

        String testId = UUID.randomUUID().toString();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        String gridEndpoint = "";

        if (deviceType.equals("real")) {

            capabilities.setCapability("testobject_api_key", rdcApiKey);
            capabilities.setCapability("phoneOnly", "true");
            if (!deviceName.isEmpty()) {
                capabilities.setCapability("deviceName", deviceName);
            }
            gridEndpoint = "https://eu1.appium.testobject.com/wd/hub";

        } else if (deviceType.equals("virtual")) {

            if (buildTag != null) {
                capabilities.setCapability("build", buildTag);
            }

            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("browserName", browserName);
            capabilities.setCapability("extendedDebugging", extendedDebugging);

            gridEndpoint = "https://" + username + ":" + accesskey + sauceURI + "/wd/hub";

        }

        capabilities.setCapability("cacheId", System.getenv("RDC_CACHE_ID"));
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("phoneOnly", "true");
        capabilities.setCapability("name", testName);

        capabilities.setCapability("uuid", testId);

        webDriver.set(new AndroidDriver(new URL(gridEndpoint), capabilities));

        reporter = new ResultReporter();

        // set current sessionId
        String id = getWebDriver().getSessionId().toString();
        sessionId.set(id);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (getWebDriver() != null) {
            String sessionId = getWebDriver().getSessionId().toString();
            boolean status = result.isSuccess();
            boolean isTOTest = getWebDriver().getCapabilities().getCapability("testobject_api_key") != null;

            if (isTOTest) {
                // TestObject REST API
                reporter = new ResultReporter();
                reporter.saveTestStatus(sessionId, status);

            } else { // test was run on Sauce
                // Sauce REST API (updateJob)
                Map<String, Object> updates = new HashMap<String, Object>();
                updates.put("passed", status);
                sauceRESTClient.updateJobInfo(sessionId, updates);
            }

            getWebDriver().quit();
        }

    }

}
