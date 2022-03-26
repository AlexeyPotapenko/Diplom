package ru.netology.web.data;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;

public class SQLHelper {

    private static String url = System.getProperty("db.url");
    private static String user = "app";
    private static String password = "pass";

    @Data
    public static class OrderEntity {
        String credit_id;
        String payment_id;
    }

    @SneakyThrows
    public static OrderEntity getOrder() {
        System.out.println(url);
        var ordersSQL = "SELECT credit_id, payment_id FROM order_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, ordersSQL, new BeanHandler<>(OrderEntity.class));
        }
    }

    @Data
    public static class PaymentEntity {
        String status;
        String transaction_id;
    }

    @SneakyThrows
    public static PaymentEntity getPayment() {
        var paymentSQL = "SELECT status, transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, paymentSQL, new BeanHandler<>(PaymentEntity.class));
        }
    }

    @Data
    public static class CreditRequestEntity {
        String status;
        String bank_id;
    }

    @SneakyThrows
    public static CreditRequestEntity getCreditRequest() {
        var creditRequestSQL = "SELECT status, bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        try (var conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, creditRequestSQL, new BeanHandler<>(CreditRequestEntity.class));
        }
    }
}