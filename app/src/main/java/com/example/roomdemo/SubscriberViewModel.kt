package com.example.roomdemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository:SubscriberRepository):ViewModel(),Observable {

    val subscribers = repository.subscribers

    private var isUpdateOrDelete =false
    private lateinit var subscriberToUpdateOrDelete:Subscriber
    @Bindable
    val inputName = MutableLiveData<String?>()
    @Bindable
    val inputEmail = MutableLiveData<String?>()
    @Bindable
    val updateButton = MutableLiveData<String?>()
    @Bindable
    val deleteButton =MutableLiveData<String?>()

    init{
        updateButton.value="SAVE"
        deleteButton.value="CLEAR ALL"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete){
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        }else{
            val name:String= inputName.value !!
            val email:String = inputEmail.value!!
            insert(Subscriber(0,name,email))
            inputName.value = null
            inputEmail.value = null
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    fun insert(subscriber: Subscriber):Job = viewModelScope.launch{
        repository.insert(subscriber)
    }

    fun update(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.update(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        updateButton.value = "SAVE"
        deleteButton.value= "CLEAR ALL"
    }

    fun delete(subscriber: Subscriber):Job = viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        updateButton.value = "SAVE"
        deleteButton.value= "CLEAR ALL"
    }

    fun clearAll() :Job = viewModelScope.launch {
        repository.deleteAll()
    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        updateButton.value = "UPDATE"
        deleteButton.value= "DELETE"

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}