package com.yunjung.mynote.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yunjung.mynote.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.yunjung.mynote.feature_note.presentation.notes.NotesScreen
import com.yunjung.mynote.feature_note.presentation.util.Screen
import com.yunjung.mynote.ui.theme.MyNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNoteTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ){
                        // 이동될 화면 정의
                        // NotesScreen 화면으로의 이동
                        composable(route = Screen.NotesScreen.route){
                            NotesScreen(navController = navController)
                        }
                        // AddEditNoteScreen 화면으로의 이동
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ){
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ){
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ){
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController =  navController,
                                noteColor = color
                            )
                        }
                    }
                }
            }
        }
    }
}