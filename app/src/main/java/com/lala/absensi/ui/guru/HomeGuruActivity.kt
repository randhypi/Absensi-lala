package com.lala.absensi.ui.guru

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.lala.absensi.model.ModelKehadiranMurid
import com.lala.absensi.ui.LoginMuridActivity
import com.lala.absensi.ui.ui.theme.AbsensiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeGuruActivity : ComponentActivity() {


    val viewModel: GuruViewModel by viewModels()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDataKehadiranMurid()


        setContent {
            Log.d("DATA KEHADIRAN value", "${viewModel.dataKehadiranMurid.value}")
            AbsensiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GuruHome()
                }
            }
        }
    }

    override fun onBackPressed() {
        finishAndRemoveTask()
        System.exit(0)
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
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(modifier =
            modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text("Hi, " + viewModel.dataGuru.value.nama,
                        modifier = modifier.padding(10.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Button(modifier = modifier
                        .width(100.dp)
                        .height(50.dp),
                        onClick = {
                            viewModel.auth.signOut()
                            coroutineScope.launch {
                                delay(2000)
                                val intent =
                                    Intent(this@HomeGuruActivity, LoginMuridActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }
                        }
                    ) {
                        Text("Keluar")
                    }
                }
            }
            Spacer(modifier = modifier.height(5.dp))

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
                    Tampilan(data = viewModel.dataKepulanganMurid.value)
                }
            }
        }
    }




    @Composable
    fun Tampilan(modifier: Modifier = Modifier, data: List<ModelKehadiranMurid>) {

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
                            Text(value.dataMurid.nama,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier.height(4.dp))
                            Text(value.dataMurid.nis,
                                fontSize = 20.sp
                            )
                            Spacer(modifier.height(4.dp))
                            Text(value.dataMurid.email,
                                fontSize = 20.sp
                            )

                            Spacer(modifier.height(4.dp))
                            Text(value.dataMurid.jurusan,
                                fontSize = 20.sp
                            )
                            Spacer(modifier.height(4.dp))
                            Text(value.dataMurid.jenisKelamin,
                                fontSize = 20.sp
                            )

                            Spacer(modifier.height(8.dp))
                            Text(value.lokasi,
                                fontSize = 20.sp
                            )

                            Spacer(modifier.height(4.dp))
                            Text(value.hariTanggal +
                                    value.waktuMasuk,
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

@Composable
fun TextCard(modifier: Modifier = Modifier,header: String,content: String) {
    Column(modifier.padding(20.dp)) {
        Text(
            header + " :",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,

        )
        Spacer(modifier = modifier.height(2.dp))
        Text(
            content,
            fontSize = 16.sp,
            )
        Spacer(modifier = modifier.height(2.dp))
        Canvas(modifier = modifier.fillMaxWidth()){
            drawLine(
                start = Offset(0f,0f),
                end = Offset(size.width,0f),
                color = Color.LightGray,
                strokeWidth = 3f
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TextCardPreview() {
    TextCard(header = "Email", content = "Randhypradanairsan@gmail.com" )
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


