package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblDimension;
import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.domain.TblInventoryMedia;
import ca.emonster.training.ecommerce.domain.TblProduct;
import ca.emonster.training.ecommerce.repository.TblInventoryRepository;
import ca.emonster.training.ecommerce.service.criteria.TblInventoryCriteria;
import ca.emonster.training.ecommerce.service.dto.TblInventoryDTO;
import ca.emonster.training.ecommerce.service.mapper.TblInventoryMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TblInventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblInventoryResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;
    private static final Double SMALLER_WEIGHT = 1D - 1D;

    private static final Long DEFAULT_SOTCK_LEVEL = 1L;
    private static final Long UPDATED_SOTCK_LEVEL = 2L;
    private static final Long SMALLER_SOTCK_LEVEL = 1L - 1L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblInventoryRepository tblInventoryRepository;

    @Autowired
    private TblInventoryMapper tblInventoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblInventoryMockMvc;

    private TblInventory tblInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblInventory createEntity(EntityManager em) {
        TblInventory tblInventory = new TblInventory()
            .sku(DEFAULT_SKU)
            .createTime(DEFAULT_CREATE_TIME)
            .weight(DEFAULT_WEIGHT)
            .sotckLevel(DEFAULT_SOTCK_LEVEL)
            .description(DEFAULT_DESCRIPTION)
            .remark(DEFAULT_REMARK);
        // Add required entity
        TblDimension tblDimension;
        if (TestUtil.findAll(em, TblDimension.class).isEmpty()) {
            tblDimension = TblDimensionResourceIT.createEntity(em);
            em.persist(tblDimension);
            em.flush();
        } else {
            tblDimension = TestUtil.findAll(em, TblDimension.class).get(0);
        }
        tblInventory.setDimension(tblDimension);
        return tblInventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblInventory createUpdatedEntity(EntityManager em) {
        TblInventory tblInventory = new TblInventory()
            .sku(UPDATED_SKU)
            .createTime(UPDATED_CREATE_TIME)
            .weight(UPDATED_WEIGHT)
            .sotckLevel(UPDATED_SOTCK_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .remark(UPDATED_REMARK);
        // Add required entity
        TblDimension tblDimension;
        if (TestUtil.findAll(em, TblDimension.class).isEmpty()) {
            tblDimension = TblDimensionResourceIT.createUpdatedEntity(em);
            em.persist(tblDimension);
            em.flush();
        } else {
            tblDimension = TestUtil.findAll(em, TblDimension.class).get(0);
        }
        tblInventory.setDimension(tblDimension);
        return tblInventory;
    }

    @BeforeEach
    public void initTest() {
        tblInventory = createEntity(em);
    }

    @Test
    @Transactional
    void createTblInventory() throws Exception {
        int databaseSizeBeforeCreate = tblInventoryRepository.findAll().size();
        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);
        restTblInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        TblInventory testTblInventory = tblInventoryList.get(tblInventoryList.size() - 1);
        assertThat(testTblInventory.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testTblInventory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTblInventory.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testTblInventory.getSotckLevel()).isEqualTo(DEFAULT_SOTCK_LEVEL);
        assertThat(testTblInventory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTblInventory.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void createTblInventoryWithExistingId() throws Exception {
        // Create the TblInventory with an existing ID
        tblInventory.setId(1L);
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        int databaseSizeBeforeCreate = tblInventoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblInventories() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList
        restTblInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].sotckLevel").value(hasItem(DEFAULT_SOTCK_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    void getTblInventory() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get the tblInventory
        restTblInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, tblInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblInventory.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.sotckLevel").value(DEFAULT_SOTCK_LEVEL.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    void getTblInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        Long id = tblInventory.getId();

        defaultTblInventoryShouldBeFound("id.equals=" + id);
        defaultTblInventoryShouldNotBeFound("id.notEquals=" + id);

        defaultTblInventoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblInventoryShouldNotBeFound("id.greaterThan=" + id);

        defaultTblInventoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblInventoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sku equals to DEFAULT_SKU
        defaultTblInventoryShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the tblInventoryList where sku equals to UPDATED_SKU
        defaultTblInventoryShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sku not equals to DEFAULT_SKU
        defaultTblInventoryShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the tblInventoryList where sku not equals to UPDATED_SKU
        defaultTblInventoryShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultTblInventoryShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the tblInventoryList where sku equals to UPDATED_SKU
        defaultTblInventoryShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sku is not null
        defaultTblInventoryShouldBeFound("sku.specified=true");

        // Get all the tblInventoryList where sku is null
        defaultTblInventoryShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySkuContainsSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sku contains DEFAULT_SKU
        defaultTblInventoryShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the tblInventoryList where sku contains UPDATED_SKU
        defaultTblInventoryShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sku does not contain DEFAULT_SKU
        defaultTblInventoryShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the tblInventoryList where sku does not contain UPDATED_SKU
        defaultTblInventoryShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where createTime equals to DEFAULT_CREATE_TIME
        defaultTblInventoryShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the tblInventoryList where createTime equals to UPDATED_CREATE_TIME
        defaultTblInventoryShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByCreateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where createTime not equals to DEFAULT_CREATE_TIME
        defaultTblInventoryShouldNotBeFound("createTime.notEquals=" + DEFAULT_CREATE_TIME);

        // Get all the tblInventoryList where createTime not equals to UPDATED_CREATE_TIME
        defaultTblInventoryShouldBeFound("createTime.notEquals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultTblInventoryShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the tblInventoryList where createTime equals to UPDATED_CREATE_TIME
        defaultTblInventoryShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where createTime is not null
        defaultTblInventoryShouldBeFound("createTime.specified=true");

        // Get all the tblInventoryList where createTime is null
        defaultTblInventoryShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight equals to DEFAULT_WEIGHT
        defaultTblInventoryShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the tblInventoryList where weight equals to UPDATED_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight not equals to DEFAULT_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.notEquals=" + DEFAULT_WEIGHT);

        // Get all the tblInventoryList where weight not equals to UPDATED_WEIGHT
        defaultTblInventoryShouldBeFound("weight.notEquals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultTblInventoryShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the tblInventoryList where weight equals to UPDATED_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight is not null
        defaultTblInventoryShouldBeFound("weight.specified=true");

        // Get all the tblInventoryList where weight is null
        defaultTblInventoryShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultTblInventoryShouldBeFound("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the tblInventoryList where weight is greater than or equal to UPDATED_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight is less than or equal to DEFAULT_WEIGHT
        defaultTblInventoryShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the tblInventoryList where weight is less than or equal to SMALLER_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight is less than DEFAULT_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the tblInventoryList where weight is less than UPDATED_WEIGHT
        defaultTblInventoryShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where weight is greater than DEFAULT_WEIGHT
        defaultTblInventoryShouldNotBeFound("weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the tblInventoryList where weight is greater than SMALLER_WEIGHT
        defaultTblInventoryShouldBeFound("weight.greaterThan=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel equals to DEFAULT_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.equals=" + DEFAULT_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel equals to UPDATED_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.equals=" + UPDATED_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel not equals to DEFAULT_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.notEquals=" + DEFAULT_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel not equals to UPDATED_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.notEquals=" + UPDATED_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel in DEFAULT_SOTCK_LEVEL or UPDATED_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.in=" + DEFAULT_SOTCK_LEVEL + "," + UPDATED_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel equals to UPDATED_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.in=" + UPDATED_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel is not null
        defaultTblInventoryShouldBeFound("sotckLevel.specified=true");

        // Get all the tblInventoryList where sotckLevel is null
        defaultTblInventoryShouldNotBeFound("sotckLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel is greater than or equal to DEFAULT_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.greaterThanOrEqual=" + DEFAULT_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel is greater than or equal to UPDATED_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.greaterThanOrEqual=" + UPDATED_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel is less than or equal to DEFAULT_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.lessThanOrEqual=" + DEFAULT_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel is less than or equal to SMALLER_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.lessThanOrEqual=" + SMALLER_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel is less than DEFAULT_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.lessThan=" + DEFAULT_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel is less than UPDATED_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.lessThan=" + UPDATED_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesBySotckLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        // Get all the tblInventoryList where sotckLevel is greater than DEFAULT_SOTCK_LEVEL
        defaultTblInventoryShouldNotBeFound("sotckLevel.greaterThan=" + DEFAULT_SOTCK_LEVEL);

        // Get all the tblInventoryList where sotckLevel is greater than SMALLER_SOTCK_LEVEL
        defaultTblInventoryShouldBeFound("sotckLevel.greaterThan=" + SMALLER_SOTCK_LEVEL);
    }

    @Test
    @Transactional
    void getAllTblInventoriesByDimensionIsEqualToSomething() throws Exception {
        // Get already existing entity
        TblDimension dimension = tblInventory.getDimension();
        tblInventoryRepository.saveAndFlush(tblInventory);
        Long dimensionId = dimension.getId();

        // Get all the tblInventoryList where dimension equals to dimensionId
        defaultTblInventoryShouldBeFound("dimensionId.equals=" + dimensionId);

        // Get all the tblInventoryList where dimension equals to (dimensionId + 1)
        defaultTblInventoryShouldNotBeFound("dimensionId.equals=" + (dimensionId + 1));
    }

    @Test
    @Transactional
    void getAllTblInventoriesByMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);
        TblInventoryMedia media;
        if (TestUtil.findAll(em, TblInventoryMedia.class).isEmpty()) {
            media = TblInventoryMediaResourceIT.createEntity(em);
            em.persist(media);
            em.flush();
        } else {
            media = TestUtil.findAll(em, TblInventoryMedia.class).get(0);
        }
        em.persist(media);
        em.flush();
        tblInventory.addMedia(media);
        tblInventoryRepository.saveAndFlush(tblInventory);
        Long mediaId = media.getId();

        // Get all the tblInventoryList where media equals to mediaId
        defaultTblInventoryShouldBeFound("mediaId.equals=" + mediaId);

        // Get all the tblInventoryList where media equals to (mediaId + 1)
        defaultTblInventoryShouldNotBeFound("mediaId.equals=" + (mediaId + 1));
    }

    @Test
    @Transactional
    void getAllTblInventoriesByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);
        TblProduct products;
        if (TestUtil.findAll(em, TblProduct.class).isEmpty()) {
            products = TblProductResourceIT.createEntity(em);
            em.persist(products);
            em.flush();
        } else {
            products = TestUtil.findAll(em, TblProduct.class).get(0);
        }
        em.persist(products);
        em.flush();
        tblInventory.addProducts(products);
        tblInventoryRepository.saveAndFlush(tblInventory);
        Long productsId = products.getId();

        // Get all the tblInventoryList where products equals to productsId
        defaultTblInventoryShouldBeFound("productsId.equals=" + productsId);

        // Get all the tblInventoryList where products equals to (productsId + 1)
        defaultTblInventoryShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblInventoryShouldBeFound(String filter) throws Exception {
        restTblInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].sotckLevel").value(hasItem(DEFAULT_SOTCK_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));

        // Check, that the count call also returns 1
        restTblInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblInventoryShouldNotBeFound(String filter) throws Exception {
        restTblInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblInventory() throws Exception {
        // Get the tblInventory
        restTblInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblInventory() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();

        // Update the tblInventory
        TblInventory updatedTblInventory = tblInventoryRepository.findById(tblInventory.getId()).get();
        // Disconnect from session so that the updates on updatedTblInventory are not directly saved in db
        em.detach(updatedTblInventory);
        updatedTblInventory
            .sku(UPDATED_SKU)
            .createTime(UPDATED_CREATE_TIME)
            .weight(UPDATED_WEIGHT)
            .sotckLevel(UPDATED_SOTCK_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .remark(UPDATED_REMARK);
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(updatedTblInventory);

        restTblInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
        TblInventory testTblInventory = tblInventoryList.get(tblInventoryList.size() - 1);
        assertThat(testTblInventory.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testTblInventory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTblInventory.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testTblInventory.getSotckLevel()).isEqualTo(UPDATED_SOTCK_LEVEL);
        assertThat(testTblInventory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTblInventory.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void putNonExistingTblInventory() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();
        tblInventory.setId(count.incrementAndGet());

        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblInventory() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();
        tblInventory.setId(count.incrementAndGet());

        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblInventory() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();
        tblInventory.setId(count.incrementAndGet());

        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblInventoryWithPatch() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();

        // Update the tblInventory using partial update
        TblInventory partialUpdatedTblInventory = new TblInventory();
        partialUpdatedTblInventory.setId(tblInventory.getId());

        partialUpdatedTblInventory.weight(UPDATED_WEIGHT);

        restTblInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblInventory))
            )
            .andExpect(status().isOk());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
        TblInventory testTblInventory = tblInventoryList.get(tblInventoryList.size() - 1);
        assertThat(testTblInventory.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testTblInventory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTblInventory.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testTblInventory.getSotckLevel()).isEqualTo(DEFAULT_SOTCK_LEVEL);
        assertThat(testTblInventory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTblInventory.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void fullUpdateTblInventoryWithPatch() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();

        // Update the tblInventory using partial update
        TblInventory partialUpdatedTblInventory = new TblInventory();
        partialUpdatedTblInventory.setId(tblInventory.getId());

        partialUpdatedTblInventory
            .sku(UPDATED_SKU)
            .createTime(UPDATED_CREATE_TIME)
            .weight(UPDATED_WEIGHT)
            .sotckLevel(UPDATED_SOTCK_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .remark(UPDATED_REMARK);

        restTblInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblInventory))
            )
            .andExpect(status().isOk());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
        TblInventory testTblInventory = tblInventoryList.get(tblInventoryList.size() - 1);
        assertThat(testTblInventory.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testTblInventory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTblInventory.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testTblInventory.getSotckLevel()).isEqualTo(UPDATED_SOTCK_LEVEL);
        assertThat(testTblInventory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTblInventory.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void patchNonExistingTblInventory() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();
        tblInventory.setId(count.incrementAndGet());

        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblInventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblInventory() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();
        tblInventory.setId(count.incrementAndGet());

        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblInventory() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryRepository.findAll().size();
        tblInventory.setId(count.incrementAndGet());

        // Create the TblInventory
        TblInventoryDTO tblInventoryDTO = tblInventoryMapper.toDto(tblInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblInventory in the database
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblInventory() throws Exception {
        // Initialize the database
        tblInventoryRepository.saveAndFlush(tblInventory);

        int databaseSizeBeforeDelete = tblInventoryRepository.findAll().size();

        // Delete the tblInventory
        restTblInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblInventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblInventory> tblInventoryList = tblInventoryRepository.findAll();
        assertThat(tblInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
