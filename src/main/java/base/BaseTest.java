package base;



import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification response200Spec;
    protected static ResponseSpecification response201Spec;

    @BeforeClass
    public void setup() {
        requestSpec = new RequestSpecBuilder()
            .setBaseUri(ConfigReader.getProperty("baseURI"))
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();

        response200Spec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

        response201Spec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectContentType(ContentType.JSON)
            .build();   
    }
} 
    

