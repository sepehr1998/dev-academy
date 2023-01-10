package fi.solita.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JourneyMapperTest {

    private JourneyMapper journeyMapper;

    @BeforeEach
    public void setUp() {
        journeyMapper = new JourneyMapperImpl();
    }
}
