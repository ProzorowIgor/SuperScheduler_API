import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {

    public static RequestSpecification requestSpecification(String url, String path){

        return new RequestSpecBuilder().setBaseUri(url).setBasePath(path).setContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification responseSpecification(int statusCode){

        return new ResponseSpecBuilder().expectStatusCode(statusCode).build();
    }

public static void installSpecifications(RequestSpecification request, ResponseSpecification response){

    RestAssured.requestSpecification = request;
    RestAssured.responseSpecification = response;

}

}
