package stepDefinitions;

import com.google.gson.Gson;
import constant.BookingEndpoints;
import entities.Booking;
import entities.BookingDates;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.Request;

import java.util.Map;

public class bookingSteps {

    private Response response;
    private Booking booking;

    @Given("I perform a GET call to the booking endpoint with id {string}")
    public void iPerformAGETCallToTheBookingEndpointWithId(String id) {
        // Asegúrate de que la URL está correctamente formada
        response = Request.getById(BookingEndpoints.GET_BOOKING, id);
    }

    @Then("I verify that the status code is {int}")
    public void iVerifyThatTheStatusCodeIs(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("The message is {string}")
    public void theMessageIs(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Then("The booking details are as follows:")
    public void theBookingDetailsAreAsFollows(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // Verificar el primer nombre
        String actualFirstName = response.jsonPath().getString("firstname");
        Assert.assertEquals(expectedData.get("firstname"), actualFirstName);

        // Verificar el apellido
        String actualLastName = response.jsonPath().getString("lastname");
        Assert.assertEquals(expectedData.get("lastname"), actualLastName);

        // Verificar el total del precio
        int actualTotalPrice = response.jsonPath().getInt("totalprice");
        Assert.assertEquals(Integer.parseInt(expectedData.get("totalprice")), actualTotalPrice);

        // Verificar si se pagó el depósito
        boolean actualDepositPaid = response.jsonPath().getBoolean("depositpaid");
        Assert.assertEquals(Boolean.parseBoolean(expectedData.get("depositpaid")), actualDepositPaid);

        // Verificar las fechas de check-in y check-out
        String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
        String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
        Assert.assertEquals(expectedData.get("checkin"), actualCheckin);
        Assert.assertEquals(expectedData.get("checkout"), actualCheckout);
    }

    @Given("I perform a POST call to the booking endpoint with the following data:")
    public void iPerformAPOSTCallToTheBookingEndpointWithTheFollowingData(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMap(String.class, String.class);
        createBookingFromData(bookingData);
        String payload = new Gson().toJson(booking);
        response = Request.post(BookingEndpoints.POST_BOOKING, payload);
    }

    @Given("I perform a POST call to the booking endpoint with the following invalid data:")
    public void iPerformAPOSTCallToTheBookingEndpointWithTheFollowingInvalidData(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMap(String.class, String.class);
        createBookingFromData(bookingData);
        String payload = new Gson().toJson(booking);
        response = Request.post(BookingEndpoints.POST_BOOKING, payload);
    }

    @Given("I perform a POST call to the booking endpoint with the following incomplete data:")
    public void iPerformAPOSTCallToTheBookingEndpointWithTheFollowingIncompleteData(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMap(String.class, String.class);
        createBookingFromData(bookingData);
        String payload = new Gson().toJson(booking);
        response = Request.post(BookingEndpoints.POST_BOOKING, payload);
    }

    private void createBookingFromData(Map<String, String> bookingData) {
        booking = new Booking();
        booking.setFirstname(bookingData.get("firstname"));
        booking.setLastname(bookingData.get("lastname"));

        // Manejo de errores para la conversión de totalprice
        try {
            if (bookingData.containsKey("totalprice")) {
                booking.setTotalprice(Integer.parseInt(bookingData.get("totalprice")));
            } else {
                booking.setTotalprice(null);
            }
        } catch (NumberFormatException e) {
            booking.setTotalprice(null); // O maneja el error según sea necesario
        }

        // Manejo de errores para la conversión de depositpaid
        try {
            if (bookingData.containsKey("depositpaid")) {
                booking.setDepositpaid(Boolean.parseBoolean(bookingData.get("depositpaid")));
            } else {
                booking.setDepositpaid(null);
            }
        } catch (Exception e) {
            booking.setDepositpaid(null); // O maneja el error según sea necesario
        }

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin(bookingData.get("checkin"));
        bookingDates.setCheckout(bookingData.get("checkout"));
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds(bookingData.get("additionalneeds"));
    }

}
