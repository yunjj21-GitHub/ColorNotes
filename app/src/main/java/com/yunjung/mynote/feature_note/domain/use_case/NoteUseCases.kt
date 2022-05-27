package com.yunjung.mynote.feature_note.domain.use_case

data class NoteUseCases(
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val getNotes: GetNotes,
    val getNote : GetNote
)
