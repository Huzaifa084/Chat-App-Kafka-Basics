package com.devaxiom.chatappkafka.files;

import com.devaxiom.chatappkafka.advices.ApiResponse;
import com.devaxiom.chatappkafka.services.PrincipalUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;

@Slf4j
@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileDataController {
    private final FileDataService fileDataService;
    private final PrincipalUserService principalUserService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = fileDataService.uploadFile(file, principalUserService.getLoggedInUserId());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        ApiResponse<Object> response = new ApiResponse<>(fileDownloadUri, "File uploaded successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {

        if (!fileDataService.isUserAuthorized(fileName, principalUserService.getLoggedInUserId()))
            throw new AccessDeniedException("You are not authorized to access this file.");

        Resource resource = fileDataService.loadFileAsResource(fileName);

        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        try {
            contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (IOException ex) {
            log.warn("Could not determine file type for {}", fileName);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", resource.getFilename()))
                .body(resource);
    }
}