package com.yunjung.mynote.feature_note.domain.use_case

import com.yunjung.mynote.feature_note.domain.model.InvalidNoteException
import com.yunjung.mynote.feature_note.domain.model.Note
import com.yunjung.mynote.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository : NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if(note.title.isBlank()){
            throw  InvalidNoteException("메모의 제목이 비었습니다.")
        }
        if(note.content.isBlank()){
            throw InvalidNoteException("메모의 내용이 비었습니다.")
        }
        repository.insertNote(note)
    }
}