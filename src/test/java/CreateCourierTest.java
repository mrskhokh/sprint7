import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    public static final String PASSWORD = "12345678";
    public static final String FIRSTNAME = "somefirstname";
    public String login;

    @DisplayName("Создания курьера пустой json")
    @Test
    public void createCourierWithNullDataTest() {
        String json = "{\n" +
                "    \"login\": \"\",\n" +
                "    \"password\": \"\",\n" +
                "    \"firstName\": \"\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //Тест кейс на создание нового пользователя
    @DisplayName("Создание курьера позитивный кейс")
    @Test
    public void createDuplicateUserCaseTest() {
        login = "login" + TestUtil.generateRandomNumbers(4);
        User.create(login, PASSWORD, FIRSTNAME);

        String json = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + PASSWORD + "\",\n" +
                "    \"firstName\": \"" + FIRSTNAME + "\"\n" +
                "}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        //Подчищаем данные
        String id = User.getID(login, PASSWORD);
        User.delete(id);
    }

    //Проверяем что нельзя создать пользователя с таким же логином. Сначала создаем нового пользователя, затем пробуем создать такого же
    @DisplayName("Создания курьера с тем же логином")
    @Test
    public void createUserPositiveCase() {
        login = "login" + TestUtil.generateRandomNumbers(4);

        String json = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + PASSWORD + "\",\n" +
                "    \"firstName\": \"" + FIRSTNAME + "\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

        response.then().assertThat().statusCode(201).and()
                .body("ok", equalTo(true));
    }

    @DisplayName("Создания курьера не заполнены необходимые поля")
    @Test
    public void courierCreateNecessaryFieldsTest() {
        login = "login" + TestUtil.generateRandomNumbers(4);

        //Проврека на отсутсвие пароля
        String jsonNoPwd = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"\",\n" +
                "    \"firstName\": \"" + FIRSTNAME + "\"\n" +
                "}";

        Response responseNoPwd =
                given()
                        .header("Content-type", "application/json")
                        .body(jsonNoPwd)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

        responseNoPwd.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

        String jsonNoLogin = "{\n" +
                "    \"login\": \"\",\n" +
                "    \"password\": \"" + PASSWORD + "\",\n" +
                "    \"firstName\": \"" + FIRSTNAME + "\"\n" +
                "}";

        Response responseNoLogin =
                given()
                        .header("Content-type", "application/json")
                        .body(jsonNoLogin)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

        responseNoLogin.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
