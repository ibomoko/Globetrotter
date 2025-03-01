package com.me.globetrotter.controller;

import com.me.globetrotter.service.DestinationService;
import com.me.globetrotter.validator.File;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/destinations")
@PreAuthorize("hasAuthority('ADMIN')")
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping(value = "/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public HttpStatus importDestinationRecords(@Validated @File(contentTypes = {"application/json"}) @RequestPart MultipartFile file) {
        destinationService.importDestinationRecords(file);
        return HttpStatus.OK;
    }
}
