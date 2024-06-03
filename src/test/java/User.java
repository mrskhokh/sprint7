import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class User {

    public static void create(String login, String password, String firstname) {

        String json = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"firstName\": \"" + firstname + "\"\n" +
                "}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

    }

    public static String getID(String login, String password) {
        String json = "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        response.then().assertThat().statusCode(200);
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.get("id").toString();
    }

    public static void delete(String id) {
        String json = "{\n" +
                "    \"id\": \"" + id + "\"\n" +
                "}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/" + id);
    }
}


