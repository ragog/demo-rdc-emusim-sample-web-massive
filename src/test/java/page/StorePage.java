package page;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class StorePage extends AbstractPage {

    @FindBy(css = "img.inventory_item_img")
    private List<MobileElement> productImages;

    @FindBy(css = "button.add-to-cart-button")
    private List<MobileElement> addToCartButtons;

    @FindBy(css = ".shopping_cart_link")
    private MobileElement cartButton;

    private By priceTagElementBy = By.cssSelector("div.inventory_details_price");
    private By anchorElementBy = By.cssSelector(".product_label");

    public StorePage(AppiumDriver driver) {
        super(driver);
    }

    @Override
    public boolean isActive() {
        return isElementShown(anchorElementBy);
    }

    public void selectFirstProduct() {
        productImages.get(0).click();
    }

    public void addFirstProductToBasket() {
        addToCartButtons.get(0).click();
    }

    public boolean isPriceShown() {
        return isElementShown(priceTagElementBy);
    }

    public void navigateToCart() {
        cartButton.click();;
    }

}
