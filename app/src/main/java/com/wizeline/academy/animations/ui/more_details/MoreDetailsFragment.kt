package com.wizeline.academy.animations.ui.more_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.wizeline.academy.animations.databinding.MoreDetailsFragmentBinding
import com.wizeline.academy.animations.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreDetailsFragment : Fragment() {

    private var _binding: MoreDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoreDetailsViewModel by viewModels()
    private val args: MoreDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreDetailsFragmentBinding.inflate(inflater, container, false)
        binding.ivImageDetailLarge.loadImage(args.imageId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) { binding.tvTitle.text = it }
        viewModel.content.observe(viewLifecycleOwner) { binding.tvFullTextContent.text = it }
        viewModel.fetchData(args.contentIndex)
        setupZoomAnimation()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupZoomAnimation() {
        val scaleXAnimation = createSpringAnimation(
            binding.ivImageDetailLarge, SpringAnimation.SCALE_X
        )
        val scaleYAnimation = createSpringAnimation(
            binding.ivImageDetailLarge, SpringAnimation.SCALE_Y
        )
        val scaleGestureDetector = createScaleGestureDetector()

        binding.ivImageDetailLarge.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                scaleXAnimation.start()
                scaleYAnimation.start()
            } else {
                // cancel animations so we can grab the view during previous animation
                scaleXAnimation.cancel()
                scaleYAnimation.cancel()
                // pass touch event to ScaleGestureDetector
                scaleGestureDetector.onTouchEvent(event)
            }
            true
        }
    }

    private fun createSpringAnimation(
        view: View, property: DynamicAnimation.ViewProperty
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(1f)
        spring.stiffness = SpringForce.STIFFNESS_MEDIUM
        spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        animation.spring = spring
        return animation
    }

    private fun createScaleGestureDetector(): ScaleGestureDetector {
        var scaleFactor = 1f
        return ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    binding.ivImageDetailLarge.scaleX *= scaleFactor
                    binding.ivImageDetailLarge.scaleY *= scaleFactor
                    return true
                }
            })
    }

}