// package com.socialbox.group.ui
//
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.fillMaxHeight
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.lazy.LazyColumn
// import androidx.compose.material.Card
// import androidx.compose.material.Icon
// import androidx.compose.material.IconButton
// import androidx.compose.material.Text
// import androidx.compose.material.TopAppBar
// import androidx.compose.material.icons.Icons.Filled
// import androidx.compose.material.icons.filled.Notifications
// import androidx.compose.material.icons.filled.Search
// import androidx.compose.material.icons.filled.SupervisedUserCircle
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.livedata.observeAsState
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview
// import androidx.compose.ui.unit.dp
// import com.socialbox.group.data.dto.GroupDTO
//
// @Composable
// fun GroupScreen(
//   // groupViewModel: GroupViewModel
// ) {
//   // val groupDTOs = groupViewModel.groupState.observeAsState(initial = listOf())
//
//   Column(
//     modifier = Modifier
//       .fillMaxHeight()
//       .fillMaxWidth()
//   ) {
//
//     TopAppBar(
//       title = { Text(text = "SocialBox") },
//       elevation = 12.dp,
//       modifier = Modifier.padding(bottom = 10.dp),
//       actions = {
//         IconButton(onClick = { /*TODO*/ }) {
//           Icon(Filled.Search, contentDescription = "Search")
//         }
//         IconButton(onClick = { /*TODO*/ }) {
//           Icon(Filled.Notifications, contentDescription = "Search")
//         }
//         IconButton(onClick = { /*TODO*/ }) {
//           Icon(Filled.SupervisedUserCircle, contentDescription = "Search")
//         }
//       }
//     )
//     Column {
//       LazyColumn {
//         items(1) {
//           for (group in listOf(GroupDTO(groupName = "name", memberCount = 3))) {
//             Card(
//               elevation = 2.dp,
//               modifier = Modifier
//                 .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
//                 .fillMaxWidth()
//                 .height(40.dp)
//             ) {
//               Text(text = group.groupName, modifier = Modifier.padding(start = 10.dp, top = 5.dp))
//               Text(text = "${group.memberCount} members")
//             }
//           }
//         }
//       }
//     }
//   }
// }
//
// @Preview
// @Composable
// fun Preview() {
//   GroupScreen()
// }