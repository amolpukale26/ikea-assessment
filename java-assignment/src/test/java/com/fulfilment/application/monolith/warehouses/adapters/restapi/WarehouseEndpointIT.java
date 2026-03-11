package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  private static final String PATH = "/warehouse";

  @Test
  public void testSimpleListWarehouses() {

    given()
        .when()
        .get(PATH)
        .then()
        .statusCode(200)
        .body(
            containsString("MWH.001"),
            containsString("MWH.012"),
            containsString("MWH.023"));
  }

  @Test
  public void testCreateWarehouse() {

    String body =
        """
        {
          "businessUnitCode": "MWH.100",
          "location": "ZWOLLE-001",
          "capacity": 200,
          "stock": 50
        }
        """;

    given()
        .contentType(ContentType.JSON)
        .body(body)
        .when()
        .post(PATH)
        .then()
        .statusCode(200)
        .body(containsString("MWH.100"));
  }

  @Test
  public void testCreateWarehouseWithDuplicateBusinessUnit() {

    String body =
        """
        {
          "businessUnitCode": "MWH.001",
          "location": "ZWOLLE-001",
          "capacity": 100,
          "stock": 10
        }
        """;

    given()
        .contentType(ContentType.JSON)
        .body(body)
        .when()
        .post(PATH)
        .then()
        .statusCode(500)
        .body(containsString("Business Unit already exists"));
  }

  @Test
  public void testCreateWarehouseStockGreaterThanCapacity() {

    String body =
        """
        {
          "businessUnitCode": "MWH.200",
          "location": "ZWOLLE-001",
          "capacity": 50,
          "stock": 100
        }
        """;

    given()
        .contentType(ContentType.JSON)
        .body(body)
        .when()
        .post(PATH)
        .then()
        .statusCode(500)
        .body(containsString("Stock cannot exceed capacity"));
  }

  @Test
  public void testArchiveWarehouse() {

    // Verify warehouse exists
    given()
        .when()
        .get(PATH)
        .then()
        .statusCode(200)
        .body(containsString("MWH.001"));

    // Archive warehouse
    given()
        .when()
        .delete(PATH + "/MWH.001")
        .then()
        .statusCode(204);

    // Verify it is archived
    given()
        .when()
        .get(PATH)
        .then()
        .statusCode(200)
        .body(not(containsString("MWH.001")));
  }
}