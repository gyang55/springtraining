package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.repository.TblContactRepository;
import ca.emonster.training.ecommerce.service.dto.TblContactDTO;
import ca.emonster.training.ecommerce.service.mapper.TblContactMapper;
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
 * Service Implementation for managing {@link TblContact}.
 */
@Service
@Transactional
public class TblContactService {

    private final Logger log = LoggerFactory.getLogger(TblContactService.class);

    private final TblContactRepository tblContactRepository;

    private final TblContactMapper tblContactMapper;

    public TblContactService(TblContactRepository tblContactRepository, TblContactMapper tblContactMapper) {
        this.tblContactRepository = tblContactRepository;
        this.tblContactMapper = tblContactMapper;
    }

    /**
     * Save a tblContact.
     *
     * @param tblContactDTO the entity to save.
     * @return the persisted entity.
     */
    public TblContactDTO save(TblContactDTO tblContactDTO) {
        log.debug("Request to save TblContact : {}", tblContactDTO);
        TblContact tblContact = tblContactMapper.toEntity(tblContactDTO);
        tblContact = tblContactRepository.save(tblContact);
        return tblContactMapper.toDto(tblContact);
    }

    /**
     * Partially update a tblContact.
     *
     * @param tblContactDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblContactDTO> partialUpdate(TblContactDTO tblContactDTO) {
        log.debug("Request to partially update TblContact : {}", tblContactDTO);

        return tblContactRepository
            .findById(tblContactDTO.getId())
            .map(existingTblContact -> {
                tblContactMapper.partialUpdate(existingTblContact, tblContactDTO);

                return existingTblContact;
            })
            .map(tblContactRepository::save)
            .map(tblContactMapper::toDto);
    }

    /**
     * Get all the tblContacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblContacts");
        return tblContactRepository.findAll(pageable).map(tblContactMapper::toDto);
    }

    /**
     *  Get all the tblContacts where Address is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TblContactDTO> findAllWhereAddressIsNull() {
        log.debug("Request to get all tblContacts where Address is null");
        return StreamSupport
            .stream(tblContactRepository.findAll().spliterator(), false)
            .filter(tblContact -> tblContact.getAddress() == null)
            .map(tblContactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the tblContacts where Order is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TblContactDTO> findAllWhereOrderIsNull() {
        log.debug("Request to get all tblContacts where Order is null");
        return StreamSupport
            .stream(tblContactRepository.findAll().spliterator(), false)
            .filter(tblContact -> tblContact.getOrder() == null)
            .map(tblContactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tblContact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblContactDTO> findOne(Long id) {
        log.debug("Request to get TblContact : {}", id);
        return tblContactRepository.findById(id).map(tblContactMapper::toDto);
    }

    /**
     * Delete the tblContact by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblContact : {}", id);
        tblContactRepository.deleteById(id);
    }
}
