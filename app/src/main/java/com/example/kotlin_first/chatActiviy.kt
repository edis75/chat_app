package com.example.kotlin_first

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class chatActiviy : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton:ImageView
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var mDbRef:DatabaseReference

    var reciveRoom:String?=null
    var senderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_activiy)


        val name=intent.getStringExtra("name")
        val revicerUid=intent.getStringExtra("uid")

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        mDbRef=FirebaseDatabase.getInstance().getReference()


        senderRoom=revicerUid+senderUid
        reciveRoom=senderUid+revicerUid


        supportActionBar?.title=name

        chatRecyclerView=findViewById(R.id.chatRecyclerView)
        messageBox=findViewById(R.id.mesageBox)
        sendButton=findViewById(R.id.send_icon1)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()


                    for(postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)

                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            } )


        sendButton.setOnClickListener{
            val message=messageBox.text.toString()
            val messageObject=Message(message,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages")
                .push().setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(reciveRoom!!).child("messages")
                        .push().setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
}