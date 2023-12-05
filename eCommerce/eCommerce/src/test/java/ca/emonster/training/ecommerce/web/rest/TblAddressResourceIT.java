package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblAddress;
import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.repository.TblAddressRepository;
import ca.emonster.training.ecommerce.service.criteria.TblAddressCriteria;
import ca.emonster.training.ecommerce.service.dto.TblAddressDTO;
import ca.emonster.training.ecommerce.service.mapper.TblAddressMapper;
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
 * Integration tests for the {@link TblAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblAddressResourceIT {

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblAddressRepository tblAddressRepository;

    @Autowired
    private TblAddressMapper tblAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblAddressMockMvc;

    private TblAddress tblAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblAddress createEntity(EntityManager em) {
        TblAddress tblAddress = new TblAddress()
            .unit(DEFAULT_UNIT)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY)
            .isActive(DEFAULT_IS_ACTIVE)
            .remark(DEFAULT_REMARK);
        return tblAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblAddress createUpdatedEntity(EntityManager em) {
        TblAddress tblAddress = new TblAddress()
            .unit(UPDATED_UNIT)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);
        return tblAddress;
    }

    @BeforeEach
    public void initTest() {
        tblAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createTblAddress() throws Exception {
        int databaseSizeBeforeCreate = tblAddressRepository.findAll().size();
        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);
        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeCreate + 1);
        TblAddress testTblAddress = tblAddressList.get(tblAddressList.size() - 1);
        assertThat(testTblAddress.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testTblAddress.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testTblAddress.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testTblAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testTblAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testTblAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testTblAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testTblAddress.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTblAddress.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void createTblAddressWithExistingId() throws Exception {
        // Create the TblAddress with an existing ID
        tblAddress.setId(1L);
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        int databaseSizeBeforeCreate = tblAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = tblAddressRepository.findAll().size();
        // set the field null
        tblAddress.setAddress1(null);

        // Create the TblAddress, which fails.
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblAddressRepository.findAll().size();
        // set the field null
        tblAddress.setCity(null);

        // Create the TblAddress, which fails.
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblAddressRepository.findAll().size();
        // set the field null
        tblAddress.setState(null);

        // Create the TblAddress, which fails.
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblAddressRepository.findAll().size();
        // set the field null
        tblAddress.setPostalCode(null);

        // Create the TblAddress, which fails.
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblAddressRepository.findAll().size();
        // set the field null
        tblAddress.setCountry(null);

        // Create the TblAddress, which fails.
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = tblAddressRepository.findAll().size();
        // set the field null
        tblAddress.setIsActive(null);

        // Create the TblAddress, which fails.
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        restTblAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isBadRequest());

        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTblAddresses() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList
        restTblAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    void getTblAddress() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get the tblAddress
        restTblAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, tblAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblAddress.getId().intValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    void getTblAddressesByIdFiltering() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        Long id = tblAddress.getId();

        defaultTblAddressShouldBeFound("id.equals=" + id);
        defaultTblAddressShouldNotBeFound("id.notEquals=" + id);

        defaultTblAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultTblAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblAddressesByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where unit equals to DEFAULT_UNIT
        defaultTblAddressShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the tblAddressList where unit equals to UPDATED_UNIT
        defaultTblAddressShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllTblAddressesByUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where unit not equals to DEFAULT_UNIT
        defaultTblAddressShouldNotBeFound("unit.notEquals=" + DEFAULT_UNIT);

        // Get all the tblAddressList where unit not equals to UPDATED_UNIT
        defaultTblAddressShouldBeFound("unit.notEquals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllTblAddressesByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultTblAddressShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the tblAddressList where unit equals to UPDATED_UNIT
        defaultTblAddressShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllTblAddressesByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where unit is not null
        defaultTblAddressShouldBeFound("unit.specified=true");

        // Get all the tblAddressList where unit is null
        defaultTblAddressShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByUnitContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where unit contains DEFAULT_UNIT
        defaultTblAddressShouldBeFound("unit.contains=" + DEFAULT_UNIT);

        // Get all the tblAddressList where unit contains UPDATED_UNIT
        defaultTblAddressShouldNotBeFound("unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllTblAddressesByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where unit does not contain DEFAULT_UNIT
        defaultTblAddressShouldNotBeFound("unit.doesNotContain=" + DEFAULT_UNIT);

        // Get all the tblAddressList where unit does not contain UPDATED_UNIT
        defaultTblAddressShouldBeFound("unit.doesNotContain=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress1IsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address1 equals to DEFAULT_ADDRESS_1
        defaultTblAddressShouldBeFound("address1.equals=" + DEFAULT_ADDRESS_1);

        // Get all the tblAddressList where address1 equals to UPDATED_ADDRESS_1
        defaultTblAddressShouldNotBeFound("address1.equals=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address1 not equals to DEFAULT_ADDRESS_1
        defaultTblAddressShouldNotBeFound("address1.notEquals=" + DEFAULT_ADDRESS_1);

        // Get all the tblAddressList where address1 not equals to UPDATED_ADDRESS_1
        defaultTblAddressShouldBeFound("address1.notEquals=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress1IsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address1 in DEFAULT_ADDRESS_1 or UPDATED_ADDRESS_1
        defaultTblAddressShouldBeFound("address1.in=" + DEFAULT_ADDRESS_1 + "," + UPDATED_ADDRESS_1);

        // Get all the tblAddressList where address1 equals to UPDATED_ADDRESS_1
        defaultTblAddressShouldNotBeFound("address1.in=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress1IsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address1 is not null
        defaultTblAddressShouldBeFound("address1.specified=true");

        // Get all the tblAddressList where address1 is null
        defaultTblAddressShouldNotBeFound("address1.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress1ContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address1 contains DEFAULT_ADDRESS_1
        defaultTblAddressShouldBeFound("address1.contains=" + DEFAULT_ADDRESS_1);

        // Get all the tblAddressList where address1 contains UPDATED_ADDRESS_1
        defaultTblAddressShouldNotBeFound("address1.contains=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress1NotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address1 does not contain DEFAULT_ADDRESS_1
        defaultTblAddressShouldNotBeFound("address1.doesNotContain=" + DEFAULT_ADDRESS_1);

        // Get all the tblAddressList where address1 does not contain UPDATED_ADDRESS_1
        defaultTblAddressShouldBeFound("address1.doesNotContain=" + UPDATED_ADDRESS_1);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress2IsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address2 equals to DEFAULT_ADDRESS_2
        defaultTblAddressShouldBeFound("address2.equals=" + DEFAULT_ADDRESS_2);

        // Get all the tblAddressList where address2 equals to UPDATED_ADDRESS_2
        defaultTblAddressShouldNotBeFound("address2.equals=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address2 not equals to DEFAULT_ADDRESS_2
        defaultTblAddressShouldNotBeFound("address2.notEquals=" + DEFAULT_ADDRESS_2);

        // Get all the tblAddressList where address2 not equals to UPDATED_ADDRESS_2
        defaultTblAddressShouldBeFound("address2.notEquals=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress2IsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address2 in DEFAULT_ADDRESS_2 or UPDATED_ADDRESS_2
        defaultTblAddressShouldBeFound("address2.in=" + DEFAULT_ADDRESS_2 + "," + UPDATED_ADDRESS_2);

        // Get all the tblAddressList where address2 equals to UPDATED_ADDRESS_2
        defaultTblAddressShouldNotBeFound("address2.in=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress2IsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address2 is not null
        defaultTblAddressShouldBeFound("address2.specified=true");

        // Get all the tblAddressList where address2 is null
        defaultTblAddressShouldNotBeFound("address2.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress2ContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address2 contains DEFAULT_ADDRESS_2
        defaultTblAddressShouldBeFound("address2.contains=" + DEFAULT_ADDRESS_2);

        // Get all the tblAddressList where address2 contains UPDATED_ADDRESS_2
        defaultTblAddressShouldNotBeFound("address2.contains=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllTblAddressesByAddress2NotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where address2 does not contain DEFAULT_ADDRESS_2
        defaultTblAddressShouldNotBeFound("address2.doesNotContain=" + DEFAULT_ADDRESS_2);

        // Get all the tblAddressList where address2 does not contain UPDATED_ADDRESS_2
        defaultTblAddressShouldBeFound("address2.doesNotContain=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where city equals to DEFAULT_CITY
        defaultTblAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the tblAddressList where city equals to UPDATED_CITY
        defaultTblAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where city not equals to DEFAULT_CITY
        defaultTblAddressShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the tblAddressList where city not equals to UPDATED_CITY
        defaultTblAddressShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultTblAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the tblAddressList where city equals to UPDATED_CITY
        defaultTblAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where city is not null
        defaultTblAddressShouldBeFound("city.specified=true");

        // Get all the tblAddressList where city is null
        defaultTblAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where city contains DEFAULT_CITY
        defaultTblAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the tblAddressList where city contains UPDATED_CITY
        defaultTblAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where city does not contain DEFAULT_CITY
        defaultTblAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the tblAddressList where city does not contain UPDATED_CITY
        defaultTblAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where state equals to DEFAULT_STATE
        defaultTblAddressShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the tblAddressList where state equals to UPDATED_STATE
        defaultTblAddressShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where state not equals to DEFAULT_STATE
        defaultTblAddressShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the tblAddressList where state not equals to UPDATED_STATE
        defaultTblAddressShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where state in DEFAULT_STATE or UPDATED_STATE
        defaultTblAddressShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the tblAddressList where state equals to UPDATED_STATE
        defaultTblAddressShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where state is not null
        defaultTblAddressShouldBeFound("state.specified=true");

        // Get all the tblAddressList where state is null
        defaultTblAddressShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByStateContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where state contains DEFAULT_STATE
        defaultTblAddressShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the tblAddressList where state contains UPDATED_STATE
        defaultTblAddressShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where state does not contain DEFAULT_STATE
        defaultTblAddressShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the tblAddressList where state does not contain UPDATED_STATE
        defaultTblAddressShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultTblAddressShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the tblAddressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultTblAddressShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultTblAddressShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the tblAddressList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultTblAddressShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultTblAddressShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the tblAddressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultTblAddressShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where postalCode is not null
        defaultTblAddressShouldBeFound("postalCode.specified=true");

        // Get all the tblAddressList where postalCode is null
        defaultTblAddressShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where postalCode contains DEFAULT_POSTAL_CODE
        defaultTblAddressShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the tblAddressList where postalCode contains UPDATED_POSTAL_CODE
        defaultTblAddressShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultTblAddressShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the tblAddressList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultTblAddressShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where country equals to DEFAULT_COUNTRY
        defaultTblAddressShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the tblAddressList where country equals to UPDATED_COUNTRY
        defaultTblAddressShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where country not equals to DEFAULT_COUNTRY
        defaultTblAddressShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the tblAddressList where country not equals to UPDATED_COUNTRY
        defaultTblAddressShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultTblAddressShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the tblAddressList where country equals to UPDATED_COUNTRY
        defaultTblAddressShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where country is not null
        defaultTblAddressShouldBeFound("country.specified=true");

        // Get all the tblAddressList where country is null
        defaultTblAddressShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByCountryContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where country contains DEFAULT_COUNTRY
        defaultTblAddressShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the tblAddressList where country contains UPDATED_COUNTRY
        defaultTblAddressShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where country does not contain DEFAULT_COUNTRY
        defaultTblAddressShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the tblAddressList where country does not contain UPDATED_COUNTRY
        defaultTblAddressShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllTblAddressesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTblAddressShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblAddressList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblAddressShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultTblAddressShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblAddressList where isActive not equals to UPDATED_IS_ACTIVE
        defaultTblAddressShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTblAddressShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the tblAddressList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblAddressShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblAddressesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        // Get all the tblAddressList where isActive is not null
        defaultTblAddressShouldBeFound("isActive.specified=true");

        // Get all the tblAddressList where isActive is null
        defaultTblAddressShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTblAddressesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);
        TblContact contact;
        if (TestUtil.findAll(em, TblContact.class).isEmpty()) {
            contact = TblContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, TblContact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        tblAddress.setContact(contact);
        tblAddressRepository.saveAndFlush(tblAddress);
        Long contactId = contact.getId();

        // Get all the tblAddressList where contact equals to contactId
        defaultTblAddressShouldBeFound("contactId.equals=" + contactId);

        // Get all the tblAddressList where contact equals to (contactId + 1)
        defaultTblAddressShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblAddressShouldBeFound(String filter) throws Exception {
        restTblAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));

        // Check, that the count call also returns 1
        restTblAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblAddressShouldNotBeFound(String filter) throws Exception {
        restTblAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblAddress() throws Exception {
        // Get the tblAddress
        restTblAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblAddress() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();

        // Update the tblAddress
        TblAddress updatedTblAddress = tblAddressRepository.findById(tblAddress.getId()).get();
        // Disconnect from session so that the updates on updatedTblAddress are not directly saved in db
        em.detach(updatedTblAddress);
        updatedTblAddress
            .unit(UPDATED_UNIT)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(updatedTblAddress);

        restTblAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
        TblAddress testTblAddress = tblAddressList.get(tblAddressList.size() - 1);
        assertThat(testTblAddress.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testTblAddress.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testTblAddress.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testTblAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTblAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTblAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testTblAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTblAddress.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblAddress.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void putNonExistingTblAddress() throws Exception {
        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();
        tblAddress.setId(count.incrementAndGet());

        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblAddress() throws Exception {
        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();
        tblAddress.setId(count.incrementAndGet());

        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblAddress() throws Exception {
        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();
        tblAddress.setId(count.incrementAndGet());

        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblAddressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblAddressWithPatch() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();

        // Update the tblAddress using partial update
        TblAddress partialUpdatedTblAddress = new TblAddress();
        partialUpdatedTblAddress.setId(tblAddress.getId());

        partialUpdatedTblAddress
            .unit(UPDATED_UNIT)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);

        restTblAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblAddress))
            )
            .andExpect(status().isOk());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
        TblAddress testTblAddress = tblAddressList.get(tblAddressList.size() - 1);
        assertThat(testTblAddress.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testTblAddress.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testTblAddress.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testTblAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTblAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testTblAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testTblAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTblAddress.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblAddress.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void fullUpdateTblAddressWithPatch() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();

        // Update the tblAddress using partial update
        TblAddress partialUpdatedTblAddress = new TblAddress();
        partialUpdatedTblAddress.setId(tblAddress.getId());

        partialUpdatedTblAddress
            .unit(UPDATED_UNIT)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);

        restTblAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblAddress))
            )
            .andExpect(status().isOk());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
        TblAddress testTblAddress = tblAddressList.get(tblAddressList.size() - 1);
        assertThat(testTblAddress.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testTblAddress.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testTblAddress.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testTblAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testTblAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTblAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testTblAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testTblAddress.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblAddress.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void patchNonExistingTblAddress() throws Exception {
        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();
        tblAddress.setId(count.incrementAndGet());

        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblAddress() throws Exception {
        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();
        tblAddress.setId(count.incrementAndGet());

        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblAddress() throws Exception {
        int databaseSizeBeforeUpdate = tblAddressRepository.findAll().size();
        tblAddress.setId(count.incrementAndGet());

        // Create the TblAddress
        TblAddressDTO tblAddressDTO = tblAddressMapper.toDto(tblAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblAddress in the database
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblAddress() throws Exception {
        // Initialize the database
        tblAddressRepository.saveAndFlush(tblAddress);

        int databaseSizeBeforeDelete = tblAddressRepository.findAll().size();

        // Delete the tblAddress
        restTblAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblAddress> tblAddressList = tblAddressRepository.findAll();
        assertThat(tblAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
