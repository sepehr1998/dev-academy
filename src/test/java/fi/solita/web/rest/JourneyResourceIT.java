package fi.solita.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fi.solita.IntegrationTest;
import fi.solita.domain.Journey;
import fi.solita.domain.Station;
import fi.solita.repository.JourneyRepository;
import fi.solita.service.JourneyService;
import fi.solita.service.criteria.JourneyCriteria;
import fi.solita.service.dto.JourneyDTO;
import fi.solita.service.mapper.JourneyMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JourneyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JourneyResourceIT {

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;
    private static final Double SMALLER_DISTANCE = 1D - 1D;

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);
    private static final Duration SMALLER_DURATION = Duration.ofHours(5);

    private static final String ENTITY_API_URL = "/api/journeys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JourneyRepository journeyRepository;

    @Mock
    private JourneyRepository journeyRepositoryMock;

    @Autowired
    private JourneyMapper journeyMapper;

    @Mock
    private JourneyService journeyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJourneyMockMvc;

    private Journey journey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journey createEntity(EntityManager em) {
        Journey journey = new Journey().distance(DEFAULT_DISTANCE).duration(DEFAULT_DURATION);
        // Add required entity
        Station station;
        if (TestUtil.findAll(em, Station.class).isEmpty()) {
            station = StationResourceIT.createEntity(em);
            em.persist(station);
            em.flush();
        } else {
            station = TestUtil.findAll(em, Station.class).get(0);
        }
        journey.setDepartureStation(station);
        // Add required entity
        journey.setReturnStation(station);
        return journey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journey createUpdatedEntity(EntityManager em) {
        Journey journey = new Journey().distance(UPDATED_DISTANCE).duration(UPDATED_DURATION);
        // Add required entity
        Station station;
        if (TestUtil.findAll(em, Station.class).isEmpty()) {
            station = StationResourceIT.createUpdatedEntity(em);
            em.persist(station);
            em.flush();
        } else {
            station = TestUtil.findAll(em, Station.class).get(0);
        }
        journey.setDepartureStation(station);
        // Add required entity
        journey.setReturnStation(station);
        return journey;
    }

    @BeforeEach
    public void initTest() {
        journey = createEntity(em);
    }

    @Test
    @Transactional
    void createJourney() throws Exception {
        int databaseSizeBeforeCreate = journeyRepository.findAll().size();
        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);
        restJourneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isCreated());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeCreate + 1);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testJourney.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createJourneyWithExistingId() throws Exception {
        // Create the Journey with an existing ID
        journey.setId(1L);
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        int databaseSizeBeforeCreate = journeyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJourneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = journeyRepository.findAll().size();
        // set the field null
        journey.setDistance(null);

        // Create the Journey, which fails.
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        restJourneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isBadRequest());

        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = journeyRepository.findAll().size();
        // set the field null
        journey.setDuration(null);

        // Create the Journey, which fails.
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        restJourneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isBadRequest());

        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJourneys() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journey.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJourneysWithEagerRelationshipsIsEnabled() throws Exception {
        when(journeyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJourneyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(journeyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJourneysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(journeyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJourneyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(journeyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get the journey
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL_ID, journey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journey.getId().intValue()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    void getJourneysByIdFiltering() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        Long id = journey.getId();

        defaultJourneyShouldBeFound("id.equals=" + id);
        defaultJourneyShouldNotBeFound("id.notEquals=" + id);

        defaultJourneyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJourneyShouldNotBeFound("id.greaterThan=" + id);

        defaultJourneyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJourneyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance equals to DEFAULT_DISTANCE
        defaultJourneyShouldBeFound("distance.equals=" + DEFAULT_DISTANCE);

        // Get all the journeyList where distance equals to UPDATED_DISTANCE
        defaultJourneyShouldNotBeFound("distance.equals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsInShouldWork() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance in DEFAULT_DISTANCE or UPDATED_DISTANCE
        defaultJourneyShouldBeFound("distance.in=" + DEFAULT_DISTANCE + "," + UPDATED_DISTANCE);

        // Get all the journeyList where distance equals to UPDATED_DISTANCE
        defaultJourneyShouldNotBeFound("distance.in=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance is not null
        defaultJourneyShouldBeFound("distance.specified=true");

        // Get all the journeyList where distance is null
        defaultJourneyShouldNotBeFound("distance.specified=false");
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance is greater than or equal to DEFAULT_DISTANCE
        defaultJourneyShouldBeFound("distance.greaterThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the journeyList where distance is greater than or equal to UPDATED_DISTANCE
        defaultJourneyShouldNotBeFound("distance.greaterThanOrEqual=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance is less than or equal to DEFAULT_DISTANCE
        defaultJourneyShouldBeFound("distance.lessThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the journeyList where distance is less than or equal to SMALLER_DISTANCE
        defaultJourneyShouldNotBeFound("distance.lessThanOrEqual=" + SMALLER_DISTANCE);
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsLessThanSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance is less than DEFAULT_DISTANCE
        defaultJourneyShouldNotBeFound("distance.lessThan=" + DEFAULT_DISTANCE);

        // Get all the journeyList where distance is less than UPDATED_DISTANCE
        defaultJourneyShouldBeFound("distance.lessThan=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllJourneysByDistanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where distance is greater than DEFAULT_DISTANCE
        defaultJourneyShouldNotBeFound("distance.greaterThan=" + DEFAULT_DISTANCE);

        // Get all the journeyList where distance is greater than SMALLER_DISTANCE
        defaultJourneyShouldBeFound("distance.greaterThan=" + SMALLER_DISTANCE);
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration equals to DEFAULT_DURATION
        defaultJourneyShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the journeyList where duration equals to UPDATED_DURATION
        defaultJourneyShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultJourneyShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the journeyList where duration equals to UPDATED_DURATION
        defaultJourneyShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration is not null
        defaultJourneyShouldBeFound("duration.specified=true");

        // Get all the journeyList where duration is null
        defaultJourneyShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration is greater than or equal to DEFAULT_DURATION
        defaultJourneyShouldBeFound("duration.greaterThanOrEqual=" + DEFAULT_DURATION);

        // Get all the journeyList where duration is greater than or equal to UPDATED_DURATION
        defaultJourneyShouldNotBeFound("duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration is less than or equal to DEFAULT_DURATION
        defaultJourneyShouldBeFound("duration.lessThanOrEqual=" + DEFAULT_DURATION);

        // Get all the journeyList where duration is less than or equal to SMALLER_DURATION
        defaultJourneyShouldNotBeFound("duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration is less than DEFAULT_DURATION
        defaultJourneyShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the journeyList where duration is less than UPDATED_DURATION
        defaultJourneyShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllJourneysByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList where duration is greater than DEFAULT_DURATION
        defaultJourneyShouldNotBeFound("duration.greaterThan=" + DEFAULT_DURATION);

        // Get all the journeyList where duration is greater than SMALLER_DURATION
        defaultJourneyShouldBeFound("duration.greaterThan=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllJourneysByDepartureStationIsEqualToSomething() throws Exception {
        Station departureStation;
        if (TestUtil.findAll(em, Station.class).isEmpty()) {
            journeyRepository.saveAndFlush(journey);
            departureStation = StationResourceIT.createEntity(em);
        } else {
            departureStation = TestUtil.findAll(em, Station.class).get(0);
        }
        em.persist(departureStation);
        em.flush();
        journey.setDepartureStation(departureStation);
        journeyRepository.saveAndFlush(journey);
        Long departureStationId = departureStation.getId();

        // Get all the journeyList where departureStation equals to departureStationId
        defaultJourneyShouldBeFound("departureStationId.equals=" + departureStationId);

        // Get all the journeyList where departureStation equals to (departureStationId + 1)
        defaultJourneyShouldNotBeFound("departureStationId.equals=" + (departureStationId + 1));
    }

    @Test
    @Transactional
    void getAllJourneysByReturnStationIsEqualToSomething() throws Exception {
        Station returnStation;
        if (TestUtil.findAll(em, Station.class).isEmpty()) {
            journeyRepository.saveAndFlush(journey);
            returnStation = StationResourceIT.createEntity(em);
        } else {
            returnStation = TestUtil.findAll(em, Station.class).get(0);
        }
        em.persist(returnStation);
        em.flush();
        journey.setReturnStation(returnStation);
        journeyRepository.saveAndFlush(journey);
        Long returnStationId = returnStation.getId();

        // Get all the journeyList where returnStation equals to returnStationId
        defaultJourneyShouldBeFound("returnStationId.equals=" + returnStationId);

        // Get all the journeyList where returnStation equals to (returnStationId + 1)
        defaultJourneyShouldNotBeFound("returnStationId.equals=" + (returnStationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJourneyShouldBeFound(String filter) throws Exception {
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journey.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));

        // Check, that the count call also returns 1
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJourneyShouldNotBeFound(String filter) throws Exception {
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJourney() throws Exception {
        // Get the journey
        restJourneyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey
        Journey updatedJourney = journeyRepository.findById(journey.getId()).get();
        // Disconnect from session so that the updates on updatedJourney are not directly saved in db
        em.detach(updatedJourney);
        updatedJourney.distance(UPDATED_DISTANCE).duration(UPDATED_DURATION);
        JourneyDTO journeyDTO = journeyMapper.toDto(updatedJourney);

        restJourneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testJourney.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJourneyWithPatch() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey using partial update
        Journey partialUpdatedJourney = new Journey();
        partialUpdatedJourney.setId(journey.getId());

        partialUpdatedJourney.distance(UPDATED_DISTANCE).duration(UPDATED_DURATION);

        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJourney.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJourney))
            )
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testJourney.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateJourneyWithPatch() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey using partial update
        Journey partialUpdatedJourney = new Journey();
        partialUpdatedJourney.setId(journey.getId());

        partialUpdatedJourney.distance(UPDATED_DISTANCE).duration(UPDATED_DURATION);

        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJourney.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJourney))
            )
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testJourney.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, journeyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeDelete = journeyRepository.findAll().size();

        // Delete the journey
        restJourneyMockMvc
            .perform(delete(ENTITY_API_URL_ID, journey.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
