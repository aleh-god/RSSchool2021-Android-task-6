package by.godevelopment.rssmusicapp.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import by.godevelopment.rssmusicapp.R
import by.godevelopment.rssmusicapp.databinding.MainFragmentBinding
import by.godevelopment.rssmusicapp.services.MusicService
import by.godevelopment.rssmusicapp.helper.secondsToTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var runnable: Runnable
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    // Bound Service
    private var musicService: MusicService? = null

    // This is a callback for the service connection state.
    private val boundServiceConnection = object : ServiceConnection {
        // When activity connects to service, the system uses MusicBinder instance and getService() to give a reference to musicService.
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("RssMusicApp", "MainFragment: onServiceConnected")
            val binder: MusicService.MusicBinder = service as MusicService.MusicBinder
            musicService = binder.getService()
            mainViewModel.isMusicServiceBound = true
        }
        // When service disconnects, the audio will stop if the service reference isn’t already null, clearing the service reference.
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("RssMusicApp", "MainFragment: onServiceDisconnected")
            musicService?.stopMusic()
            musicService = null
            mainViewModel.isMusicServiceBound = false
        }
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
//        if (!mainViewModel.isMusicServiceBound) bindToMusicService()
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        Log.i("RssMusicApp", "MainFragment: setupUI")
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initializeSeekBar()
    }

    private fun setupListeners() {
        Log.i("RssMusicApp", "MainFragment: setupListeners")
        binding.seekBar.setOnSeekBarChangeListener(this)
        // binding.playButton.isEnabled = false

        binding.playButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: playButton.setOnClickListener")
            musicService?.startMusic()
            updateSeekBar()
        }

        binding.pauseButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: pauseButton.setOnClickListener")
            musicService?.pauseMusic()
        }

        binding.stopButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: stopButton.setOnClickListener")
            musicService?.stopMusic()

            handler.removeCallbacks(runnable)
        }

        binding.nextButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: nextButton.setOnClickListener")
            // musicService?.pauseMusic()
        }

        binding.previousButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: previousButton.setOnClickListener")
            // musicService?.pauseMusic()
        }
    }

    // Initialize seekBar
    private fun initializeSeekBar() {
        Log.i("RssMusicApp", "MainFragment: initializeSeekBar")
        // Sets the maximum value for SeekBar
        binding.seekBar.max = musicService?.getMaxSecondsMedia() ?: 0
        // Sets default values for TextViews which shows the progress and the total duration of the video.
        binding.textProgress.text = getString(R.string.default_value)
        binding.textTotalTime.text = (musicService?.getMaxSecondsMedia() ?: 0).secondsToTime()
        // Hides the ProgressBar.
        binding.progressBar.visibility = View.GONE
        // Enables the play button.
        binding.playButton.isEnabled = true
    }

    // Update seek bar after every 1 second
    private fun updateSeekBar() {
        Log.i("RssMusicApp", "MainFragment: updateSeekBar")
        runnable = Runnable { // Runnable is a Java interface and executes on a separate thread.
            // Since it executes on a separate thread, it won't block your UI and the SeekBar and TextViews will update periodically.
            binding.textProgress.text = (musicService?.getCurrentSecondsMedia() ?: 0).secondsToTime()
            binding.seekBar.progress = musicService?.getCurrentSecondsMedia() ?: 0
            handler.postDelayed(runnable, SECOND.toLong())
        }
        handler.postDelayed(runnable, SECOND.toLong())
    }

    // Update media player when user changes seekBar
    // seekBar: Instance of the seekBar.
    // progress: Progress of seekBar in seconds.
    // fromUser: Boolean which tells you if the change is because of user interaction.
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        Log.i("RssMusicApp", "MainFragment: onProgressChanged $progress")
        if (fromUser){  // If the change in progress is due to user interaction, it'll be true. If not, it'll be false.
            musicService?.setProgressPlayingMedia(progress * SECOND)
        }
    }

    // start:
    // requireActivity().startService(Intent(context, ServiceName::class.java)

    //   Intent intent = new Intent(getActivity(), OdometrService.class);
    //   getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

    // TODO(25): Bind to music service if it isn't bound
    override fun onStart() {
        super.onStart()
        Log.i("RssMusicApp", "MainFragment: onStart - bindToMusicService()")
        if (!mainViewModel.isMusicServiceBound) bindToMusicService()
    }

    // Bound Service Methods
    // Declare an intent to start MusicService.
    // Provide the Intent to the service along with the connection callback and a flag that automatically creates the service if the binding exists.
    private fun bindToMusicService() {
        Log.i("RssMusicApp", "MainFragment: bindToMusicService")
        activity?.let {
        val intent = Intent(it, MusicService::class.java)
        it.bindService(intent, boundServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("RssMusicApp", "MainFragment: onDestroy")
        unbindMusicService()
    }

    private fun unbindMusicService() {
        Log.i("RssMusicApp", "MainFragment: unbindMusicService")
        if (mainViewModel.isMusicServiceBound) {
            musicService?.stopMusic()  // stop the audio
            activity?.unbindService(boundServiceConnection) // disconnect the service and save state
            mainViewModel.isMusicServiceBound = false
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        const val SECOND = 1000
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("RssMusicApp", "MainFragment: onDestroyView")
        handler.removeCallbacks(runnable)
        _binding = null
    }

    // Ignore
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    // Converting seconds to mm:ss format to display on screen
//    private fun timeInString(seconds: Int): String {
//        return String.format(
//            "%02d:%02d",
//            (seconds / 3600 * 60 + ((seconds % 3600) / 60)),
//            (seconds % 60)
//        )
//    }

}
