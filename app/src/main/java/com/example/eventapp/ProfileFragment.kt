package com.example.eventapp

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.eventapp.Data.getUserId
import com.example.eventapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private var selectedImageUri: Uri? = null
    private var tempImageUri: Uri? = null
    private var userImageViewForDialog: ImageView? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            tempImageUri = it
            userImageViewForDialog?.setImageURI(tempImageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding?.toEditingProfile?.setOnClickListener { showEditProfileDialog() }

        loadUserProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        private const val ARG_DATA = "arg_data"

        @JvmStatic
        fun newInstance(param1: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA, param1)
                }
            }
    }

    private fun loadUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val user = EventAppDB.getDB(requireContext()).userDao().getUserById(getUserId(requireContext()))
            withContext(Dispatchers.Main) {
                user?.let {
                    binding?.userTxt?.text = it.login
                    binding?.infoTxt?.text = it.userInfo ?: "Пользователь еще не добавил информацию о себе"
                    it.profilePhoto?.let { photo ->
                        val bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.size)
                        binding?.userImg?.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun convertUriToByteArray(uri: Uri): ByteArray? {
        return try {
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val editAboutMe = dialogView.findViewById<EditText>(R.id.editAboutMe)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveChanges)
        val btnChangePhoto = dialogView.findViewById<Button>(R.id.btnChangePhoto)
        val userImageView = dialogView.findViewById<ImageView>(R.id.editUserImg)

        userImageViewForDialog = userImageView

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val user = EventAppDB.getDB(requireContext()).userDao().getUserById(getUserId(requireContext()))
            withContext(Dispatchers.Main) {
                user?.let {
                    editAboutMe.setText(it.userInfo ?: "")
                    it.profilePhoto?.let { photo ->
                        val bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.size)
                        userImageView.setImageBitmap(bitmap)
                    }
                }
            }
        }

        btnChangePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            val newInfo = editAboutMe.text.toString()
            val photoBytes = tempImageUri?.let { uri -> convertUriToByteArray(uri) }

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                EventAppDB.getDB(requireContext()).userDao().updateUserProfile(
                    userId = getUserId(requireContext()),
                    userInfo = newInfo,
                    profilePhoto = photoBytes
                )
                withContext(Dispatchers.Main) {
                    binding?.infoTxt?.text = newInfo
                    tempImageUri?.let {
                        selectedImageUri = it
                        binding?.userImg?.setImageURI(selectedImageUri)
                    }
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Профиль обновлен", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.setOnDismissListener {
            userImageViewForDialog = null
        }

        dialog.show()
    }
}
