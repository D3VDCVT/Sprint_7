import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.CourierLogin;
import models.ListOrders;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static models.Constants.*;
import static models.CourierRequests.*;
import static models.OrderRequests.sendGetRequestCourier;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListOrderTest extends BaseTest {
    @Test
    @DisplayName("Получение списка заказов без параметров")
    public void listOrdersWithoutParameters() {
        Response response = sendGetRequestCourier();
        response.then().assertThat().body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов с фильтрацией по станциям метро")
    public void listOrdersWithNearestStation() {
        String stationOne = "10";
        String stationTwo = "121";
        String parameters = "?nearestStation=[\"" + stationOne + "\", \"" + stationTwo + "\"]";
        ListOrders listOrders = sendGetRequestCourier(parameters);
        String resultOne = listOrders.getAvailableStations().get(0).getNumber();
        String resultTwo = listOrders.getAvailableStations().get(1).getNumber();
        assertEquals(stationOne, resultOne);
        assertEquals(stationTwo, resultTwo);
    }

    @Test
    @DisplayName("Получение списка заказов с лимитом на страницу")
    public void listOrdersWithLimit() {
        int limit = 5;
        ListOrders listOrders = given()
                .get(ORDERS + "?limit=" + limit)
                .body().as(ListOrders.class);
        int resultResponseOrders = listOrders.getOrders().size();
        int resultLimit = listOrders.getPageInfo().getLimit();
        assertEquals(limit, resultResponseOrders);
        assertEquals(limit, resultLimit);
    }

    @Test
    @DisplayName("Получение списка заказов с указанием страницы")
    public void listOrdersWithPage() {
        int page = 2;
        ListOrders listOrders = given()
                .get(ORDERS + "?page=" + page)
                .body().as(ListOrders.class);
        int resultPage = listOrders.getPageInfo().getPage();
        assertEquals(page, resultPage);
    }

    @Test
    @DisplayName("Получение списка заказов с фильтрацией идентификатору курьера")
    public void listOrdersWithCourierId() {
        Courier courier = new Courier(LOGIN, PASSWORD, "Abu");
        sendPostCreateCourier(courier);
        CourierLogin newLogin = new CourierLogin(LOGIN, PASSWORD);
        Response response = sendPostLoginCourier(newLogin);
        String id = getCourierID(response);
        ListOrders listOrders = given()
                .get(ORDERS + "?courierId=" + id)
                .body().as(ListOrders.class);
        assertTrue(listOrders.getOrders().isEmpty());
        deleteCourierByID(id);
    }
}
