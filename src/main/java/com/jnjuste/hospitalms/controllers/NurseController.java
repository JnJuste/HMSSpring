package com.jnjuste.hospitalms.controllers;

import com.jnjuste.hospitalms.models.Nurse;
import com.jnjuste.hospitalms.services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable UUID id) {
        return ResponseEntity.ok(nurseService.getNurseById(id));
    }

    @GetMapping
    public ResponseEntity<List<Nurse>> getAllNurses() {
        return ResponseEntity.ok(nurseService.getAllNurses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable UUID id, @RequestBody Nurse nurse) {
        return ResponseEntity.ok(nurseService.updateNurse(id, nurse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable UUID id) {
        nurseService.deleteNurse(id);
        return ResponseEntity.ok().build();
    }
}
