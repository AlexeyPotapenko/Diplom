package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.BuyByCard;
import ru.netology.web.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.data.SQLHelper.getOrder;
import static ru.netology.web.data.SQLHelper.getPayment;

public class TestBuyByCard {

    private MainPage mainPage = new MainPage();
    private BuyByCard buyByCard = mainPage.debitPurchase();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeAll
    static void openWindow() {
        Selenide.open("http://localhost:8080");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    static void closeWindow() {
        Selenide.closeWindow();
    }

    @AfterEach
    void refresh() {
        Selenide.refresh();
    }

    @Test
    void shouldPayApprovedDebitCardNamedInEng() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        BuyByCard.checkApprovedMessage();
        assertEquals(getOrder().getPayment_id(), getPayment().getTransaction_id());
        assertEquals("APPROVED", getPayment().getStatus());

    }


    @Test
    void shouldPayDeclinedDebitCardNumberCard() {
        buyByCard.pageFieldInfo(getDeclinedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkDeclinedMessage();
        assertEquals(getOrder().getPayment_id(), getPayment().getTransaction_id());
        assertEquals("DECLINED", getPayment().getStatus()); // отсутствует окно ошибки

    }

    @Test
    void shouldPayDebitWithoutCardNumber() {
        buyByCard.pageFieldInfo(getEmptyCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageCard();
    }

    @Test
    void shouldPayApprovedDebitWithoutMoth() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getEmptyMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageMonth();
    }

    @Test
    void shouldPayApprovedDebitWithoutYear() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getEmptyYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageYear();
    }

    @Test
    void shouldPayApprovedDebitWithoutOwner() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getEmptyOwner(), getValidCvc());
        buyByCard.checkErrorMessageOwner();
    }

    @Test
    void shouldPayApprovedDebitWithoutCVC() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getEmptyCVC());
        buyByCard.checkErrorMessageCVC();
    }

    @Test
    void shouldPayApprovedDebitOwnerSymbol() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerLength(), getValidCvc());
        buyByCard.checkErrorMessageOwnerSymbol(); // нет сообщения об ошибке
    }

    @Test
    void shouldPayApprovedDebitCardNotFull() {
        buyByCard.pageFieldInfo(getNotFullCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageCard();
    }

    @Test
    void shouldPayApprovedDebitCardYearExpired() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getInvalidLastYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageYearExpired();
    }

    @Test
    void shouldPayApprovedDebitCardFieldsMothNull() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMothNulls(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageMonth(); //сообщение об ошибке не появляется ,запись в бд происходит с нулевым месяцем
    }


}
