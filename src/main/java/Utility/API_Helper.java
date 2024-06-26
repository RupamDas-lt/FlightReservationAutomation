package Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class API_Helper {
  org.apache.logging.log4j.Logger logger = LogManager.getLogger(API_Helper.class);

  public Response getResponse(String uri, Map<String, Object> headers, int expectedStatusCode, String... body) {
    logger.info("URI:- " + uri);
    if (body.length != 0) {
      if (headers == null)
        return RestAssured.given().body(body).get(uri).then().statusCode(expectedStatusCode).extract().response();
      else
        return RestAssured.given().body(body).headers(headers).get(uri).then().statusCode(expectedStatusCode).extract()
          .response();
    } else {
      if (headers == null)
        return RestAssured.given().get(uri).then().statusCode(expectedStatusCode).extract().response();
      else
        return RestAssured.given().headers(headers).get(uri).then().statusCode(expectedStatusCode).extract().response();
    }
  }

  public Response getResponse(String uri) {
    logger.info("URI:- " + uri);
    return RestAssured.given().get(uri);
  }

  public String getResponseAsString(String uri) {
    return getResponse(uri).getBody().asString();
  }

  public Response getResponseWithBasicAuth(String uri, String userName, String userPass) {
    return RestAssured.given().auth().preemptive().basic(userName, userPass).when().get(uri).then().extract()
      .response();
  }

  public <T> T convertJsonToPojo(String jsonString, TypeToken<T> type) {
    Object object = null;
    try {
      object = new Gson().fromJson(jsonString, type.getType());
    } catch (Exception e) {
      logger.error("Unable to convert json to pojo, Exception: " + e);
      e.printStackTrace();
    }
    return (T) object;
  }
}
