package com.amayasan.exploreaway.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amayasan.exploreaway.R
import com.amayasan.exploreaway.model.Model
import com.amayasan.exploreaway.service.FoursquareApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.venue_search_fragment.*


class VenueSearchFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = VenueSearchFragment()
    }

    private val foursquareApiService by lazy {
        FoursquareApiService.create()
    }

    private var disposable: Disposable? = null

    private lateinit var viewModel: VenueSearchViewModel
    private lateinit var recyclerViewAdapter : VenueCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.venue_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Initialize the ViewModel and observe changes
        viewModel = ViewModelProviders.of(this).get(VenueSearchViewModel::class.java)
        viewModel.venues.observe(this, Observer {
            recyclerViewAdapter.replaceItems(it ?: emptyList())
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVenueSearchViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
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
        venue_search_button.setOnClickListener { beginVenueSearch(venue_search_auto_complete_text_view.text.toString()) }

        // Set up the RecyclerView with and Adapter
        venue_search_recycler_view.layoutManager = LinearLayoutManager(context)

        recyclerViewAdapter = VenueCardAdapter()
        venue_search_recycler_view.adapter = recyclerViewAdapter

    }

    private fun beginVenueSearch(query: String) {
        disposable =
                foursquareApiService.searchVenues(query = query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result -> showResult(result) },
                        { error -> showError(error.message) }
                    )
    }

    fun showResult(result : Model.Result) {
        viewModel.venues.postValue(result.response.venues)
        //recyclerViewAdapter.replaceItems(result.response.venues)
    }

    fun showError(message : String?) {
        message?.length
    }

    class VenueCardAdapter :
        RecyclerView.Adapter<VenueCardAdapter.VenueCardViewHolder>() {

        var items: List<Model.Venue> = emptyList()

        fun replaceItems(newItems: List<Model.Venue>) {
            items = newItems
            notifyDataSetChanged()
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class VenueCardViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): VenueCardAdapter.VenueCardViewHolder {
            // create a new view
            val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.venue_search_item_view_holder, parent, false) as CardView
            // set the view's size, margins, paddings and layout parameters
            return VenueCardViewHolder(cardView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: VenueCardViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.cardView.findViewById<TextView>(R.id.venue_name).text = items[position].name
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = items.size
    }
}