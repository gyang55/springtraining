package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblDimension;
import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.repository.TblDimensionRepository;
import ca.emonster.training.ecommerce.service.criteria.TblDimensionCriteria;
import ca.emonster.training.ecommerce.service.dto.TblDimensionDTO;
import ca.emonster.training.ecommerce.service.mapper.TblDimensionMapper;
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
 * Integration tests for the {@link TblDimensionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblDimensionResourceIT {

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;
    private static final Double SMALLER_LENGTH = 1D - 1D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;
    private static final Double SMALLER_HEIGHT = 1D - 1D;

    private static final Double DEFAULT_WIDTH = 1D;
    private static final Double UPDATED_WIDTH = 2D;
    private static final Double SMALLER_WIDTH = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/tbl-dimensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblDimensionRepository tblDimensionRepository;

    @Autowired
    private TblDimensionMapper tblDimensionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblDimensionMockMvc;

    private TblDimension tblDimension;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblDimension createEntity(EntityManager em) {
        TblDimension tblDimension = new TblDimension().length(DEFAULT_LENGTH).height(DEFAULT_HEIGHT).width(DEFAULT_WIDTH);
        return tblDimension;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblDimension createUpdatedEntity(EntityManager em) {
        TblDimension tblDimension = new TblDimension().length(UPDATED_LENGTH).height(UPDATED_HEIGHT).width(UPDATED_WIDTH);
        return tblDimension;
    }

    @BeforeEach
    public void initTest() {
        tblDimension = createEntity(em);
    }

    @Test
    @Transactional
    void createTblDimension() throws Exception {
        int databaseSizeBeforeCreate = tblDimensionRepository.findAll().size();
        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);
        restTblDimensionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeCreate + 1);
        TblDimension testTblDimension = tblDimensionList.get(tblDimensionList.size() - 1);
        assertThat(testTblDimension.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testTblDimension.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testTblDimension.getWidth()).isEqualTo(DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void createTblDimensionWithExistingId() throws Exception {
        // Create the TblDimension with an existing ID
        tblDimension.setId(1L);
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        int databaseSizeBeforeCreate = tblDimensionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblDimensionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblDimensionRepository.findAll().size();
        // set the field null
        tblDimension.setLength(null);

        // Create the TblDimension, which fails.
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        restTblDimensionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblDimensionRepository.findAll().size();
        // set the field null
        tblDimension.setHeight(null);

        // Create the TblDimension, which fails.
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        restTblDimensionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblDimensionRepository.findAll().size();
        // set the field null
        tblDimension.setWidth(null);

        // Create the TblDimension, which fails.
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        restTblDimensionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTblDimensions() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList
        restTblDimensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblDimension.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())));
    }

    @Test
    @Transactional
    void getTblDimension() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get the tblDimension
        restTblDimensionMockMvc
            .perform(get(ENTITY_API_URL_ID, tblDimension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblDimension.getId().intValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()));
    }

    @Test
    @Transactional
    void getTblDimensionsByIdFiltering() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        Long id = tblDimension.getId();

        defaultTblDimensionShouldBeFound("id.equals=" + id);
        defaultTblDimensionShouldNotBeFound("id.notEquals=" + id);

        defaultTblDimensionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblDimensionShouldNotBeFound("id.greaterThan=" + id);

        defaultTblDimensionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblDimensionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length equals to DEFAULT_LENGTH
        defaultTblDimensionShouldBeFound("length.equals=" + DEFAULT_LENGTH);

        // Get all the tblDimensionList where length equals to UPDATED_LENGTH
        defaultTblDimensionShouldNotBeFound("length.equals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length not equals to DEFAULT_LENGTH
        defaultTblDimensionShouldNotBeFound("length.notEquals=" + DEFAULT_LENGTH);

        // Get all the tblDimensionList where length not equals to UPDATED_LENGTH
        defaultTblDimensionShouldBeFound("length.notEquals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsInShouldWork() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length in DEFAULT_LENGTH or UPDATED_LENGTH
        defaultTblDimensionShouldBeFound("length.in=" + DEFAULT_LENGTH + "," + UPDATED_LENGTH);

        // Get all the tblDimensionList where length equals to UPDATED_LENGTH
        defaultTblDimensionShouldNotBeFound("length.in=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length is not null
        defaultTblDimensionShouldBeFound("length.specified=true");

        // Get all the tblDimensionList where length is null
        defaultTblDimensionShouldNotBeFound("length.specified=false");
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length is greater than or equal to DEFAULT_LENGTH
        defaultTblDimensionShouldBeFound("length.greaterThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the tblDimensionList where length is greater than or equal to UPDATED_LENGTH
        defaultTblDimensionShouldNotBeFound("length.greaterThanOrEqual=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length is less than or equal to DEFAULT_LENGTH
        defaultTblDimensionShouldBeFound("length.lessThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the tblDimensionList where length is less than or equal to SMALLER_LENGTH
        defaultTblDimensionShouldNotBeFound("length.lessThanOrEqual=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length is less than DEFAULT_LENGTH
        defaultTblDimensionShouldNotBeFound("length.lessThan=" + DEFAULT_LENGTH);

        // Get all the tblDimensionList where length is less than UPDATED_LENGTH
        defaultTblDimensionShouldBeFound("length.lessThan=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where length is greater than DEFAULT_LENGTH
        defaultTblDimensionShouldNotBeFound("length.greaterThan=" + DEFAULT_LENGTH);

        // Get all the tblDimensionList where length is greater than SMALLER_LENGTH
        defaultTblDimensionShouldBeFound("length.greaterThan=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height equals to DEFAULT_HEIGHT
        defaultTblDimensionShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the tblDimensionList where height equals to UPDATED_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height not equals to DEFAULT_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.notEquals=" + DEFAULT_HEIGHT);

        // Get all the tblDimensionList where height not equals to UPDATED_HEIGHT
        defaultTblDimensionShouldBeFound("height.notEquals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultTblDimensionShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the tblDimensionList where height equals to UPDATED_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height is not null
        defaultTblDimensionShouldBeFound("height.specified=true");

        // Get all the tblDimensionList where height is null
        defaultTblDimensionShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height is greater than or equal to DEFAULT_HEIGHT
        defaultTblDimensionShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the tblDimensionList where height is greater than or equal to UPDATED_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height is less than or equal to DEFAULT_HEIGHT
        defaultTblDimensionShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the tblDimensionList where height is less than or equal to SMALLER_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height is less than DEFAULT_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the tblDimensionList where height is less than UPDATED_HEIGHT
        defaultTblDimensionShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where height is greater than DEFAULT_HEIGHT
        defaultTblDimensionShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the tblDimensionList where height is greater than SMALLER_HEIGHT
        defaultTblDimensionShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width equals to DEFAULT_WIDTH
        defaultTblDimensionShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the tblDimensionList where width equals to UPDATED_WIDTH
        defaultTblDimensionShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width not equals to DEFAULT_WIDTH
        defaultTblDimensionShouldNotBeFound("width.notEquals=" + DEFAULT_WIDTH);

        // Get all the tblDimensionList where width not equals to UPDATED_WIDTH
        defaultTblDimensionShouldBeFound("width.notEquals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultTblDimensionShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the tblDimensionList where width equals to UPDATED_WIDTH
        defaultTblDimensionShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width is not null
        defaultTblDimensionShouldBeFound("width.specified=true");

        // Get all the tblDimensionList where width is null
        defaultTblDimensionShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width is greater than or equal to DEFAULT_WIDTH
        defaultTblDimensionShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the tblDimensionList where width is greater than or equal to UPDATED_WIDTH
        defaultTblDimensionShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width is less than or equal to DEFAULT_WIDTH
        defaultTblDimensionShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the tblDimensionList where width is less than or equal to SMALLER_WIDTH
        defaultTblDimensionShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width is less than DEFAULT_WIDTH
        defaultTblDimensionShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the tblDimensionList where width is less than UPDATED_WIDTH
        defaultTblDimensionShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        // Get all the tblDimensionList where width is greater than DEFAULT_WIDTH
        defaultTblDimensionShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the tblDimensionList where width is greater than SMALLER_WIDTH
        defaultTblDimensionShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllTblDimensionsByInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);
        TblInventory inventory;
        if (TestUtil.findAll(em, TblInventory.class).isEmpty()) {
            inventory = TblInventoryResourceIT.createEntity(em);
            em.persist(inventory);
            em.flush();
        } else {
            inventory = TestUtil.findAll(em, TblInventory.class).get(0);
        }
        em.persist(inventory);
        em.flush();
        tblDimension.setInventory(inventory);
        inventory.setDimension(tblDimension);
        tblDimensionRepository.saveAndFlush(tblDimension);
        Long inventoryId = inventory.getId();

        // Get all the tblDimensionList where inventory equals to inventoryId
        defaultTblDimensionShouldBeFound("inventoryId.equals=" + inventoryId);

        // Get all the tblDimensionList where inventory equals to (inventoryId + 1)
        defaultTblDimensionShouldNotBeFound("inventoryId.equals=" + (inventoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblDimensionShouldBeFound(String filter) throws Exception {
        restTblDimensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblDimension.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())));

        // Check, that the count call also returns 1
        restTblDimensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblDimensionShouldNotBeFound(String filter) throws Exception {
        restTblDimensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblDimensionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblDimension() throws Exception {
        // Get the tblDimension
        restTblDimensionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblDimension() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();

        // Update the tblDimension
        TblDimension updatedTblDimension = tblDimensionRepository.findById(tblDimension.getId()).get();
        // Disconnect from session so that the updates on updatedTblDimension are not directly saved in db
        em.detach(updatedTblDimension);
        updatedTblDimension.length(UPDATED_LENGTH).height(UPDATED_HEIGHT).width(UPDATED_WIDTH);
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(updatedTblDimension);

        restTblDimensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblDimensionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
        TblDimension testTblDimension = tblDimensionList.get(tblDimensionList.size() - 1);
        assertThat(testTblDimension.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testTblDimension.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testTblDimension.getWidth()).isEqualTo(UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void putNonExistingTblDimension() throws Exception {
        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();
        tblDimension.setId(count.incrementAndGet());

        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblDimensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblDimensionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblDimension() throws Exception {
        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();
        tblDimension.setId(count.incrementAndGet());

        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblDimensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblDimension() throws Exception {
        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();
        tblDimension.setId(count.incrementAndGet());

        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblDimensionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblDimensionWithPatch() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();

        // Update the tblDimension using partial update
        TblDimension partialUpdatedTblDimension = new TblDimension();
        partialUpdatedTblDimension.setId(tblDimension.getId());

        partialUpdatedTblDimension.height(UPDATED_HEIGHT).width(UPDATED_WIDTH);

        restTblDimensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblDimension.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblDimension))
            )
            .andExpect(status().isOk());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
        TblDimension testTblDimension = tblDimensionList.get(tblDimensionList.size() - 1);
        assertThat(testTblDimension.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testTblDimension.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testTblDimension.getWidth()).isEqualTo(UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void fullUpdateTblDimensionWithPatch() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();

        // Update the tblDimension using partial update
        TblDimension partialUpdatedTblDimension = new TblDimension();
        partialUpdatedTblDimension.setId(tblDimension.getId());

        partialUpdatedTblDimension.length(UPDATED_LENGTH).height(UPDATED_HEIGHT).width(UPDATED_WIDTH);

        restTblDimensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblDimension.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblDimension))
            )
            .andExpect(status().isOk());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
        TblDimension testTblDimension = tblDimensionList.get(tblDimensionList.size() - 1);
        assertThat(testTblDimension.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testTblDimension.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testTblDimension.getWidth()).isEqualTo(UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void patchNonExistingTblDimension() throws Exception {
        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();
        tblDimension.setId(count.incrementAndGet());

        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblDimensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblDimensionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblDimension() throws Exception {
        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();
        tblDimension.setId(count.incrementAndGet());

        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblDimensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblDimension() throws Exception {
        int databaseSizeBeforeUpdate = tblDimensionRepository.findAll().size();
        tblDimension.setId(count.incrementAndGet());

        // Create the TblDimension
        TblDimensionDTO tblDimensionDTO = tblDimensionMapper.toDto(tblDimension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblDimensionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblDimensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblDimension in the database
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblDimension() throws Exception {
        // Initialize the database
        tblDimensionRepository.saveAndFlush(tblDimension);

        int databaseSizeBeforeDelete = tblDimensionRepository.findAll().size();

        // Delete the tblDimension
        restTblDimensionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblDimension.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblDimension> tblDimensionList = tblDimensionRepository.findAll();
        assertThat(tblDimensionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
