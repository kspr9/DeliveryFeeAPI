package com.fujitsu.delivery_fee_api.controller;

import com.fujitsu.delivery_fee_api.dto.BaseFeeDTO;
import com.fujitsu.delivery_fee_api.service.BaseFeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees/base_fee")
public class BaseFeeController {

    private final BaseFeeService baseFeeService;

    public BaseFeeController(BaseFeeService baseFeeService) {
        this.baseFeeService = baseFeeService;
    }

    /**
     * Creates a new base fee.
     *
     * @param baseFeeDto    the base fee data transfer object containing the city ID and vehicle type ID
     */
    @PostMapping
    public void createBaseFee(@RequestBody BaseFeeDTO baseFeeDto) {
        baseFeeService.createBaseFee(baseFeeDto);
    }

    /**
     * Retrieves all base fees.
     *
     * @return  a list of BaseFeeDTO objects representing all base fees
     */
    @GetMapping
    public ResponseEntity<List<BaseFeeDTO>> getAllBaseFees() {
        List<BaseFeeDTO> baseFees = baseFeeService.getAllBaseFees();
        return new ResponseEntity<>(baseFees, HttpStatus.OK);
    }

    /**
     * Updates a base fee by ID.
     *
     * @param id          the ID of the base fee to update
     * @param baseFeeDto  the updated base fee data transfer object
     */
    @PutMapping("/{id}")
    public void updateBaseFee(@PathVariable Long id, @RequestBody BaseFeeDTO baseFeeDto) {
        baseFeeService.updateBaseFee(id, baseFeeDto);
    }

    /**
     * Deletes a base fee by its ID.
     *
     * @param id    the ID of the base fee to be deleted
     * @return      an HTTP response with a status code indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaseFee(@PathVariable Long id) {
        baseFeeService.deleteBaseFee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}