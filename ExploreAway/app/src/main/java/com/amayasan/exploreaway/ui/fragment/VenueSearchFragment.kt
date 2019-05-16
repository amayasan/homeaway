package com.amayasan.exploreaway.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amayasan.exploreaway.AppConstants
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.database.VenueDatabase
import com.amayasan.exploreaway.database.VenueRepository
import com.amayasan.exploreaway.model.Model
import com.amayasan.exploreaway.service.FoursquareApiService
import com.amayasan.exploreaway.ui.activity.DetailActivity
import com.amayasan.exploreaway.ui.activity.MapsActivity
import com.amayasan.exploreaway.ui.adapter.RecyclerBaseAdapter
import com.jakewharton.rxbinding.widget.RxTextView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.venue_card_view_holder.view.*
import kotlinx.android.synthetic.main.venue_search_fragment.*
import java.util.concurrent.TimeUnit

class VenueSearchFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = VenueSearchFragment()
    }

    private val iFoursquareApiService by lazy {
        FoursquareApiService.create()
    }

    private var mDisposable: Disposable? = null

    private lateinit var mVenueSearchViewModel: VenueSearchViewModel
    private lateinit var mRecyclerViewAdapter: VenueCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.venue_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Initialize the ViewModel and observe changes to the venues
        mVenueSearchViewModel = ViewModelProviders.of(this).get(VenueSearchViewModel::class.java)
        mVenueSearchViewModel.venues.observe(this, Observer {
            // Venues changed, update the data in the adapter
            mRecyclerViewAdapter.setData(it ?: emptyList())
            // Hide or show views appropriately
            if (it.isNotEmpty()) {
                venue_search_recycler_view.visibility = View.VISIBLE
                venue_search_empty_view.visibility = View.GONE
                venue_search_fab.show()
            } else {
                venue_search_recycler_view.visibility = View.GONE
                venue_search_empty_view.visibility = View.VISIBLE
                venue_search_fab.hide()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVenueSearchViews()
    }

    override fun onResume() {
        super.onResume()

        mRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
    }

    private fun initVenueSearchViews() {

        // Debounce the text in the Search field and perform a typeahead venue search against the Foursquare API accordingly
        RxTextView.textChanges(venue_search_actv).filter { charSequence -> charSequence.isNotEmpty() }
            .debounce(500, TimeUnit.MILLISECONDS).map { charSequence -> charSequence.toString() }
            .subscribe { text ->
                doVenueSearch(text)
            }

        venue_search_actv.requestFocus()
        venue_search_empty_view.visibility = View.GONE

        // Set up the RecyclerView with and Adapter
        venue_search_recycler_view.layoutManager = LinearLayoutManager(context)

        mRecyclerViewAdapter = VenueCardAdapter(context)
        venue_search_recycler_view.adapter = mRecyclerViewAdapter

        // Set up the click listener for the FAB
        venue_search_fab.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putParcelableArrayListExtra(
                AppConstants.VENUES_LIST_KEY,
                mVenueSearchViewModel.venues.value as java.util.ArrayList<out Parcelable>
            )
            startActivity(intent)
        }
    }

    private fun doVenueSearch(query: String) {
        mDisposable =
            iFoursquareApiService.doVenueSearchV2(query = query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> showResult(result) },
                    { error -> showError(error.message) }
                )
    }

    fun showResult(result: Model.Result) {
        // Update the venues in ViewModel
        mVenueSearchViewModel.venues.postValue(result.response.venues)
    }

    fun showError(message: String?) {
        // TODO: Show an error when API call fails
    }

    class VenueCardAdapter(context: Context?) : RecyclerBaseAdapter() {
        private var mData: List<Model.Venue> = emptyList()
        private val repository: VenueRepository

        init {
            val venueDao = VenueDatabase.getDatabase(context!!).venueDao()
            repository = VenueRepository(venueDao)
        }

        override fun getLayoutIdForPosition(position: Int) = R.layout.venue_card_view_holder

        override fun getViewModel(position: Int) = mData[position]

        override fun getItemCount() = mData.size

        fun setData(newData: List<Model.Venue>) {
            mData = newData
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)

            // Handle clicking on the card, open Venue detail activity
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra(AppConstants.VENUE_OBJ_KEY, mData[position])
                startActivity(holder.itemView.context, intent, Bundle())
            }

            // Check to see if venue has been favorited and toggle button state accordingly
            repository.getByIdSingle(mData[position].id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { venue : Model.FavVenue ->
                        holder.itemView.venue_favorite_toggle_btn.isChecked = true
                    },
                    onError = { error ->
                        holder.itemView.venue_favorite_toggle_btn.isChecked = false
                    }
                )

            // Handle favorite/unfavorite clicks
            holder.itemView.venue_favorite_toggle_btn.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Single.fromCallable { repository.insert(Model.FavVenue(mData[position].id))}
                        .subscribeOn(Schedulers.io())
                        .subscribeBy(onError = { error -> })
                } else {
                    Single.fromCallable { repository.delete(Model.FavVenue(mData[position].id))}
                        .subscribeOn(Schedulers.io())
                        .subscribeBy(onError = { error -> })
                }
            }
        }
    }
}