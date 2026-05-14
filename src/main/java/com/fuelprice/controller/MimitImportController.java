package com.fuelprice.controller;

import com.fuelprice.dto.ImportResultResponse;
import com.fuelprice.service.MimitFuelImportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/import")
@CrossOrigin
public class MimitImportController {
    private final MimitFuelImportService importService;

    public MimitImportController(MimitFuelImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/mimit")
    public ImportResultResponse importMimit() {
        return importService.importFromMimit();
    }
}
