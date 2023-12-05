package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblItem;
import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.domain.TblProduct;
import ca.emonster.training.ecommerce.repository.TblItemRepository;
import ca.emonster.training.ecommerce.service.criteria.TblItemCriteria;
import ca.emonster.training.ecommerce.service.dto.TblItemDTO;
import ca.emonster.training.ecommerce.service.mapper.TblItemMapper;
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
 * Integration tests for the {@link TblItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblItemResourceIT {

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;
    private static final Long SMALLER_QUANTITY = 1L - 1L;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblItemRepository tblItemRepository;

    @Autowired
    private TblItemMapper tblItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblItemMockMvc;

    private TblItem tblItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblItem createEntity(EntityManager em) {
        TblItem tblItem = new TblItem().quantity(DEFAULT_QUANTITY).remark(DEFAULT_REMARK);
        return tblItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblItem createUpdatedEntity(EntityManager em) {
        TblItem tblItem = new TblItem().quantity(UPDATED_QUANTITY).remark(UPDATED_REMARK);
        return tblItem;
    }

    @BeforeEach
    public void initTest() {
        tblItem = createEntity(em);
    }

    @Test
    @Transactional
    void createTblItem() throws Exception {
        int databaseSizeBeforeCreate = tblItemRepository.findAll().size();
        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);
        restTblItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblItemDTO)))
            .andExpect(status().isCreated());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeCreate + 1);
        TblItem testTblItem = tblItemList.get(tblItemList.size() - 1);
        assertThat(testTblItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testTblItem.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void createTblItemWithExistingId() throws Exception {
        // Create the TblItem with an existing ID
        tblItem.setId(1L);
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        int databaseSizeBeforeCreate = tblItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblItems() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList
        restTblItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }

    @Test
    @Transactional
    void getTblItem() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get the tblItem
        restTblItemMockMvc
            .perform(get(ENTITY_API_URL_ID, tblItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    void getTblItemsByIdFiltering() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        Long id = tblItem.getId();

        defaultTblItemShouldBeFound("id.equals=" + id);
        defaultTblItemShouldNotBeFound("id.notEquals=" + id);

        defaultTblItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblItemShouldNotBeFound("id.greaterThan=" + id);

        defaultTblItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity equals to DEFAULT_QUANTITY
        defaultTblItemShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the tblItemList where quantity equals to UPDATED_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity not equals to DEFAULT_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the tblItemList where quantity not equals to UPDATED_QUANTITY
        defaultTblItemShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultTblItemShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the tblItemList where quantity equals to UPDATED_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity is not null
        defaultTblItemShouldBeFound("quantity.specified=true");

        // Get all the tblItemList where quantity is null
        defaultTblItemShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultTblItemShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the tblItemList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultTblItemShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the tblItemList where quantity is less than or equal to SMALLER_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity is less than DEFAULT_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the tblItemList where quantity is less than UPDATED_QUANTITY
        defaultTblItemShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        // Get all the tblItemList where quantity is greater than DEFAULT_QUANTITY
        defaultTblItemShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the tblItemList where quantity is greater than SMALLER_QUANTITY
        defaultTblItemShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTblItemsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);
        TblProduct product;
        if (TestUtil.findAll(em, TblProduct.class).isEmpty()) {
            product = TblProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, TblProduct.class).get(0);
        }
        em.persist(product);
        em.flush();
        tblItem.setProduct(product);
        tblItemRepository.saveAndFlush(tblItem);
        Long productId = product.getId();

        // Get all the tblItemList where product equals to productId
        defaultTblItemShouldBeFound("productId.equals=" + productId);

        // Get all the tblItemList where product equals to (productId + 1)
        defaultTblItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllTblItemsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);
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
        tblItem.setOrder(order);
        tblItemRepository.saveAndFlush(tblItem);
        Long orderId = order.getId();

        // Get all the tblItemList where order equals to orderId
        defaultTblItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the tblItemList where order equals to (orderId + 1)
        defaultTblItemShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblItemShouldBeFound(String filter) throws Exception {
        restTblItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));

        // Check, that the count call also returns 1
        restTblItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblItemShouldNotBeFound(String filter) throws Exception {
        restTblItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblItem() throws Exception {
        // Get the tblItem
        restTblItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblItem() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();

        // Update the tblItem
        TblItem updatedTblItem = tblItemRepository.findById(tblItem.getId()).get();
        // Disconnect from session so that the updates on updatedTblItem are not directly saved in db
        em.detach(updatedTblItem);
        updatedTblItem.quantity(UPDATED_QUANTITY).remark(UPDATED_REMARK);
        TblItemDTO tblItemDTO = tblItemMapper.toDto(updatedTblItem);

        restTblItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
        TblItem testTblItem = tblItemList.get(tblItemList.size() - 1);
        assertThat(testTblItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTblItem.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void putNonExistingTblItem() throws Exception {
        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();
        tblItem.setId(count.incrementAndGet());

        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblItem() throws Exception {
        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();
        tblItem.setId(count.incrementAndGet());

        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblItem() throws Exception {
        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();
        tblItem.setId(count.incrementAndGet());

        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblItemWithPatch() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();

        // Update the tblItem using partial update
        TblItem partialUpdatedTblItem = new TblItem();
        partialUpdatedTblItem.setId(tblItem.getId());

        partialUpdatedTblItem.quantity(UPDATED_QUANTITY);

        restTblItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblItem))
            )
            .andExpect(status().isOk());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
        TblItem testTblItem = tblItemList.get(tblItemList.size() - 1);
        assertThat(testTblItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTblItem.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void fullUpdateTblItemWithPatch() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();

        // Update the tblItem using partial update
        TblItem partialUpdatedTblItem = new TblItem();
        partialUpdatedTblItem.setId(tblItem.getId());

        partialUpdatedTblItem.quantity(UPDATED_QUANTITY).remark(UPDATED_REMARK);

        restTblItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblItem))
            )
            .andExpect(status().isOk());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
        TblItem testTblItem = tblItemList.get(tblItemList.size() - 1);
        assertThat(testTblItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTblItem.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void patchNonExistingTblItem() throws Exception {
        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();
        tblItem.setId(count.incrementAndGet());

        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblItem() throws Exception {
        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();
        tblItem.setId(count.incrementAndGet());

        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblItem() throws Exception {
        int databaseSizeBeforeUpdate = tblItemRepository.findAll().size();
        tblItem.setId(count.incrementAndGet());

        // Create the TblItem
        TblItemDTO tblItemDTO = tblItemMapper.toDto(tblItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblItem in the database
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblItem() throws Exception {
        // Initialize the database
        tblItemRepository.saveAndFlush(tblItem);

        int databaseSizeBeforeDelete = tblItemRepository.findAll().size();

        // Delete the tblItem
        restTblItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblItem> tblItemList = tblItemRepository.findAll();
        assertThat(tblItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
