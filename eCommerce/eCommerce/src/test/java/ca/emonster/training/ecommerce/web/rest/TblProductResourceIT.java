package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.domain.TblProduct;
import ca.emonster.training.ecommerce.domain.enumeration.Currency;
import ca.emonster.training.ecommerce.repository.TblProductRepository;
import ca.emonster.training.ecommerce.service.criteria.TblProductCriteria;
import ca.emonster.training.ecommerce.service.dto.TblProductDTO;
import ca.emonster.training.ecommerce.service.mapper.TblProductMapper;
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
 * Integration tests for the {@link TblProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblProductResourceIT {

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY = Currency.CAD;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;
    private static final Long SMALLER_PRICE = 1L - 1L;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblProductRepository tblProductRepository;

    @Autowired
    private TblProductMapper tblProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblProductMockMvc;

    private TblProduct tblProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblProduct createEntity(EntityManager em) {
        TblProduct tblProduct = new TblProduct()
            .displayName(DEFAULT_DISPLAY_NAME)
            .currency(DEFAULT_CURRENCY)
            .price(DEFAULT_PRICE)
            .isActive(DEFAULT_IS_ACTIVE)
            .remark(DEFAULT_REMARK);
        return tblProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblProduct createUpdatedEntity(EntityManager em) {
        TblProduct tblProduct = new TblProduct()
            .displayName(UPDATED_DISPLAY_NAME)
            .currency(UPDATED_CURRENCY)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);
        return tblProduct;
    }

    @BeforeEach
    public void initTest() {
        tblProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createTblProduct() throws Exception {
        int databaseSizeBeforeCreate = tblProductRepository.findAll().size();
        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);
        restTblProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblProductDTO)))
            .andExpect(status().isCreated());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeCreate + 1);
        TblProduct testTblProduct = tblProductList.get(tblProductList.size() - 1);
        assertThat(testTblProduct.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testTblProduct.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testTblProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTblProduct.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTblProduct.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void createTblProductWithExistingId() throws Exception {
        // Create the TblProduct with an existing ID
        tblProduct.setId(1L);
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        int databaseSizeBeforeCreate = tblProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblProducts() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList
        restTblProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    void getTblProduct() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get the tblProduct
        restTblProductMockMvc
            .perform(get(ENTITY_API_URL_ID, tblProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblProduct.getId().intValue()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    void getTblProductsByIdFiltering() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        Long id = tblProduct.getId();

        defaultTblProductShouldBeFound("id.equals=" + id);
        defaultTblProductShouldNotBeFound("id.notEquals=" + id);

        defaultTblProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblProductShouldNotBeFound("id.greaterThan=" + id);

        defaultTblProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblProductsByDisplayNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultTblProductShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the tblProductList where displayName equals to UPDATED_DISPLAY_NAME
        defaultTblProductShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllTblProductsByDisplayNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where displayName not equals to DEFAULT_DISPLAY_NAME
        defaultTblProductShouldNotBeFound("displayName.notEquals=" + DEFAULT_DISPLAY_NAME);

        // Get all the tblProductList where displayName not equals to UPDATED_DISPLAY_NAME
        defaultTblProductShouldBeFound("displayName.notEquals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllTblProductsByDisplayNameIsInShouldWork() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultTblProductShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the tblProductList where displayName equals to UPDATED_DISPLAY_NAME
        defaultTblProductShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllTblProductsByDisplayNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where displayName is not null
        defaultTblProductShouldBeFound("displayName.specified=true");

        // Get all the tblProductList where displayName is null
        defaultTblProductShouldNotBeFound("displayName.specified=false");
    }

    @Test
    @Transactional
    void getAllTblProductsByDisplayNameContainsSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where displayName contains DEFAULT_DISPLAY_NAME
        defaultTblProductShouldBeFound("displayName.contains=" + DEFAULT_DISPLAY_NAME);

        // Get all the tblProductList where displayName contains UPDATED_DISPLAY_NAME
        defaultTblProductShouldNotBeFound("displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllTblProductsByDisplayNameNotContainsSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where displayName does not contain DEFAULT_DISPLAY_NAME
        defaultTblProductShouldNotBeFound("displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME);

        // Get all the tblProductList where displayName does not contain UPDATED_DISPLAY_NAME
        defaultTblProductShouldBeFound("displayName.doesNotContain=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllTblProductsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where currency equals to DEFAULT_CURRENCY
        defaultTblProductShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the tblProductList where currency equals to UPDATED_CURRENCY
        defaultTblProductShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTblProductsByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where currency not equals to DEFAULT_CURRENCY
        defaultTblProductShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the tblProductList where currency not equals to UPDATED_CURRENCY
        defaultTblProductShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTblProductsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultTblProductShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the tblProductList where currency equals to UPDATED_CURRENCY
        defaultTblProductShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTblProductsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where currency is not null
        defaultTblProductShouldBeFound("currency.specified=true");

        // Get all the tblProductList where currency is null
        defaultTblProductShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price equals to DEFAULT_PRICE
        defaultTblProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the tblProductList where price equals to UPDATED_PRICE
        defaultTblProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price not equals to DEFAULT_PRICE
        defaultTblProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the tblProductList where price not equals to UPDATED_PRICE
        defaultTblProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultTblProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the tblProductList where price equals to UPDATED_PRICE
        defaultTblProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price is not null
        defaultTblProductShouldBeFound("price.specified=true");

        // Get all the tblProductList where price is null
        defaultTblProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price is greater than or equal to DEFAULT_PRICE
        defaultTblProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the tblProductList where price is greater than or equal to UPDATED_PRICE
        defaultTblProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price is less than or equal to DEFAULT_PRICE
        defaultTblProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the tblProductList where price is less than or equal to SMALLER_PRICE
        defaultTblProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price is less than DEFAULT_PRICE
        defaultTblProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the tblProductList where price is less than UPDATED_PRICE
        defaultTblProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where price is greater than DEFAULT_PRICE
        defaultTblProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the tblProductList where price is greater than SMALLER_PRICE
        defaultTblProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllTblProductsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTblProductShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblProductList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblProductShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblProductsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultTblProductShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the tblProductList where isActive not equals to UPDATED_IS_ACTIVE
        defaultTblProductShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblProductsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTblProductShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the tblProductList where isActive equals to UPDATED_IS_ACTIVE
        defaultTblProductShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTblProductsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        // Get all the tblProductList where isActive is not null
        defaultTblProductShouldBeFound("isActive.specified=true");

        // Get all the tblProductList where isActive is null
        defaultTblProductShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTblProductsByInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);
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
        tblProduct.setInventory(inventory);
        tblProductRepository.saveAndFlush(tblProduct);
        Long inventoryId = inventory.getId();

        // Get all the tblProductList where inventory equals to inventoryId
        defaultTblProductShouldBeFound("inventoryId.equals=" + inventoryId);

        // Get all the tblProductList where inventory equals to (inventoryId + 1)
        defaultTblProductShouldNotBeFound("inventoryId.equals=" + (inventoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblProductShouldBeFound(String filter) throws Exception {
        restTblProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));

        // Check, that the count call also returns 1
        restTblProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblProductShouldNotBeFound(String filter) throws Exception {
        restTblProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblProduct() throws Exception {
        // Get the tblProduct
        restTblProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblProduct() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();

        // Update the tblProduct
        TblProduct updatedTblProduct = tblProductRepository.findById(tblProduct.getId()).get();
        // Disconnect from session so that the updates on updatedTblProduct are not directly saved in db
        em.detach(updatedTblProduct);
        updatedTblProduct
            .displayName(UPDATED_DISPLAY_NAME)
            .currency(UPDATED_CURRENCY)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);
        TblProductDTO tblProductDTO = tblProductMapper.toDto(updatedTblProduct);

        restTblProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
        TblProduct testTblProduct = tblProductList.get(tblProductList.size() - 1);
        assertThat(testTblProduct.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testTblProduct.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTblProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTblProduct.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblProduct.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void putNonExistingTblProduct() throws Exception {
        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();
        tblProduct.setId(count.incrementAndGet());

        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblProduct() throws Exception {
        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();
        tblProduct.setId(count.incrementAndGet());

        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblProduct() throws Exception {
        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();
        tblProduct.setId(count.incrementAndGet());

        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblProductDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblProductWithPatch() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();

        // Update the tblProduct using partial update
        TblProduct partialUpdatedTblProduct = new TblProduct();
        partialUpdatedTblProduct.setId(tblProduct.getId());

        partialUpdatedTblProduct.displayName(UPDATED_DISPLAY_NAME).price(UPDATED_PRICE).isActive(UPDATED_IS_ACTIVE);

        restTblProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblProduct))
            )
            .andExpect(status().isOk());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
        TblProduct testTblProduct = tblProductList.get(tblProductList.size() - 1);
        assertThat(testTblProduct.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testTblProduct.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testTblProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTblProduct.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblProduct.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void fullUpdateTblProductWithPatch() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();

        // Update the tblProduct using partial update
        TblProduct partialUpdatedTblProduct = new TblProduct();
        partialUpdatedTblProduct.setId(tblProduct.getId());

        partialUpdatedTblProduct
            .displayName(UPDATED_DISPLAY_NAME)
            .currency(UPDATED_CURRENCY)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .remark(UPDATED_REMARK);

        restTblProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblProduct))
            )
            .andExpect(status().isOk());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
        TblProduct testTblProduct = tblProductList.get(tblProductList.size() - 1);
        assertThat(testTblProduct.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testTblProduct.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTblProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTblProduct.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTblProduct.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void patchNonExistingTblProduct() throws Exception {
        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();
        tblProduct.setId(count.incrementAndGet());

        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblProductDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblProduct() throws Exception {
        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();
        tblProduct.setId(count.incrementAndGet());

        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblProduct() throws Exception {
        int databaseSizeBeforeUpdate = tblProductRepository.findAll().size();
        tblProduct.setId(count.incrementAndGet());

        // Create the TblProduct
        TblProductDTO tblProductDTO = tblProductMapper.toDto(tblProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblProduct in the database
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblProduct() throws Exception {
        // Initialize the database
        tblProductRepository.saveAndFlush(tblProduct);

        int databaseSizeBeforeDelete = tblProductRepository.findAll().size();

        // Delete the tblProduct
        restTblProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblProduct> tblProductList = tblProductRepository.findAll();
        assertThat(tblProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
