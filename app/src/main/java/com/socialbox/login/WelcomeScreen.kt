// package com.socialbox.login
//
// import android.content.Intent
// import androidx.compose.foundation.Image
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.fillMaxHeight
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.width
// import androidx.compose.material.Button
// import androidx.compose.material.ButtonDefaults
// import androidx.compose.material.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.platform.LocalContext
// import androidx.compose.ui.res.painterResource
// import androidx.compose.ui.tooling.preview.Preview
// import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp
// import com.socialbox.R.drawable
// import com.socialbox.login.ui.login.LoginActivity
// import com.socialbox.theme.logoSansFamily
//
// @Composable
// fun WelcomeScreen() {
//   Column(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
//     Column(modifier = Modifier.padding(start = 25.dp, end = 25.dp)) {
//       Text(
//         text = "Social Box", fontSize = 80.sp, fontFamily = logoSansFamily,
//         modifier = Modifier.padding(top = 55.dp)
//       )
//       Image(
//         painter = painterResource(drawable.app_logo),
//         contentDescription = "Social Box Logo",
//         modifier = Modifier
//           .fillMaxWidth()
//           .height(470.dp)
//       )
//     }
//     Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
//       Row {
//         SignButton(text = "Sign In", LoginActivity::class.java)
//         SignButton(text = "Sign Up", LoginActivity::class.java)
//       }
//     }
//   }
// }
//
// @Composable
// fun <T> SignButton(text: String, clazz: Class<T>) {
//   val currentContext = LocalContext.current
//   val intent = Intent(currentContext, clazz)
//
//   Button(
//     onClick = {
//       currentContext.startActivity(intent)
//     },
//     colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF26C6DA)),
//     modifier = Modifier
//       .width(200.dp)
//       .height(100.dp)
//   ) {
//     Text(text = text)
//   }
// }
//
// @Preview(showBackground = true)
// @Composable
// fun DefaultPreview() {
//   WelcomeScreen()
// }