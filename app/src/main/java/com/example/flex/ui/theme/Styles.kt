package com.example.flex.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.TextFieldColors

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexSegmentedButtonColors(): SegmentedButtonColors = SegmentedButtonColors(
     activeContainerColor = Blue100,
 activeContentColor= Navy900,
 activeBorderColor= Blue900,
// enabled & inactive
 inactiveContainerColor= Blue50,
 inactiveContentColor= Navy900,
 inactiveBorderColor= Navy900,
// disable & active
 disabledActiveContainerColor= Blue50,
 disabledActiveContentColor= Blue700,
 disabledActiveBorderColor= Navy900,
// disable & inactive
 disabledInactiveContainerColor= Blue50,
 disabledInactiveContentColor = Blue500,
 disabledInactiveBorderColor= Navy900
)


@Composable

fun FlexTextFieldColors(): TextFieldColors = TextFieldColors(
 focusedTextColor = Color.Black,
 unfocusedTextColor = Color.Black,
 disabledTextColor = Color.Gray,
 errorTextColor = Color.Red,
 focusedContainerColor = Color.Transparent,
 unfocusedContainerColor = Color.Transparent,
 disabledContainerColor = Color.Transparent,
 errorContainerColor = Color.Transparent,
 cursorColor = Blue400,
 errorCursorColor = Blue400,
 textSelectionColors = TextSelectionColors(Blue500, Blue50),
 focusedIndicatorColor = Green400,
 unfocusedIndicatorColor = Blue200,
 disabledIndicatorColor = Navy200,
 errorIndicatorColor = Color.Red,
 focusedLeadingIconColor = Navy900,
 unfocusedLeadingIconColor = Navy900,
 disabledLeadingIconColor = Navy900,
 errorLeadingIconColor = Color.Red,
 focusedTrailingIconColor = Pink900,
 unfocusedTrailingIconColor = Pink900,
 disabledTrailingIconColor = Pink900,
 errorTrailingIconColor = Pink900,
 focusedLabelColor = Navy500,
 unfocusedLabelColor = Navy500,
 disabledLabelColor = Blue100,
 errorLabelColor = Color.Red,
 focusedPlaceholderColor = Color.Gray,
 unfocusedPlaceholderColor = Color.Gray,
 disabledPlaceholderColor = Color.Gray,
 errorPlaceholderColor = Color.Gray,
 focusedSupportingTextColor = Color.Gray,
 unfocusedSupportingTextColor = Color.Gray,
 disabledSupportingTextColor = Color.Gray,
 errorSupportingTextColor = Color.Gray,
 focusedPrefixColor = Color.Gray,
 unfocusedPrefixColor = Color.Gray,
 disabledPrefixColor = Color.Gray,
 errorPrefixColor = Color.Gray,
 focusedSuffixColor = Color.Gray,
 unfocusedSuffixColor = Color.Gray,
 disabledSuffixColor = Color.Gray,
 errorSuffixColor = Color.Gray
)

@Composable
fun FlexButtonColors(): ButtonColors = ButtonColors(
 containerColor = Blue100,
 contentColor = Navy900,
 disabledContainerColor = Navy200,
 disabledContentColor = Navy900
)

@Composable
fun FlexOutlinedButtonColors():
        ButtonColors = ButtonColors(
 containerColor = Color.Transparent,
 contentColor = Navy500,
 disabledContainerColor = Color.Transparent,
 disabledContentColor = Navy200
)


fun FlexRadioButtonColors(): RadioButtonColors =  RadioButtonColors(
 selectedColor = Green400,
 unselectedColor = Color.Gray,
 disabledSelectedColor = Navy500,
 disabledUnselectedColor = Color.LightGray
)