package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import ca.emonster.training.ecommerce.domain.TblTaxRegion;
import ca.emonster.training.ecommerce.domain.enumeration.TaxType;
import ca.emonster.training.ecommerce.repository.TblTaxRegionRepository;
import ca.emonster.training.ecommerce.service.criteria.TblTaxRegionCriteria;
import ca.emonster.training.ecommerce.service.dto.TblTaxRegionDTO;
import ca.emonster.training.ecommerce.service.mapper.TblTaxRegionMapper;
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
 * Integration tests for the {@link TblTaxRegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblTaxRegionResourceIT {

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final TaxType DEFAULT_TAX_TYPE = TaxType.CAHST;
    private static final TaxType UPDATED_TAX_TYPE = TaxType.CAPST;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;
    private static final Double SMALLER_VALUE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/tbl-tax-regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblTaxRegionRepository tblTaxRegionRepository;

    @Autowired
    private TblTaxRegionMapper tblTaxRegionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblTaxRegionMockMvc;

    private TblTaxRegion tblTaxRegion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblTaxRegion createEntity(EntityManager em) {
        TblTaxRegion tblTaxRegion = new TblTaxRegion()
            .country(DEFAULT_COUNTRY)
            .state(DEFAULT_STATE)
            .taxType(DEFAULT_TAX_TYPE)
            .value(DEFAULT_VALUE);
        return tblTaxRegion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblTaxRegion createUpdatedEntity(EntityManager em) {
        TblTaxRegion tblTaxRegion = new TblTaxRegion()
            .country(UPDATED_COUNTRY)
            .state(UPDATED_STATE)
            .taxType(UPDATED_TAX_TYPE)
            .value(UPDATED_VALUE);
        return tblTaxRegion;
    }

    @BeforeEach
    public void initTest() {
        tblTaxRegion = createEntity(em);
    }

    @Test
    @Transactional
    void createTblTaxRegion() throws Exception {
        int databaseSizeBeforeCreate = tblTaxRegionRepository.findAll().size();
        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);
        restTblTaxRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeCreate + 1);
        TblTaxRegion testTblTaxRegion = tblTaxRegionList.get(tblTaxRegionList.size() - 1);
        assertThat(testTblTaxRegion.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testTblTaxRegion.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testTblTaxRegion.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testTblTaxRegion.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTblTaxRegionWithExistingId() throws Exception {
        // Create the TblTaxRegion with an existing ID
        tblTaxRegion.setId(1L);
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        int databaseSizeBeforeCreate = tblTaxRegionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblTaxRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblTaxRegionRepository.findAll().size();
        // set the field null
        tblTaxRegion.setCountry(null);

        // Create the TblTaxRegion, which fails.
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        restTblTaxRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblTaxRegionRepository.findAll().size();
        // set the field null
        tblTaxRegion.setState(null);

        // Create the TblTaxRegion, which fails.
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        restTblTaxRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblTaxRegionRepository.findAll().size();
        // set the field null
        tblTaxRegion.setTaxType(null);

        // Create the TblTaxRegion, which fails.
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        restTblTaxRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblTaxRegionRepository.findAll().size();
        // set the field null
        tblTaxRegion.setValue(null);

        // Create the TblTaxRegion, which fails.
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        restTblTaxRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTblTaxRegions() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList
        restTblTaxRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblTaxRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getTblTaxRegion() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get the tblTaxRegion
        restTblTaxRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, tblTaxRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblTaxRegion.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.taxType").value(DEFAULT_TAX_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getTblTaxRegionsByIdFiltering() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        Long id = tblTaxRegion.getId();

        defaultTblTaxRegionShouldBeFound("id.equals=" + id);
        defaultTblTaxRegionShouldNotBeFound("id.notEquals=" + id);

        defaultTblTaxRegionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblTaxRegionShouldNotBeFound("id.greaterThan=" + id);

        defaultTblTaxRegionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblTaxRegionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where country equals to DEFAULT_COUNTRY
        defaultTblTaxRegionShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the tblTaxRegionList where country equals to UPDATED_COUNTRY
        defaultTblTaxRegionShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where country not equals to DEFAULT_COUNTRY
        defaultTblTaxRegionShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the tblTaxRegionList where country not equals to UPDATED_COUNTRY
        defaultTblTaxRegionShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultTblTaxRegionShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the tblTaxRegionList where country equals to UPDATED_COUNTRY
        defaultTblTaxRegionShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where country is not null
        defaultTblTaxRegionShouldBeFound("country.specified=true");

        // Get all the tblTaxRegionList where country is null
        defaultTblTaxRegionShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByCountryContainsSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where country contains DEFAULT_COUNTRY
        defaultTblTaxRegionShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the tblTaxRegionList where country contains UPDATED_COUNTRY
        defaultTblTaxRegionShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where country does not contain DEFAULT_COUNTRY
        defaultTblTaxRegionShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the tblTaxRegionList where country does not contain UPDATED_COUNTRY
        defaultTblTaxRegionShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where state equals to DEFAULT_STATE
        defaultTblTaxRegionShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the tblTaxRegionList where state equals to UPDATED_STATE
        defaultTblTaxRegionShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where state not equals to DEFAULT_STATE
        defaultTblTaxRegionShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the tblTaxRegionList where state not equals to UPDATED_STATE
        defaultTblTaxRegionShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where state in DEFAULT_STATE or UPDATED_STATE
        defaultTblTaxRegionShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the tblTaxRegionList where state equals to UPDATED_STATE
        defaultTblTaxRegionShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where state is not null
        defaultTblTaxRegionShouldBeFound("state.specified=true");

        // Get all the tblTaxRegionList where state is null
        defaultTblTaxRegionShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByStateContainsSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where state contains DEFAULT_STATE
        defaultTblTaxRegionShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the tblTaxRegionList where state contains UPDATED_STATE
        defaultTblTaxRegionShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where state does not contain DEFAULT_STATE
        defaultTblTaxRegionShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the tblTaxRegionList where state does not contain UPDATED_STATE
        defaultTblTaxRegionShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByTaxTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where taxType equals to DEFAULT_TAX_TYPE
        defaultTblTaxRegionShouldBeFound("taxType.equals=" + DEFAULT_TAX_TYPE);

        // Get all the tblTaxRegionList where taxType equals to UPDATED_TAX_TYPE
        defaultTblTaxRegionShouldNotBeFound("taxType.equals=" + UPDATED_TAX_TYPE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByTaxTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where taxType not equals to DEFAULT_TAX_TYPE
        defaultTblTaxRegionShouldNotBeFound("taxType.notEquals=" + DEFAULT_TAX_TYPE);

        // Get all the tblTaxRegionList where taxType not equals to UPDATED_TAX_TYPE
        defaultTblTaxRegionShouldBeFound("taxType.notEquals=" + UPDATED_TAX_TYPE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByTaxTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where taxType in DEFAULT_TAX_TYPE or UPDATED_TAX_TYPE
        defaultTblTaxRegionShouldBeFound("taxType.in=" + DEFAULT_TAX_TYPE + "," + UPDATED_TAX_TYPE);

        // Get all the tblTaxRegionList where taxType equals to UPDATED_TAX_TYPE
        defaultTblTaxRegionShouldNotBeFound("taxType.in=" + UPDATED_TAX_TYPE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByTaxTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where taxType is not null
        defaultTblTaxRegionShouldBeFound("taxType.specified=true");

        // Get all the tblTaxRegionList where taxType is null
        defaultTblTaxRegionShouldNotBeFound("taxType.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value equals to DEFAULT_VALUE
        defaultTblTaxRegionShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the tblTaxRegionList where value equals to UPDATED_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value not equals to DEFAULT_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the tblTaxRegionList where value not equals to UPDATED_VALUE
        defaultTblTaxRegionShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTblTaxRegionShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the tblTaxRegionList where value equals to UPDATED_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value is not null
        defaultTblTaxRegionShouldBeFound("value.specified=true");

        // Get all the tblTaxRegionList where value is null
        defaultTblTaxRegionShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value is greater than or equal to DEFAULT_VALUE
        defaultTblTaxRegionShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the tblTaxRegionList where value is greater than or equal to UPDATED_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value is less than or equal to DEFAULT_VALUE
        defaultTblTaxRegionShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the tblTaxRegionList where value is less than or equal to SMALLER_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value is less than DEFAULT_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the tblTaxRegionList where value is less than UPDATED_VALUE
        defaultTblTaxRegionShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        // Get all the tblTaxRegionList where value is greater than DEFAULT_VALUE
        defaultTblTaxRegionShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the tblTaxRegionList where value is greater than SMALLER_VALUE
        defaultTblTaxRegionShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllTblTaxRegionsByTaxExemptIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);
        TblTaxExempt taxExempt;
        if (TestUtil.findAll(em, TblTaxExempt.class).isEmpty()) {
            taxExempt = TblTaxExemptResourceIT.createEntity(em);
            em.persist(taxExempt);
            em.flush();
        } else {
            taxExempt = TestUtil.findAll(em, TblTaxExempt.class).get(0);
        }
        em.persist(taxExempt);
        em.flush();
        tblTaxRegion.setTaxExempt(taxExempt);
        taxExempt.setRegion(tblTaxRegion);
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);
        Long taxExemptId = taxExempt.getId();

        // Get all the tblTaxRegionList where taxExempt equals to taxExemptId
        defaultTblTaxRegionShouldBeFound("taxExemptId.equals=" + taxExemptId);

        // Get all the tblTaxRegionList where taxExempt equals to (taxExemptId + 1)
        defaultTblTaxRegionShouldNotBeFound("taxExemptId.equals=" + (taxExemptId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblTaxRegionShouldBeFound(String filter) throws Exception {
        restTblTaxRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblTaxRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));

        // Check, that the count call also returns 1
        restTblTaxRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblTaxRegionShouldNotBeFound(String filter) throws Exception {
        restTblTaxRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblTaxRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblTaxRegion() throws Exception {
        // Get the tblTaxRegion
        restTblTaxRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblTaxRegion() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();

        // Update the tblTaxRegion
        TblTaxRegion updatedTblTaxRegion = tblTaxRegionRepository.findById(tblTaxRegion.getId()).get();
        // Disconnect from session so that the updates on updatedTblTaxRegion are not directly saved in db
        em.detach(updatedTblTaxRegion);
        updatedTblTaxRegion.country(UPDATED_COUNTRY).state(UPDATED_STATE).taxType(UPDATED_TAX_TYPE).value(UPDATED_VALUE);
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(updatedTblTaxRegion);

        restTblTaxRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblTaxRegionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
        TblTaxRegion testTblTaxRegion = tblTaxRegionList.get(tblTaxRegionList.size() - 1);
        assertThat(testTblTaxRegion.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTblTaxRegion.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTblTaxRegion.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testTblTaxRegion.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTblTaxRegion() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();
        tblTaxRegion.setId(count.incrementAndGet());

        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblTaxRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblTaxRegionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblTaxRegion() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();
        tblTaxRegion.setId(count.incrementAndGet());

        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblTaxRegion() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();
        tblTaxRegion.setId(count.incrementAndGet());

        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxRegionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblTaxRegionWithPatch() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();

        // Update the tblTaxRegion using partial update
        TblTaxRegion partialUpdatedTblTaxRegion = new TblTaxRegion();
        partialUpdatedTblTaxRegion.setId(tblTaxRegion.getId());

        partialUpdatedTblTaxRegion.country(UPDATED_COUNTRY).state(UPDATED_STATE);

        restTblTaxRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblTaxRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblTaxRegion))
            )
            .andExpect(status().isOk());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
        TblTaxRegion testTblTaxRegion = tblTaxRegionList.get(tblTaxRegionList.size() - 1);
        assertThat(testTblTaxRegion.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTblTaxRegion.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTblTaxRegion.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testTblTaxRegion.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTblTaxRegionWithPatch() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();

        // Update the tblTaxRegion using partial update
        TblTaxRegion partialUpdatedTblTaxRegion = new TblTaxRegion();
        partialUpdatedTblTaxRegion.setId(tblTaxRegion.getId());

        partialUpdatedTblTaxRegion.country(UPDATED_COUNTRY).state(UPDATED_STATE).taxType(UPDATED_TAX_TYPE).value(UPDATED_VALUE);

        restTblTaxRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblTaxRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblTaxRegion))
            )
            .andExpect(status().isOk());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
        TblTaxRegion testTblTaxRegion = tblTaxRegionList.get(tblTaxRegionList.size() - 1);
        assertThat(testTblTaxRegion.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTblTaxRegion.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTblTaxRegion.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testTblTaxRegion.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTblTaxRegion() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();
        tblTaxRegion.setId(count.incrementAndGet());

        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblTaxRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblTaxRegionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblTaxRegion() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();
        tblTaxRegion.setId(count.incrementAndGet());

        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblTaxRegion() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxRegionRepository.findAll().size();
        tblTaxRegion.setId(count.incrementAndGet());

        // Create the TblTaxRegion
        TblTaxRegionDTO tblTaxRegionDTO = tblTaxRegionMapper.toDto(tblTaxRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxRegionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxRegionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblTaxRegion in the database
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblTaxRegion() throws Exception {
        // Initialize the database
        tblTaxRegionRepository.saveAndFlush(tblTaxRegion);

        int databaseSizeBeforeDelete = tblTaxRegionRepository.findAll().size();

        // Delete the tblTaxRegion
        restTblTaxRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblTaxRegion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblTaxRegion> tblTaxRegionList = tblTaxRegionRepository.findAll();
        assertThat(tblTaxRegionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
