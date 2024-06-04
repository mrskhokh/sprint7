import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isA;


public class OrdersListTest {

    //Проверяем что возвращаемый json фсвляется массивом
    @DisplayName("Список ордеров возвразается по get запросу Тест")
    @Test
            public void GetOrdersListTest() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("https://qa-scooter.praktikum-services.ru/api/v1/orders");
        response.then().assertThat().statusCode(200)
                .and().body("orders", notNullValue())
                .and().body("orders", isA(ArrayList.class))
                .and().body("orders.id",  everyItem(notNullValue()));

    }


}
