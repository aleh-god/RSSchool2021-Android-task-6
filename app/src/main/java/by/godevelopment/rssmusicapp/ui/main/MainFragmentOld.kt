package by.godevelopment.rssmusicapp.ui.main

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import by.godevelopment.rssmusicapp.R
import by.godevelopment.rssmusicapp.databinding.MainFragmentOldBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@RequiresApi(Build.VERSION_CODES.O)
class MainFragmentOld : Fragment(), SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnDrmInfoListener {

    private var _binding: MainFragmentOldBinding? = null
    private val binding get() = _binding!!

    private val mediaPlayer = MediaPlayer()
    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var selectedVideoUri: Uri

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentOldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnDrmInfoListener(this)
        //  this gets a callback when the VideoView‘s surface is ready for playing video.
        //  When this surface is ready, it calls surfaceCreated().
        binding.videoView.holder.addCallback(this)
        binding.seekBar.setOnSeekBarChangeListener(this)
        // binding.playButton.isEnabled = false

        //TODO(7): Вынести в отделльный метод
        binding.playButton.setOnClickListener {
            mediaPlayer.start()
        }

        binding.pauseButton.setOnClickListener {
            mediaPlayer.pause()
        }
    }

    // Converting seconds to mm:ss format to display on screen
    private fun timeInString(seconds: Int): String {
        return String.format(
            "%02d:%02d",
            (seconds / 3600 * 60 + ((seconds % 3600) / 60)),
            (seconds % 60)
        )
    }

    // Initialize seekBar
    private fun initializeSeekBar() {
        // Sets the maximum value for SeekBar
        binding.seekBar.max = mediaPlayer.seconds
        // Sets default values for TextViews which shows the progress and the total duration of the video.
        binding.textProgress.text = getString(R.string.default_value)
        binding.textTotalTime.text = timeInString(mediaPlayer.seconds)
        // Hides the ProgressBar.
        binding.progressBar.visibility = View.GONE
        // Enables the play button.
        binding.playButton.isEnabled = true
    }

    // Update seek bar after every 1 second
    private fun updateSeekBar() {
        runnable = Runnable { // Runnable is a Java interface and executes on a separate thread.
            // Since it executes on a separate thread, it won't block your UI and the SeekBar and TextViews will update periodically.
            binding.textProgress.text = timeInString(mediaPlayer.currentSeconds)
            binding.seekBar.progress = mediaPlayer.currentSeconds
            handler.postDelayed(runnable, SECOND.toLong())
        }
        handler.postDelayed(runnable, SECOND.toLong())
    }

    // : SurfaceHolder.Callback - SurfaceHolder is ready.
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        mediaPlayer.apply {
//            setDataSource(requireActivity().application, Uri.parse("android.resource://by.godevelopment.rssmusicapp/raw/test")) // First, you pass the location, URI, of the video.
            setDataSource(URL)
            setDisplay(surfaceHolder) // If you started the MediaPlayer without calling this function, you wouldn’t see any video because MediaPlayer wouldn’t know where to display the video.
            prepareAsync()  // Finally, you call prepare(). This function prepares MediaPlayer to playback the video synchronously on the main thread.
        }
    }

    // This function gets called when the media player gets ready
    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        initializeSeekBar()
        updateSeekBar()
        binding.picturesView?.let {
            Glide.with(binding.root)
                .load(URL_PIC)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.image_not_loaded)
                .placeholder(R.drawable.image)
                .into(it)
        }
    }

    // Update media player when user changes seekBar
    // seekBar: Instance of the seekBar.
    // progress: Progress of seekBar in seconds.
    // fromUser: Boolean which tells you if the change is because of user interaction.
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser){  // If the change in progress is due to user interaction, it'll be true. If not, it'll be false.
            mediaPlayer.seekTo(progress * SECOND)
        }
    }

    // TODO(1): Release the media player resources when activity gets destroyed
    fun mediaPlayerRelease() {
        mediaPlayer.release()
    }

    // Extension properties to get the media player total duration and current duration in seconds
    private val MediaPlayer.seconds: Int // These extension properties help you implement those functionalities.
        get() {
            return this.duration / SECOND
        }

    private val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / SECOND
        }

    companion object {
        fun newInstance() = MainFragment()

        const val SECOND = 1000
        // TODO(delete): const val URL
        const val URL = "https://freepd.com/music/Ice and Snow.mp3"
        //            "https://res.cloudinary.com/dit0lwal4/video/upload/v1597756157/samples/elephants.mp4"
        const val URL_PIC = "https://www.pelicanwater.com/blog/wp-content/uploads/2019/02/cropped-Snow_Blog_Jan_2019-01.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        _binding = null
    }

    // Ignore
    override fun surfaceChanged(surfaceHolder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}
    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    // For DRM files
    override fun onDrmInfo(mediaPlayer: MediaPlayer?, drmInfo: MediaPlayer.DrmInfo?) {}

}
