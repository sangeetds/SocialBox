package com.socialbox.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.socialbox.R.drawable
import com.socialbox.ui.theme.logoSansFamily

@Composable
fun LoginScreen(loginFunction: () -> Unit) {
  Image(
    painter = painterResource(drawable.app_logo),
    contentDescription = "Social Box Logo",
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 160.dp)
      .height(470.dp)
      .alpha(0.2f)
      .padding(start = 25.dp, end = 25.dp)
  )
  Column(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
    Column(
      modifier = Modifier.padding(start = 25.dp, end = 25.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Social Box", fontSize = 80.sp, fontFamily = logoSansFamily,
        modifier = Modifier.padding(top = 55.dp)
      )
      val emailState = remember { mutableStateOf(TextFieldValue()) }
      val passwordState = remember { mutableStateOf(TextFieldValue()) }
      val passwordVisibility = remember { mutableStateOf(false) }
      val image = if (passwordVisibility.value)
        painterResource(id = drawable.design_ic_visibility)
      else
        painterResource(id = drawable.design_ic_visibility_off)

      TextField(
        value = emailState.value,
        onValueChange = { emailState.value = it },
        label = { Text("Email Id") },
        placeholder = { Text("Email-Id") },
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 100.dp)
      )
      TextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text("Password") },
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
          IconButton(onClick = {
            passwordVisibility.value = !passwordVisibility.value
          }) {
            Icon(painter = image, "")
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 50.dp)
      )
      Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF26C6DA)),
        modifier = Modifier
          .padding(top = 100.dp)
          .height(50.dp)
          .width(250.dp)
      ) {
        Text(text = "Login", fontSize = 20.sp)
      }
      Button(
        onClick = { loginFunction() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEC407A)),
        modifier = Modifier
          .padding(top = 10.dp)
          .height(50.dp)
          .width(250.dp)
      ) {
        Text(text = "Login With Google", fontSize = 20.sp, color = Color(0xFFFFFFFF))
      }
    }
  }
}

