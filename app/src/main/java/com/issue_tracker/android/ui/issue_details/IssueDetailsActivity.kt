package com.issue_tracker.android.ui.issue_details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.issue_tracker.android.base.KotlinBaseActivity
import com.issue_tracker.android.coroutine.Resource
import com.issue_tracker.android.databinding.ActivityIssueDetailsBinding
import com.issue_tracker.android.network.model.IssueListResponse
import com.issue_tracker.android.ui.issue_list.adapter.IssueListAdapter
import com.issue_tracker.android.utils.extension.customToast
import com.issue_tracker.android.utils.extension.gone
import com.issue_tracker.android.utils.extension.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueDetailsActivity : KotlinBaseActivity() {

    private lateinit var binding: ActivityIssueDetailsBinding
    private val viewModel: IssueDetailsViewModel by viewModels()
    private val commentsAdapter: IssueListAdapter by lazy { IssueListAdapter(null) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIssueDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        fetchComments()

    }

    private fun fetchComments() {
        if (!intent.hasExtra("issue_number")) return
        if (!intent.hasExtra("repo_name")) return
        viewModel.getComments(
            intent.getIntExtra("issue_number",0),
            intent.getStringExtra("repo_name")?:""
        ).observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible()
                }
                is Resource.Error -> {
                    binding.progressbar.gone()
                }
                is Resource.Success -> {
                    binding.progressbar.gone()
                    if (it.data.isEmpty()) {
                        customToast("No comments found")
                        return@observe
                    }
                    commentsAdapter.submitList(it.data)
                }
                else -> {}
            }
        }
    }

    private fun initViews() {
        if (intent.hasExtra("data")) {
            val dataStr = intent.getStringExtra("data")
            val issueData = Gson().fromJson(dataStr,IssueListResponse.IssueListResponseItem::class.java)
            binding.data = issueData
        } else customToast("No data found!")

        binding.commentsRV.apply {
            layoutManager = LinearLayoutManager(this@IssueDetailsActivity)
            adapter = commentsAdapter
        }
    }
}