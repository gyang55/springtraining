package ca.emonster.training.ecommerce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.emonster.training.ecommerce.IntegrationTest;
import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import ca.emonster.training.ecommerce.domain.User;
import ca.emonster.training.ecommerce.domain.enumeration.CustomerStatus;
import ca.emonster.training.ecommerce.repository.TblCustomerRepository;
import ca.emonster.training.ecommerce.service.criteria.TblCustomerCriteria;
import ca.emonster.training.ecommerce.service.dto.TblCustomerDTO;
import ca.emonster.training.ecommerce.service.mapper.TblCustomerMapper;
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
 * Integration tests for the {@link TblCustomerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TblCustomerResourceIT {

    private static final CustomerStatus DEFAULT_STATUS = CustomerStatus.ACTIVE;
    private static final CustomerStatus UPDATED_STATUS = CustomerStatus.INACTIVE;

    private static final Long DEFAULT_TOTAL_SPEND = 1L;
    private static final Long UPDATED_TOTAL_SPEND = 2L;
    private static final Long SMALLER_TOTAL_SPEND = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/tbl-customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TblCustomerRepository tblCustomerRepository;

    @Autowired
    private TblCustomerMapper tblCustomerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTblCustomerMockMvc;

    private TblCustomer tblCustomer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblCustomer createEntity(EntityManager em) {
        TblCustomer tblCustomer = new TblCustomer().status(DEFAULT_STATUS).totalSpend(DEFAULT_TOTAL_SPEND);
        return tblCustomer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TblCustomer createUpdatedEntity(EntityManager em) {
        TblCustomer tblCustomer = new TblCustomer().status(UPDATED_STATUS).totalSpend(UPDATED_TOTAL_SPEND);
        return tblCustomer;
    }

    @BeforeEach
    public void initTest() {
        tblCustomer = createEntity(em);
    }

    @Test
    @Transactional
    void createTblCustomer() throws Exception {
        int databaseSizeBeforeCreate = tblCustomerRepository.findAll().size();
        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);
        restTblCustomerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeCreate + 1);
        TblCustomer testTblCustomer = tblCustomerList.get(tblCustomerList.size() - 1);
        assertThat(testTblCustomer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTblCustomer.getTotalSpend()).isEqualTo(DEFAULT_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void createTblCustomerWithExistingId() throws Exception {
        // Create the TblCustomer with an existing ID
        tblCustomer.setId(1L);
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        int databaseSizeBeforeCreate = tblCustomerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTblCustomerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTblCustomers() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList
        restTblCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalSpend").value(hasItem(DEFAULT_TOTAL_SPEND.intValue())));
    }

    @Test
    @Transactional
    void getTblCustomer() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get the tblCustomer
        restTblCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, tblCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tblCustomer.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalSpend").value(DEFAULT_TOTAL_SPEND.intValue()));
    }

    @Test
    @Transactional
    void getTblCustomersByIdFiltering() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        Long id = tblCustomer.getId();

        defaultTblCustomerShouldBeFound("id.equals=" + id);
        defaultTblCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultTblCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTblCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultTblCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTblCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTblCustomersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where status equals to DEFAULT_STATUS
        defaultTblCustomerShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the tblCustomerList where status equals to UPDATED_STATUS
        defaultTblCustomerShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTblCustomersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where status not equals to DEFAULT_STATUS
        defaultTblCustomerShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the tblCustomerList where status not equals to UPDATED_STATUS
        defaultTblCustomerShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTblCustomersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTblCustomerShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the tblCustomerList where status equals to UPDATED_STATUS
        defaultTblCustomerShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTblCustomersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where status is not null
        defaultTblCustomerShouldBeFound("status.specified=true");

        // Get all the tblCustomerList where status is null
        defaultTblCustomerShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend equals to DEFAULT_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.equals=" + DEFAULT_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend equals to UPDATED_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.equals=" + UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend not equals to DEFAULT_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.notEquals=" + DEFAULT_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend not equals to UPDATED_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.notEquals=" + UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsInShouldWork() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend in DEFAULT_TOTAL_SPEND or UPDATED_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.in=" + DEFAULT_TOTAL_SPEND + "," + UPDATED_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend equals to UPDATED_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.in=" + UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsNullOrNotNull() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend is not null
        defaultTblCustomerShouldBeFound("totalSpend.specified=true");

        // Get all the tblCustomerList where totalSpend is null
        defaultTblCustomerShouldNotBeFound("totalSpend.specified=false");
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend is greater than or equal to DEFAULT_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.greaterThanOrEqual=" + DEFAULT_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend is greater than or equal to UPDATED_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.greaterThanOrEqual=" + UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend is less than or equal to DEFAULT_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.lessThanOrEqual=" + DEFAULT_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend is less than or equal to SMALLER_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.lessThanOrEqual=" + SMALLER_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsLessThanSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend is less than DEFAULT_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.lessThan=" + DEFAULT_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend is less than UPDATED_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.lessThan=" + UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByTotalSpendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        // Get all the tblCustomerList where totalSpend is greater than DEFAULT_TOTAL_SPEND
        defaultTblCustomerShouldNotBeFound("totalSpend.greaterThan=" + DEFAULT_TOTAL_SPEND);

        // Get all the tblCustomerList where totalSpend is greater than SMALLER_TOTAL_SPEND
        defaultTblCustomerShouldBeFound("totalSpend.greaterThan=" + SMALLER_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void getAllTblCustomersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        tblCustomer.setUser(user);
        tblCustomerRepository.saveAndFlush(tblCustomer);
        Long userId = user.getId();

        // Get all the tblCustomerList where user equals to userId
        defaultTblCustomerShouldBeFound("userId.equals=" + userId);

        // Get all the tblCustomerList where user equals to (userId + 1)
        defaultTblCustomerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllTblCustomersByTaxExemptsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);
        TblTaxExempt taxExempts;
        if (TestUtil.findAll(em, TblTaxExempt.class).isEmpty()) {
            taxExempts = TblTaxExemptResourceIT.createEntity(em);
            em.persist(taxExempts);
            em.flush();
        } else {
            taxExempts = TestUtil.findAll(em, TblTaxExempt.class).get(0);
        }
        em.persist(taxExempts);
        em.flush();
        tblCustomer.addTaxExempts(taxExempts);
        tblCustomerRepository.saveAndFlush(tblCustomer);
        Long taxExemptsId = taxExempts.getId();

        // Get all the tblCustomerList where taxExempts equals to taxExemptsId
        defaultTblCustomerShouldBeFound("taxExemptsId.equals=" + taxExemptsId);

        // Get all the tblCustomerList where taxExempts equals to (taxExemptsId + 1)
        defaultTblCustomerShouldNotBeFound("taxExemptsId.equals=" + (taxExemptsId + 1));
    }

    @Test
    @Transactional
    void getAllTblCustomersByContactsIsEqualToSomething() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);
        TblContact contacts;
        if (TestUtil.findAll(em, TblContact.class).isEmpty()) {
            contacts = TblContactResourceIT.createEntity(em);
            em.persist(contacts);
            em.flush();
        } else {
            contacts = TestUtil.findAll(em, TblContact.class).get(0);
        }
        em.persist(contacts);
        em.flush();
        tblCustomer.addContacts(contacts);
        tblCustomerRepository.saveAndFlush(tblCustomer);
        Long contactsId = contacts.getId();

        // Get all the tblCustomerList where contacts equals to contactsId
        defaultTblCustomerShouldBeFound("contactsId.equals=" + contactsId);

        // Get all the tblCustomerList where contacts equals to (contactsId + 1)
        defaultTblCustomerShouldNotBeFound("contactsId.equals=" + (contactsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTblCustomerShouldBeFound(String filter) throws Exception {
        restTblCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tblCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalSpend").value(hasItem(DEFAULT_TOTAL_SPEND.intValue())));

        // Check, that the count call also returns 1
        restTblCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTblCustomerShouldNotBeFound(String filter) throws Exception {
        restTblCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTblCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTblCustomer() throws Exception {
        // Get the tblCustomer
        restTblCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTblCustomer() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();

        // Update the tblCustomer
        TblCustomer updatedTblCustomer = tblCustomerRepository.findById(tblCustomer.getId()).get();
        // Disconnect from session so that the updates on updatedTblCustomer are not directly saved in db
        em.detach(updatedTblCustomer);
        updatedTblCustomer.status(UPDATED_STATUS).totalSpend(UPDATED_TOTAL_SPEND);
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(updatedTblCustomer);

        restTblCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isOk());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
        TblCustomer testTblCustomer = tblCustomerList.get(tblCustomerList.size() - 1);
        assertThat(testTblCustomer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTblCustomer.getTotalSpend()).isEqualTo(UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void putNonExistingTblCustomer() throws Exception {
        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();
        tblCustomer.setId(count.incrementAndGet());

        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tblCustomerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTblCustomer() throws Exception {
        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();
        tblCustomer.setId(count.incrementAndGet());

        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTblCustomer() throws Exception {
        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();
        tblCustomer.setId(count.incrementAndGet());

        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTblCustomerWithPatch() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();

        // Update the tblCustomer using partial update
        TblCustomer partialUpdatedTblCustomer = new TblCustomer();
        partialUpdatedTblCustomer.setId(tblCustomer.getId());

        partialUpdatedTblCustomer.status(UPDATED_STATUS);

        restTblCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblCustomer))
            )
            .andExpect(status().isOk());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
        TblCustomer testTblCustomer = tblCustomerList.get(tblCustomerList.size() - 1);
        assertThat(testTblCustomer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTblCustomer.getTotalSpend()).isEqualTo(DEFAULT_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void fullUpdateTblCustomerWithPatch() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();

        // Update the tblCustomer using partial update
        TblCustomer partialUpdatedTblCustomer = new TblCustomer();
        partialUpdatedTblCustomer.setId(tblCustomer.getId());

        partialUpdatedTblCustomer.status(UPDATED_STATUS).totalSpend(UPDATED_TOTAL_SPEND);

        restTblCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTblCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTblCustomer))
            )
            .andExpect(status().isOk());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
        TblCustomer testTblCustomer = tblCustomerList.get(tblCustomerList.size() - 1);
        assertThat(testTblCustomer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTblCustomer.getTotalSpend()).isEqualTo(UPDATED_TOTAL_SPEND);
    }

    @Test
    @Transactional
    void patchNonExistingTblCustomer() throws Exception {
        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();
        tblCustomer.setId(count.incrementAndGet());

        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTblCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tblCustomerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTblCustomer() throws Exception {
        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();
        tblCustomer.setId(count.incrementAndGet());

        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTblCustomer() throws Exception {
        int databaseSizeBeforeUpdate = tblCustomerRepository.findAll().size();
        tblCustomer.setId(count.incrementAndGet());

        // Create the TblCustomer
        TblCustomerDTO tblCustomerDTO = tblCustomerMapper.toDto(tblCustomer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTblCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tblCustomerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TblCustomer in the database
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTblCustomer() throws Exception {
        // Initialize the database
        tblCustomerRepository.saveAndFlush(tblCustomer);

        int databaseSizeBeforeDelete = tblCustomerRepository.findAll().size();

        // Delete the tblCustomer
        restTblCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, tblCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TblCustomer> tblCustomerList = tblCustomerRepository.findAll();
        assertThat(tblCustomerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
