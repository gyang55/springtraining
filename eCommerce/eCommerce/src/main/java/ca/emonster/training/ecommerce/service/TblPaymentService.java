package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblPayment;
import ca.emonster.training.ecommerce.repository.TblPaymentRepository;
import ca.emonster.training.ecommerce.service.dto.TblPaymentDTO;
import ca.emonster.training.ecommerce.service.mapper.TblPaymentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblPayment}.
 */
@Service
@Transactional
public class TblPaymentService {

    private final Logger log = LoggerFactory.getLogger(TblPaymentService.class);

    private final TblPaymentRepository tblPaymentRepository;

    private final TblPaymentMapper tblPaymentMapper;

    public TblPaymentService(TblPaymentRepository tblPaymentRepository, TblPaymentMapper tblPaymentMapper) {
        this.tblPaymentRepository = tblPaymentRepository;
        this.tblPaymentMapper = tblPaymentMapper;
    }

    /**
     * Save a tblPayment.
     *
     * @param tblPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    public TblPaymentDTO save(TblPaymentDTO tblPaymentDTO) {
        log.debug("Request to save TblPayment : {}", tblPaymentDTO);
        TblPayment tblPayment = tblPaymentMapper.toEntity(tblPaymentDTO);
        tblPayment = tblPaymentRepository.save(tblPayment);
        return tblPaymentMapper.toDto(tblPayment);
    }

    /**
     * Partially update a tblPayment.
     *
     * @param tblPaymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblPaymentDTO> partialUpdate(TblPaymentDTO tblPaymentDTO) {
        log.debug("Request to partially update TblPayment : {}", tblPaymentDTO);

        return tblPaymentRepository
            .findById(tblPaymentDTO.getId())
            .map(existingTblPayment -> {
                tblPaymentMapper.partialUpdate(existingTblPayment, tblPaymentDTO);

                return existingTblPayment;
            })
            .map(tblPaymentRepository::save)
            .map(tblPaymentMapper::toDto);
    }

    /**
     * Get all the tblPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblPaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblPayments");
        return tblPaymentRepository.findAll(pageable).map(tblPaymentMapper::toDto);
    }

    /**
     *  Get all the tblPayments where Order is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TblPaymentDTO> findAllWhereOrderIsNull() {
        log.debug("Request to get all tblPayments where Order is null");
        return StreamSupport
            .stream(tblPaymentRepository.findAll().spliterator(), false)
            .filter(tblPayment -> tblPayment.getOrder() == null)
            .map(tblPaymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tblPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblPaymentDTO> findOne(Long id) {
        log.debug("Request to get TblPayment : {}", id);
        return tblPaymentRepository.findById(id).map(tblPaymentMapper::toDto);
    }

    /**
     * Delete the tblPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblPayment : {}", id);
        tblPaymentRepository.deleteById(id);
    }
}
