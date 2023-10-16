package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteMapper noteMapper;

    public void addNote(Note note, int userId) {
        note.setUserId(userId);
        noteMapper.insertNote(note);
    }

    public void updateNote(Note newNote) {
        noteMapper.updateNote(newNote);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public List<Note> getNoteList(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getOneNote(noteId);
    }
}
