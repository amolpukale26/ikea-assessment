// package com.fulfilment.application.monolith.stores;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static org.hamcrest.CoreMatchers.containsString;
// import static io.restassured.RestAssured.given;

// import io.quarkus.test.junit.QuarkusTest;
// import io.quarkus.test.junit.mockito.InjectMock;
// import io.restassured.http.ContentType;
// import jakarta.ws.rs.WebApplicationException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import java.util.List;

// @QuarkusTest
// public class StoreEndpointTest {

//     @InjectMock
//     StoreResource storeResource;

//     private Store sampleStore;

//     @BeforeEach
//     public void setup() {
//         sampleStore = new Store("TEST_STORE");
//         sampleStore.id = 1L;
//         sampleStore.quantityProductsInStock = 50;
//     }

//     @Test
//     public void testListStores() {
//         when(storeResource.get()).thenReturn(List.of(sampleStore));

//         given()
//             .when()
//             .get("/store")
//             .then()
//             .statusCode(200)
//             .body(containsString("TEST_STORE"));
//     }

//     @Test
//     public void testCreateStore() {
//         when(storeResource.create(any(Store.class)))
//             .thenReturn(jakarta.ws.rs.core.Response.ok(sampleStore).status(201).build());

//         String body = """
//             {
//               "name": "TEST_STORE",
//               "quantityProductsInStock": 50
//             }
//             """;

//         given()
//             .contentType(ContentType.JSON)
//             .body(body)
//             .when()
//             .post("/store")
//             .then()
//             .statusCode(201)
//             .body(containsString("TEST_STORE"));
//     }

//     @Test
//     public void testCreateStoreWithIdShouldFail() {
//         when(storeResource.create(any(Store.class)))
//             .thenThrow(new WebApplicationException("Id was invalidly set on request.", 422));

//         String body = """
//             {
//               "id": 10,
//               "name": "INVALID_STORE",
//               "quantityProductsInStock": 20
//             }
//             """;

//         given()
//             .contentType(ContentType.JSON)
//             .body(body)
//             .when()
//             .post("/store")
//             .then()
//             .statusCode(422);
//     }

//     @Test
//     public void testGetStoreById() {
//         when(storeResource.getSingle(1L)).thenReturn(sampleStore);

//         given()
//             .when()
//             .get("/store/1")
//             .then()
//             .statusCode(200)
//             .body(containsString("TEST_STORE"));
//     }

//     @Test
//     public void testUpdateStore() {
//         Store updatedStore = new Store("UPDATED_STORE");
//         updatedStore.id = 1L;
//         updatedStore.quantityProductsInStock = 100;

//         when(storeResource.update(eq(1L), any(Store.class))).thenReturn(updatedStore);

//         String body = """
//             {
//               "name": "UPDATED_STORE",
//               "quantityProductsInStock": 100
//             }
//             """;

//         given()
//             .contentType(ContentType.JSON)
//             .body(body)
//             .when()
//             .put("/store/1")
//             .then()
//             .statusCode(200)
//             .body(containsString("UPDATED_STORE"));
//     }

//     @Test
//     public void testPatchStore() {
//         Store patchedStore = new Store("TEST_STORE");
//         patchedStore.id = 1L;
//         patchedStore.quantityProductsInStock = 200;

//         when(storeResource.patch(eq(1L), any(Store.class))).thenReturn(patchedStore);

//         String body = """
//             {
//               "quantityProductsInStock": 200
//             }
//             """;

//         given()
//             .contentType(ContentType.JSON)
//             .body(body)
//             .when()
//             .patch("/store/1")
//             .then()
//             .statusCode(200);
//     }

//     @Test
//     public void testDeleteStore() {
//         doNothing().when(storeResource).delete(1L);

//         given()
//             .when()
//             .delete("/store/1")
//             .then()
//             .statusCode(204);
//     }

//     @Test
//     public void testGetNonExistingStore() {
//         when(storeResource.getSingle(9999L))
//             .thenThrow(new WebApplicationException("Store with id of 9999 does not exist.", 404));

