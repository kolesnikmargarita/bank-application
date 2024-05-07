package by.kolesnik.course.bank.controller;

import by.kolesnik.course.bank.dto.UploadResultDto;
import by.kolesnik.course.bank.service.UserImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/{id}/image")
public class UserImageController {

    private final UserImageService imageService;

    public UserImageController(UserImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public UploadResultDto upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return imageService.save(id, file);
    }

    @GetMapping
    public ResponseEntity<Resource> find(@PathVariable Long id) {
        final Resource resource = imageService.findByUserId(id);

        final String header = """
        attachment;filename="%s";
        """
                .formatted(resource.getFilename());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, header)
                .body(resource);

    }
}
