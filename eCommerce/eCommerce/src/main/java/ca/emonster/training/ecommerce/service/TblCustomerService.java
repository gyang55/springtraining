package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.repository.TblCustomerRepository;
import ca.emonster.training.ecommerce.service.dto.TblCustomerDTO;
import ca.emonster.training.ecommerce.service.mapper.TblCustomerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblCustomer}.
 */
@Service
@Transactional
public class TblCustomerService {

    private final Logger log = LoggerFactory.getLogger(TblCustomerService.class);

    private final TblCustomerRepository tblCustomerRepository;

    private final TblCustomerMapper tblCustomerMapper;

    public TblCustomerService(TblCustomerRepository tblCustomerRepository, TblCustomerMapper tblCustomerMapper) {
        this.tblCustomerRepository = tblCustomerRepository;
        this.tblCustomerMapper = tblCustomerMapper;
    }

    /**
     * Save a tblCustomer.
     *
     * @param tblCustomerDTO the entity to save.
     * @return the persisted entity.
     */
    public TblCustomerDTO save(TblCustomerDTO tblCustomerDTO) {
        log.debug("Request to save TblCustomer : {}", tblCustomerDTO);
        TblCustomer tblCustomer = tblCustomerMapper.toEntity(tblCustomerDTO);
        tblCustomer = tblCustomerRepository.save(tblCustomer);
        return tblCustomerMapper.toDto(tblCustomer);
    }

    /**
     * Partially update a tblCustomer.
     *
     * @param tblCustomerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblCustomerDTO> partialUpdate(TblCustomerDTO tblCustomerDTO) {
        log.debug("Request to partially update TblCustomer : {}", tblCustomerDTO);

        return tblCustomerRepository
            .findById(tblCustomerDTO.getId())
            .map(existingTblCustomer -> {
                tblCustomerMapper.partialUpdate(existingTblCustomer, tblCustomerDTO);

                return existingTblCustomer;
            })
            .map(tblCustomerRepository::save)
            .map(tblCustomerMapper::toDto);
    }

    /**
     * Get all the tblCustomers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblCustomerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblCustomers");
        return tblCustomerRepository.findAll(pageable).map(tblCustomerMapper::toDto);
    }

    /**
     * Get one tblCustomer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblCustomerDTO> findOne(Long id) {
        log.debug("Request to get TblCustomer : {}", id);
        return tblCustomerRepository.findById(id).map(tblCustomerMapper::toDto);
    }

    /**
     * Delete the tblCustomer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblCustomer : {}", id);
        tblCustomerRepository.deleteById(id);
    }
}
