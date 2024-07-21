package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.services.NurseService;
import com.jnjuste.hospitalms.services.impl.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {
    private final NurseService nurseService;

    @Autowired
    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @PostMapping
    public ResponseEntity<Nurse> createNurse(@RequestBody Nurse nurse) {
        return ResponseEntity.ok(nurseService.saveNurse(nurse));
    }

    @GetMapping
    public ResponseEntity<List<Nurse>> getAllNurses() {
        return ResponseEntity.ok(nurseService.getAllNurses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable UUID id) {
        Optional<Nurse> nurse = nurseService.getNurseById(id);
        return nurse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable UUID id, @RequestBody Nurse nurseDetails) {
        try {
            return ResponseEntity.ok(nurseService.updateNurse(id, nurseDetails));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable UUID id) {
        nurseService.deleteNurse(id);
        return ResponseEntity.noContent().build();
    }
}
