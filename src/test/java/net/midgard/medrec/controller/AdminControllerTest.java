package net.midgard.medrec.controller;

/**
 *
 * @author thor
 */
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import net.midgard.medrec.service.AdminService;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import net.midgard.medrec.domain.Administrator;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminControllerTest {

  @Autowired
  private WebApplicationContext ctx;

  @Autowired
  private AdminService service;

  /*
    all admins
   */
  @Test
  public void test01() throws Exception {
    RestAssuredMockMvc.webAppContextSetup(ctx);
    RestAssuredMockMvc.given().
            when().
            get("/admin/all").
            then().
            statusCode(200);
  }

  /*
    found single admin by id
   */
  @Test
  public void test02a() throws Exception {
    RestAssuredMockMvc.webAppContextSetup(ctx);
    RestAssuredMockMvc.given().
            when().
            get("/admin/1").
            then().
            statusCode(200).
            and()
            .body("id", equalTo(1))
            .body("username", equalTo("thor"))
            .body("email", equalTo("thor.odinson@medrec.com"));
  }

  /*
    not found single admin by id
   */
  @Test
  public void test02b() throws Exception {
    long notFoundId = getAbsentId();
    RestAssuredMockMvc.webAppContextSetup(ctx);
    RestAssuredMockMvc.given().
            when().
            get("/admin/" + notFoundId).
            then().
            statusCode(404);
  }

  /*
    found admin search by username
   */
  @Test
  public void test03a() throws Exception {
    RestAssuredMockMvc.webAppContextSetup(ctx);
    RestAssuredMockMvc.given().
            when().
            get("/admin/username/thor").
            then().
            statusCode(200).
            and()
            .body("id", equalTo(1))
            .body("username", equalTo("thor"))
            .body("email", equalTo("thor.odinson@medrec.com"));
  }

  /*
    not found admin search by username
   */
  @Test
  public void test03b() throws Exception {
    RestAssuredMockMvc.webAppContextSetup(ctx);

    RestAssuredMockMvc.given().
            when().
            get("/admin/username/steve").
            then().
            statusCode(404);
  }

  /*
  add admin ok
   */
  @Test
  public void test04a() throws Exception {
    Administrator newAdmin = new Administrator("steve", "password", "steve@rogers.com", "usa");
    RestAssuredMockMvc.webAppContextSetup(ctx);

    RestAssuredMockMvc.given().
            contentType("application/json")
            .body(newAdmin)
            .when().post("/admin").then()
            .statusCode(201);
    given()
            .when()
            .get("/admin/username/steve")
            .then()
            .statusCode(200);
  }

  /*
  add admin conflict
   */
  @Test
  public void test04b() throws Exception {
    Administrator newAdmin = new Administrator("thor", "welcome1", "thor@odinson.com", "midgard");

    RestAssuredMockMvc.webAppContextSetup(ctx);
    RestAssuredMockMvc.
            given().
            contentType("application/json")
            .body(newAdmin)
            .when().post("/admin").then()
            .statusCode(409);
  }

  /*
  update admin ok
   */
  @Test
  public void test05a() throws Exception {
    Administrator newAdmin = new Administrator("thor", "welcome1", "thor@odinson.com", "valhalla");

    RestAssuredMockMvc.webAppContextSetup(ctx);

    RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(newAdmin)
            .when()
            .put("/admin/2")
            .then()
            .statusCode(200);

    given().
            when().
            get("/admin/2").
            then().
            statusCode(200).
            and()
            .body("id", equalTo(2))
            .body("username", equalTo("thor"));
  }

  /*
  update admin not found
   */
  @Test
  public void test05b() throws Exception {
    long notFoundId = getAbsentId();

    Administrator newAdmin = new Administrator("thor", "welcome1", "thor@odinson.com", "midgard");

    RestAssuredMockMvc.webAppContextSetup(ctx);

    RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(newAdmin)
            .when()
            .put("/admin/" + notFoundId)
            .then()
            .statusCode(404);
  }

  /*
  delete admin ok
   */
  @Test
  public void test06a() throws Exception {
    RestAssuredMockMvc.webAppContextSetup(ctx);

    RestAssuredMockMvc.given()
            .when()
            .delete("/admin/2")
            .then()
            .statusCode(200);

    given().
            when().
            get("/admin/2").
            then().
            statusCode(404);
  }

  /*
  delete admin not found
   */
  @Test
  public void test06b() throws Exception {
    Administrator newAdmin = new Administrator("thor", "welcome1", "thor@odinson.com", "midgard");
    long notFoundId = getAbsentId();
    RestAssuredMockMvc.webAppContextSetup(ctx);

    RestAssuredMockMvc.given()
            .when()
            .delete("/admin/" + notFoundId)
            .then()
            .statusCode(404);
  }

  private long getAbsentId() {
    Administrator[] admins = new Administrator[(int) service.findAll().get().size()];
    service.findAll().get().toArray(admins);
    long notFoundId = 1;
    for (Administrator admin : admins) {
      if (admin.getId() != notFoundId) {
        break;
      }
      else {
        notFoundId++;
      }
    }
    return notFoundId;
  }
}
