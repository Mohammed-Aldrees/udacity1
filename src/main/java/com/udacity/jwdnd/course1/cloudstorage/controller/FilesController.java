package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.Objects;

@Controller
@RequestMapping("/file")
@AllArgsConstructor
public class FilesController {
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("delete")
    public String deleteController(@RequestParam("fileid") int fileId) {
        try {
            File file = fileService.getFileById(fileId);

            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            if (!Objects.equals(currentUser.getUserId(), file.getUserId())) {
                return "redirect:/result?status=3";
            }
            fileService.deleteFile(fileId);
            return "redirect:/result?status=0";
        } catch (Exception e) {
            return "redirect:/result?status=1";
        }

    }

    @GetMapping("download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileid") int fileId) throws SQLException, IOException {
        File file = fileService.getFileById(fileId);
        HttpHeaders headers = new HttpHeaders();
        User currentUser = userService.getUser(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());

        if (!Objects.equals(currentUser.getUserId(), file.getUserId())) {
            headers.add("Location", "/result?status=3");
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        }

        ByteArrayResource fileBytes = new ByteArrayResource(file.getFileData().getBinaryStream().readAllBytes());


        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .headers(headers)
                .body(new InputStreamResource(fileBytes.getInputStream()));
    }

    @PostMapping("upload")
    public String homePost(@RequestParam("fileUpload") MultipartFile uploadedFile, Model model) throws SQLException, IOException {
        try {
            boolean isUploadSuccessful, isNoteSuccessful, isCredentialSuccessful = false;
            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            fileService.uploadFile(uploadedFile, currentUser.getUserId());
        } catch (FileAlreadyExistsException e) {
            return "redirect:/result?status=2";
        } catch (FileUploadException e) {
            return "redirect:/result?status=6";
        } catch (Error e) {
            return "redirect:/result?status=1";
        }
        return "redirect:/result?status=0";
    }
}
