package com.amayasan.exploreaway.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.AppConstants
import com.amayasan.exploreaway.model.Model

class VenueDetailFragment : Fragment() {

    companion object {
        fun newInstance(venue : Model.Venue): VenueDetailFragment {
            val fragment = VenueDetailFragment()
            val args = Bundle()
            args.putParcelable(AppConstants.VENUE_OBJ_KEY, venue)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mVenueDetailViewModel: VenueDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.venue_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mVenueDetailViewModel = ViewModelProviders.of(this).get(VenueDetailViewModel::class.java)

        if (arguments != null) {
            mVenueDetailViewModel.venue = arguments!!.getParcelable(AppConstants.VENUE_OBJ_KEY)

            Toast.makeText(context, mVenueDetailViewModel.venue.id, Toast.LENGTH_LONG).show()
        }
    }
}
