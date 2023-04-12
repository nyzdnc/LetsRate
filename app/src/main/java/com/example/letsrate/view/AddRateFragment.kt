package com.example.letsrate.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.letsrate.databinding.FragmentAddRateBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*


class AddRateFragment : Fragment() {
    private lateinit var binding : FragmentAddRateBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    private lateinit var firestore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddRateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage

        /* binding.addRateRatingBar.setOnRatingBarChangeListener(OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            binding.myEventsPoint.text = "$rating"
        }) */

        binding.addRateRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            binding.myEventsPoint.text = rating.toString()
        }

        binding.buttonAddRate.setOnClickListener {

            val uuid = UUID.randomUUID()
            val imageName = "$uuid.jpg"

            val reference = storage.reference
            val imageReference = reference.child("images").child(imageName)

            if(selectedPicture != null){

                if( binding.inputSellerName.equals("") ||
                            binding.inputProductName.equals("") ||
                            binding.inputCommentTitle.equals("") ||
                            binding.inputCommentTitle.equals("") ||
                            binding.addRateRatingBar.rating == 0.0f
                    ){
                    Toast.makeText(binding.root.context,"Please fill in all values.",Toast.LENGTH_LONG).show()
                } else {

                    imageReference.putFile(selectedPicture!!).addOnSuccessListener {

                        val uploadPictureReference = storage.reference.child("images").child(imageName)
                        uploadPictureReference.downloadUrl.addOnSuccessListener {

                            val downloadUrl = it.toString()
                            val rateMap = hashMapOf<String , Any>()

                            rateMap.put("userEmail" , auth.currentUser!!.email!!)
                            rateMap.put("commentTitle" , binding.inputCommentTitle.text.toString())
                            rateMap.put("productName" , binding.inputProductName.text.toString())
                            rateMap.put("sellerName" , binding.inputSellerName.text.toString())
                            rateMap.put("comment" , binding.inputComment.text.toString())
                            rateMap.put("downloadUrl" , downloadUrl)
                            rateMap.put("createdDate" , Timestamp.now())
                            rateMap.put("rate" , binding.addRateRatingBar.rating.toString())

                            firestore.collection("Ratings").add(rateMap)
                                .addOnSuccessListener {
                                    val action = AddRateFragmentDirections.actionAddRateFragmentToHomeFragment()
                                    Navigation.findNavController(binding.root).navigate(action)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this.context,it.localizedMessage,Toast.LENGTH_LONG).show()
                                }

                        }

                    }.addOnFailureListener {
                        Toast.makeText(this.context,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }


            } else{ Toast.makeText(this.context,"Please choose an image.", Toast.LENGTH_LONG).show() }

        }

        binding.imageView.setOnClickListener{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                // If Android API 33+
                if(ContextCompat.checkSelfPermission(it.context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                    // permission denied
                    println("TEST")
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
                if(result.resultCode == RESULT_OK){
                    val intentFromResult = result.data
                    if(intentFromResult != null){
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let {
                            binding.imageView.setImageURI(it)
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

