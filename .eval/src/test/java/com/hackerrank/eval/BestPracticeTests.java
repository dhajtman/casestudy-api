package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import com.hackerrank.eval.fixtures.VulnerabilityDataFixtures;
import com.hackerrank.eval.model.Vulnerability;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith({RESTExtension.class})
class BestPracticeTests {

  //Best practices tests
  @Test
  void whenVulnerabilityRequestedThenCorrectOneIsReturned() {
    Vulnerability vulnerability = VulnerabilityDataFixtures.createAnyVulnerability();
    get("/vulnerability/" + vulnerability.getId())
            .then()
            .statusCode(SC_OK)
            .body("id", equalTo(vulnerability.getId().intValue()));
  }

  @Test
  void whenRequestingDeletionVulnerabilityIsSoftDeleted() {
    Vulnerability newVulnerability = VulnerabilityDataFixtures.createAnyVulnerability();
    Optional<Vulnerability> optionalApiVulnerability = getVulnerabilityById(newVulnerability.getId());
    assertThat(optionalApiVulnerability.isPresent(), is(true));

    newVulnerability = optionalApiVulnerability.get();
    assertThat(newVulnerability.isDeleted(), is(false));

    delete("/vulnerability/" + newVulnerability.getId());

    // It's possible that the Vulnerability is returned with property deleted = true, or that it now returns
    // a 404 - no Vulnerability
    getVulnerabilityById(newVulnerability.getId()).ifPresent(Vulnerability -> assertThat(Vulnerability.isDeleted(), is(true)));
  }

  @Test
  void whenRequestingADeletedVulnerabilityThen404IsReturned() {
    Vulnerability newVulnerability = VulnerabilityDataFixtures.createAnyVulnerability();
    get("/vulnerability/" + newVulnerability.getId()).then().statusCode(SC_OK);

    delete("/vulnerability/" + newVulnerability.getId());

    get("/vulnerability/" + newVulnerability.getId()).then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void deletedVulnerabilityNotPresentWhenRequestingAllVulnerability() {
    whenRequestingDeletionVulnerabilityIsSoftDeleted();
    Vulnerability newVulnerability = VulnerabilityDataFixtures.createAnyVulnerability();
    assertVulnerabilityPresent(true, newVulnerability);

    delete("/vulnerability/" + newVulnerability.getId());

    assertVulnerabilityPresent(false, newVulnerability);
  }

  @Test
  void statusCode404WhenNonExistentVulnerabilityRequested() {
    whenVulnerabilityRequestedThenCorrectOneIsReturned();
    get("/vulnerability/-1").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void statusCodeSuccessWhenDeletingVulnerability() {
    Vulnerability newVulnerability = VulnerabilityDataFixtures.createAnyVulnerability();
    delete("/vulnerability/" + newVulnerability.getId()).then().statusCode(anyOf(is(SC_OK), is(SC_NO_CONTENT)));
  }

  @Test
  void statusCode404WhenTryingToDeleteNonExistentVulnerability() {
    whenRequestingDeletionVulnerabilityIsSoftDeleted();
    delete("/vulnerability/-1").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void statusCode400WhenSortByInvalid() {
    get("/vulnerability/search/Laravel?sortBy=invalid").then().statusCode(SC_BAD_REQUEST);
  }

  //get by id
  private Optional<Vulnerability> getVulnerabilityById(Long id) {
    Vulnerability Vulnerability = get("/vulnerability/" + id).thenReturn().as(Vulnerability.class);
    if (Vulnerability.getId() == null) {
      return Optional.empty();
    } else {
      return Optional.of(Vulnerability);
    }
  }

  //get all
  private void assertVulnerabilityPresent(boolean expected, Vulnerability newVulnerability) {
    Vulnerability[] allVulnerability = get("/vulnerability").andReturn().as(Vulnerability[].class);
    assertThat(
            Stream.of(allVulnerability).anyMatch(Vulnerability -> Vulnerability.getId().equals(newVulnerability.getId())),
            is(expected));
  }
}
