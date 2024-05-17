import io.restassured.RestAssured;
import org.junit.Before;

import static models.Constants.BASE_URL;

public class BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

}
