package com.issue_tracker.android.ui.issue_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.issue_tracker.android.coroutine.Resource
import com.issue_tracker.android.network.model.IssueListResponse
import com.issue_tracker.android.repo.AppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IssueDetailsViewModel @Inject constructor(
    private val appRepo: AppRepo
): ViewModel() {

    fun getComments(issueNumber: Int,repoName: String) = liveData {
        try {
            emit(Resource.Loading)
            val comments = appRepo.getIssueComments(repoName = repoName, issueNumber = issueNumber)
            emit(Resource.Success(comments))
        } catch (exp: Exception) {
            emit(Resource.Error(exp.message))
            Timber.i("issue_error -------------- ${exp.message}")
        }
    }

}