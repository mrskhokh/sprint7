import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String firstName;
    private final String lastname;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String rentTime;

    private final String deliveryDate;
    private final String comment;
    private final List<Color> colors;

    public OrderCreateTest(String firstName, String lastname, String address,
                           String metroStation, String phone, String rentTime,
                           String deliveryDate, String comment, List<Color> colors) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Ivanov", "Ivan", "Some test address",
                        "Unname station", "89276050505", "5", "2024-06-09",
                        "someComment", List.of(Color.BLACK)},
                {"Ivanov", "Ivan", "Some test address",
                        "Unname station", "89276050505", "5", "2024-06-09",
                        "someComment", List.of(Color.GRAY)},
                {"Ivanov", "Ivan", "Some test address",
                        "Unname station", "89276050505", "5", "2024-06-09",
                        "someComment", List.of(Color.BLACK, Color.GRAY)},
                {"Ivanov", "Ivan", "Some test address",
                        "Unname station", "89276050505", "5", "2024-06-09",
                        "someComment", Collections.emptyList()},
        };
    }

    @DisplayName("Создание заказа позитивный кейс с цветом черным, серым, с обоими, без цвета")
    @Test
    public void createOrderPositiveCaseTest() {
        Order order = new Order(firstName, lastname, address,
                metroStation, phone, rentTime, deliveryDate, comment, colors);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/orders");
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());

        //Отменяем созданный заказ
        JsonPath jsonPath = new JsonPath(response.asString());
        String track = jsonPath.get("track").toString();
        order.cancel(track);
    }

}
