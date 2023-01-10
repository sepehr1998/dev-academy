package fi.solita.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fi.solita.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Station.class);
        Station station1 = new Station();
        station1.setId(1L);
        Station station2 = new Station();
        station2.setId(station1.getId());
        assertThat(station1).isEqualTo(station2);
        station2.setId(2L);
        assertThat(station1).isNotEqualTo(station2);
        station1.setId(null);
        assertThat(station1).isNotEqualTo(station2);
    }
}
