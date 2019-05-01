package com.amayasan.exploreaway.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.amayasan.exploreaway.R
import kotlinx.android.synthetic.main.venue_search_fragment.*


class VenueSearchFragment : Fragment() {

    companion object {
        fun newInstance() = VenueSearchFragment()
    }

    private lateinit var viewModel: VenueSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.venue_search_fragment, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VenueSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchField()
    }

    private fun initSearchField() {
        // TODO: Initialize a new array with elements
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
        root_layout.setOnClickListener {
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
    }


}