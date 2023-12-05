package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.repository.TblOrderRepository;
import ca.emonster.training.ecommerce.service.dto.TblOrderDTO;
import ca.emonster.training.ecommerce.service.mapper.TblOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblOrder}.
 */
@Service
@Transactional
public class TblOrderService {

    private final Logger log = LoggerFactory.getLogger(TblOrderService.class);

    private final TblOrderRepository tblOrderRepository;

    private final TblOrderMapper tblOrderMapper;

    public TblOrderService(TblOrderRepository tblOrderRepository, TblOrderMapper tblOrderMapper) {
        this.tblOrderRepository = tblOrderRepository;
        this.tblOrderMapper = tblOrderMapper;
    }

    /**
     * Save a tblOrder.
     *
     * @param tblOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public TblOrderDTO save(TblOrderDTO tblOrderDTO) {
        log.debug("Request to save TblOrder : {}", tblOrderDTO);
        TblOrder tblOrder = tblOrderMapper.toEntity(tblOrderDTO);
        tblOrder = tblOrderRepository.save(tblOrder);
        return tblOrderMapper.toDto(tblOrder);
    }

    /**
     * Partially update a tblOrder.
     *
     * @param tblOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblOrderDTO> partialUpdate(TblOrderDTO tblOrderDTO) {
        log.debug("Request to partially update TblOrder : {}", tblOrderDTO);

        return tblOrderRepository
            .findById(tblOrderDTO.getId())
            .map(existingTblOrder -> {
                tblOrderMapper.partialUpdate(existingTblOrder, tblOrderDTO);

                return existingTblOrder;
            })
            .map(tblOrderRepository::save)
            .map(tblOrderMapper::toDto);
    }

    /**
     * Get all the tblOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblOrders");
        return tblOrderRepository.findAll(pageable).map(tblOrderMapper::toDto);
    }

    /**
     * Get one tblOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblOrderDTO> findOne(Long id) {
        log.debug("Request to get TblOrder : {}", id);
        return tblOrderRepository.findById(id).map(tblOrderMapper::toDto);
    }

    /**
     * Delete the tblOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblOrder : {}", id);
        tblOrderRepository.deleteById(id);
    }
}