//         given()
//             .when()
//             .get("/store/9999")
//             .then()
//             .statusCode(404);
//     }
// }
// package com.fulfilment.application.monolith.stores;

// import io.quarkus.test.junit.QuarkusTest;
// import io.restassured.http.ContentType;
// import org.junit.jupiter.api.Test;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.CoreMatchers.containsString;

// @QuarkusTest
// public class StoreEndpointTest {

//     @Test
//     public void testCreateAndDeleteStore() {
//         // 1️⃣ Create a store
//         String storeJson = """
//             {
//                 "name": "TEST_STORE",
//                 "quantityProductsInStock": 50
//             }
//             """;

//         // POST /store
//         Integer storeId =
//         given()
//             .contentType(ContentType.JSON)
//             .body(storeJson)
//             .when()
//             .post("/store")
//             .then()
//             .statusCode(201)
//             .body(containsString("TEST_STORE"))
//             .extract()
//             .path("id"); // extract the generated id

//         // 2️⃣ Delete the store
//         given()
//             .when()
//             .delete("/store/" + storeId)
//             .then()
//             .statusCode(204);

//         // 3️⃣ Verify deletion (should return 404)
//         given()
//             .when()
//             .get("/store/" + storeId)
//             .then()
//             .statusCode(404);
//     }
// }
// package com.fulfilment.application.monolith.stores;

// import io.quarkus.test.junit.QuarkusTest;
// import org.junit.jupiter.api.Test;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.CoreMatchers.containsString;

// import jakarta.inject.Inject;
// import jakarta.ws.rs.core.Response;

// @QuarkusTest
// public class StoreEndpointTest {

//     @Inject
//     StoreResource storeResource;

//     @Inject
//     LegacyStoreManagerGateway legacyGateway;

//     // ------------------------
//     // Test for StoreResource
//     // ------------------------
//     @Test
//     public void testCreateAndDeleteStore() {
//         // 1️⃣ Create a store
//         String storeJson = """
//             {
//                 "name": "TEST_STORE",
//                 "quantityProductsInStock": 50
//             }
//             """;

//         // POST /store
//         Integer storeId =
//         given()
//             .contentType("application/json")
//             .body(storeJson)
//             .when()
//             .post("/store")
//             .then()
//             .statusCode(201)
//             .body(containsString("TEST_STORE"))
//             .extract()
//             .path("id"); // extract the generated id

//         // 2️⃣ Delete the store
//         given()
//             .when()
//             .delete("/store/" + storeId)
//             .then()
//             .statusCode(204);

//         // 3️⃣ Verify deletion (should return 404)
//         given()
//             .when()
//             .get("/store/" + storeId)
//             .then()
//             .statusCode(404);
//     }

//     // ------------------------
//     // Test for LegacyStoreManagerGateway
//     // ------------------------
//     @Test
//     public void testLegacyGatewayCreateStore() {
//         Store tempStore = new Store("LEGACY_TEST_STORE");
//         tempStore.quantityProductsInStock = 100;

//         // This method writes to a temp file and deletes it; we just ensure it doesn't throw
//         legacyGateway.createStoreOnLegacySystem(tempStore);
//     }

//     @Test
//     public void testLegacyGatewayUpdateStore() {
//         Store tempStore = new Store("LEGACY_UPDATE_STORE");
//         tempStore.quantityProductsInStock = 200;

//         // Ensure the update method runs without exceptions
//         legacyGateway.updateStoreOnLegacySystem(tempStore);
//     }
// }

