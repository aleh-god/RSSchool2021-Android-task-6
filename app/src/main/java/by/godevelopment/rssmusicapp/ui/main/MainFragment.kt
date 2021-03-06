package by.godevelopment.rssmusicapp.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.godevelopment.rssmusicapp.MusicApp
import by.godevelopment.rssmusicapp.R
import by.godevelopment.rssmusicapp.databinding.MainFragmentBinding
import by.godevelopment.rssmusicapp.model.MusicState
import by.godevelopment.rssmusicapp.services.BROADCAST_COMMAND
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

const val MUSIC_ACTION = "MusicAction"

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((activity?.application as MusicApp).musicPlayerHandler)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        Log.i("RssMusicApp", "MainFragment: setupUI")
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        updateUI()
    }

    private fun updateUI() {
        Log.i("RssMusicApp", "MainFragment: updateUI")
        binding.run {
            nextButton.isEnabled = mainViewModel.chekNextMusicItem()
            previousButton.isEnabled = mainViewModel.chekPrevMusicItem()
            playButton.isEnabled = (mainViewModel.getCurrentMusicState() != MusicState.PLAY)
            pauseButton.isEnabled = (mainViewModel.getCurrentMusicState() == MusicState.PLAY)
            stopButton.isEnabled = (mainViewModel.getCurrentMusicState() != MusicState.STOP)
        }

        // TODO = "progressBar GONE // VISIBLE"
        binding.progressBar.visibility = View.GONE

        val current = mainViewModel.getCurrentMusicItem()
        binding.run {
            authorMedia.text = "Artist: ${current.artist}" as String
            titleMedia.text = "Title: ${current.title}" as String
            Glide.with(root)
                .load(current.bitmapUri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.image_not_loaded)
                .placeholder(R.drawable.image)
                .into(picturesView)
        }
    }

    private fun setupListeners() {
        Log.i("RssMusicApp", "MainFragment: setupListeners")
        binding.playButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: playButton.setOnClickListener")
            mainViewModel.startMusic()
            updateUI()
        }

        binding.pauseButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: pauseButton.setOnClickListener")
            mainViewModel.pauseMusic()
            updateUI()
        }

        binding.stopButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: stopButton.setOnClickListener")
            mainViewModel.stopMusic()
            updateUI()
        }

        binding.nextButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: nextButton.setOnClickListener")
            mainViewModel.startNextMusic()
            updateUI()
        }

        binding.previousButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: previousButton.setOnClickListener")
            mainViewModel.startPrevMusic()
            updateUI()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("RssMusicApp", "MainFragment: onDestroyView")
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private fun updateState(action: Int) {
        when (action) {
            0 -> mainViewModel.startMusic()
            1 -> mainViewModel.pauseMusic()
            2 -> mainViewModel.stopMusic()
            3 -> mainViewModel.startNextMusic()
            4 -> mainViewModel.startPrevMusic()
        }
        updateUI()
    }

    inner class MusicReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == MUSIC_ACTION) updateState(intent.getIntExtra(BROADCAST_COMMAND, 0))
        }
    }
}
