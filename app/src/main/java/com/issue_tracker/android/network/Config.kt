package com.issue_tracker.android.network

const val GITHUB_TOKEN = "" // enter your token or auth key
const val OWNER = "" // your the owner name

const val ISSUE_LIST = "repos/$OWNER/{repo_name}/issues"
const val REPO_LIST = "user/repos"
const val ISSUE_COMMENTS = "repos/$OWNER/{repo_name}/issues/{issue_number}/comments"