package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class StoreEndpointTest {

    @Test
    public void testGetAllStores() {
        given()
            .when().get("/store")
            .then()
            .statusCode(200)
            .body(containsString("TONSTAD"))
            .body(containsString("KALLAX"))
            .body(containsString("BESTÅ"));
    }

    @Test
    public void testGetSingleStoreSuccess() {
        given()
            .when().get("/store/1")
            .then()
            .statusCode(200)
            .body(containsString("TONSTAD"));
    }

    @Test
    public void testGetSingleStoreNotFound() {
        given()
            .when().get("/store/9999")
            .then()
            .statusCode(404)
            .body(containsString("does not exist"));
    }

    @Test
    public void testCreateStoreWithIdShouldFail() {
        String invalidJson = """
            { "id": 123, "name": "INVALID", "quantityProductsInStock": 10 }
            """;
        given()
            .contentType(ContentType.JSON)
            .body(invalidJson)
            .when().post("/store")
            .then()
            .statusCode(422)
            .body(containsString("Id was invalidly set"));
    }

    @Test
    public void testUpdateStoreSuccess() {
        String storeJson = """
            { "name": "STORE_TO_UPDATE", "quantityProductsInStock": 20 }
            """;
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(storeJson)
            .when().post("/store")
            .then().statusCode(201)
            .extract().path("id");

        String updateJson = """
            { "name": "UPDATED_STORE", "quantityProductsInStock": 99 }
            """;
        given()
            .contentType(ContentType.JSON)
            .body(updateJson)
            .when().put("/store/" + id)
            .then()
            .statusCode(200)
            .body(containsString("UPDATED_STORE"));
    }

    @Test
    public void testUpdateStoreMissingNameShouldFail() {
        String storeJson = """
            { "name": "STORE_TO_FAIL", "quantityProductsInStock": 20 }
            """;
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(storeJson)
            .when().post("/store")
            .then().statusCode(201)
            .extract().path("id");

        String updateJson = """
            { "quantityProductsInStock": 99 }
            """;
        given()
            .contentType(ContentType.JSON)
            .body(updateJson)
            .when().put("/store/" + id)
            .then()
            .statusCode(422)
            .body(containsString("Store Name was not set"));
    }

    @Test
    public void testPatchStoreSuccess() {
        String storeJson = """
            { "name": "STORE_TO_PATCH", "quantityProductsInStock": 20 }
            """;
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(storeJson)
            .when().post("/store")
            .then().statusCode(201)
            .extract().path("id");

        String patchJson = """
            { "name": "PATCHED_STORE" }
            """;
        given()
            .contentType(ContentType.JSON)
            .body(patchJson)
            .when().patch("/store/" + id)
            .then()
            .statusCode(200)
            .body(containsString("PATCHED_STORE"));
    }

    @Test
    public void testPatchStoreNotFound() {
        String patchJson = """
            { "name": "PATCHED_STORE" }
            """;
        given()
            .contentType(ContentType.JSON)
            .body(patchJson)
            .when().patch("/store/9999")
            .then()
            .statusCode(404)
            .body(containsString("does not exist"));
    }

    @Test
    public void testDeleteStoreNotFound() {
        given()
            .when().delete("/store/9999")
            .then()
            .statusCode(404)
            .body(containsString("does not exist"));
    }

    @Test
    public void testUpdateStoreNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"name\": \"NONE\", \"quantityProductsInStock\": 1 }")
            .when().put("/store/9999")
            .then()
            .statusCode(404)
            .body(containsString("does not exist"));
    }

    @Test
    public void testPatchStoreQuantityUpdate() {
        String storeJson = """
            { "name": "STORE_TO_PATCH", "quantityProductsInStock": 20 }
            """;
        Integer id = given()
            .contentType(ContentType.JSON)
            .body(storeJson)
            .when().post("/store")
            .then().statusCode(201)
            .extract().path("id");

        String patchJson = """
            { "quantityProductsInStock": 55 }
            """;
        given()
            .contentType(ContentType.JSON)
            .body(patchJson)
            .when().patch("/store/" + id)
            .then()
            .statusCode(200)
            .body(containsString("55"));
    }

    @Test
    public void testErrorMapperGenericException() {
        // Send malformed JSON to trigger a 400
        given()
            .contentType(ContentType.JSON)
            .body("{ invalid json }")
            .when().post("/store")
            .then()
            .statusCode(400)
            .body(containsString("exceptionType"))
            .body(containsString("error"));
    }
}