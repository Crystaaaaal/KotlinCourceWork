package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.Repositorys.UserInfoRepository
import com.example.kotlincoursework.ui.theme.state.GetUserInfoState
import com.example.kotlincoursework.ui.theme.state.UpdateUserInfoState
import dataBase.ActiveUser
import dataBase.TokenAndNumberRecive
import dataBase.UpdateUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences
):ViewModel() {

    //Активный пользователь
    private val _ActiveUser = MutableStateFlow(ActiveUser(byteArrayOf(), ""))
    val ActiveUser: MutableStateFlow<ActiveUser> get() = _ActiveUser

    fun updateUserImageMas(array: ByteArray) {
        _ActiveUser.update { currentUser ->
            currentUser.copy(userImage = array)
        }
    }

    fun updateUserFullName(fullName: String) {
        _ActiveUser.update { currentUser ->
            currentUser.copy(fullName = fullName)
        }
    }

    // Состояние получения данных пользователя
    private val _getUserInfoState = MutableStateFlow<GetUserInfoState>(GetUserInfoState.Idle)
    val getUserInfoState: StateFlow<GetUserInfoState> get() = _getUserInfoState

    private val _updateUserInfoState = MutableStateFlow<UpdateUserInfoState>(UpdateUserInfoState.Idle)
    val updateUserInfoState: StateFlow<UpdateUserInfoState> get() = _updateUserInfoState

    fun resertUpdateUserInfoStat(){
        _updateUserInfoState.value = UpdateUserInfoState.Idle
    }
    fun resetGetUserInfoState() {
        _getUserInfoState.value = GetUserInfoState.Idle
    }

    fun getUserInfo() {
        viewModelScope.launch {
            _getUserInfoState.value = GetUserInfoState.Loading
            val loginRecive = createLoginRecive()
            if (loginRecive == null) {
                //TODO выкидывание из ака и переход на экран входа
                return@launch
            }
            val result = UserInfoRepository(applicationContext).getUserInfo(loginRecive!!)
            _getUserInfoState.value = result
            if (result is GetUserInfoState.Success) {
                _ActiveUser.value = result.success
                resetGetUserInfoState()
            }
        }
    }


    fun udpateUserImage(){
        viewModelScope.launch {
            Log.i("settingsViewModel","")
            _updateUserInfoState.value = UpdateUserInfoState.Idle
            val loginRecive = createLoginRecive()
            if (loginRecive == null) {
                //TODO выкидывание из ака и переход на экран входа
                return@launch
            }
            val updateUser = UpdateUser(loginRecive!!,ActiveUser.value)
            val result = UserInfoRepository(applicationContext).updateUserInfo(updateUser)
            _updateUserInfoState.value = result
        }
    }

    private fun createLoginRecive(): TokenAndNumberRecive? {
        val token = sharedPreferences.getString("auth_token", null)
        val phoneNumber = sharedPreferences.getString("auth_phone", null)
        if (token.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) {
            return null
        }
        val tokenAndNumberRecive = TokenAndNumberRecive(token!!, phoneNumber!!)
        return tokenAndNumberRecive
    }

}