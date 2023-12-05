package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblAddress;
import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.domain.enumeration.ContactType;
import ca.emonster.training.ecommerce.repository.TblContactRepository;
import ca.emonster.training.ecommerce.service.criteria.TblContactCriteria;
import ca.emonster.training.ecommerce.service.dto.TblContactDTO;
import ca.emonster.training.ecommerce.service.mapper.TblContactMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TblContactResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblContactResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ContactType DEFAULT_TYPE = ContactType.CUSTOMER;
    private static final ContactType UPDATED_TYPE = ContactType.SHIPPING;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblContactRepository tblContactRepository;

    @Autowired
    private TblContactMapper tblContactMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblContactMockMvc;

    private TblContact tblContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblContact createEntity(EntityManager em) {
        TblContact tblContact = new TblContact()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .businessName(DEFAULT_BUSINESS_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .type(DEFAULT_TYPE)
            .remark(DEFAULT_REMARK);
        return tblContact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblContact createUpdatedEntity(EntityManager em) {
        TblContact tblContact = new TblContact()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .businessName(UPDATED_BUSINESS_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE)
            .remark(UPDATED_REMARK);
        return tblContact;
    }

    @BeforeEach
    public void initTest() {
        tblContact = createEntity(em);
    }

    @Test
    @Transactional
    void createTblContact() throws Exception {
        int databaseSizeBeforeCreate = tblContactRepository.findAll().size();
        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);
        restTblContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblContactDTO)))
            .andExpect(status().isCreated());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeCreate + 1);
        TblContact testTblContact = tblContactList.get(tblContactList.size() - 1);
        assertThat(testTblContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTblContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTblContact.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testTblContact.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testTblContact.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTblContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTblContact.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTblContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTblContact.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void createTblContactWithExistingId() throws Exception {
        // Create the TblContact with an existing ID
        tblContact.setId(1L);
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        int databaseSizeBeforeCreate = tblContactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblContactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblContactRepository.findAll().size();
        // set the field null
        tblContact.setIsActive(null);

        // Create the TblContact, which fails.
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        restTblContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblContactDTO)))
            .andExpect(status().isBadRequest());

        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblContactRepository.findAll().size();
        // set the field null
        tblContact.setType(null);

        // Create the TblContact, which fails.
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        restTblContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblContactDTO)))
            .andExpect(status().isBadRequest());

        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTblContacts() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList
        restTblContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    void getTblContact() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get the tblContact
        restTblContactMockMvc
            .perform(get(ENTITY_API_URL_ID, tblContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblContact.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    void getTblContactsByIdFiltering() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        Long id = tblContact.getId();

        defaultTblContactShouldBeFound("id.equals=" + id);
        defaultTblContactShouldNotBeFound("id.notEquals=" + id);

        defaultTblContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblContactShouldNotBeFound("id.greaterThan=" + id);

        defaultTblContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblContactsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where firstName equals to DEFAULT_FIRST_NAME
        defaultTblContactShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the tblContactList where firstName equals to UPDATED_FIRST_NAME
        defaultTblContactShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTblContactShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the tblContactList where firstName not equals to UPDATED_FIRST_NAME
        defaultTblContactShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTblContactShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the tblContactList where firstName equals to UPDATED_FIRST_NAME
        defaultTblContactShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where firstName is not null
        defaultTblContactShouldBeFound("firstName.specified=true");

        // Get all the tblContactList where firstName is null
        defaultTblContactShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where firstName contains DEFAULT_FIRST_NAME
        defaultTblContactShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the tblContactList where firstName contains UPDATED_FIRST_NAME
        defaultTblContactShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTblContactShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the tblContactList where firstName does not contain UPDATED_FIRST_NAME
        defaultTblContactShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where lastName equals to DEFAULT_LAST_NAME
        defaultTblContactShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the tblContactList where lastName equals to UPDATED_LAST_NAME
        defaultTblContactShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where lastName not equals to DEFAULT_LAST_NAME
        defaultTblContactShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the tblContactList where lastName not equals to UPDATED_LAST_NAME
        defaultTblContactShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTblContactShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the tblContactList where lastName equals to UPDATED_LAST_NAME
        defaultTblContactShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where lastName is not null
        defaultTblContactShouldBeFound("lastName.specified=true");

        // Get all the tblContactList where lastName is null
        defaultTblContactShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where lastName contains DEFAULT_LAST_NAME
        defaultTblContactShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the tblContactList where lastName contains UPDATED_LAST_NAME
        defaultTblContactShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where lastName does not contain DEFAULT_LAST_NAME
        defaultTblContactShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the tblContactList where lastName does not contain UPDATED_LAST_NAME
        defaultTblContactShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultTblContactShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the tblContactList where middleName equals to UPDATED_MIDDLE_NAME
        defaultTblContactShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultTblContactShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the tblContactList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultTblContactShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultTblContactShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the tblContactList where middleName equals to UPDATED_MIDDLE_NAME
        defaultTblContactShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where middleName is not null
        defaultTblContactShouldBeFound("middleName.specified=true");

        // Get all the tblContactList where middleName is null
        defaultTblContactShouldNotBeFound("middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where middleName contains DEFAULT_MIDDLE_NAME
        defaultTblContactShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the tblContactList where middleName contains UPDATED_MIDDLE_NAME
        defaultTblContactShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultTblContactShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the tblContactList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultTblContactShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByBusinessNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where businessName equals to DEFAULT_BUSINESS_NAME
        defaultTblContactShouldBeFound("businessName.equals=" + DEFAULT_BUSINESS_NAME);

        // Get all the tblContactList where businessName equals to UPDATED_BUSINESS_NAME
        defaultTblContactShouldNotBeFound("businessName.equals=" + UPDATED_BUSINESS_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByBusinessNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where businessName not equals to DEFAULT_BUSINESS_NAME
        defaultTblContactShouldNotBeFound("businessName.notEquals=" + DEFAULT_BUSINESS_NAME);

        // Get all the tblContactList where businessName not equals to UPDATED_BUSINESS_NAME
        defaultTblContactShouldBeFound("businessName.notEquals=" + UPDATED_BUSINESS_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByBusinessNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where businessName in DEFAULT_BUSINESS_NAME or UPDATED_BUSINESS_NAME
        defaultTblContactShouldBeFound("businessName.in=" + DEFAULT_BUSINESS_NAME + "," + UPDATED_BUSINESS_NAME);

        // Get all the tblContactList where businessName equals to UPDATED_BUSINESS_NAME
        defaultTblContactShouldNotBeFound("businessName.in=" + UPDATED_BUSINESS_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByBusinessNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where businessName is not null
        defaultTblContactShouldBeFound("businessName.specified=true");

        // Get all the tblContactList where businessName is null
        defaultTblContactShouldNotBeFound("businessName.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByBusinessNameContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where businessName contains DEFAULT_BUSINESS_NAME
        defaultTblContactShouldBeFound("businessName.contains=" + DEFAULT_BUSINESS_NAME);

        // Get all the tblContactList where businessName contains UPDATED_BUSINESS_NAME
        defaultTblContactShouldNotBeFound("businessName.contains=" + UPDATED_BUSINESS_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByBusinessNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where businessName does not contain DEFAULT_BUSINESS_NAME
        defaultTblContactShouldNotBeFound("businessName.doesNotContain=" + DEFAULT_BUSINESS_NAME);

        // Get all the tblContactList where businessName does not contain UPDATED_BUSINESS_NAME
        defaultTblContactShouldBeFound("businessName.doesNotContain=" + UPDATED_BUSINESS_NAME);
    }

    @Test
    @Transactional
    void getAllTblContactsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where phone equals to DEFAULT_PHONE
        defaultTblContactShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the tblContactList where phone equals to UPDATED_PHONE
        defaultTblContactShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllTblContactsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where phone not equals to DEFAULT_PHONE
        defaultTblContactShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the tblContactList where phone not equals to UPDATED_PHONE
        defaultTblContactShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllTblContactsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTblContactShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the tblContactList where phone equals to UPDATED_PHONE
        defaultTblContactShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllTblContactsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where phone is not null
        defaultTblContactShouldBeFound("phone.specified=true");

        // Get all the tblContactList where phone is null
        defaultTblContactShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where phone contains DEFAULT_PHONE
        defaultTblContactShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the tblContactList where phone contains UPDATED_PHONE
        defaultTblContactShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllTblContactsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where phone does not contain DEFAULT_PHONE
        defaultTblContactShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the tblContactList where phone does not contain UPDATED_PHONE
        defaultTblContactShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllTblContactsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where email equals to DEFAULT_EMAIL
        defaultTblContactShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the tblContactList where email equals to UPDATED_EMAIL
        defaultTblContactShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTblContactsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where email not equals to DEFAULT_EMAIL
        defaultTblContactShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the tblContactList where email not equals to UPDATED_EMAIL
        defaultTblContactShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTblContactsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTblContactShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the tblContactList where email equals to UPDATED_EMAIL
        defaultTblContactShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTblContactsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where email is not null
        defaultTblContactShouldBeFound("email.specified=true");

        // Get all the tblContactList where email is null
        defaultTblContactShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByEmailContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where email contains DEFAULT_EMAIL
        defaultTblContactShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the tblContactList where email contains UPDATED_EMAIL
        defaultTblContactShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTblContactsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where email does not contain DEFAULT_EMAIL
        defaultTblContactShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the tblContactList where email does not contain UPDATED_EMAIL
        defaultTblContactShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTblContactsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTblContactShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblContactList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblContactShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblContactsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultTblContactShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblContactList where isActive not equals to UPDATED_IS_ACTIVE
        defaultTblContactShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblContactsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTblContactShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the tblContactList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblContactShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblContactsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where isActive is not null
        defaultTblContactShouldBeFound("isActive.specified=true");

        // Get all the tblContactList where isActive is null
        defaultTblContactShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where type equals to DEFAULT_TYPE
        defaultTblContactShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the tblContactList where type equals to UPDATED_TYPE
        defaultTblContactShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTblContactsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where type not equals to DEFAULT_TYPE
        defaultTblContactShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the tblContactList where type not equals to UPDATED_TYPE
        defaultTblContactShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTblContactsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTblContactShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the tblContactList where type equals to UPDATED_TYPE
        defaultTblContactShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTblContactsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        // Get all the tblContactList where type is not null
        defaultTblContactShouldBeFound("type.specified=true");

        // Get all the tblContactList where type is null
        defaultTblContactShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTblContactsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);
        TblAddress address;
        if (TestUtil.findAll(em, TblAddress.class).isEmpty()) {
            address = TblAddressResourceIT.createEntity(em);
            em.persist(address);
            em.flush();
        } else {
            address = TestUtil.findAll(em, TblAddress.class).get(0);
        }
        em.persist(address);
        em.flush();
        tblContact.setAddress(address);
        address.setContact(tblContact);
        tblContactRepository.saveAndFlush(tblContact);
        Long addressId = address.getId();

        // Get all the tblContactList where address equals to addressId
        defaultTblContactShouldBeFound("addressId.equals=" + addressId);

        // Get all the tblContactList where address equals to (addressId + 1)
        defaultTblContactShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllTblContactsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);
        TblOrder order;
        if (TestUtil.findAll(em, TblOrder.class).isEmpty()) {
            order = TblOrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, TblOrder.class).get(0);
        }
        em.persist(order);
        em.flush();
        tblContact.setOrder(order);
        order.setShipTo(tblContact);
        tblContactRepository.saveAndFlush(tblContact);
        Long orderId = order.getId();

        // Get all the tblContactList where order equals to orderId
        defaultTblContactShouldBeFound("orderId.equals=" + orderId);

        // Get all the tblContactList where order equals to (orderId + 1)
        defaultTblContactShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllTblContactsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);
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
        tblContact.setCustomer(customer);
        tblContactRepository.saveAndFlush(tblContact);
        Long customerId = customer.getId();

        // Get all the tblContactList where customer equals to customerId
        defaultTblContactShouldBeFound("customerId.equals=" + customerId);

        // Get all the tblContactList where customer equals to (customerId + 1)
        defaultTblContactShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblContactShouldBeFound(String filter) throws Exception {
        restTblContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));

        // Check, that the count call also returns 1
        restTblContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblContactShouldNotBeFound(String filter) throws Exception {
        restTblContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblContact() throws Exception {
        // Get the tblContact
        restTblContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblContact() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();

        // Update the tblContact
        TblContact updatedTblContact = tblContactRepository.findById(tblContact.getId()).get();
        // Disconnect from session so that the updates on updatedTblContact are not directly saved in db
        em.detach(updatedTblContact);
        updatedTblContact
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .businessName(UPDATED_BUSINESS_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE)
            .remark(UPDATED_REMARK);
        TblContactDTO tblContactDTO = tblContactMapper.toDto(updatedTblContact);

        restTblContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblContactDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
        TblContact testTblContact = tblContactList.get(tblContactList.size() - 1);
        assertThat(testTblContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTblContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTblContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testTblContact.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testTblContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTblContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTblContact.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTblContact.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void putNonExistingTblContact() throws Exception {
        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();
        tblContact.setId(count.incrementAndGet());

        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblContact() throws Exception {
        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();
        tblContact.setId(count.incrementAndGet());

        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblContact() throws Exception {
        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();
        tblContact.setId(count.incrementAndGet());

        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblContactMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblContactDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblContactWithPatch() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();

        // Update the tblContact using partial update
        TblContact partialUpdatedTblContact = new TblContact();
        partialUpdatedTblContact.setId(tblContact.getId());

        partialUpdatedTblContact.firstName(UPDATED_FIRST_NAME).middleName(UPDATED_MIDDLE_NAME).phone(UPDATED_PHONE).email(UPDATED_EMAIL);

        restTblContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblContact))
            )
            .andExpect(status().isOk());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
        TblContact testTblContact = tblContactList.get(tblContactList.size() - 1);
        assertThat(testTblContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTblContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTblContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testTblContact.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testTblContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTblContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTblContact.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTblContact.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTblContact.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void fullUpdateTblContactWithPatch() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();

        // Update the tblContact using partial update
        TblContact partialUpdatedTblContact = new TblContact();
        partialUpdatedTblContact.setId(tblContact.getId());

        partialUpdatedTblContact
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .businessName(UPDATED_BUSINESS_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE)
            .remark(UPDATED_REMARK);

        restTblContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblContact))
            )
            .andExpect(status().isOk());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
        TblContact testTblContact = tblContactList.get(tblContactList.size() - 1);
        assertThat(testTblContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTblContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTblContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testTblContact.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testTblContact.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTblContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTblContact.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblContact.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTblContact.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void patchNonExistingTblContact() throws Exception {
        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();
        tblContact.setId(count.incrementAndGet());

        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblContactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblContact() throws Exception {
        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();
        tblContact.setId(count.incrementAndGet());

        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblContact() throws Exception {
        int databaseSizeBeforeUpdate = tblContactRepository.findAll().size();
        tblContact.setId(count.incrementAndGet());

        // Create the TblContact
        TblContactDTO tblContactDTO = tblContactMapper.toDto(tblContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblContactMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblContact in the database
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblContact() throws Exception {
        // Initialize the database
        tblContactRepository.saveAndFlush(tblContact);

        int databaseSizeBeforeDelete = tblContactRepository.findAll().size();

        // Delete the tblContact
        restTblContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblContact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblContact> tblContactList = tblContactRepository.findAll();
        assertThat(tblContactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
