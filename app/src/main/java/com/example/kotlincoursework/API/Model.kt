package dataBase

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class SearchingResponse(
    val userList: List<User>,
)
@Serializable
data class PhoneOrLoginRemote(
    val phoneOrLogin: String,
    val token: LoginRecive
)
@Serializable
data class UpdateUser(
    val loginRecive: LoginRecive,
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
){
    // Переопределяем equals и hashCode для корректного сравнения пользователей
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (phoneNumber != other.phoneNumber) return false
        if (login != other.login) return false
        return true
    }

    override fun hashCode(): Int {
        var result = phoneNumber.hashCode()
        result = 31 * result + login.hashCode()
        return result
    }
}


@Serializable
data class LoginRecive(
    val token: String,
    val phoneNumber: String

)

@Serializable
data class Chat(
    val id: Int,
    val createdAt: String
) {
    fun createdAtDateTime(): LocalDateTime {
        return LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}

@Serializable
data class Message(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val messageText: String,
    val sentAt: String
) {
    fun sentAtDateTime(): LocalDateTime {
        return LocalDateTime.parse(sentAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}

@Serializable
data class UserRegistrationRequest(
    val phoneNumber: String,
    val passwordHash: String,
    val fullName: String,
    val profileImage: ByteArray?
)

@Serializable
data class ChatCreationRequest(
    val createdAt: String
)