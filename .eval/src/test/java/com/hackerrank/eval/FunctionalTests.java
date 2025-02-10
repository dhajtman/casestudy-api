package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import com.hackerrank.eval.fixtures.VulnerabilityDataFixtures;
import com.hackerrank.eval.model.Vulnerability;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.emptyIterable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith({RESTExtension.class})
class FunctionalTests {

  @Test
  void whenRequestingVulnerabilitiesSortedThenExpectRightOrder() {
    List<Vulnerability> vulnerabilitiesSamples = VulnerabilityDataFixtures.getVulnerabilitySamples();
    List<Vulnerability> expectedList = VulnerabilityDataFixtures.getListOrderedByScore(vulnerabilitiesSamples, "Wordpress");

    List<Vulnerability> resultList =
            Arrays.asList(
                    get("/vulnerability/search/Wordpress?orderBy=score")
                            .then()
                            .statusCode(SC_OK)
                            .extract()
                            .response()
                            .as(Vulnerability[].class));

    assertThat(
            resultList.stream().map(Vulnerability::getId).toArray(),
            arrayContaining(expectedList.stream().map(Vulnerability::getId).toArray()));
  }

  @Test
  void whenRequestingVulnerabilitiesSortedWithNoVulnerabilitiesExpectEmptyList() {
    get("/vulnerability/search/-1?orderBy=score")
            .then()
            .statusCode(SC_OK)
            .body("$", emptyIterable());
  }
}
