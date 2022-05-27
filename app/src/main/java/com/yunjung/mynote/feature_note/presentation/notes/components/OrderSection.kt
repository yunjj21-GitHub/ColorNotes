package com.yunjung.mynote.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunjung.mynote.feature_note.domain.util.NoteOrder
import com.yunjung.mynote.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder : NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange : (NoteOrder) -> Unit
){
    Column(
        modifier = modifier
    ) {
        // 제목순, 날짜순, 색상순 정렬 옵션
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text =  "제목순",
                checked = noteOrder is NoteOrder.Title,
                onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text =  "날짜순",
                checked = noteOrder is NoteOrder.Date,
                onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text =  "색상순",
                checked = noteOrder is NoteOrder.Color,
                onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 오름차순, 내림차순 정렬 옵션
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                DefaultRadioButton(
                    text =  "오름차순",
                    checked = noteOrder.orderType is OrderType.Ascending,
                    onSelect = {
                        onOrderChange(noteOrder.copy(OrderType.Ascending))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text =  "내림차순",
                    checked = noteOrder.orderType is OrderType.Descending,
                    onSelect = {
                        onOrderChange(noteOrder.copy(OrderType.Descending))
                    }
                )
            }
        }
    }
}