import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CreateOrder;
import models.OrderRequests;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {
    private final String firstName = "Тестий";
    private final String lastName = "Тестович";
    private final String address = "ул. Пушкина, д.Колотушкина";
    private final String metroStation = "4";
    private final String phone = "+7 800 555 35 35";
    private final int rentTime = 5;
    private final String deliveryDate = "2024-05-05";
    private final String comment = "Допустим, комментарий";
    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][][] orderParameters() {
        return new String[][][]{
                {{"BLACK"}},
                {{"GREY"}},
                {{"BLACK", "GREY"}},
                {{}},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrder() {
        CreateOrder createOrder;
        if (color.length == 0) {
            createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);
        } else {
            createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        }
        Response response = OrderRequests.sendPostRequestOrder(createOrder);

        response.then().assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(HttpStatus.SC_CREATED);
    }
}
