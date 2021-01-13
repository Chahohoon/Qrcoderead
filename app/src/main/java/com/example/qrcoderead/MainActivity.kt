package com.example.qrcoderead

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var detector : BarcodeDetector
    private lateinit var cameraSource: CameraSource
    val integrator = IntentIntegrator(this)
    var realm : Realm? = null
    var userdata = UserDataClass()
    var usercore = UserCoreClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onInitDataBase()
        setUserDataPost()
    }

    //realm 초기화
    fun onInitDataBase(){
        Realm.init(this)
        var config = RealmConfiguration.Builder().name("myrealm.realm").build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

    }

    // realm 읽어오기
    fun onReadDataBase() {
        realm?.executeTransaction {
           realm?.where(UserDataLoadClass::class.java)?.findFirst()?.let {
                   userdata.name = it.isName()
                   userdata.hp = it.isnumber()
           }
        }
    }

    // 스캐너 시작
    fun ReadQrcode() {

        integrator.setCaptureActivity(ScannerActivity::class.java)
        integrator.initiateScan()
        integrator.setBeepEnabled(true) //인식 시 "삑" 소리 남
    }

    // 메모 변경 클래스
    fun setMemo() {
        ed_memo.addTextChangedListener(object : TextWatcher {
            //입력하기 전에
            override fun afterTextChanged(p0: Editable?) {
                userdata.data = p0.toString()
            }

            //입력이 끝날때
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ed_memo.clearFocus()
            }
        })

        //default
        if(userdata.data == "") {
            userdata.data = "방문"
        }

        realm?.executeTransaction {
            var namecheck = realm?.where(UserDataLoadClass::class.java)?.equalTo("memo",userdata.data)?.findFirst()
            if(namecheck == null) {
                var temp = it.createObject(UserDataLoadClass::class.java)
                temp.setData(userdata.name,userdata.hp,userdata.data)
            }
        }
    }

    //DB 불러옴
    fun setUserDataPost() {
        onReadDataBase()
    }

    // 파이어베이스 테스트 클래스
    fun FirebaseDataWrite() {
        //realm 데이터 불러오기
        onReadDataBase()
        // 파이어 베이스 저장
        FirebaseDatabase.getInstance().reference.child(userdata.name).setValue(userdata)

    }

    override fun onClick(v: View) {
        when (v.id){
            /// 사용자 설정 화면 이동
             R.id.btn_qrcode-> {
                integrator.setCaptureActivity(ScannerActivity::class.java)
                integrator.initiateScan()
                integrator.setBeepEnabled(true) //인식 시 "삑" 소리 남
            }

//            /// 파트너 접속 화면 이동
            R.id.btn_qrcodelist-> {
                startActivity(Intent(this, UserQrcodeList::class.java))
            }

            R.id.btn_visit-> {
                usercore.getUserVisitList(userdata.name, userdata.hp, userdata.data, userdata.dstamp)
                startActivity(Intent(this, UserVisitList::class.java))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        ed_memo.text?.clear()
        ed_memo.clearFocus()
        setMemo()
    }

    // 스캐너결과 받아오는 곳
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                userdata.dstamp = result.contents
                //DB 불러옴
                onReadDataBase()

                usercore.setUserDataPost(userdata.name, userdata.hp, userdata.data, userdata.dstamp)
                usercore.getUserDataList(userdata.name, userdata.hp, userdata.data, userdata.dstamp)

//                FirebaseDataWrite()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    
    //카메라 권한 퍼미션 체크
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 200){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                cameraSource.start(qr_barcode.holder)
            } else {
                Toast.makeText(this, "스캐너는 카메라 권한이 허용되어야 사용 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

