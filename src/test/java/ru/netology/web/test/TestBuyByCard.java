package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.page.BuyByCard;
import ru.netology.web.page.MainPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.data.SQLHelper.getOrder;
import static ru.netology.web.data.SQLHelper.getPayment;

public class TestBuyByCard {

    private MainPage mainPage;
    private BuyByCard buyByCard;


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void openWindow() {
        mainPage = Selenide.open("http://localhost:8080", MainPage.class);
        buyByCard = mainPage.debitPurchase();
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
    void shouldPayDeclinedDebitCard() {
        buyByCard.pageFieldInfo(getDeclinedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkDeclinedMessage();
        assertEquals(getOrder().getPayment_id(), getPayment().getTransaction_id());
        assertEquals("DECLINED", getPayment().getStatus());

    }

    @Test
    void shouldPayDebitWithoutCardNumber() {
        buyByCard.pageFieldInfo(getEmptyCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageCard();
    }

    @Test
    void shouldPayApprovedDebitWithoutMoth() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getEmptyMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageMonthForEmptyFields();
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
        buyByCard.checkErrorMessageOwnerSymbol();
    }

    @Test
    void shouldPayApprovedDebitCardNotFullNumber() {
        buyByCard.pageFieldInfo(getNotFullCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageCard();
    }

    @Test
    void shouldPayApprovedDebitCardYearExpired() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getInvalidLastYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageYearExpired();
    }

    @Test
    void shouldPayApprovedDebitCardNullsCVC() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getValidYear(), getOwnerInEng(), getNullsCvc());
        buyByCard.checkErrorMessageCVC();
    }

    @Test
    void shouldPayApprovedDebitCardNullsYear() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(),getMoth(),getNullsYear(),getOwnerInEng(),getValidCvc());
        buyByCard.checkErrorMessageYearExpired();
    }
    @Test
    void shouldPayApprovedDebitCardNullsMoth() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(),getMothNulls(),getValidYear(),getOwnerInEng(),getValidCvc());
        buyByCard.checkErrorMessageMonthExpirationDate();
    }
    @Test
    void shouldPayApprovedDebitCardMonthOverMax() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMonthOverMax(), getValidYear(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageMonthExpirationDate();
    }
    @Test
    void shouldPayApprovedDebitCardYearOverMax() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(), getMoth(), getYearOverMax(), getOwnerInEng(), getValidCvc());
        buyByCard.checkErrorMessageYearExpirationDate();
    }
    @Test
    void shouldPayApprovedDebitCardOwnerNumbers() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(),getMoth(),getValidYear(),getOwnerNumbers(),getValidCvc());
        buyByCard.checkErrorMessageInvalidOwner();
    }
    @Test
    void shouldPayApprovedDebitCardOwnerSpecialCharacter() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(),getMoth(),getValidYear(),getOwnerSpecialCharacter(),getValidCvc());
        buyByCard.checkErrorMessageInvalidOwner();
    }
    @Test
    void shouldPayApprovedDebitCardOwnerRusName() {
        buyByCard.pageFieldInfo(getApprovedCardNumber(),getMoth(),getValidYear(),getOwnerRus(),getValidCvc());
        buyByCard.checkErrorMessageInvalidOwner();//отсутствует сообщение об ошибке невенрый формат и запись в бд происходит
    }



}
