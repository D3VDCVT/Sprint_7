import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierLogin;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import static models.Constants.*;
import static models.CourierRequests.*;
import static org.apache.http.HttpStatus.*;


public class CreateCourierTest extends BaseTest {
    @Test
    @DisplayName("Создание нового курьера")
    public void createCourierPositive(){
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response response = sendPostRequestCourier(courier);
        responseThen(response, "ok", true, SC_CREATED);

    }

    @Test
    @DisplayName("Создание двух полностью одинаковых курьеров")
    public void createDuplicationCourier() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response responseCreateFirstCourier = sendPostRequestCourier(courier);
        responseCreateFirstCourier.then().statusCode(HttpStatus.SC_CREATED);
        Response responseCreateSecondCourier = sendPostRequestCourier(courier);
        responseThen(responseCreateSecondCourier, "message", "Этот логин уже используется. Попробуйте другой.", SC_CONFLICT);
    }

    @Test
    @DisplayName("Создание курьера без поля login")
    public void createNewCourierWithoutLogin() {
        Courier courier = new Courier(null, PASSWORD, FIRST_NAME);
        Response response = sendPostRequestCourier(courier);
        responseThen(response, "message", "Недостаточно данных для создания учетной записи", SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера без поля password")
    public void createNewCourierWithoutPassword() {
        Courier courier = new Courier(LOGIN, null, FIRST_NAME);
        Response response = sendPostRequestCourier(courier);
        responseThen(response, "message", "Недостаточно данных для создания учетной записи", SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера без поля firstName")
    public void createNewCourierWithoutFirstName() {
        Courier courier = new Courier(LOGIN, PASSWORD, null);
        Response response = sendPostRequestCourier(courier);
        responseThen(response, "ok", true, SC_CREATED);
    }

    @Test
    @DisplayName("Создание курьера с повторяющимся логином")
    public void createCourierWithDuplicateLogin() {
        Courier courierOne = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response responseCreateFirstCourier = sendPostRequestCourier(courierOne);
        responseCreateFirstCourier.then().statusCode(HttpStatus.SC_CREATED);
        Courier courierTwo = new Courier(LOGIN, "123123", "Тестий");
        Response responseCreateSecondCourier = sendPostRequestCourier(courierTwo);
        responseThen(responseCreateSecondCourier, "message", "Этот логин уже используется. Попробуйте другой.", SC_CONFLICT);
    }

    @After
    public void deleteCourier() {
        CourierLogin newLogin = new CourierLogin(LOGIN, PASSWORD);
        Response responseGetID = sendPostLoginCourier(newLogin);
        String id = getCourierID(responseGetID);
        deleteCourierByID(id);
    }

}
