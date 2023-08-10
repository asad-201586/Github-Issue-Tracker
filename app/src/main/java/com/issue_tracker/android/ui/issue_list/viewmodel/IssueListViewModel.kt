package com.issue_tracker.android.ui.issue_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.issue_tracker.android.coroutine.Resource
import com.issue_tracker.android.repo.AppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IssueListViewModel @Inject constructor(
    private val appRepo: AppRepo
): ViewModel() {

    fun getIssueList(page: Int,repoName: String) = liveData {
        try {
            emit(Resource.Loading)
            val issues = appRepo.getIssueList(page,repoName)
            emit(Resource.Success(issues))
        } catch (exp: Exception) {
            emit(Resource.Error(exp.message))
        }
    }

}