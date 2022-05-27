package com.yunjung.mynote.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.mynote.feature_note.domain.model.Note
import com.yunjung.mynote.feature_note.domain.use_case.NoteUseCases
import com.yunjung.mynote.feature_note.domain.util.NoteOrder
import com.yunjung.mynote.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state : State<NotesState> = _state

    private var recentlyDeletedNote : Note? = null

    private var getNotesJob : Job?= null

    init{
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order -> { // 정렬 이벤트 발생시
                if(state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType
                ){ // 정렬기준과 정렬방식이 이전과 동일하다면
                    return
                }
                // 새로이 정렬된 메모 리스트를 가져온다.
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> { // 메모 삭제 이벤트 발생시
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note // 최근 삭제된 메모정보 갱신
                }
            }
            is NotesEvent.RestoreNote->{ // 메모 복구 이벤트 발생시
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null // 최근 삭제된 메모정보를 비운다.
                }
            }
            is NotesEvent.ToggleOrderSection->{ // 정렬 아이콘 클릭 이벤트 발생시
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}