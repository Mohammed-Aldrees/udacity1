package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/credential")
@AllArgsConstructor
public class CreateCredential {
    private final CredentialService credentialService;
    private final UserService userService;

    @PostMapping
    public String postCredential(Model model, Credential credential) {
        if (credential.getCredentialId() != null) {
            return "forward:/credential/update";
        }
        User currentUser = userService.getUser(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());
        try {
            credentialService.addCredential(credential, currentUser.getUserId());
            return "redirect:/result?status=0";
        } catch (Error e) {
            return "redirect:/result?status=4";
        }

    }

    @GetMapping("delete")
    public String deleteCredential(@RequestParam("credid") Integer credentialId) {
        Credential credential = credentialService.getCredentialById(credentialId);

        User currentUser = userService.getUser(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());

        if (!Objects.equals(currentUser.getUserId(), credential.getUserid())) {
            return "redirect:/result?status=3";
        }
        try {
            credentialService.deleteCredential(credentialId);
            return "redirect:/result?status=0";
        } catch (Error e) {
            return "redirect:/result?status=6";
        }
    }

    @PostMapping("update")
    public String postUpdateCredential(Model model, Credential credential) {
        try {
            credentialService.updateCredential(credential);
            return "redirect:/result?status=0";
        } catch (Error e) {
            return "redirect:/result?status=5";
        }
    }
}
