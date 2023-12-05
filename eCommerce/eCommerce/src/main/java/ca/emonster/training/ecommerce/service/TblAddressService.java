package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblAddress;
import ca.emonster.training.ecommerce.repository.TblAddressRepository;
import ca.emonster.training.ecommerce.service.dto.TblAddressDTO;
import ca.emonster.training.ecommerce.service.mapper.TblAddressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblAddress}.
 */
@Service
@Transactional
public class TblAddressService {

    private final Logger log = LoggerFactory.getLogger(TblAddressService.class);

    private final TblAddressRepository tblAddressRepository;

    private final TblAddressMapper tblAddressMapper;

    public TblAddressService(TblAddressRepository tblAddressRepository, TblAddressMapper tblAddressMapper) {
        this.tblAddressRepository = tblAddressRepository;
        this.tblAddressMapper = tblAddressMapper;
    }

    /**
     * Save a tblAddress.
     *
     * @param tblAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public TblAddressDTO save(TblAddressDTO tblAddressDTO) {
        log.debug("Request to save TblAddress : {}", tblAddressDTO);
        TblAddress tblAddress = tblAddressMapper.toEntity(tblAddressDTO);
        tblAddress = tblAddressRepository.save(tblAddress);
        return tblAddressMapper.toDto(tblAddress);
    }

    /**
     * Partially update a tblAddress.
     *
     * @param tblAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblAddressDTO> partialUpdate(TblAddressDTO tblAddressDTO) {
        log.debug("Request to partially update TblAddress : {}", tblAddressDTO);

        return tblAddressRepository
            .findById(tblAddressDTO.getId())
            .map(existingTblAddress -> {
                tblAddressMapper.partialUpdate(existingTblAddress, tblAddressDTO);

                return existingTblAddress;
            })
            .map(tblAddressRepository::save)
            .map(tblAddressMapper::toDto);
    }

    /**
     * Get all the tblAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblAddresses");
        return tblAddressRepository.findAll(pageable).map(tblAddressMapper::toDto);
    }

    /**
     * Get one tblAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblAddressDTO> findOne(Long id) {
        log.debug("Request to get TblAddress : {}", id);
        return tblAddressRepository.findById(id).map(tblAddressMapper::toDto);
    }

    /**
     * Delete the tblAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblAddress : {}", id);
        tblAddressRepository.deleteById(id);
    }
}
