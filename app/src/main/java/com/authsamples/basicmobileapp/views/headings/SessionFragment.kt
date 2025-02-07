package com.authsamples.basicmobileapp.views.headings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.authsamples.basicmobileapp.R
import com.authsamples.basicmobileapp.app.MainActivityViewModel
import com.authsamples.basicmobileapp.databinding.FragmentSessionBinding
import com.authsamples.basicmobileapp.plumbing.events.NavigatedEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/*
 * Render the UI session id used in API logs
 */
class SessionFragment : androidx.fragment.app.Fragment() {

    // Binding properties
    private lateinit var binding: FragmentSessionBinding

    /*
     * Initialise the view
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the view
        this.binding = FragmentSessionBinding.inflate(inflater, container, false)

        // Create our view model using data from the main view model
        val mainViewModel: MainActivityViewModel by activityViewModels()
        this.binding.model = SessionViewModel(
            mainViewModel.apiClient.sessionId,
            this.getString(R.string.api_session_id)
        )

        return this.binding.root
    }

    /*
     * View initialization
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Subscribe to events
        EventBus.getDefault().register(this)
    }

    /*
     * Unsubscribe from events upon exit
     */
    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    /*
     * Change visibility based on whether showing a main view
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NavigatedEvent) {
        this.binding.model!!.setVisibility(event.isMainView)
    }
}
