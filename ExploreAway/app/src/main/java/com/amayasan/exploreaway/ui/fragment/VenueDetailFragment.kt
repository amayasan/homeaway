package com.amayasan.exploreaway.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.amayasan.exploreaway.AppConstants
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.databinding.VenueDetailFragmentBinding
import com.amayasan.exploreaway.model.Model
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.venue_detail_fragment.*

class VenueDetailFragment : Fragment() {

    private lateinit var mBinding : VenueDetailFragmentBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.venue_detail_fragment, container, false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mVenueDetailViewModel = ViewModelProviders.of(this).get(VenueDetailViewModel::class.java)

        if (arguments != null) {
            mVenueDetailViewModel.venue = arguments!!.getParcelable(AppConstants.VENUE_OBJ_KEY)
            mBinding.venue = mVenueDetailViewModel.venue

            initMapImageView()
        }

    }

    private fun initMapImageView() {
        Glide
            .with(this)
            .load(getGoogleMapImageUrl())
            .centerCrop()
            //.placeholder(R.drawable.loading_spinner)
            .into(venue_detail_map_iv)
    }

    private fun getGoogleMapImageUrl() : String {
        val lat = mVenueDetailViewModel.venue.location.lat
        val lng = mVenueDetailViewModel.venue.location.lng

        return "https://maps.googleapis.com/maps/api/staticmap?center=Downtown,Seattle,WA&zoom=13&size=500x500&maptype=roadmap&markers=color:red%7Clabel:V%7C$lat,$lng&markers=color:green%7Clabel:D%7C${AppConstants.DOWNTOWN_SEATTLE_LAT},${AppConstants.DOWNTOWN_SEATTLE_LNG}&key=${AppConstants.GOOGLE_MAPS_API_KEY}"
    }
}
