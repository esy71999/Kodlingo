package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeBg
import com.example.ui.theme.CodeFg
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoCardBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoTextDark

@Composable
fun DebApkInfoDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val debScript = """
# HP G62 ve Düşük Donanımlı Linux Sistemler İçin Doğrudan .deb Kurulumu:
# (Waydroid gerektirmez, doğrudan Linux masaüstünde yerel çalışır!)

# 1. Proje ZIP dosyasını indirip arşivden çıkarın veya codelingo.deb dosyasını alın.
# 2. Terminalde paketi kurun:
sudo dpkg -i codelingo.deb
sudo apt-get install -f -y

# 3. Çalıştırın:
codelingo
# (Veya uygulama menüsünden CodeLingo ikonuna tıklayın)
    """.trimIndent()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DuoTextButton(
                text = "KAPAT",
                onClick = onDismiss,
                color = DuoButtonColor.GREEN,
                modifier = Modifier.fillMaxWidth()
            )
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DuoBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = DuoBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "APK & Debian (.deb) Rehberi",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = DuoTextDark
                    )
                    Text(
                        text = "İmza Hatasız Kurulum Bilgisi",
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // Section 1: APK Signature Fix
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DuoGreen.copy(alpha = 0.1f))
                        .border(1.dp, DuoGreen, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Android,
                                contentDescription = null,
                                tint = DuoGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Android APK İmza Durumu: GEÇERLİ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DuoGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Uygulama standart V1 ve V2 Android Debug/Release Keystore ile otomatik imzalanmıştır. Bu sayede 'Arşiv İmzası Geçersiz' (Signature Mismatch / Parse Error) hatası vermeden doğrudan kurulabilir.\n\n🖥️ Yatay Ekran (Landscape) & Pencere Desteği:\nLinux masaüstünde, Waydroid'de veya tabletlerde tam ekran ya da serbest boyutlu pencere olarak açıldığında arayüz otomatik olarak yatay moda ve iki sütunlu düzene geçer (NavigationRail, yan yana kod editörü ve konsol).",
                            fontSize = 12.sp,
                            color = DuoTextDark,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section 2: Linux Debian (.deb)
                Text(
                    text = "🐧 Linux Debian (.deb) Kurulumu:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DuoTextDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Linux (Debian / Ubuntu / Pardus / Mint) üzerinde doğrudan çalıştırmak için:",
                    fontSize = 12.sp,
                    color = Color(0xFF555555)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CodeBg)
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Bash Script",
                                color = Color(0xFF88C0D0),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("deb script", debScript)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Komut kopyalandı!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Kopyala",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = debScript,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = CodeFg,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}
