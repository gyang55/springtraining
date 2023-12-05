package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.domain.TblItem;
import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.domain.TblPayment;
import ca.emonster.training.ecommerce.domain.enumeration.OrderStatus;
import ca.emonster.training.ecommerce.repository.TblOrderRepository;
import ca.emonster.training.ecommerce.service.criteria.TblOrderCriteria;
import ca.emonster.training.ecommerce.service.dto.TblOrderDTO;
import ca.emonster.training.ecommerce.service.mapper.TblOrderMapper;
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
 * Integration tests for the {@link TblOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblOrderResourceIT {

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_DATE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_DATE = "BBBBBBBBBB";

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.DRAFT;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PROCESSING;

    private static final String ENTITY_API_URL = "/api/tbl-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblOrderRepository tblOrderRepository;

    @Autowired
    private TblOrderMapper tblOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblOrderMockMvc;

    private TblOrder tblOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblOrder createEntity(EntityManager em) {
        TblOrder tblOrder = new TblOrder().orderNumber(DEFAULT_ORDER_NUMBER).orderDate(DEFAULT_ORDER_DATE).status(DEFAULT_STATUS);
        return tblOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblOrder createUpdatedEntity(EntityManager em) {
        TblOrder tblOrder = new TblOrder().orderNumber(UPDATED_ORDER_NUMBER).orderDate(UPDATED_ORDER_DATE).status(UPDATED_STATUS);
        return tblOrder;
    }

    @BeforeEach
    public void initTest() {
        tblOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createTblOrder() throws Exception {
        int databaseSizeBeforeCreate = tblOrderRepository.findAll().size();
        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);
        restTblOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeCreate + 1);
        TblOrder testTblOrder = tblOrderList.get(tblOrderList.size() - 1);
        assertThat(testTblOrder.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testTblOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testTblOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createTblOrderWithExistingId() throws Exception {
        // Create the TblOrder with an existing ID
        tblOrder.setId(1L);
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        int databaseSizeBeforeCreate = tblOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblOrders() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList
        restTblOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getTblOrder() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get the tblOrder
        restTblOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, tblOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getTblOrdersByIdFiltering() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        Long id = tblOrder.getId();

        defaultTblOrderShouldBeFound("id.equals=" + id);
        defaultTblOrderShouldNotBeFound("id.notEquals=" + id);

        defaultTblOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultTblOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderNumber equals to DEFAULT_ORDER_NUMBER
        defaultTblOrderShouldBeFound("orderNumber.equals=" + DEFAULT_ORDER_NUMBER);

        // Get all the tblOrderList where orderNumber equals to UPDATED_ORDER_NUMBER
        defaultTblOrderShouldNotBeFound("orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderNumber not equals to DEFAULT_ORDER_NUMBER
        defaultTblOrderShouldNotBeFound("orderNumber.notEquals=" + DEFAULT_ORDER_NUMBER);

        // Get all the tblOrderList where orderNumber not equals to UPDATED_ORDER_NUMBER
        defaultTblOrderShouldBeFound("orderNumber.notEquals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderNumber in DEFAULT_ORDER_NUMBER or UPDATED_ORDER_NUMBER
        defaultTblOrderShouldBeFound("orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER);

        // Get all the tblOrderList where orderNumber equals to UPDATED_ORDER_NUMBER
        defaultTblOrderShouldNotBeFound("orderNumber.in=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderNumber is not null
        defaultTblOrderShouldBeFound("orderNumber.specified=true");

        // Get all the tblOrderList where orderNumber is null
        defaultTblOrderShouldNotBeFound("orderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderNumberContainsSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderNumber contains DEFAULT_ORDER_NUMBER
        defaultTblOrderShouldBeFound("orderNumber.contains=" + DEFAULT_ORDER_NUMBER);

        // Get all the tblOrderList where orderNumber contains UPDATED_ORDER_NUMBER
        defaultTblOrderShouldNotBeFound("orderNumber.contains=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderNumberNotContainsSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderNumber does not contain DEFAULT_ORDER_NUMBER
        defaultTblOrderShouldNotBeFound("orderNumber.doesNotContain=" + DEFAULT_ORDER_NUMBER);

        // Get all the tblOrderList where orderNumber does not contain UPDATED_ORDER_NUMBER
        defaultTblOrderShouldBeFound("orderNumber.doesNotContain=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderDate equals to DEFAULT_ORDER_DATE
        defaultTblOrderShouldBeFound("orderDate.equals=" + DEFAULT_ORDER_DATE);

        // Get all the tblOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultTblOrderShouldNotBeFound("orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderDate not equals to DEFAULT_ORDER_DATE
        defaultTblOrderShouldNotBeFound("orderDate.notEquals=" + DEFAULT_ORDER_DATE);

        // Get all the tblOrderList where orderDate not equals to UPDATED_ORDER_DATE
        defaultTblOrderShouldBeFound("orderDate.notEquals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderDate in DEFAULT_ORDER_DATE or UPDATED_ORDER_DATE
        defaultTblOrderShouldBeFound("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE);

        // Get all the tblOrderList where orderDate equals to UPDATED_ORDER_DATE
        defaultTblOrderShouldNotBeFound("orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderDate is not null
        defaultTblOrderShouldBeFound("orderDate.specified=true");

        // Get all the tblOrderList where orderDate is null
        defaultTblOrderShouldNotBeFound("orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderDateContainsSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderDate contains DEFAULT_ORDER_DATE
        defaultTblOrderShouldBeFound("orderDate.contains=" + DEFAULT_ORDER_DATE);

        // Get all the tblOrderList where orderDate contains UPDATED_ORDER_DATE
        defaultTblOrderShouldNotBeFound("orderDate.contains=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllTblOrdersByOrderDateNotContainsSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where orderDate does not contain DEFAULT_ORDER_DATE
        defaultTblOrderShouldNotBeFound("orderDate.doesNotContain=" + DEFAULT_ORDER_DATE);

        // Get all the tblOrderList where orderDate does not contain UPDATED_ORDER_DATE
        defaultTblOrderShouldBeFound("orderDate.doesNotContain=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllTblOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where status equals to DEFAULT_STATUS
        defaultTblOrderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the tblOrderList where status equals to UPDATED_STATUS
        defaultTblOrderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTblOrdersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where status not equals to DEFAULT_STATUS
        defaultTblOrderShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the tblOrderList where status not equals to UPDATED_STATUS
        defaultTblOrderShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTblOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTblOrderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the tblOrderList where status equals to UPDATED_STATUS
        defaultTblOrderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTblOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        // Get all the tblOrderList where status is not null
        defaultTblOrderShouldBeFound("status.specified=true");

        // Get all the tblOrderList where status is null
        defaultTblOrderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTblOrdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);
        TblPayment payment;
        if (TestUtil.findAll(em, TblPayment.class).isEmpty()) {
            payment = TblPaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, TblPayment.class).get(0);
        }
        em.persist(payment);
        em.flush();
        tblOrder.setPayment(payment);
        tblOrderRepository.saveAndFlush(tblOrder);
        Long paymentId = payment.getId();

        // Get all the tblOrderList where payment equals to paymentId
        defaultTblOrderShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the tblOrderList where payment equals to (paymentId + 1)
        defaultTblOrderShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllTblOrdersByShipToIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);
        TblContact shipTo;
        if (TestUtil.findAll(em, TblContact.class).isEmpty()) {
            shipTo = TblContactResourceIT.createEntity(em);
            em.persist(shipTo);
            em.flush();
        } else {
            shipTo = TestUtil.findAll(em, TblContact.class).get(0);
        }
        em.persist(shipTo);
        em.flush();
        tblOrder.setShipTo(shipTo);
        tblOrderRepository.saveAndFlush(tblOrder);
        Long shipToId = shipTo.getId();

        // Get all the tblOrderList where shipTo equals to shipToId
        defaultTblOrderShouldBeFound("shipToId.equals=" + shipToId);

        // Get all the tblOrderList where shipTo equals to (shipToId + 1)
        defaultTblOrderShouldNotBeFound("shipToId.equals=" + (shipToId + 1));
    }

    @Test
    @Transactional
    void getAllTblOrdersByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);
        TblItem items;
        if (TestUtil.findAll(em, TblItem.class).isEmpty()) {
            items = TblItemResourceIT.createEntity(em);
            em.persist(items);
            em.flush();
        } else {
            items = TestUtil.findAll(em, TblItem.class).get(0);
        }
        em.persist(items);
        em.flush();
        tblOrder.addItems(items);
        tblOrderRepository.saveAndFlush(tblOrder);
        Long itemsId = items.getId();

        // Get all the tblOrderList where items equals to itemsId
        defaultTblOrderShouldBeFound("itemsId.equals=" + itemsId);

        // Get all the tblOrderList where items equals to (itemsId + 1)
        defaultTblOrderShouldNotBeFound("itemsId.equals=" + (itemsId + 1));
    }

    @Test
    @Transactional
    void getAllTblOrdersByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);
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
        tblOrder.setCustomer(customer);
        tblOrderRepository.saveAndFlush(tblOrder);
        Long customerId = customer.getId();

        // Get all the tblOrderList where customer equals to customerId
        defaultTblOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the tblOrderList where customer equals to (customerId + 1)
        defaultTblOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblOrderShouldBeFound(String filter) throws Exception {
        restTblOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restTblOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblOrderShouldNotBeFound(String filter) throws Exception {
        restTblOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblOrder() throws Exception {
        // Get the tblOrder
        restTblOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblOrder() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();

        // Update the tblOrder
        TblOrder updatedTblOrder = tblOrderRepository.findById(tblOrder.getId()).get();
        // Disconnect from session so that the updates on updatedTblOrder are not directly saved in db
        em.detach(updatedTblOrder);
        updatedTblOrder.orderNumber(UPDATED_ORDER_NUMBER).orderDate(UPDATED_ORDER_DATE).status(UPDATED_STATUS);
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(updatedTblOrder);

        restTblOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
        TblOrder testTblOrder = tblOrderList.get(tblOrderList.size() - 1);
        assertThat(testTblOrder.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testTblOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testTblOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingTblOrder() throws Exception {
        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();
        tblOrder.setId(count.incrementAndGet());

        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblOrder() throws Exception {
        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();
        tblOrder.setId(count.incrementAndGet());

        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblOrder() throws Exception {
        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();
        tblOrder.setId(count.incrementAndGet());

        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblOrderWithPatch() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();

        // Update the tblOrder using partial update
        TblOrder partialUpdatedTblOrder = new TblOrder();
        partialUpdatedTblOrder.setId(tblOrder.getId());

        partialUpdatedTblOrder.orderNumber(UPDATED_ORDER_NUMBER).status(UPDATED_STATUS);

        restTblOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblOrder))
            )
            .andExpect(status().isOk());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
        TblOrder testTblOrder = tblOrderList.get(tblOrderList.size() - 1);
        assertThat(testTblOrder.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testTblOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testTblOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateTblOrderWithPatch() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();

        // Update the tblOrder using partial update
        TblOrder partialUpdatedTblOrder = new TblOrder();
        partialUpdatedTblOrder.setId(tblOrder.getId());

        partialUpdatedTblOrder.orderNumber(UPDATED_ORDER_NUMBER).orderDate(UPDATED_ORDER_DATE).status(UPDATED_STATUS);

        restTblOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblOrder))
            )
            .andExpect(status().isOk());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
        TblOrder testTblOrder = tblOrderList.get(tblOrderList.size() - 1);
        assertThat(testTblOrder.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testTblOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testTblOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingTblOrder() throws Exception {
        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();
        tblOrder.setId(count.incrementAndGet());

        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblOrder() throws Exception {
        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();
        tblOrder.setId(count.incrementAndGet());

        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblOrder() throws Exception {
        int databaseSizeBeforeUpdate = tblOrderRepository.findAll().size();
        tblOrder.setId(count.incrementAndGet());

        // Create the TblOrder
        TblOrderDTO tblOrderDTO = tblOrderMapper.toDto(tblOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblOrder in the database
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblOrder() throws Exception {
        // Initialize the database
        tblOrderRepository.saveAndFlush(tblOrder);

        int databaseSizeBeforeDelete = tblOrderRepository.findAll().size();

        // Delete the tblOrder
        restTblOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblOrder> tblOrderList = tblOrderRepository.findAll();
        assertThat(tblOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
