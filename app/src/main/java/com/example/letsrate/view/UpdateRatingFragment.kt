package com.example.letsrate.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.letsrate.R
import com.example.letsrate.databinding.FragmentUpdateRatingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.common.base.MoreObjects.ToStringHelper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UpdateRatingFragment : Fragment() {

    private lateinit var binding : FragmentUpdateRatingBinding
    private lateinit var firebase : FirebaseFirestore
    private lateinit var navController : NavController
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedPicture : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        binding.updateRateRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            binding.updateRatePoint.text = rating.toString()
        }

        val bundle = this.arguments
        if(bundle != null){

            val productName = bundle.getString("productName").toString()
            val sellerName = bundle.getString("sellerName").toString()
            val commentTitle = bundle.getString("commentTitle").toString()
            val rate = bundle.getString("rate").toString()
            val downloadUrl = bundle.getString("downloadUrl").toString()
            val rateId = bundle.getString("rateId").toString()
            val comment = bundle.getString("comment").toString()

            binding.updateInputComment.setText(comment)



            binding.updateInputSellerName.setText(sellerName)
            binding.updateInputProductName.setText(productName)
            binding.updateInputCommentTitle.setText(commentTitle)
            binding.updateRatePoint.text = rate

            binding.updateInputComment.movementMethod = ScrollingMovementMethod()

            Glide.with(binding.root).load(downloadUrl).into(binding.updateImageView)

            binding.updateRateRatingBar.rating = rate.toFloat()

            binding.buttonUpdateRate.setOnClickListener {
                //Toast.makeText(context,rateId,Toast.LENGTH_LONG).show()
                firebase = FirebaseFirestore.getInstance()
                val myCollection = firebase.collection("Ratings")
                rateId?.let { it1 ->
                    myCollection.document(it1)
                        .update(
                            mapOf(
                                "comment" to binding.updateInputComment.text.toString(),
                                "commentTitle" to binding.updateInputCommentTitle.text.toString(),
                                "createdDate" to Timestamp.now(),
                                "productName" to binding.updateInputProductName.text.toString(),
                                "rate" to binding.updateRateRatingBar.rating.toString(),
                                "sellerName" to binding.updateInputSellerName.text.toString(),
                                "downloadUrl" to selectedPicture
                            ))

                        .addOnSuccessListener {
                            Toast.makeText(context,"Rate Updated !", Toast.LENGTH_LONG).show()
                            navController.navigate(R.id.myRatingsFragment)
                            //navController.navigate(R.id.myRatingsFragment)

                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()

                        }

                }

            }

            binding.updateImageView.setOnClickListener{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    // If Android API 33+
                    if(ContextCompat.checkSelfPermission(it.context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                        // permission denied
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)){
                            Snackbar.make(requireView(), "Permission needed for gallery !", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                                // request permission

                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                            }.show()

                        } else {
                            // request permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    } else {
                        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        // start activity for result
                        activityResultLauncher.launch(intentToGallery)
                    }

                } else {
                    // If Android API 32-
                    if(ContextCompat.checkSelfPermission(binding.root.context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        // permission denied
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                            Snackbar.make(view, "Permission needed for gallery !", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                                // request permission
                                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                            }.show()

                        } else {
                            // request permission
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    } else {
                        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        // start activity for result
                        activityResultLauncher.launch(intentToGallery)
                    }
                }

            }

            fun registerLaunchers(){

                activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                    if(result.resultCode == Activity.RESULT_OK){
                        val intentFromResult = result.data
                        if(intentFromResult != null){
                            selectedPicture = intentFromResult.data
                            selectedPicture?.let {
                                binding.updateImageView.setImageURI(it)

                            }
                        }
                    }
                }

                permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
                    if(result){
                        // permission granted
                        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        activityResultLauncher.launch(intentToGallery)
                    } else {
                        // permission denied
                        Toast.makeText(context,"Permission Needed !", Toast.LENGTH_LONG).show()
                    }

                }

            }

            registerLaunchers()

        }

    }
}