// ValueEventAdapter.kt
package dk.musgames.app.helper

import com.google.firebase.database.*

class ValueEventAdapter(
    private val onData: (DataSnapshot) -> Unit
) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) = onData(snapshot)
    override fun onCancelled(error: DatabaseError)     {}
}
