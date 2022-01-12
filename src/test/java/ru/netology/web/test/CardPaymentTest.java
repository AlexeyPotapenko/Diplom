package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.CardPayment;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CardPaymentTest {
    private CardPayment cardPayment = new CardPayment();

    private final static String statusAPPROVED = "APPROVED";
    private final static String statusDECLINED = "DECLINED";
    private final static String cardAPPROVED = "4444444444444441";
    private final static String cardDECLINED = "4444444444444442";


    @BeforeEach
    void shouldOpen() {
        open("http://localhost:8080", CardPayment.class);
    }

    @AfterEach
    void clearAll() throws SQLException {
        SQLHelper.clearDBTables();
    }


    @Test
    void shouldPayApprovedDebitCardNamedInEng() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkApprovedMessage();
        assertEquals(statusAPPROVED, SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedCreditCardNamedInEng() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkApprovedMessage();
        assertEquals(statusAPPROVED, SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void shouldPayApprovedDebitCardInvalidNumberCard() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardDECLINED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkApprovedMessage();
        assertEquals(statusDECLINED, SQLHelper.getStatusFromCreditRequestEntity()); //запись в бд не происходит
    }

    @Test
    void shouldPayApprovedDebitWithoutCardNumber() throws SQLException {
        val cardNumber = DataHelper.getCardInfo("", DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCard();
        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedDebitWithoutMonth() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, "", DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageMonth();
        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedDebitWithoutYear() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), "", DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYear();
        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedDebitWithoutOwner() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), "", DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwner();
        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedDebitWithoutCVC() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), "");
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCVC();
        assertNull(SQLHelper.getStatusFromPaymentEntity()); // подчеркивает поле ввода владельца ошибкой,статус approved
    }

    @Test
    void shouldPayApprovedDebitOwnerLength50() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerLength(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        assertEquals(DataHelper.getOwnerLength().substring(50), cardPayment.checkLengthOwner()); //записывает больше 50
    }

    @Test
    void shouldPayApprovedDebitOwnerSymbol() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), "_________________________________", DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwnerSymbol();
        assertNull(SQLHelper.getStatusFromPaymentEntity()); // ошибка неверный формат не выходит
    }

    @Test
    void shouldPayApprovedDebitCardNotFull() throws SQLException {
        val cardNumber = DataHelper.getCardInfo("444444444444", DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCard();
        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedDebitCardYearExpired() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(-1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYearExpired();
        assertNull(SQLHelper.getStatusFromPaymentEntity());
    }

    @Test
    void shouldPayApprovedDebitCardFieldsYearNull() {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), "0", DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYear();
    }

    @Test
    void shouldPayApprovedDebitCardFieldsMothNull() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, "00", DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.debitPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageMonth();
        assertNull(SQLHelper.getStatusFromPaymentEntity()); //отсутствует ошибка неверного формата
    }

    @Test
    void shouldPayApprovedCreditCardInvalidNumberCard() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardDECLINED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkDeclinedMessage();
        assertEquals(statusDECLINED, SQLHelper.getStatusFromCreditRequestEntity());

    }

    @Test
    void shouldPayApprovedCreditInvalidCVC() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), "000");
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCVC();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity()); // отсутствует подчеркивание поле ввода cvc красным запись в бд не происходит
    }

    @Test
    void shouldPayApprovedCreditWithoutCardNumber() throws SQLException {
        val cardNumber = DataHelper.getCardInfo("", DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCard();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void shouldPayApprovedCreditWithoutMoth() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, "", DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageMonth();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void shouldPayApprovedCreditWithoutYear() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), "", DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYear();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void shouldPayApprovedCreditWithoutOwner() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), "", DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwner();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void shouldPayApprovedCreditOwnerNumber() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), "123", DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwner();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity()); // Ошибка : одобряет покупку
    }

    @Test
    void shouldPayApprovedCreditOwnerSymbol() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(1), "_________________", DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageOwner();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity()); // Ошибка : одобряет покупку
    }

    @Test
    void shouldPayApprovedCreditNumberCardNotFull() throws SQLException {
        val cardNumber = DataHelper.getCardInfo("4444 4444 4444", DataHelper.getMoth(), DataHelper.getYear(1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageCard();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

    @Test
    void shouldPayApprovedCreditYearExpired() throws SQLException {
        val cardNumber = DataHelper.getCardInfo(cardAPPROVED, DataHelper.getMoth(), DataHelper.getYear(-1), DataHelper.getOwnerInEng(), DataHelper.getValidCvc());
        cardPayment.creditPurchase();
        cardPayment.pageFieldInfo(cardNumber);
        cardPayment.checkErrorMessageYearExpired();
        assertNull(SQLHelper.getStatusFromCreditRequestEntity());
    }

}
