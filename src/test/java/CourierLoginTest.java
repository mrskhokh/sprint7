import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CourierLoginTest {

    public static final String PASSWORD = "12345678";
    public static final String FIRSTNAME = "somefirstname";
    public String login;

    @Before
    public void beforeEach() {
        login = "login" + TestUtil.generateRandomNumbers(4);
        User.create(login, PASSWORD, FIRSTNAME);
    }

    @After
    public void afterEach() {
        //подчищаем данные
        String id = User.getID(login, PASSWORD);
        User.delete(id);
    }

    @DisplayName("Ошибка авторизации тест")
    @Test
    public void courierWrongLoginTest() {
        String json = "{\n" +
                "    \"login\": \"" + "Wrong" + login + "\",\n" +
                "    \"password\": \"" + PASSWORD + "\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        response.then().assertThat().statusCode(404).and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @DisplayName("Отсутсвие логина при авторизации тест")
    @Test
    public void courierNoLoginDataTest() {
        String json = "{\n" +
                "    \"login\": \"\",\n" +
                "    \"password\": \"" + PASSWORD + "\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @DisplayName("Пустой пароль при авторизации тест")
    @Test
    public void courierNoPasswordDataTest() {
        String json = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }
    @DisplayName("Авторизация позитивный кейс тест")
    @Test
    public void courierSuccessLoginTest() {
        String json = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + PASSWORD + "\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }
}