package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class DataHelper {
    static Faker faker = new Faker();

    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String month;
        public String year;
        public String owner;
        public String cvc;
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getNotFullCardNumber() {
        return "4444 4444 4444 444";
    }


    public static CardInfo getCardInfo(String cardNumber, String month, String year, String owner, String cvc) {
        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    public static String getMoth() {
        String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int random = new Random().nextInt(month.length);
        return month[random];
    }

    public static String getMothNulls() {
        return "00";
    }

    public static String getEmptyMoth() {
        return " ";
    }

    public static String getValidYear() {
        return LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidLastYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getEmptyYear() {
        return " ";
    }

    public static String getEmptyCVC() {
        return " ";
    }

    public static String getNullsCvc() {
        return "000";
    }

    public static String getEmptyCardNumber() {
        return " ";
    }

    public static String getEmptyOwner() {
        return " ";
    }

    public static String getOwnerInEng() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }


    public static String getValidCvc() {
        return String.valueOf(faker.number().numberBetween(100, 999));
    }

    public static String getOwnerLength() {
        return "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    }
}