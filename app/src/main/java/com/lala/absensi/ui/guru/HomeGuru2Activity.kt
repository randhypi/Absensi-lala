package com.lala.absensi.ui.guru

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.lala.absensi.model.ModelMurid
import com.lala.absensi.ui.LoginMuridActivity
import com.lala.absensi.ui.ui.theme.AbsensiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeGuru2Activity : ComponentActivity() {


    val viewModel: GuruViewModel by viewModels()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDataKehadirdanMurid()


        setContent {

            AbsensiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GuruHome()
                }
            }
        }
    }


    @ExperimentalPagerApi
    @Composable
    fun GuruHome(modifier: Modifier = Modifier) {

        val tabData = listOf(
            "MASUK",
            "PULANG",
        )

        val pagerState = rememberPagerState(
            pageCount = tabData.size,
            initialOffscreenLimit = 2,
            infiniteLoop = false,
            initialPage = 0
        )

        val tabIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()

        Column {
            Button(modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
                onClick = {
                    viewModel.auth.signOut()
                    coroutineScope.launch {
                        delay(2000)
                        val intent = Intent(this@HomeGuru2Activity, LoginMuridActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
            ) {
                Text("Keluar")
            }
            TabRow(
                selectedTabIndex = tabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                tabData.forEachIndexed { index, text ->
                    Tab(selected = tabIndex == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(text = text)
                    })
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { index ->
                if (index == 0) {
                    Tampilan(data = viewModel.dataKehadiranMurid.value)
                } else {
                    Tampilan(data = viewModel.dataKehadiranMurid.value)
                }
            }
        }
    }


    @Composable
    fun Tampilan(modifier: Modifier = Modifier, data: List<ModelMurid>) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = data) { value ->
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Text(value.nis,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier.height(4.dp))
                            Text(value.nama,
                                fontSize = 20.sp
                            )
                            Spacer(modifier.height(4.dp))
                            Text(value.kehadiranMurid.hariTanggal +
                                    value.kehadiranMurid.waktuMasuk,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun TampilanMasukPreview() {
        Tampilan(data = viewModel.dataKehadiranMurid.value)
    }

    @ExperimentalPagerApi
    @Preview
    @Composable
    fun GuruHomePreview() {
        GuruHome()
    }


}


@Preview
@Composable
fun CardPreview(modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("NIS",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier.height(4.dp))
            Text("Nama",
                fontSize = 20.sp
            )
            Spacer(modifier.height(4.dp))
            Text("Waktu",
                fontSize = 20.sp
            )
        }
    }

}


