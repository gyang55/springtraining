package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.domain.TblInventoryMedia;
import ca.emonster.training.ecommerce.domain.enumeration.InventoryMediaType;
import ca.emonster.training.ecommerce.repository.TblInventoryMediaRepository;
import ca.emonster.training.ecommerce.service.criteria.TblInventoryMediaCriteria;
import ca.emonster.training.ecommerce.service.dto.TblInventoryMediaDTO;
import ca.emonster.training.ecommerce.service.mapper.TblInventoryMediaMapper;
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
 * Integration tests for the {@link TblInventoryMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblInventoryMediaResourceIT {

    private static final InventoryMediaType DEFAULT_TYPE = InventoryMediaType.IMAGE;
    private static final InventoryMediaType UPDATED_TYPE = InventoryMediaType.VIDEO;

    private static final String DEFAULT_OBJECT_ID = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tbl-inventory-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblInventoryMediaRepository tblInventoryMediaRepository;

    @Autowired
    private TblInventoryMediaMapper tblInventoryMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblInventoryMediaMockMvc;

    private TblInventoryMedia tblInventoryMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblInventoryMedia createEntity(EntityManager em) {
        TblInventoryMedia tblInventoryMedia = new TblInventoryMedia().type(DEFAULT_TYPE).objectId(DEFAULT_OBJECT_ID).key(DEFAULT_KEY);
        return tblInventoryMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblInventoryMedia createUpdatedEntity(EntityManager em) {
        TblInventoryMedia tblInventoryMedia = new TblInventoryMedia().type(UPDATED_TYPE).objectId(UPDATED_OBJECT_ID).key(UPDATED_KEY);
        return tblInventoryMedia;
    }

    @BeforeEach
    public void initTest() {
        tblInventoryMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createTblInventoryMedia() throws Exception {
        int databaseSizeBeforeCreate = tblInventoryMediaRepository.findAll().size();
        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);
        restTblInventoryMediaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeCreate + 1);
        TblInventoryMedia testTblInventoryMedia = tblInventoryMediaList.get(tblInventoryMediaList.size() - 1);
        assertThat(testTblInventoryMedia.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTblInventoryMedia.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testTblInventoryMedia.getKey()).isEqualTo(DEFAULT_KEY);
    }

    @Test
    @Transactional
    void createTblInventoryMediaWithExistingId() throws Exception {
        // Create the TblInventoryMedia with an existing ID
        tblInventoryMedia.setId(1L);
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        int databaseSizeBeforeCreate = tblInventoryMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblInventoryMediaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblInventoryMedias() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList
        restTblInventoryMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblInventoryMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)));
    }

    @Test
    @Transactional
    void getTblInventoryMedia() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get the tblInventoryMedia
        restTblInventoryMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, tblInventoryMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblInventoryMedia.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY));
    }

    @Test
    @Transactional
    void getTblInventoryMediasByIdFiltering() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        Long id = tblInventoryMedia.getId();

        defaultTblInventoryMediaShouldBeFound("id.equals=" + id);
        defaultTblInventoryMediaShouldNotBeFound("id.notEquals=" + id);

        defaultTblInventoryMediaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblInventoryMediaShouldNotBeFound("id.greaterThan=" + id);

        defaultTblInventoryMediaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblInventoryMediaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where type equals to DEFAULT_TYPE
        defaultTblInventoryMediaShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the tblInventoryMediaList where type equals to UPDATED_TYPE
        defaultTblInventoryMediaShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where type not equals to DEFAULT_TYPE
        defaultTblInventoryMediaShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the tblInventoryMediaList where type not equals to UPDATED_TYPE
        defaultTblInventoryMediaShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTblInventoryMediaShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the tblInventoryMediaList where type equals to UPDATED_TYPE
        defaultTblInventoryMediaShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where type is not null
        defaultTblInventoryMediaShouldBeFound("type.specified=true");

        // Get all the tblInventoryMediaList where type is null
        defaultTblInventoryMediaShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByObjectIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where objectId equals to DEFAULT_OBJECT_ID
        defaultTblInventoryMediaShouldBeFound("objectId.equals=" + DEFAULT_OBJECT_ID);

        // Get all the tblInventoryMediaList where objectId equals to UPDATED_OBJECT_ID
        defaultTblInventoryMediaShouldNotBeFound("objectId.equals=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByObjectIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where objectId not equals to DEFAULT_OBJECT_ID
        defaultTblInventoryMediaShouldNotBeFound("objectId.notEquals=" + DEFAULT_OBJECT_ID);

        // Get all the tblInventoryMediaList where objectId not equals to UPDATED_OBJECT_ID
        defaultTblInventoryMediaShouldBeFound("objectId.notEquals=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByObjectIdIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where objectId in DEFAULT_OBJECT_ID or UPDATED_OBJECT_ID
        defaultTblInventoryMediaShouldBeFound("objectId.in=" + DEFAULT_OBJECT_ID + "," + UPDATED_OBJECT_ID);

        // Get all the tblInventoryMediaList where objectId equals to UPDATED_OBJECT_ID
        defaultTblInventoryMediaShouldNotBeFound("objectId.in=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByObjectIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where objectId is not null
        defaultTblInventoryMediaShouldBeFound("objectId.specified=true");

        // Get all the tblInventoryMediaList where objectId is null
        defaultTblInventoryMediaShouldNotBeFound("objectId.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByObjectIdContainsSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where objectId contains DEFAULT_OBJECT_ID
        defaultTblInventoryMediaShouldBeFound("objectId.contains=" + DEFAULT_OBJECT_ID);

        // Get all the tblInventoryMediaList where objectId contains UPDATED_OBJECT_ID
        defaultTblInventoryMediaShouldNotBeFound("objectId.contains=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByObjectIdNotContainsSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where objectId does not contain DEFAULT_OBJECT_ID
        defaultTblInventoryMediaShouldNotBeFound("objectId.doesNotContain=" + DEFAULT_OBJECT_ID);

        // Get all the tblInventoryMediaList where objectId does not contain UPDATED_OBJECT_ID
        defaultTblInventoryMediaShouldBeFound("objectId.doesNotContain=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where key equals to DEFAULT_KEY
        defaultTblInventoryMediaShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the tblInventoryMediaList where key equals to UPDATED_KEY
        defaultTblInventoryMediaShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where key not equals to DEFAULT_KEY
        defaultTblInventoryMediaShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the tblInventoryMediaList where key not equals to UPDATED_KEY
        defaultTblInventoryMediaShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where key in DEFAULT_KEY or UPDATED_KEY
        defaultTblInventoryMediaShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the tblInventoryMediaList where key equals to UPDATED_KEY
        defaultTblInventoryMediaShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where key is not null
        defaultTblInventoryMediaShouldBeFound("key.specified=true");

        // Get all the tblInventoryMediaList where key is null
        defaultTblInventoryMediaShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByKeyContainsSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where key contains DEFAULT_KEY
        defaultTblInventoryMediaShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the tblInventoryMediaList where key contains UPDATED_KEY
        defaultTblInventoryMediaShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        // Get all the tblInventoryMediaList where key does not contain DEFAULT_KEY
        defaultTblInventoryMediaShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the tblInventoryMediaList where key does not contain UPDATED_KEY
        defaultTblInventoryMediaShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllTblInventoryMediasByInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);
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
        tblInventoryMedia.setInventory(inventory);
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);
        Long inventoryId = inventory.getId();

        // Get all the tblInventoryMediaList where inventory equals to inventoryId
        defaultTblInventoryMediaShouldBeFound("inventoryId.equals=" + inventoryId);

        // Get all the tblInventoryMediaList where inventory equals to (inventoryId + 1)
        defaultTblInventoryMediaShouldNotBeFound("inventoryId.equals=" + (inventoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblInventoryMediaShouldBeFound(String filter) throws Exception {
        restTblInventoryMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblInventoryMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)));

        // Check, that the count call also returns 1
        restTblInventoryMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblInventoryMediaShouldNotBeFound(String filter) throws Exception {
        restTblInventoryMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblInventoryMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblInventoryMedia() throws Exception {
        // Get the tblInventoryMedia
        restTblInventoryMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblInventoryMedia() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();

        // Update the tblInventoryMedia
        TblInventoryMedia updatedTblInventoryMedia = tblInventoryMediaRepository.findById(tblInventoryMedia.getId()).get();
        // Disconnect from session so that the updates on updatedTblInventoryMedia are not directly saved in db
        em.detach(updatedTblInventoryMedia);
        updatedTblInventoryMedia.type(UPDATED_TYPE).objectId(UPDATED_OBJECT_ID).key(UPDATED_KEY);
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(updatedTblInventoryMedia);

        restTblInventoryMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblInventoryMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
        TblInventoryMedia testTblInventoryMedia = tblInventoryMediaList.get(tblInventoryMediaList.size() - 1);
        assertThat(testTblInventoryMedia.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTblInventoryMedia.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testTblInventoryMedia.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    void putNonExistingTblInventoryMedia() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();
        tblInventoryMedia.setId(count.incrementAndGet());

        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblInventoryMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblInventoryMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblInventoryMedia() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();
        tblInventoryMedia.setId(count.incrementAndGet());

        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblInventoryMedia() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();
        tblInventoryMedia.setId(count.incrementAndGet());

        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMediaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblInventoryMediaWithPatch() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();

        // Update the tblInventoryMedia using partial update
        TblInventoryMedia partialUpdatedTblInventoryMedia = new TblInventoryMedia();
        partialUpdatedTblInventoryMedia.setId(tblInventoryMedia.getId());

        partialUpdatedTblInventoryMedia.key(UPDATED_KEY);

        restTblInventoryMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblInventoryMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblInventoryMedia))
            )
            .andExpect(status().isOk());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
        TblInventoryMedia testTblInventoryMedia = tblInventoryMediaList.get(tblInventoryMediaList.size() - 1);
        assertThat(testTblInventoryMedia.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTblInventoryMedia.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testTblInventoryMedia.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    void fullUpdateTblInventoryMediaWithPatch() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();

        // Update the tblInventoryMedia using partial update
        TblInventoryMedia partialUpdatedTblInventoryMedia = new TblInventoryMedia();
        partialUpdatedTblInventoryMedia.setId(tblInventoryMedia.getId());

        partialUpdatedTblInventoryMedia.type(UPDATED_TYPE).objectId(UPDATED_OBJECT_ID).key(UPDATED_KEY);

        restTblInventoryMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblInventoryMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblInventoryMedia))
            )
            .andExpect(status().isOk());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
        TblInventoryMedia testTblInventoryMedia = tblInventoryMediaList.get(tblInventoryMediaList.size() - 1);
        assertThat(testTblInventoryMedia.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTblInventoryMedia.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testTblInventoryMedia.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    void patchNonExistingTblInventoryMedia() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();
        tblInventoryMedia.setId(count.incrementAndGet());

        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblInventoryMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblInventoryMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblInventoryMedia() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();
        tblInventoryMedia.setId(count.incrementAndGet());

        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblInventoryMedia() throws Exception {
        int databaseSizeBeforeUpdate = tblInventoryMediaRepository.findAll().size();
        tblInventoryMedia.setId(count.incrementAndGet());

        // Create the TblInventoryMedia
        TblInventoryMediaDTO tblInventoryMediaDTO = tblInventoryMediaMapper.toDto(tblInventoryMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblInventoryMediaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblInventoryMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblInventoryMedia in the database
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblInventoryMedia() throws Exception {
        // Initialize the database
        tblInventoryMediaRepository.saveAndFlush(tblInventoryMedia);

        int databaseSizeBeforeDelete = tblInventoryMediaRepository.findAll().size();

        // Delete the tblInventoryMedia
        restTblInventoryMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblInventoryMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblInventoryMedia> tblInventoryMediaList = tblInventoryMediaRepository.findAll();
        assertThat(tblInventoryMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
