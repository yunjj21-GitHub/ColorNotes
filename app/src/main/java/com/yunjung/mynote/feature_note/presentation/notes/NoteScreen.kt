package com.yunjung.mynote.feature_note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yunjung.mynote.feature_note.presentation.notes.components.NoteItem
import com.yunjung.mynote.feature_note.presentation.notes.components.OrderSection
import com.yunjung.mynote.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel : NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // FloatingActionButton 클릭 이벤트 (AddEditNoteScreen으로 이동)
                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add , contentDescription = "Add note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 화면의 상단 부
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                // Title
                Text(
                    text = "메모장",
                    style = MaterialTheme.typography.h4
                )
                // Sort Menu Icon
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    },
                ){
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
            // Sort Menu
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Notes List
            LazyColumn(modifier = Modifier.fillMaxSize()){
                items(state.notes){ note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // NoteItem 클릭 이벤트 (AddEditNoteScreen으로 이동)
                                navController.navigate(Screen.AddEditNoteScreen.route
                                        + "?noteId=${note.id}&noteColor=${note.color}")
                            },
                        onDeleteClick = {
                            // 삭제하기 버튼 클릭 이벤트
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                // 스낵바를 띄운다.
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "메모가 삭제되었습니다.",
                                    actionLabel = "되돌리기"
                                )
                                // 스낵바의 되돌리기 버튼 클릭시
                                if(result == SnackbarResult.ActionPerformed){
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}