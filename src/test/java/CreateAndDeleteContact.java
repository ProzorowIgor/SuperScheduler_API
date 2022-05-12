import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateAndDeleteContact {

    //public static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Imlnb3JAbWFpbC5ydSJ9.s7bUIfjB-42LtC0HlSuTE72AR1Ph85ARwvDdYYQF_NE";
    public static String token;
    public static int id;

    @BeforeMethod
    public void precondition() {
        RestAssured.baseURI = "https://super-scheduler-app.herokuapp.com/";
        RestAssured.basePath = "api";
    }

    @Test

    public void login() {
        Specifications.installSpecifications(
                Specifications.requestSpecification(baseURI, basePath)
                ,Specifications.responseSpecification(200));

        Map<String, String> user = new HashMap<>();
        user.put("email", "VASYA@mail.ru");
        user.put("password", "Awww123~");

        Response response = given().log().all()
                //    .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("login")
                .then()
                .log().all()
                //    .assertThat().statusCode(200)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        token = jsonPath.get("token");
        System.out.println(token);

    }

    @Test(dependsOnMethods = "login")
    public void createContact() {
        Specifications.installSpecifications(
                Specifications.requestSpecification(baseURI, basePath)
                ,Specifications.responseSpecification(200));

        int contactId = (int) ((System.currentTimeMillis() / 1000) % 3600);
        String res = given().log().all()
                .header("Authorization", token)
                .body("{\n" +
                        "  \"breaks\": 1,\n" +
                        "  \"currency\": \"NIS\",\n" +
                        "  \"date\": {\n" +
                        "    \"dayOfMonth\": 1,\n" +
                        "    \"dayOfWeek\": \"1\",\n" +
                        "    \"month\": 8,\n" +
                        "    \"year\": 2021\n" +
                        "  },\n" +
                        "  \"hours\": 4,\n" +
                        "  \"id\": " + contactId + ",\n" +
                        "  \"timeFrom\": \"09:00\",\n" +
                        "  \"timeTo\": \"15:00\",\n" +
                        "  \"title\": \"Title\",\n" +
                        "  \"totalSalary\": 500,\n" +
                        "  \"type\": \"\",\n" +
                        "  \"wage\": 100\n" +
                        "}")
                .when()
                .post("record")
                .then().log().all()
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(res);
        id = Integer.parseInt(jsonPath.getString("id"));
        System.out.println(">>>>>>>>>>>>>>>>>>>> " + id);


    }

    @Test(dependsOnMethods = "createContact")
    public void deleteContact() {
        Specifications.installSpecifications(
                Specifications.requestSpecification(baseURI, basePath)
                ,Specifications.responseSpecification(200));

        String response = given().log().all()
                .header("Authorization", token)
                .when()
                .delete("record/"+id)
                .then().log().all()
                .assertThat().body("status", equalTo("Record with id: " + id + " was deleted"))
                .extract().body().asString();
        System.out.println(response);
    }


}
