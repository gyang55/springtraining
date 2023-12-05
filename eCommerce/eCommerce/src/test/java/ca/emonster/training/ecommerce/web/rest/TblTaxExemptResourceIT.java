package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import ca.emonster.training.ecommerce.domain.TblTaxRegion;
import ca.emonster.training.ecommerce.repository.TblTaxExemptRepository;
import ca.emonster.training.ecommerce.service.criteria.TblTaxExemptCriteria;
import ca.emonster.training.ecommerce.service.dto.TblTaxExemptDTO;
import ca.emonster.training.ecommerce.service.mapper.TblTaxExemptMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TblTaxExemptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblTaxExemptResourceIT {

    private static final String DEFAULT_EXEMPT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_EXEMPT_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_EFFECTIVE_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EFFECTIVE_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tbl-tax-exempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblTaxExemptRepository tblTaxExemptRepository;

    @Autowired
    private TblTaxExemptMapper tblTaxExemptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblTaxExemptMockMvc;

    private TblTaxExempt tblTaxExempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblTaxExempt createEntity(EntityManager em) {
        TblTaxExempt tblTaxExempt = new TblTaxExempt()
            .exemptNumber(DEFAULT_EXEMPT_NUMBER)
            .effectiveStartDate(DEFAULT_EFFECTIVE_START_DATE)
            .effectiveEndDate(DEFAULT_EFFECTIVE_END_DATE);
        return tblTaxExempt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblTaxExempt createUpdatedEntity(EntityManager em) {
        TblTaxExempt tblTaxExempt = new TblTaxExempt()
            .exemptNumber(UPDATED_EXEMPT_NUMBER)
            .effectiveStartDate(UPDATED_EFFECTIVE_START_DATE)
            .effectiveEndDate(UPDATED_EFFECTIVE_END_DATE);
        return tblTaxExempt;
    }

    @BeforeEach
    public void initTest() {
        tblTaxExempt = createEntity(em);
    }

    @Test
    @Transactional
    void createTblTaxExempt() throws Exception {
        int databaseSizeBeforeCreate = tblTaxExemptRepository.findAll().size();
        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);
        restTblTaxExemptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeCreate + 1);
        TblTaxExempt testTblTaxExempt = tblTaxExemptList.get(tblTaxExemptList.size() - 1);
        assertThat(testTblTaxExempt.getExemptNumber()).isEqualTo(DEFAULT_EXEMPT_NUMBER);
        assertThat(testTblTaxExempt.getEffectiveStartDate()).isEqualTo(DEFAULT_EFFECTIVE_START_DATE);
        assertThat(testTblTaxExempt.getEffectiveEndDate()).isEqualTo(DEFAULT_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void createTblTaxExemptWithExistingId() throws Exception {
        // Create the TblTaxExempt with an existing ID
        tblTaxExempt.setId(1L);
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        int databaseSizeBeforeCreate = tblTaxExemptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblTaxExemptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEffectiveStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblTaxExemptRepository.findAll().size();
        // set the field null
        tblTaxExempt.setEffectiveStartDate(null);

        // Create the TblTaxExempt, which fails.
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        restTblTaxExemptMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTblTaxExempts() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList
        restTblTaxExemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblTaxExempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].exemptNumber").value(hasItem(DEFAULT_EXEMPT_NUMBER)))
            .andExpect(jsonPath("$.[*].effectiveStartDate").value(hasItem(DEFAULT_EFFECTIVE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].effectiveEndDate").value(hasItem(DEFAULT_EFFECTIVE_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getTblTaxExempt() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get the tblTaxExempt
        restTblTaxExemptMockMvc
            .perform(get(ENTITY_API_URL_ID, tblTaxExempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblTaxExempt.getId().intValue()))
            .andExpect(jsonPath("$.exemptNumber").value(DEFAULT_EXEMPT_NUMBER))
            .andExpect(jsonPath("$.effectiveStartDate").value(DEFAULT_EFFECTIVE_START_DATE.toString()))
            .andExpect(jsonPath("$.effectiveEndDate").value(DEFAULT_EFFECTIVE_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getTblTaxExemptsByIdFiltering() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        Long id = tblTaxExempt.getId();

        defaultTblTaxExemptShouldBeFound("id.equals=" + id);
        defaultTblTaxExemptShouldNotBeFound("id.notEquals=" + id);

        defaultTblTaxExemptShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblTaxExemptShouldNotBeFound("id.greaterThan=" + id);

        defaultTblTaxExemptShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblTaxExemptShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByExemptNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where exemptNumber equals to DEFAULT_EXEMPT_NUMBER
        defaultTblTaxExemptShouldBeFound("exemptNumber.equals=" + DEFAULT_EXEMPT_NUMBER);

        // Get all the tblTaxExemptList where exemptNumber equals to UPDATED_EXEMPT_NUMBER
        defaultTblTaxExemptShouldNotBeFound("exemptNumber.equals=" + UPDATED_EXEMPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByExemptNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where exemptNumber not equals to DEFAULT_EXEMPT_NUMBER
        defaultTblTaxExemptShouldNotBeFound("exemptNumber.notEquals=" + DEFAULT_EXEMPT_NUMBER);

        // Get all the tblTaxExemptList where exemptNumber not equals to UPDATED_EXEMPT_NUMBER
        defaultTblTaxExemptShouldBeFound("exemptNumber.notEquals=" + UPDATED_EXEMPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByExemptNumberIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where exemptNumber in DEFAULT_EXEMPT_NUMBER or UPDATED_EXEMPT_NUMBER
        defaultTblTaxExemptShouldBeFound("exemptNumber.in=" + DEFAULT_EXEMPT_NUMBER + "," + UPDATED_EXEMPT_NUMBER);

        // Get all the tblTaxExemptList where exemptNumber equals to UPDATED_EXEMPT_NUMBER
        defaultTblTaxExemptShouldNotBeFound("exemptNumber.in=" + UPDATED_EXEMPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByExemptNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where exemptNumber is not null
        defaultTblTaxExemptShouldBeFound("exemptNumber.specified=true");

        // Get all the tblTaxExemptList where exemptNumber is null
        defaultTblTaxExemptShouldNotBeFound("exemptNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByExemptNumberContainsSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where exemptNumber contains DEFAULT_EXEMPT_NUMBER
        defaultTblTaxExemptShouldBeFound("exemptNumber.contains=" + DEFAULT_EXEMPT_NUMBER);

        // Get all the tblTaxExemptList where exemptNumber contains UPDATED_EXEMPT_NUMBER
        defaultTblTaxExemptShouldNotBeFound("exemptNumber.contains=" + UPDATED_EXEMPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByExemptNumberNotContainsSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where exemptNumber does not contain DEFAULT_EXEMPT_NUMBER
        defaultTblTaxExemptShouldNotBeFound("exemptNumber.doesNotContain=" + DEFAULT_EXEMPT_NUMBER);

        // Get all the tblTaxExemptList where exemptNumber does not contain UPDATED_EXEMPT_NUMBER
        defaultTblTaxExemptShouldBeFound("exemptNumber.doesNotContain=" + UPDATED_EXEMPT_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveStartDate equals to DEFAULT_EFFECTIVE_START_DATE
        defaultTblTaxExemptShouldBeFound("effectiveStartDate.equals=" + DEFAULT_EFFECTIVE_START_DATE);

        // Get all the tblTaxExemptList where effectiveStartDate equals to UPDATED_EFFECTIVE_START_DATE
        defaultTblTaxExemptShouldNotBeFound("effectiveStartDate.equals=" + UPDATED_EFFECTIVE_START_DATE);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveStartDate not equals to DEFAULT_EFFECTIVE_START_DATE
        defaultTblTaxExemptShouldNotBeFound("effectiveStartDate.notEquals=" + DEFAULT_EFFECTIVE_START_DATE);

        // Get all the tblTaxExemptList where effectiveStartDate not equals to UPDATED_EFFECTIVE_START_DATE
        defaultTblTaxExemptShouldBeFound("effectiveStartDate.notEquals=" + UPDATED_EFFECTIVE_START_DATE);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveStartDate in DEFAULT_EFFECTIVE_START_DATE or UPDATED_EFFECTIVE_START_DATE
        defaultTblTaxExemptShouldBeFound("effectiveStartDate.in=" + DEFAULT_EFFECTIVE_START_DATE + "," + UPDATED_EFFECTIVE_START_DATE);

        // Get all the tblTaxExemptList where effectiveStartDate equals to UPDATED_EFFECTIVE_START_DATE
        defaultTblTaxExemptShouldNotBeFound("effectiveStartDate.in=" + UPDATED_EFFECTIVE_START_DATE);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveStartDate is not null
        defaultTblTaxExemptShouldBeFound("effectiveStartDate.specified=true");

        // Get all the tblTaxExemptList where effectiveStartDate is null
        defaultTblTaxExemptShouldNotBeFound("effectiveStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveEndDate equals to DEFAULT_EFFECTIVE_END_DATE
        defaultTblTaxExemptShouldBeFound("effectiveEndDate.equals=" + DEFAULT_EFFECTIVE_END_DATE);

        // Get all the tblTaxExemptList where effectiveEndDate equals to UPDATED_EFFECTIVE_END_DATE
        defaultTblTaxExemptShouldNotBeFound("effectiveEndDate.equals=" + UPDATED_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveEndDate not equals to DEFAULT_EFFECTIVE_END_DATE
        defaultTblTaxExemptShouldNotBeFound("effectiveEndDate.notEquals=" + DEFAULT_EFFECTIVE_END_DATE);

        // Get all the tblTaxExemptList where effectiveEndDate not equals to UPDATED_EFFECTIVE_END_DATE
        defaultTblTaxExemptShouldBeFound("effectiveEndDate.notEquals=" + UPDATED_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveEndDate in DEFAULT_EFFECTIVE_END_DATE or UPDATED_EFFECTIVE_END_DATE
        defaultTblTaxExemptShouldBeFound("effectiveEndDate.in=" + DEFAULT_EFFECTIVE_END_DATE + "," + UPDATED_EFFECTIVE_END_DATE);

        // Get all the tblTaxExemptList where effectiveEndDate equals to UPDATED_EFFECTIVE_END_DATE
        defaultTblTaxExemptShouldNotBeFound("effectiveEndDate.in=" + UPDATED_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByEffectiveEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        // Get all the tblTaxExemptList where effectiveEndDate is not null
        defaultTblTaxExemptShouldBeFound("effectiveEndDate.specified=true");

        // Get all the tblTaxExemptList where effectiveEndDate is null
        defaultTblTaxExemptShouldNotBeFound("effectiveEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);
        TblTaxRegion region;
        if (TestUtil.findAll(em, TblTaxRegion.class).isEmpty()) {
            region = TblTaxRegionResourceIT.createEntity(em);
            em.persist(region);
            em.flush();
        } else {
            region = TestUtil.findAll(em, TblTaxRegion.class).get(0);
        }
        em.persist(region);
        em.flush();
        tblTaxExempt.setRegion(region);
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);
        Long regionId = region.getId();

        // Get all the tblTaxExemptList where region equals to regionId
        defaultTblTaxExemptShouldBeFound("regionId.equals=" + regionId);

        // Get all the tblTaxExemptList where region equals to (regionId + 1)
        defaultTblTaxExemptShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    @Test
    @Transactional
    void getAllTblTaxExemptsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);
        TblCustomer customer;
        if (TestUtil.findAll(em, TblCustomer.class).isEmpty()) {
            customer = TblCustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, TblCustomer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        tblTaxExempt.setCustomer(customer);
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);
        Long customerId = customer.getId();

        // Get all the tblTaxExemptList where customer equals to customerId
        defaultTblTaxExemptShouldBeFound("customerId.equals=" + customerId);

        // Get all the tblTaxExemptList where customer equals to (customerId + 1)
        defaultTblTaxExemptShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblTaxExemptShouldBeFound(String filter) throws Exception {
        restTblTaxExemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblTaxExempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].exemptNumber").value(hasItem(DEFAULT_EXEMPT_NUMBER)))
            .andExpect(jsonPath("$.[*].effectiveStartDate").value(hasItem(DEFAULT_EFFECTIVE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].effectiveEndDate").value(hasItem(DEFAULT_EFFECTIVE_END_DATE.toString())));

        // Check, that the count call also returns 1
        restTblTaxExemptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblTaxExemptShouldNotBeFound(String filter) throws Exception {
        restTblTaxExemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblTaxExemptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblTaxExempt() throws Exception {
        // Get the tblTaxExempt
        restTblTaxExemptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblTaxExempt() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();

        // Update the tblTaxExempt
        TblTaxExempt updatedTblTaxExempt = tblTaxExemptRepository.findById(tblTaxExempt.getId()).get();
        // Disconnect from session so that the updates on updatedTblTaxExempt are not directly saved in db
        em.detach(updatedTblTaxExempt);
        updatedTblTaxExempt
            .exemptNumber(UPDATED_EXEMPT_NUMBER)
            .effectiveStartDate(UPDATED_EFFECTIVE_START_DATE)
            .effectiveEndDate(UPDATED_EFFECTIVE_END_DATE);
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(updatedTblTaxExempt);

        restTblTaxExemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblTaxExemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
        TblTaxExempt testTblTaxExempt = tblTaxExemptList.get(tblTaxExemptList.size() - 1);
        assertThat(testTblTaxExempt.getExemptNumber()).isEqualTo(UPDATED_EXEMPT_NUMBER);
        assertThat(testTblTaxExempt.getEffectiveStartDate()).isEqualTo(UPDATED_EFFECTIVE_START_DATE);
        assertThat(testTblTaxExempt.getEffectiveEndDate()).isEqualTo(UPDATED_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTblTaxExempt() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();
        tblTaxExempt.setId(count.incrementAndGet());

        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblTaxExemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblTaxExemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblTaxExempt() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();
        tblTaxExempt.setId(count.incrementAndGet());

        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxExemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblTaxExempt() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();
        tblTaxExempt.setId(count.incrementAndGet());

        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxExemptMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblTaxExemptWithPatch() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();

        // Update the tblTaxExempt using partial update
        TblTaxExempt partialUpdatedTblTaxExempt = new TblTaxExempt();
        partialUpdatedTblTaxExempt.setId(tblTaxExempt.getId());

        partialUpdatedTblTaxExempt.effectiveStartDate(UPDATED_EFFECTIVE_START_DATE).effectiveEndDate(UPDATED_EFFECTIVE_END_DATE);

        restTblTaxExemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblTaxExempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblTaxExempt))
            )
            .andExpect(status().isOk());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
        TblTaxExempt testTblTaxExempt = tblTaxExemptList.get(tblTaxExemptList.size() - 1);
        assertThat(testTblTaxExempt.getExemptNumber()).isEqualTo(DEFAULT_EXEMPT_NUMBER);
        assertThat(testTblTaxExempt.getEffectiveStartDate()).isEqualTo(UPDATED_EFFECTIVE_START_DATE);
        assertThat(testTblTaxExempt.getEffectiveEndDate()).isEqualTo(UPDATED_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTblTaxExemptWithPatch() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();

        // Update the tblTaxExempt using partial update
        TblTaxExempt partialUpdatedTblTaxExempt = new TblTaxExempt();
        partialUpdatedTblTaxExempt.setId(tblTaxExempt.getId());

        partialUpdatedTblTaxExempt
            .exemptNumber(UPDATED_EXEMPT_NUMBER)
            .effectiveStartDate(UPDATED_EFFECTIVE_START_DATE)
            .effectiveEndDate(UPDATED_EFFECTIVE_END_DATE);

        restTblTaxExemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblTaxExempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblTaxExempt))
            )
            .andExpect(status().isOk());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
        TblTaxExempt testTblTaxExempt = tblTaxExemptList.get(tblTaxExemptList.size() - 1);
        assertThat(testTblTaxExempt.getExemptNumber()).isEqualTo(UPDATED_EXEMPT_NUMBER);
        assertThat(testTblTaxExempt.getEffectiveStartDate()).isEqualTo(UPDATED_EFFECTIVE_START_DATE);
        assertThat(testTblTaxExempt.getEffectiveEndDate()).isEqualTo(UPDATED_EFFECTIVE_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTblTaxExempt() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();
        tblTaxExempt.setId(count.incrementAndGet());

        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblTaxExemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblTaxExemptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblTaxExempt() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();
        tblTaxExempt.setId(count.incrementAndGet());

        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxExemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblTaxExempt() throws Exception {
        int databaseSizeBeforeUpdate = tblTaxExemptRepository.findAll().size();
        tblTaxExempt.setId(count.incrementAndGet());

        // Create the TblTaxExempt
        TblTaxExemptDTO tblTaxExemptDTO = tblTaxExemptMapper.toDto(tblTaxExempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblTaxExemptMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblTaxExemptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblTaxExempt in the database
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblTaxExempt() throws Exception {
        // Initialize the database
        tblTaxExemptRepository.saveAndFlush(tblTaxExempt);

        int databaseSizeBeforeDelete = tblTaxExemptRepository.findAll().size();

        // Delete the tblTaxExempt
        restTblTaxExemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblTaxExempt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblTaxExempt> tblTaxExemptList = tblTaxExemptRepository.findAll();
        assertThat(tblTaxExemptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
