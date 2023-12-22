package com.example.flex.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.sp


val bodyLabel = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.15.sp,
    baselineShift = BaselineShift.Subscript
)

val bodyValue = TextStyle(
    fontWeight = FontWeight.W400,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.15.sp,
    baselineShift = BaselineShift.Subscript,
    color = Color.DarkGray
)

val caption = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Italic,
    fontSize = 12.sp,
    lineHeight = 1.sp,
    letterSpacing = 0.15.sp,
    baselineShift = BaselineShift.Subscript,
    color = Color.Gray
)

val screenHeading = TextStyle(
    fontWeight = FontWeight.Light,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 32.sp,
    lineHeight = 40.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)

val subHeading = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 20.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)

val rowItem = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp,
    color = Navy900
)

val clock = TextStyle(
    fontWeight = FontWeight.Light,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 65.sp,
    lineHeight = 0.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.None
)

val banner = TextStyle(
    fontWeight = FontWeight.Light,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 65.sp,
    lineHeight = 70.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)
val bannerBold = TextStyle(
    fontWeight = FontWeight.ExtraBold,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 75.sp,
    lineHeight = 70.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)
val bannerSmall = TextStyle(
    fontWeight = FontWeight.Light,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 35.sp,
    lineHeight = 40.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)
val bannerCaption = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Italic,
    fontSize = 16.sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)

val instruction = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 20.sp,
    lineHeight = 30.0.sp,
    letterSpacing = 0.0.sp,
    baselineShift = BaselineShift.Subscript
)