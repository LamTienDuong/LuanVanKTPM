package vn.luanvan.ktpm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.luanvan.ktpm.domain.response.file.ResUploadFileDTO;
import vn.luanvan.ktpm.domain.response.file.ResUploadMultipartFileDTO;
import vn.luanvan.ktpm.service.FileService;
import vn.luanvan.ktpm.util.annotation.ApiMessage;
import vn.luanvan.ktpm.util.error.FileUploadException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
            ) throws URISyntaxException, IOException, FileUploadException {
        // skip validate
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is empty. Please upload a file.");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new FileUploadException("Invalid file extension. Only allows " + allowedExtensions.toString());
        }

        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);
        // store file
        String uploadFile = this.fileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/files/multipartfile")
    @ApiMessage("Upload multipartfile file")
    public ResponseEntity<ResUploadFileDTO> uploadMultipartFile(
            @RequestParam(name = "file", required = false) MultipartFile[] file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException, FileUploadException {
        // skip validate
        if (file == null || file.length == 0) {
            throw new FileUploadException("File is empty. Please upload a file.");
        }

        List<String> listFileName = new ArrayList<>();
        listFileName = Arrays.stream(file).map(item -> item.getOriginalFilename()).collect(Collectors.toList());

        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        AtomicBoolean isValid = new AtomicBoolean(true);

        listFileName.forEach((fileName) -> {
            if (!allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item))) {
                isValid.set(false);
            }
        });

        if (!isValid.get()) {
            throw new FileUploadException("Invalid file extension. Only allows " + allowedExtensions.toString());
        }

        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);
        // store file
        List<String> listUploadFile = Arrays.stream(file).map(item -> {
            try {
                return this.fileService.store(item, folder);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        ResUploadMultipartFileDTO res = new ResUploadMultipartFileDTO();

        System.out.println(listUploadFile);
            return null;
    }

    @GetMapping("/files")
    @ApiMessage("Download a single file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder
    ) throws FileUploadException, URISyntaxException, FileNotFoundException {

        if (fileName == null || folder == null) {
            throw new FileUploadException("Missing request params: (fileName or folder)");
        }

        // check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw  new FileUploadException("File with name = " + fileName + " not found");
        }

        // download a file
        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
