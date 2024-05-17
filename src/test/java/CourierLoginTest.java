import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierLogin;
import org.junit.After;
import org.junit.Test;

import static models.Constants.*;
import static models.CourierRequests.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CourierLoginTest extends BaseTest {

    @Test
    @DisplayName("Авторизация существующего курьера")

    public void courierAuthorizationTrue() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        sendPostCreateCourier(courier);
        CourierLogin newLogin = new CourierLogin(LOGIN, PASSWORD);
        Response response = sendPostLoginCourier(newLogin);
        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация без обязательного поля login")
    public void courierAuthorizationWithoutLogin() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        sendPostCreateCourier(courier);
        CourierLogin newLogin = new CourierLogin(null, PASSWORD);
        Response response = sendPostLoginCourier(newLogin);
        responseThen(response, "message", "Недостаточно данных для входа", SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация без обязательного поля password")
    public void courierAuthorizationWithoutPassword() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        sendPostCreateCourier(courier);
        CourierLogin newLogin = new CourierLogin(LOGIN, null);
        Response response = sendPostLoginCourier(newLogin);
        responseThen(response, "message", "Недостаточно данных для входа", SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация с неправильным login")
    public void courierAuthorizationWithInvalidLogin() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        sendPostCreateCourier(courier);
        CourierLogin newLogin = new CourierLogin(LOGIN + "isNotCorrect", PASSWORD);
        Response response = sendPostLoginCourier(newLogin);
        responseThen(response, "message", "Учетная запись не найдена", SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Авторизация с неправильным password")
    public void courierAuthorizationWithInvalidPassword() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        sendPostCreateCourier(courier);
        CourierLogin newLogin = new CourierLogin(LOGIN, "wrongpass");
        Response response = sendPostLoginCourier(newLogin);
        responseThen(response, "message", "Учетная запись не найдена", SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем")

    public void courierAuthorizationWithMissingUser() {
        CourierLogin newLogin = new CourierLogin(LOGIN, PASSWORD);
        Response response = sendPostLoginCourier(newLogin);
        responseThen(response, "message", "Учетная запись не найдена", SC_NOT_FOUND);
    }

    @After
    public void deleteCourier() {
        CourierLogin newLogin = new CourierLogin(LOGIN, PASSWORD);
        Response responseGetID = sendPostLoginCourier(newLogin);
        String id = getCourierID(responseGetID);
        deleteCourierByID(id);
    }

}
