package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.domain.TblPayment;
import ca.emonster.training.ecommerce.domain.enumeration.PaymentChannel;
import ca.emonster.training.ecommerce.domain.enumeration.PaymentMethod;
import ca.emonster.training.ecommerce.repository.TblPaymentRepository;
import ca.emonster.training.ecommerce.service.criteria.TblPaymentCriteria;
import ca.emonster.training.ecommerce.service.dto.TblPaymentDTO;
import ca.emonster.training.ecommerce.service.mapper.TblPaymentMapper;
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
 * Integration tests for the {@link TblPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblPaymentResourceIT {

    private static final PaymentChannel DEFAULT_CHANNEL = PaymentChannel.PAYPAL;
    private static final PaymentChannel UPDATED_CHANNEL = PaymentChannel.STRIPE;

    private static final PaymentMethod DEFAULT_METHOD = PaymentMethod.CREDITCARD;
    private static final PaymentMethod UPDATED_METHOD = PaymentMethod.APPLEPAY;

    private static final String ENTITY_API_URL = "/api/tbl-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblPaymentRepository tblPaymentRepository;

    @Autowired
    private TblPaymentMapper tblPaymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblPaymentMockMvc;

    private TblPayment tblPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblPayment createEntity(EntityManager em) {
        TblPayment tblPayment = new TblPayment().channel(DEFAULT_CHANNEL).method(DEFAULT_METHOD);
        return tblPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblPayment createUpdatedEntity(EntityManager em) {
        TblPayment tblPayment = new TblPayment().channel(UPDATED_CHANNEL).method(UPDATED_METHOD);
        return tblPayment;
    }

    @BeforeEach
    public void initTest() {
        tblPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createTblPayment() throws Exception {
        int databaseSizeBeforeCreate = tblPaymentRepository.findAll().size();
        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);
        restTblPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        TblPayment testTblPayment = tblPaymentList.get(tblPaymentList.size() - 1);
        assertThat(testTblPayment.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testTblPayment.getMethod()).isEqualTo(DEFAULT_METHOD);
    }

    @Test
    @Transactional
    void createTblPaymentWithExistingId() throws Exception {
        // Create the TblPayment with an existing ID
        tblPayment.setId(1L);
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        int databaseSizeBeforeCreate = tblPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblPayments() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList
        restTblPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())));
    }

    @Test
    @Transactional
    void getTblPayment() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get the tblPayment
        restTblPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, tblPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblPayment.getId().intValue()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()));
    }

    @Test
    @Transactional
    void getTblPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        Long id = tblPayment.getId();

        defaultTblPaymentShouldBeFound("id.equals=" + id);
        defaultTblPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultTblPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultTblPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblPaymentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where channel equals to DEFAULT_CHANNEL
        defaultTblPaymentShouldBeFound("channel.equals=" + DEFAULT_CHANNEL);

        // Get all the tblPaymentList where channel equals to UPDATED_CHANNEL
        defaultTblPaymentShouldNotBeFound("channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByChannelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where channel not equals to DEFAULT_CHANNEL
        defaultTblPaymentShouldNotBeFound("channel.notEquals=" + DEFAULT_CHANNEL);

        // Get all the tblPaymentList where channel not equals to UPDATED_CHANNEL
        defaultTblPaymentShouldBeFound("channel.notEquals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where channel in DEFAULT_CHANNEL or UPDATED_CHANNEL
        defaultTblPaymentShouldBeFound("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL);

        // Get all the tblPaymentList where channel equals to UPDATED_CHANNEL
        defaultTblPaymentShouldNotBeFound("channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where channel is not null
        defaultTblPaymentShouldBeFound("channel.specified=true");

        // Get all the tblPaymentList where channel is null
        defaultTblPaymentShouldNotBeFound("channel.specified=false");
    }

    @Test
    @Transactional
    void getAllTblPaymentsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where method equals to DEFAULT_METHOD
        defaultTblPaymentShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the tblPaymentList where method equals to UPDATED_METHOD
        defaultTblPaymentShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where method not equals to DEFAULT_METHOD
        defaultTblPaymentShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the tblPaymentList where method not equals to UPDATED_METHOD
        defaultTblPaymentShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultTblPaymentShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the tblPaymentList where method equals to UPDATED_METHOD
        defaultTblPaymentShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllTblPaymentsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        // Get all the tblPaymentList where method is not null
        defaultTblPaymentShouldBeFound("method.specified=true");

        // Get all the tblPaymentList where method is null
        defaultTblPaymentShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllTblPaymentsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);
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
        tblPayment.setOrder(order);
        order.setPayment(tblPayment);
        tblPaymentRepository.saveAndFlush(tblPayment);
        Long orderId = order.getId();

        // Get all the tblPaymentList where order equals to orderId
        defaultTblPaymentShouldBeFound("orderId.equals=" + orderId);

        // Get all the tblPaymentList where order equals to (orderId + 1)
        defaultTblPaymentShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblPaymentShouldBeFound(String filter) throws Exception {
        restTblPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())));

        // Check, that the count call also returns 1
        restTblPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblPaymentShouldNotBeFound(String filter) throws Exception {
        restTblPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblPayment() throws Exception {
        // Get the tblPayment
        restTblPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblPayment() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();

        // Update the tblPayment
        TblPayment updatedTblPayment = tblPaymentRepository.findById(tblPayment.getId()).get();
        // Disconnect from session so that the updates on updatedTblPayment are not directly saved in db
        em.detach(updatedTblPayment);
        updatedTblPayment.channel(UPDATED_CHANNEL).method(UPDATED_METHOD);
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(updatedTblPayment);

        restTblPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
        TblPayment testTblPayment = tblPaymentList.get(tblPaymentList.size() - 1);
        assertThat(testTblPayment.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTblPayment.getMethod()).isEqualTo(UPDATED_METHOD);
    }

    @Test
    @Transactional
    void putNonExistingTblPayment() throws Exception {
        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();
        tblPayment.setId(count.incrementAndGet());

        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblPayment() throws Exception {
        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();
        tblPayment.setId(count.incrementAndGet());

        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblPayment() throws Exception {
        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();
        tblPayment.setId(count.incrementAndGet());

        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblPaymentWithPatch() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();

        // Update the tblPayment using partial update
        TblPayment partialUpdatedTblPayment = new TblPayment();
        partialUpdatedTblPayment.setId(tblPayment.getId());

        partialUpdatedTblPayment.channel(UPDATED_CHANNEL).method(UPDATED_METHOD);

        restTblPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblPayment))
            )
            .andExpect(status().isOk());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
        TblPayment testTblPayment = tblPaymentList.get(tblPaymentList.size() - 1);
        assertThat(testTblPayment.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTblPayment.getMethod()).isEqualTo(UPDATED_METHOD);
    }

    @Test
    @Transactional
    void fullUpdateTblPaymentWithPatch() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();

        // Update the tblPayment using partial update
        TblPayment partialUpdatedTblPayment = new TblPayment();
        partialUpdatedTblPayment.setId(tblPayment.getId());

        partialUpdatedTblPayment.channel(UPDATED_CHANNEL).method(UPDATED_METHOD);

        restTblPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblPayment))
            )
            .andExpect(status().isOk());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
        TblPayment testTblPayment = tblPaymentList.get(tblPaymentList.size() - 1);
        assertThat(testTblPayment.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testTblPayment.getMethod()).isEqualTo(UPDATED_METHOD);
    }

    @Test
    @Transactional
    void patchNonExistingTblPayment() throws Exception {
        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();
        tblPayment.setId(count.incrementAndGet());

        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblPaymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblPayment() throws Exception {
        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();
        tblPayment.setId(count.incrementAndGet());

        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblPayment() throws Exception {
        int databaseSizeBeforeUpdate = tblPaymentRepository.findAll().size();
        tblPayment.setId(count.incrementAndGet());

        // Create the TblPayment
        TblPaymentDTO tblPaymentDTO = tblPaymentMapper.toDto(tblPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblPaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblPayment in the database
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblPayment() throws Exception {
        // Initialize the database
        tblPaymentRepository.saveAndFlush(tblPayment);

        int databaseSizeBeforeDelete = tblPaymentRepository.findAll().size();

        // Delete the tblPayment
        restTblPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblPayment> tblPaymentList = tblPaymentRepository.findAll();
        assertThat(tblPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
