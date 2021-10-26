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
import by.godevelopment.rssmusicapp.helper.secondsToTime
import by.godevelopment.rssmusicapp.services.MusicServiceBound
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@RequiresApi(Build.VERSION_CODES.O)
class MainFragmentBound : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    // Bound Service
    private var musicService: MusicServiceBound? = null

    // This is a callback for the service connection state.
    private val boundServiceConnection = object : ServiceConnection {
        // When activity connects to service, the system uses MusicBinder instance and getService() to give a reference to musicService.
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("RssMusicApp", "MainFragment: onServiceConnected")
            val binder: MusicServiceBound.MusicBinder = service as MusicServiceBound.MusicBinder
            musicService = binder.getService()
//            mainViewModel.isMusicServiceBound = true
        }
        // When service disconnects, the audio will stop if the service reference isn’t already null, clearing the service reference.
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("RssMusicApp", "MainFragment: onServiceDisconnected")
            musicService?.stopMusic()
            musicService = null
//            mainViewModel.isMusicServiceBound = false
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
        // TODO = "tittle"
    }

    private fun setupListeners() {
        Log.i("RssMusicApp", "MainFragment: setupListeners")
        binding.playButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: playButton.setOnClickListener")
            musicService?.startMusic()
        }

        binding.pauseButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: pauseButton.setOnClickListener")
            musicService?.pauseMusic()
        }

        binding.stopButton.setOnClickListener {
            Log.i("RssMusicApp", "MainFragment: stopButton.setOnClickListener")
            musicService?.stopMusic()
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

    // start:
    // requireActivity().startService(Intent(context, ServiceName::class.java)

    //   Intent intent = new Intent(getActivity(), OdometrService.class);
    //   getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

    // TODO(25): Bind to music service if it isn't bound
    override fun onStart() {
        super.onStart()
        Log.i("RssMusicApp", "MainFragment: onStart - bindToMusicService()")
//        if (!mainViewModel.isMusicServiceBound) bindToMusicService()
    }

    // Bound Service Methods
    // Declare an intent to start MusicService.
    // Provide the Intent to the service along with the connection callback and a flag that automatically creates the service if the binding exists.
    private fun bindToMusicService() {
        Log.i("RssMusicApp", "MainFragment: bindToMusicService")
        activity?.let {
            val intent = Intent(it, MusicServiceBound::class.java)
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
//        if (mainViewModel.isMusicServiceBound) {
//            musicService?.stopMusic()  // stop the audio
//            activity?.unbindService(boundServiceConnection) // disconnect the service and save state
//            mainViewModel.isMusicServiceBound = false
//        }
    }

    companion object {
        fun newInstance() = MainFragment()
        const val SECOND = 1000
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("RssMusicApp", "MainFragment: onDestroyView")
        _binding = null
    }
}
