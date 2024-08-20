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

    @PostMapping
    public void createBaseFee(@RequestBody BaseFeeDTO baseFeeDto) {
        baseFeeService.createBaseFee(baseFeeDto);
    }

    @GetMapping
    public ResponseEntity<List<BaseFeeDTO>> getAllBaseFees() {
        List<BaseFeeDTO> baseFees = baseFeeService.getAllBaseFees();
        return new ResponseEntity<>(baseFees, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public void updateBaseFee(@PathVariable Long id, @RequestBody BaseFeeDTO baseFeeDto) {
        baseFeeService.updateBaseFee(id, baseFeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaseFee(@PathVariable Long id) {
        baseFeeService.deleteBaseFee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}