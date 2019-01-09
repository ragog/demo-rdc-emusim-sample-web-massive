package page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CheckoutPage extends AbstractPage {

    @FindBy(css = "div.cart_item")
    private List<MobileElement> cartItems;

    public CheckoutPage(AppiumDriver driver) {
        super(driver);
    }

    public int basketCount() {
        return cartItems.size();
    }

    @Override
    boolean isActive() {
        return false;
    }

}
