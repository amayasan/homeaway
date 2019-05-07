package com.amayasan.exploreaway.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.AppConstants
import com.amayasan.exploreaway.model.Model
import com.amayasan.exploreaway.service.FoursquareApiService
import com.amayasan.exploreaway.ui.activity.DetailActivity
import com.amayasan.exploreaway.ui.adapter.RecyclerBaseAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.venue_search_fragment.*


class VenueSearchFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = VenueSearchFragment()
    }

    private val iFoursquareApiService by lazy {
        FoursquareApiService.create()
    }

    private var mDisposable: Disposable? = null

    private lateinit var mVenueSearchViewModel: VenueSearchViewModel
    private lateinit var mRecyclerViewAdapter : VenueCardAdapter

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
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVenueSearchViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
    }

    private fun initVenueSearchViews() {
        // TODO: Populate array with Foursquare categories
        val categories = arrayOf(
            "coffee", "pizza", "whole foods"
        )

        // Initialize a new array adapter object
        val adapter = ArrayAdapter<String>(
            context, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            categories // Array
        )

        // Set the AutoCompleteTextView adapter
        venue_search_auto_complete_text_view.setAdapter(adapter)

        // Request Focus for the search field
        venue_search_auto_complete_text_view.requestFocus()

        // Set an item click listener for auto complete text view
        venue_search_auto_complete_text_view.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    // Display the clicked item using toast
                    Toast.makeText(context, "Selected : $selectedItem", Toast.LENGTH_SHORT).show()
                }

        // Set a dismiss listener for auto complete text view
        venue_search_auto_complete_text_view.setOnDismissListener {
            Toast.makeText(context, "Suggestion closed.", Toast.LENGTH_SHORT).show()
        }

        // Set a click listener for root layout
        venue_search_root_layout.setOnClickListener {
            val text = venue_search_auto_complete_text_view.text
            Toast.makeText(context, "Inputted : $text", Toast.LENGTH_SHORT).show()
        }

        // Set a focus change listener for auto complete text view
        venue_search_auto_complete_text_view.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                venue_search_auto_complete_text_view.showDropDown()
            }
        }

        // Set the OnClickListener for the Search button
        venue_search_button.setOnClickListener { doVenueSearch(venue_search_auto_complete_text_view.text.toString()) }

        // Set up the RecyclerView with and Adapter
        venue_search_recycler_view.layoutManager = LinearLayoutManager(context)

        mRecyclerViewAdapter = VenueCardAdapter()
        venue_search_recycler_view.adapter = mRecyclerViewAdapter

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

    fun showResult(result : Model.Result) {
        // Update the venues in ViewModel
        mVenueSearchViewModel.venues.postValue(result.response.venues)
    }

    fun showError(message : String?) {
        // TODO: Show an error when API call fails
    }

    class VenueCardAdapter : RecyclerBaseAdapter() {
        private var mData: List<Model.Venue> = emptyList()

        override fun getLayoutIdForPosition(position: Int) = R.layout.venue_card_view_holder

        override fun getViewModel(position: Int) = mData[position]

        override fun getItemCount() = mData.size

        fun setData(newData: List<Model.Venue>) {
            mData = newData
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra(AppConstants.VENUE_OBJ_KEY, mData[position])
                startActivity(holder.itemView.context, intent, Bundle())
            }
        }
    }
}