package ltd.evilcorp.atox

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import im.tox.tox4j.core.options.ProxyOptions
import im.tox.tox4j.core.options.SaveDataOptions
import im.tox.tox4j.core.options.ToxOptions
import kotlinx.android.synthetic.main.activity_profile.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import java.io.File

private const val HEX_CHARS = "0123456789ABCDEF"

private fun String.hexToByteArray(): ByteArray {
    val bytes = ByteArray(length / 2)

    for (i in 0 until length step 2) {
        bytes[i.shr(1)] = HEX_CHARS.indexOf(this[i]).shl(4).or(HEX_CHARS.indexOf(this[i + 1])).toByte()
    }

    return bytes
}

private fun loadToxSave(saveFile: File): ByteArray? {
    if (!saveFile.exists()) {
        return null
    }

    val data = saveFile.readBytes()

    return data
}

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btnCreate.setOnClickListener {
            btnCreate.isEnabled = false
            App.profile = if (username.text.isNotEmpty()) username.text.toString() else "aTox user"
            App.password = if (password.text.isNotEmpty()) password.text.toString() else ""
            startActivity(Intent(this@ProfileActivity, ContactListActivity::class.java))

            var profile: File? = null
            filesDir.walk().forEach {
                if (it.extension.equals("tox") && it.isFile) {
                    profile = it
                    Log.e("Profile", "Found profile: ${profile.toString()}")
                }
            }

            var saveOption: SaveDataOptions = SaveDataOptions.`None$`()
            if (profile != null) {
                val data = loadToxSave(profile!!)
                if (data != null) {
                    saveOption = SaveDataOptions.`ToxSave`(data)
                }
            }

            thread(start = true) {
                val tox = Tox(
                    ToxOptions(
                        true,
                        true,
                        true,
                        ProxyOptions.`None$`(),
                        0,
                        0,
                        0,
                        saveOption,
                        true
                    )
                )

                Log.e("Profile", tox.getName())

                tox.bootstrap(
                    "tox.verdict.gg",
                    33445,
                    "1C5293AEF2114717547B39DA8EA6F1E331E5E358B35F9B6B5F19317911C5F976".hexToByteArray()
                )
                tox.bootstrap(
                    "tox.kurnevsky.net",
                    33445,
                    "82EF82BA33445A1F91A7DB27189ECFC0C013E06E3DA71F588ED692BED625EC23".hexToByteArray()
                )
                tox.bootstrap(
                    "tox.abilinski.com",
                    33445,
                    "10C00EB250C3233E343E2AEBA07115A5C28920E9C8D29492F6D00B29049EDC7E".hexToByteArray()
                )

                tox.setName(App.profile)
                tox.save(App.profile, filesDir.toString())
                Log.e("Profile", tox.getName())
                Log.e("Profile", App.profile)

                while (true) {
                    sleep(tox.iterate().toLong())
                }
            }

            finish()
        }
    }
}
