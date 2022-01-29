package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.BuyInCredit;
import ru.netology.web.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.data.SQLHelper.*;
import static ru.netology.web.data.SQLHelper.getPayment;

public class TestBuyInCredit {
    private MainPage mainPage = new MainPage();
    private BuyInCredit buyInCredit = mainPage.creditPurchase();

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
    void shouldPayApprovedCreditCardNamedInEng() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        BuyInCredit.checkApprovedMessage();
        assertEquals(getOrder().getPayment_id(), getCreditRequest().getBank_id());
        assertEquals("APPROVED", getCreditRequest().getStatus());
    }

    @Test
    void shouldPayDeclinedCreditCardNumberCard() {
        buyInCredit.pageFieldInfo(getDeclinedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkDeclinedMessage();
        assertEquals(getOrder().getPayment_id(), getCreditRequest().getBank_id());
        assertEquals("DECLINED", getCreditRequest().getStatus());

    }

    @Test
    void shouldPayCreditWithoutCardNumber() {
        buyInCredit.pageFieldInfo(getEmptyCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageCard();
    }

    @Test
    void shouldPayApprovedCreditWithoutMoth() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getEmptyMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageMonth();
    }

    @Test
    void shouldPayApprovedCreditWithoutYear() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getEmptyYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageYear();
    }

    @Test
    void shouldPayApprovedCreditWithoutOwner() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getEmptyOwner(), getValidCvc());
        buyInCredit.checkErrorMessageOwner();
    }

    @Test
    void shouldPayApprovedCreditWithoutCVC() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getEmptyCVC());
        buyInCredit.checkErrorMessageCVC();
    }

    @Test
    void shouldPayApprovedCreditOwnerSymbol() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerLength(), getValidCvc());
        buyInCredit.checkErrorMessageOwnerSymbol();
    }

    @Test
    void shouldPayApprovedCreditCardNotFull() {
        buyInCredit.pageFieldInfo(getNotFullCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageCard();
    }

    @Test
    void shouldPayApprovedCreditCardYearExpired() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getInvalidLastYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageYearExpired();
    }

    @Test
    void shouldPayApprovedCreditCardNullsCVC() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMothNulls(), getValidYear(), getOwnerInEng(), getNullsCvc());
        buyInCredit.checkErrorMessageCVC();
    }
}
