package com.issue_tracker.android.ui.issue_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.issue_tracker.android.base.KotlinBaseActivity
import com.issue_tracker.android.coroutine.Resource
import com.issue_tracker.android.databinding.ActivityHomeBinding
import com.issue_tracker.android.network.model.IssueListResponse
import com.issue_tracker.android.ui.issue_details.IssueDetailsActivity
import com.issue_tracker.android.ui.issue_list.adapter.IssueListAdapter
import com.issue_tracker.android.ui.issue_list.viewmodel.IssueListViewModel
import com.issue_tracker.android.utils.SharedPreferenceUtils
import com.issue_tracker.android.utils.extension.customToast
import com.issue_tracker.android.utils.extension.gone
import com.issue_tracker.android.utils.extension.openActivity
import com.issue_tracker.android.utils.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IssueListActivity : KotlinBaseActivity(), IssueListAdapter.ItemClickListener {
    private var repoName: String? = null
    private val viewModel: IssueListViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private val issueListAdapter: IssueListAdapter by lazy { IssueListAdapter(this) }

    @Inject
    lateinit var pref: SharedPreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initListeners()
        fetchData()
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener { fetchData() }
    }

    private fun fetchData() {
        binding.swipeRefresh.isRefreshing = false
        viewModel.getIssueList(1,repoName?:"").observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible()
                }
                is Resource.Error -> {
                    binding.progressbar.gone()
                    binding.textNoDataFound.visible()
                }
                is Resource.Success -> {
                    binding.textNoDataFound.gone()
                    binding.progressbar.gone()
                    if (it.data.isEmpty()) {
                        customToast("No data found")
                        binding.textNoDataFound.visible()
                        return@observe
                    }

                    val filterList = it.data.filter { item -> item.title?.lowercase()?.contains("flutter") == false }

                    issueListAdapter.submitList(filterList)
                }
                else -> {}
            }
        }
    }

    private fun initViews() {
        repoName = intent.getStringExtra("repo_name")
        binding.issueRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@IssueListActivity)
            adapter = issueListAdapter
        }
        binding.textRepoName.text = repoName
    }

    override fun onItemClickListener(item: IssueListResponse.IssueListResponseItem) {
        openActivity<IssueDetailsActivity> {
            putExtra("data", Gson().toJson(item))
            putExtra("issue_number",item.number?:0)
            putExtra("repo_name",repoName?:"")
        }
    }
}