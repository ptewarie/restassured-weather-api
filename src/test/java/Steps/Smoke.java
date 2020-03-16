package Steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Smoke {

    private Response response;
    private RequestSpecification request;
    private String temperatureInUnit;

    private static final String API_KEY = "b76f8e3a37e63a6cb3a28925b62843ee"; //can converted into a process env
    private static final String ENDPOINT_GET_WEATHER_FORECAST = "http://api.openweathermap.org/data/2.5/weather";

    @Given("I request a weather forecast for my holiday")
    public void iRequestAWeatherForecastFor() {
        request = given().contentType(ContentType.JSON);
    }

    @And("the forecast temperature is in {string} units")
    public void theForecastRequestIsIn(String unit) {
        if (!unit.equalsIgnoreCase("metric")) {
            throw new Error("Unit of measure does not exist, try 'metric'");
        } else {
            this.temperatureInUnit = unit;
        }
    }

    @And("the forecast request is from the city {string} with country code {string}")
    public void iNeedTheForecastToBeFromTheCity(String cityName, String countryCode) {
        response = request.when()
                .queryParam("q", cityName + "," + countryCode)
                .queryParam("cnt", 16)
                .queryParam("units", temperatureInUnit)
                .queryParam("APPID", API_KEY)
                .with()
                .get(ENDPOINT_GET_WEATHER_FORECAST);

    }

    @And("I receive the forecast correctly")
    public void iReceiveTheForecastCorrectly() {
        response.then().statusCode(200); //can be moved to gherkin layer
    }

    @And("the temperature should be above {float} degrees")
    public void theTemperatureShouldBeAboveDegrees(float temperature) {
        response.then().body("main.temp.toFloat()", greaterThan(temperature));
    }

    @And("the forecast day should be on {string}")
    public void theForecastDayIsOn(String day) {
        int dateOfRequest = response.then().extract().path("dt");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH); //using locale since im on a dutch computer with different language settings
        Timestamp ts = new Timestamp(dateOfRequest);
        Date date = new Date(ts.getTime() * 1000);
        String weekday = sdf.format(date);

        assertThat(day, equalTo(weekday));
    }

    @And("the forecast city should be {string}")
    public void theForecastCityShouldBe(String city) {
        response.then().body("name", is(city));
    }

}
