package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
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
@RequestMapping("/note")
@AllArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    @PostMapping
    public String postNote(Model model, Note note) {
        try {
            if (note.getNoteId() != null) {
                return "forward:/note/update";
            }
            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            noteService.addNote(note, currentUser.getUserId());
            return "redirect:/result?status=0";
        } catch (Error e) {
            return "redirect:/result?status=4";
        }

    }

    @GetMapping("delete")
    public String getDeleteNote(@RequestParam("noteId") Integer noteId) {
        try {
            Note note = noteService.getNoteById(noteId);

            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            if (!Objects.equals(currentUser.getUserId(), note.getUserId())) {
                return "redirect:/result?status=3";
            }
            noteService.deleteNote(noteId);
            return "redirect:/result?status=0";
        } catch (Error e) {
            return "redirect:/result?status=6";
        }
    }

    @PostMapping("/update")
    public String postUpdateNote(Model model, Note note) {
        try {
            noteService.updateNote(note);
            return "redirect:/result?status=0";
        } catch (Error e) {
            return "redirect:/result?status=5";
        }

    }
}
