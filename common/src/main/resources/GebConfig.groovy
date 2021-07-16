import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

waiting {
	timeout = 60
}

atCheckWaiting = true
autoClearCookies = false


reportsDir = "build/geb-reports"

environments {

    remote {
        driver = {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome()
            def width = System.getProperty('browserWidth')
            def height = System.getProperty('browserHeight')
            capabilities.setCapability("screenResolution", "${width}x${height}")

            new RemoteWebDriver(new URL(System.getProperty('seleniumRemoteUrl')),
                    capabilities);
        }
    }

	chrome {
        driver = {
            DesiredCapabilities capabilities =  DesiredCapabilities.chrome()
            def width = System.getProperty('browserWidth')
            def height = System.getProperty('browserHeight')
            capabilities.setCapability("screenResolution", "${width}x${height}")

            new ChromeDriver(capabilities)
        }
	}
}
