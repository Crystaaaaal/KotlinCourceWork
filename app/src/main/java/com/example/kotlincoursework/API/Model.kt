package dataBase

import com.example.kotlincoursework.DB.Models.Chat
import kotlinx.serialization.Serializable

@Serializable
data class SearchingResponse(
    val userList: List<User>,
)
@Serializable
data class PhoneOrLoginRemote(
    val phoneOrLogin: String,
    val token: TokenAndNumberRecive
)
@Serializable
data class UpdateUser(
    val tokenAndNumberRecive: TokenAndNumberRecive,
    val activeUser:ActiveUser
)
@Serializable
data class ActiveUser(
    val userImage: ByteArray,
    val fullName:String)


@Serializable
data class ServerResponse(
    val result: Boolean
)
@Serializable
data class RegistrationUserInfo(
    val phoneNumber: String,
    val login: String,
    val password: String,
    val secondName: String,
    val firstName:String,
    val fatherName:String
)

@Serializable
data class LoginUser(
    val phoneNumber: String,
    val password: String
)

@Serializable
data class User(
    val phoneNumber: String,
    val hashPassword:String,
    val fullName: String,
    val login:String,
    val profileImage: ByteArray?,
    val createdAt: String
)

@Serializable
data class TokenAndNumberRecive(
    val token: String,
    val phoneNumber: String
)

@Serializable
data class LoginRecive(
    val token: String,
    val user: User
)


@Serializable
data class Message(
    val forUser: User,
    val fromUser: TokenAndNumberRecive,
    val messageText: String,
    val sentAt: String
)

@Serializable
data class MessageIncoming(
    val forUser: String,
    val fromUser: User,
    val messageText: String,
    val sentAt: String
)

data class MessageForShow(
    val messageText: String,
    val sentAt: String
)

@Serializable
data class MessagesRecive(
    val Chat: ChatRecive,
    val token: TokenAndNumberRecive
)
@Serializable
data class ChatRecive(
    val chatId: Int,
    val userPhone: String,
    val contactPhone: String,
    val createdAt: String
)


@Serializable
data class MessagesRemote(
    val userMessagesRecive: List<MessageIncoming>,
    val contactMessage:List<MessageIncoming>,
)

@Serializable
data class ChatRemote(
    val userChatsRecive: List<ChatRecive>,
    val Users: List<User>
)
