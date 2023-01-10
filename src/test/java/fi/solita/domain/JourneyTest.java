package fi.solita.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fi.solita.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JourneyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Journey.class);
        Journey journey1 = new Journey();
        journey1.setId(1L);
        Journey journey2 = new Journey();
        journey2.setId(journey1.getId());
        assertThat(journey1).isEqualTo(journey2);
        journey2.setId(2L);
        assertThat(journey1).isNotEqualTo(journey2);
        journey1.setId(null);
        assertThat(journey1).isNotEqualTo(journey2);
    }
}
