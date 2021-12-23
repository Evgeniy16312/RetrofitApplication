package com.example.retrofitapplication.mvpuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.retrofitapplication.R
import com.example.retrofitapplication.data.GitHubUser
import com.example.retrofitapplication.data.GitHubUserRepositoryFactory
import com.example.retrofitapplication.databinding.UserInfoLayoutBinding
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter


class UserFragment : MvpAppCompatFragment(R.layout.user_info_layout),
    UserView {

    private val userId: String by lazy {
        arguments?.getString(ARG_USER_LOGIN).orEmpty()
    }
    private val presenter: UserPresenter by moxyPresenter {
        UserPresenter(
            userId.compareTo(String()),
            userRepository = GitHubUserRepositoryFactory.create()
        )
    }

    private lateinit var viewBinging: UserInfoLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinging = UserInfoLayoutBinding.bind(view)
    }

    override fun toastError(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun showResult(user: GitHubUser) {
        viewBinging.userLink.text = user.url
        viewBinging.userName.text = user.login
        Glide.with(viewBinging.userPhoto.context)
            .load(user.avatarUrl)
            .into(viewBinging.userPhoto)
    }

    companion object {
        private const val ARG_USER_LOGIN = "arg_user_login"
        fun newInstance(userId: String): Fragment =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_LOGIN, userId)
                }
            }
    }
}