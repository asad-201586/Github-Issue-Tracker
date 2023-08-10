package com.issue_tracker.android.ui.issue_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.issue_tracker.android.databinding.ItemIssueBinding
import com.issue_tracker.android.network.model.IssueListResponse
import com.issue_tracker.android.ui.issue_details.IssueDetailsActivity
import com.issue_tracker.android.utils.extension.onClick
import com.issue_tracker.android.utils.extension.openActivity

class IssueListAdapter(private val itemClickListener: ItemClickListener?): ListAdapter<IssueListResponse.IssueListResponseItem,IssueListAdapter.IssueListViewHolder>(DIFF_CALLBACK) {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<IssueListResponse.IssueListResponseItem>() {
            override fun areItemsTheSame(oldItem: IssueListResponse.IssueListResponseItem, newItem: IssueListResponse.IssueListResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: IssueListResponse.IssueListResponseItem, newItem: IssueListResponse.IssueListResponseItem): Boolean {
                return oldItem == newItem
            }
        }

    }

    class IssueListViewHolder private constructor(
        private val binding: ItemIssueBinding,
        private val itemClickListener: ItemClickListener?
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: IssueListResponse.IssueListResponseItem) {
            binding.data = item
            binding.executePendingBindings()

            binding.root onClick {
                itemClickListener?.onItemClickListener(item)
            }

        }

        companion object {
            fun from(
                parent: ViewGroup,
                itemClickListener: ItemClickListener?
            ): IssueListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemIssueBinding.inflate(layoutInflater, parent, false)
                return IssueListViewHolder(binding,itemClickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueListViewHolder {
        return IssueListViewHolder.from(parent,itemClickListener)
    }

    override fun onBindViewHolder(holder: IssueListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    interface ItemClickListener {
        fun onItemClickListener(item: IssueListResponse.IssueListResponseItem)
    }

}