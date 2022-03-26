package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.page.BuyInCredit;
import ru.netology.web.page.MainPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.data.SQLHelper.getCreditRequest;
import static ru.netology.web.data.SQLHelper.getOrder;

public class TestBuyInCredit {

    private MainPage mainPage;
    private BuyInCredit buyInCredit;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void openWindow() {
        mainPage = Selenide.open("http://localhost:8080", MainPage.class);
        buyInCredit = mainPage.creditPurchase();
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

    @Test
    void shouldPayApprovedCreditCardNullsYear() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getNullsYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageYearExpired();
    }

    @Test
    void shouldPayApprovedDebitCardNullsMoth() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMothNulls(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageMonthExpirationDate();
    }// не всплывает ошибка Неверно указан срок действия карты
    @Test
    void shouldPayApprovedDebitCardMonthOverMax() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMonthOverMax(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageMonthExpirationDate();
    }
    @Test
    void shouldPayApprovedDebitCardYearOverMax() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(), getMoth(), getYearOverMax(), getOwnerInEng(), getValidCvc());
        buyInCredit.checkErrorMessageYearExpirationDate();
    }
    @Test
    void shouldPayApprovedDebitCardOwnerNumbers() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(),getMoth(),getValidYear(),getOwnerNumbers(),getValidCvc());
        buyInCredit.checkErrorMessageInvalidOwner();
    }//отсутствует сообщение об ошибке невенрый формат и запись в бд происходит
    @Test
    void shouldPayApprovedDebitCardOwnerSpecialCharacter() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(),getMoth(),getValidYear(),getOwnerSpecialCharacter(),getValidCvc());
        buyInCredit.checkErrorMessageInvalidOwner();//отсутствует сообщение об ошибке невенрый формат и запись в бд происходит
    }
    @Test
    void shouldPayApprovedDebitCardOwnerRusName() {
        buyInCredit.pageFieldInfo(getApprovedCardNumber(),getMoth(),getValidYear(),getOwnerRus(),getValidCvc());
        buyInCredit.checkErrorMessageInvalidOwner();//отсутствует сообщение об ошибке невенрый формат и запись в бд происходит
    }



}

