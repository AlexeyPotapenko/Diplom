package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
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

    public static CardInfo getCardInfo(String cardNumber, String month, String year, String owner, String cvc) {
        return new CardInfo(cardNumber, month, year, owner, cvc);
    }

    static Faker fakerRus = new Faker(new Locale("ru"));
    static Faker fakerEng = new Faker(new Locale("en"));
    static Calendar calendar = new GregorianCalendar();

    public static String getMoth() {
        String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int random = new Random().nextInt(month.length);
        return month[random];
    }

    public static String getYear(int shift) {
        return String.valueOf(calendar.get(Calendar.YEAR) + shift).substring(2);
    }

    public static String getOwnerInEng() {
        return fakerEng.name().fullName();
    }

    public static String getValidCvc() {
        return String.valueOf(fakerEng.number().numberBetween(100, 999));
    }

    public static String getOwnerLength() {
        return "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    }
}