package fi.solita.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fi.solita.IntegrationTest;
import fi.solita.domain.Station;
import fi.solita.repository.StationRepository;
import fi.solita.service.criteria.StationCriteria;
import fi.solita.service.dto.StationDTO;
import fi.solita.service.mapper.StationMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_STARTED_FROM = 1L;
    private static final Long UPDATED_STARTED_FROM = 2L;
    private static final Long SMALLER_STARTED_FROM = 1L - 1L;

    private static final Long DEFAULT_ENDING_IN = 1L;
    private static final Long UPDATED_ENDING_IN = 2L;
    private static final Long SMALLER_ENDING_IN = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/stations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStationMockMvc;

    private Station station;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Station createEntity(EntityManager em) {
        Station station = new Station()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .startedFrom(DEFAULT_STARTED_FROM)
            .endingIn(DEFAULT_ENDING_IN);
        return station;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Station createUpdatedEntity(EntityManager em) {
        Station station = new Station()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .startedFrom(UPDATED_STARTED_FROM)
            .endingIn(UPDATED_ENDING_IN);
        return station;
    }

    @BeforeEach
    public void initTest() {
        station = createEntity(em);
    }

    @Test
    @Transactional
    void createStation() throws Exception {
        int databaseSizeBeforeCreate = stationRepository.findAll().size();
        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);
        restStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stationDTO)))
            .andExpect(status().isCreated());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeCreate + 1);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStation.getStartedFrom()).isEqualTo(DEFAULT_STARTED_FROM);
        assertThat(testStation.getEndingIn()).isEqualTo(DEFAULT_ENDING_IN);
    }

    @Test
    @Transactional
    void createStationWithExistingId() throws Exception {
        // Create the Station with an existing ID
        station.setId(1L);
        StationDTO stationDTO = stationMapper.toDto(station);

        int databaseSizeBeforeCreate = stationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stationRepository.findAll().size();
        // set the field null
        station.setName(null);

        // Create the Station, which fails.
        StationDTO stationDTO = stationMapper.toDto(station);

        restStationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stationDTO)))
            .andExpect(status().isBadRequest());

        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStations() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList
        restStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(station.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].startedFrom").value(hasItem(DEFAULT_STARTED_FROM.intValue())))
            .andExpect(jsonPath("$.[*].endingIn").value(hasItem(DEFAULT_ENDING_IN.intValue())));
    }

    @Test
    @Transactional
    void getStation() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get the station
        restStationMockMvc
            .perform(get(ENTITY_API_URL_ID, station.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(station.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.startedFrom").value(DEFAULT_STARTED_FROM.intValue()))
            .andExpect(jsonPath("$.endingIn").value(DEFAULT_ENDING_IN.intValue()));
    }

    @Test
    @Transactional
    void getStationsByIdFiltering() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        Long id = station.getId();

        defaultStationShouldBeFound("id.equals=" + id);
        defaultStationShouldNotBeFound("id.notEquals=" + id);

        defaultStationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStationShouldNotBeFound("id.greaterThan=" + id);

        defaultStationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name equals to DEFAULT_NAME
        defaultStationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stationList where name equals to UPDATED_NAME
        defaultStationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stationList where name equals to UPDATED_NAME
        defaultStationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name is not null
        defaultStationShouldBeFound("name.specified=true");

        // Get all the stationList where name is null
        defaultStationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllStationsByNameContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name contains DEFAULT_NAME
        defaultStationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the stationList where name contains UPDATED_NAME
        defaultStationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where name does not contain DEFAULT_NAME
        defaultStationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the stationList where name does not contain UPDATED_NAME
        defaultStationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where address equals to DEFAULT_ADDRESS
        defaultStationShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the stationList where address equals to UPDATED_ADDRESS
        defaultStationShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultStationShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the stationList where address equals to UPDATED_ADDRESS
        defaultStationShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where address is not null
        defaultStationShouldBeFound("address.specified=true");

        // Get all the stationList where address is null
        defaultStationShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllStationsByAddressContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where address contains DEFAULT_ADDRESS
        defaultStationShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the stationList where address contains UPDATED_ADDRESS
        defaultStationShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStationsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where address does not contain DEFAULT_ADDRESS
        defaultStationShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the stationList where address does not contain UPDATED_ADDRESS
        defaultStationShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom equals to DEFAULT_STARTED_FROM
        defaultStationShouldBeFound("startedFrom.equals=" + DEFAULT_STARTED_FROM);

        // Get all the stationList where startedFrom equals to UPDATED_STARTED_FROM
        defaultStationShouldNotBeFound("startedFrom.equals=" + UPDATED_STARTED_FROM);
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom in DEFAULT_STARTED_FROM or UPDATED_STARTED_FROM
        defaultStationShouldBeFound("startedFrom.in=" + DEFAULT_STARTED_FROM + "," + UPDATED_STARTED_FROM);

        // Get all the stationList where startedFrom equals to UPDATED_STARTED_FROM
        defaultStationShouldNotBeFound("startedFrom.in=" + UPDATED_STARTED_FROM);
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom is not null
        defaultStationShouldBeFound("startedFrom.specified=true");

        // Get all the stationList where startedFrom is null
        defaultStationShouldNotBeFound("startedFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom is greater than or equal to DEFAULT_STARTED_FROM
        defaultStationShouldBeFound("startedFrom.greaterThanOrEqual=" + DEFAULT_STARTED_FROM);

        // Get all the stationList where startedFrom is greater than or equal to UPDATED_STARTED_FROM
        defaultStationShouldNotBeFound("startedFrom.greaterThanOrEqual=" + UPDATED_STARTED_FROM);
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom is less than or equal to DEFAULT_STARTED_FROM
        defaultStationShouldBeFound("startedFrom.lessThanOrEqual=" + DEFAULT_STARTED_FROM);

        // Get all the stationList where startedFrom is less than or equal to SMALLER_STARTED_FROM
        defaultStationShouldNotBeFound("startedFrom.lessThanOrEqual=" + SMALLER_STARTED_FROM);
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsLessThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom is less than DEFAULT_STARTED_FROM
        defaultStationShouldNotBeFound("startedFrom.lessThan=" + DEFAULT_STARTED_FROM);

        // Get all the stationList where startedFrom is less than UPDATED_STARTED_FROM
        defaultStationShouldBeFound("startedFrom.lessThan=" + UPDATED_STARTED_FROM);
    }

    @Test
    @Transactional
    void getAllStationsByStartedFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where startedFrom is greater than DEFAULT_STARTED_FROM
        defaultStationShouldNotBeFound("startedFrom.greaterThan=" + DEFAULT_STARTED_FROM);

        // Get all the stationList where startedFrom is greater than SMALLER_STARTED_FROM
        defaultStationShouldBeFound("startedFrom.greaterThan=" + SMALLER_STARTED_FROM);
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn equals to DEFAULT_ENDING_IN
        defaultStationShouldBeFound("endingIn.equals=" + DEFAULT_ENDING_IN);

        // Get all the stationList where endingIn equals to UPDATED_ENDING_IN
        defaultStationShouldNotBeFound("endingIn.equals=" + UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsInShouldWork() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn in DEFAULT_ENDING_IN or UPDATED_ENDING_IN
        defaultStationShouldBeFound("endingIn.in=" + DEFAULT_ENDING_IN + "," + UPDATED_ENDING_IN);

        // Get all the stationList where endingIn equals to UPDATED_ENDING_IN
        defaultStationShouldNotBeFound("endingIn.in=" + UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsNullOrNotNull() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn is not null
        defaultStationShouldBeFound("endingIn.specified=true");

        // Get all the stationList where endingIn is null
        defaultStationShouldNotBeFound("endingIn.specified=false");
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn is greater than or equal to DEFAULT_ENDING_IN
        defaultStationShouldBeFound("endingIn.greaterThanOrEqual=" + DEFAULT_ENDING_IN);

        // Get all the stationList where endingIn is greater than or equal to UPDATED_ENDING_IN
        defaultStationShouldNotBeFound("endingIn.greaterThanOrEqual=" + UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn is less than or equal to DEFAULT_ENDING_IN
        defaultStationShouldBeFound("endingIn.lessThanOrEqual=" + DEFAULT_ENDING_IN);

        // Get all the stationList where endingIn is less than or equal to SMALLER_ENDING_IN
        defaultStationShouldNotBeFound("endingIn.lessThanOrEqual=" + SMALLER_ENDING_IN);
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsLessThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn is less than DEFAULT_ENDING_IN
        defaultStationShouldNotBeFound("endingIn.lessThan=" + DEFAULT_ENDING_IN);

        // Get all the stationList where endingIn is less than UPDATED_ENDING_IN
        defaultStationShouldBeFound("endingIn.lessThan=" + UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void getAllStationsByEndingInIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        // Get all the stationList where endingIn is greater than DEFAULT_ENDING_IN
        defaultStationShouldNotBeFound("endingIn.greaterThan=" + DEFAULT_ENDING_IN);

        // Get all the stationList where endingIn is greater than SMALLER_ENDING_IN
        defaultStationShouldBeFound("endingIn.greaterThan=" + SMALLER_ENDING_IN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStationShouldBeFound(String filter) throws Exception {
        restStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(station.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].startedFrom").value(hasItem(DEFAULT_STARTED_FROM.intValue())))
            .andExpect(jsonPath("$.[*].endingIn").value(hasItem(DEFAULT_ENDING_IN.intValue())));

        // Check, that the count call also returns 1
        restStationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStationShouldNotBeFound(String filter) throws Exception {
        restStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStation() throws Exception {
        // Get the station
        restStationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStation() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Update the station
        Station updatedStation = stationRepository.findById(station.getId()).get();
        // Disconnect from session so that the updates on updatedStation are not directly saved in db
        em.detach(updatedStation);
        updatedStation.name(UPDATED_NAME).address(UPDATED_ADDRESS).startedFrom(UPDATED_STARTED_FROM).endingIn(UPDATED_ENDING_IN);
        StationDTO stationDTO = stationMapper.toDto(updatedStation);

        restStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStation.getStartedFrom()).isEqualTo(UPDATED_STARTED_FROM);
        assertThat(testStation.getEndingIn()).isEqualTo(UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void putNonExistingStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();
        station.setId(count.incrementAndGet());

        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();
        station.setId(count.incrementAndGet());

        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();
        station.setId(count.incrementAndGet());

        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStationWithPatch() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Update the station using partial update
        Station partialUpdatedStation = new Station();
        partialUpdatedStation.setId(station.getId());

        partialUpdatedStation.name(UPDATED_NAME).address(UPDATED_ADDRESS).startedFrom(UPDATED_STARTED_FROM).endingIn(UPDATED_ENDING_IN);

        restStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStation))
            )
            .andExpect(status().isOk());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStation.getStartedFrom()).isEqualTo(UPDATED_STARTED_FROM);
        assertThat(testStation.getEndingIn()).isEqualTo(UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void fullUpdateStationWithPatch() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        int databaseSizeBeforeUpdate = stationRepository.findAll().size();

        // Update the station using partial update
        Station partialUpdatedStation = new Station();
        partialUpdatedStation.setId(station.getId());

        partialUpdatedStation.name(UPDATED_NAME).address(UPDATED_ADDRESS).startedFrom(UPDATED_STARTED_FROM).endingIn(UPDATED_ENDING_IN);

        restStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStation))
            )
            .andExpect(status().isOk());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
        Station testStation = stationList.get(stationList.size() - 1);
        assertThat(testStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStation.getStartedFrom()).isEqualTo(UPDATED_STARTED_FROM);
        assertThat(testStation.getEndingIn()).isEqualTo(UPDATED_ENDING_IN);
    }

    @Test
    @Transactional
    void patchNonExistingStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();
        station.setId(count.incrementAndGet());

        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();
        station.setId(count.incrementAndGet());

        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStation() throws Exception {
        int databaseSizeBeforeUpdate = stationRepository.findAll().size();
        station.setId(count.incrementAndGet());

        // Create the Station
        StationDTO stationDTO = stationMapper.toDto(station);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Station in the database
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStation() throws Exception {
        // Initialize the database
        stationRepository.saveAndFlush(station);

        int databaseSizeBeforeDelete = stationRepository.findAll().size();

        // Delete the station
        restStationMockMvc
            .perform(delete(ENTITY_API_URL_ID, station.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Station> stationList = stationRepository.findAll();
        assertThat(stationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
