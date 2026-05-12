package dk.musgames.app.viewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dk.musgames.app.data.Message
import dk.musgames.app.data.ChatTheme
import dk.musgames.app.data.chatThemes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.get

class ChatViewModel(private val database: FirebaseDatabase) {
    private val chatRef = database.getReference("messages")
    private val userRef = database.getReference("users")

    // Holds the current user's selected theme name
    var currentThemeName: String = chatThemes.first().name
        private set

    private fun formatTimestampIso8601(timeMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(timeMillis))
    }

    fun sendMessage(message: Message, onComplete: (() -> Unit)? = null) {
        val key = chatRef.push().key ?: return
        val isoTime = formatTimestampIso8601(message.timeStamp)
        val msgMap = mapOf(
            "text" to message.text,
            "userName" to message.userName,
            "timeStamp" to isoTime,
            "gameId" to message.gameId
        )
        chatRef.child(key).setValue(msgMap).addOnCompleteListener { onComplete?.invoke() }
    }

    private fun parseIso8601ToMillis(iso: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(iso)?.time ?: 0L
        } catch (_: Exception) {
            0L
        }
    }

    fun listenForMessages(gameId: String, onMessage: (Message) -> Unit): ChildEventListener {
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val map = snapshot.value as? Map<*, *> ?: return
                val text = map["text"] as? String ?: ""
                val userName = map["userName"] as? String ?: ""
                val gameIdValue = map["gameId"] as? String ?: ""
                val timeStampRaw = map["timeStamp"]
                val timeStamp = when (timeStampRaw) {
                    is Long -> timeStampRaw
                    is String -> timeStampRaw.toLongOrNull() ?: parseIso8601ToMillis(timeStampRaw)
                    else -> 0L
                }
                if (gameIdValue == gameId) {
                    onMessage(Message(text, userName, timeStamp, gameIdValue))
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }
        chatRef.addChildEventListener(listener)
        return listener
    }

    fun removeListener(listener: ChildEventListener) {
        chatRef.removeEventListener(listener)
    }

    fun cleanOldMessages() {
        chatRef.get().addOnSuccessListener { snapshot ->
            val now = System.currentTimeMillis()
            snapshot.children.forEach { child ->
                val map = child.value as? Map<*, *> ?: return@forEach
                val timeStampRaw = map["timeStamp"]
                val timeStamp = when (timeStampRaw) {
                    is Long -> timeStampRaw
                    is String -> timeStampRaw.toLongOrNull() ?: parseIso8601ToMillis(timeStampRaw)
                    else -> 0L
                }
                if (now - timeStamp > 3600000) {
                    child.key?.let { chatRef.child(it).removeValue() }
                }
            }
        }
    }

    fun fetchAllMessages(gameId: String, onMessages: (List<Message>) -> Unit) {
        chatRef.get().addOnSuccessListener { snapshot ->
            val messages = snapshot.children.mapNotNull { child ->
                val map = child.value as? Map<*, *> ?: return@mapNotNull null
                val text = map["text"] as? String ?: ""
                val userName = map["userName"] as? String ?: ""
                val gameIdValue = map["gameId"] as? String ?: ""
                val timeStampRaw = map["timeStamp"]
                val timeStamp = when (timeStampRaw) {
                    is Long -> timeStampRaw
                    is String -> timeStampRaw.toLongOrNull() ?: parseIso8601ToMillis(timeStampRaw)
                    else -> 0L
                }
                if (gameIdValue == gameId) Message(text, userName, timeStamp, gameIdValue) else null
            }.sortedBy { it.timeStamp }
            onMessages(messages)
        }
    }

    fun saveUserTheme(themeName: String, onComplete: (() -> Unit)? = null) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        userRef.child(userId).child("chatTheme").setValue(themeName)
            .addOnCompleteListener { onComplete?.invoke() }
        currentThemeName = themeName
    }

    fun loadUserTheme(onTheme: (ChatTheme) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        userRef.child(userId).child("chatTheme").get().addOnSuccessListener { snapshot ->
            val themeName = snapshot.value as? String ?: chatThemes.first().name
            currentThemeName = themeName
            val theme = chatThemes.find { it.name == themeName } ?: chatThemes.first()
            onTheme(theme)
        }.addOnFailureListener {
            // Fallback to default theme
            currentThemeName = chatThemes.first().name
            onTheme(chatThemes.first())
        }
    }

    fun getCurrentTheme(): ChatTheme {
        return chatThemes.find { it.name == currentThemeName } ?: chatThemes.first()
    }
}