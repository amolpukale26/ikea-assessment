package com.fulfilment.application.monolith.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ProductEndpointTest {

  private static int createProduct(String name) {
    String body = "{\"name\":\"" + name + "\",\"description\":\"desc\",\"price\":10.00,\"stock\":5}";

    return given()
        .contentType("application/json")
        .body(body)
        .when()
        .post("product")
        .then()
        .statusCode(201)
        .extract()
        .path("id");
  }

  @Test
  public void testListAndDeleteProduct() {
    int id = createProduct("PRODUCT_LIST_DELETE");

    given()
        .when()
        .get("product")
        .then()
        .statusCode(200)
        .body(containsString("PRODUCT_LIST_DELETE"));

    given().when().delete("product/" + id).then().statusCode(204);

    given()
        .when()
        .get("product/" + id)
        .then()
        .statusCode(404);
  }

  @Test
  public void testGetSingleExisting() {
    int id = createProduct("EXISTING_PRODUCT");

    given()
        .when()
        .get("product/" + id)
        .then()
        .statusCode(200)
        .body(containsString("EXISTING_PRODUCT"));
  }

  @Test
  public void testGetSingleNotFound() {
    given()
        .when()
        .get("product/999")
        .then()
        .statusCode(404)
        .body(containsString("Product with id of 999 does not exist."));
  }

  @Test
  public void testCreateProduct() {
    String name = "NEWPRODUCT" + System.nanoTime();
    int id = createProduct(name);

    // Verify it exists
    given()
        .when()
        .get("product/" + id)
        .then()
        .statusCode(200)
        .body(containsString(name));
  }

  @Test
  public void testCreateProductWithId() {
    String invalidProduct = "{\"id\":1,\"name\":\"INVALID\"}";
    given()
        .contentType("application/json")
        .body(invalidProduct)
        .when()
        .post("product")
        .then()
        .statusCode(422);
  }

  @Test
  public void testUpdateProduct() {
    int id = createProduct("UPDATE_ME");

    String updateJson = "{\"name\":\"UPDATED\",\"description\":\"updated desc\",\"price\":20.00,\"stock\":10}";
    given()
        .contentType("application/json")
        .body(updateJson)
        .when()
        .put("product/" + id)
        .then()
        .statusCode(200);

    // Verify
    given()
        .when()
        .get("product/" + id)
        .then()
        .statusCode(200)
        .body(containsString("UPDATED"));
  }

  @Test
  public void testUpdateProductNotFound() {
    String updateJson = "{\"name\":\"SOMETHING\"}";
    given()
        .contentType("application/json")
        .body(updateJson)
        .when()
        .put("product/999")
        .then()
        .statusCode(404)
        .body(containsString("Product with id of 999 does not exist."));
  }

  @Test
  public void testUpdateProductNameNull() {
    int id = createProduct("NAME_NULL");

    String updateJson = "{\"description\":\"something\"}";
    given()
        .contentType("application/json")
        .body(updateJson)
        .when()
        .put("product/" + id)
        .then()
        .statusCode(422)
        .body(containsString("Product Name was not set on request."));
  }

  @Test
  public void testDeleteProductNotFound() {
    given()
        .when()
        .delete("product/999")
        .then()
        .statusCode(404);
  }
}