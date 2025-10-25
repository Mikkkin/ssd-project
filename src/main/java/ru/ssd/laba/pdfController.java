package ru.ssd.laba;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class pdfController {

    @GetMapping("/{filename}")
    public ResponseEntity<InputStreamResource> inline(@PathVariable String filename) throws IOException {
        Resource pdf = new ClassPathResource("static/docs/" + filename);
        if (!pdf.exists()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdf.contentLength())
                .body(new InputStreamResource(pdf.getInputStream()));
    }
}
