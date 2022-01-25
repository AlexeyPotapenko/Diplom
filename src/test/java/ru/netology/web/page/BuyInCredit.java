package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyInCredit {

    private SelenideElement heading = $$(".heading").find(exactText("Оплата по карте"));
    private static SelenideElement inputCardNumber = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private static SelenideElement inputMonth = $("input[type=\"text\"][placeholder=\"08\"]");
    private static SelenideElement inputYear = $("input[type=\"text\"][placeholder=\"22\"]");
    private static SelenideElement inputOwner = $$(".input").find(exactText("Владелец")).$(".input__control");
    private static SelenideElement inputCVC = $("input[type=\"text\"][placeholder=\"999\"]");
    private static SelenideElement buyContinue = $$(".button").find(exactText("Продолжить"));
    private static SelenideElement checkApprovedMessage = $$(".notification__title").find(exactText("Успешно"));
    private SelenideElement checkDeclinedMessage = $$(".notification__title").find(exactText("Ошибка"));
    private SelenideElement checkErrorMessageCard = $$(".input__top").find(exactText("Номер карты")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageMonth = $$(".input__top").find(exactText("Месяц")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageYear = $$(".input__top").find(exactText("Год")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageYearExpired = $$(".input__top").find(exactText("Год")).parent().
            $$(".input__sub").find(exactText("Истёк срок действия карты"));
    private SelenideElement checkErrorMessageOwner = $$(".input__top").find(exactText("Владелец")).parent().
            $$(".input__sub").find(exactText("Поле обязательно для заполнения"));
    private SelenideElement checkErrorMessageOwnerSimbol = $$(".input__top").find(exactText("Владелец")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement checkErrorMessageCVC = $$(".input__top").find(exactText("CVC/CVV")).parent().
            $$(".input__sub").find(exactText("Неверный формат"));
    private SelenideElement buyTour = $$(".button").find(exactText("Купить"));
    private SelenideElement buyTourInCredit = $$(".button").find(exactText("Купить в кредит"));

    public void pageFieldInfo(String cardNumber, String month, String year, String owner, String cvc) {
        inputCardNumber.setValue(cardNumber);
        inputMonth.setValue(month);
        inputYear.setValue(year);
        inputOwner.setValue(owner);
        inputCVC.setValue(cvc);
        buyContinue.click();
    }

    public static void checkApprovedMessage() {
        checkApprovedMessage.should(Condition.visible, Duration.ofSeconds(15));
    }

    public void checkDeclinedMessage() {
        checkDeclinedMessage.should(Condition.visible, Duration.ofSeconds(15));
    }

    public void checkErrorMessageCard() {
        checkErrorMessageCard.shouldBe(visible);
    }

    public void checkErrorMessageMonth() {
        checkErrorMessageMonth.shouldBe(visible);
    }

    public void checkErrorMessageYear() {
        checkErrorMessageYear.shouldBe(visible);
    }

    public void checkErrorMessageYearExpired() {
        checkErrorMessageYearExpired.shouldBe(visible);
    }

    public void checkErrorMessageOwner() {
        checkErrorMessageOwner.shouldBe(visible);
    }

    public void checkErrorMessageOwnerSymbol() {
        checkErrorMessageOwnerSimbol.shouldBe(visible);
    }

    public void checkErrorMessageCVC() {
        checkErrorMessageCVC.shouldBe(visible);
    }

    public String checkLengthOwner() {
        return String.valueOf(inputOwner.getValue());
    }

}
