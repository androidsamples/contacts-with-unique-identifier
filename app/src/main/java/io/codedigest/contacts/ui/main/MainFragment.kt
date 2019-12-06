package io.codedigest.contacts.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import io.codedigest.contacts.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var contactLookUp = ""

    private val textWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            contactLookUp = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        btn_contacts_list.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_contactsFragment)
        }
        lookupItem.addTextChangedListener(textWatcher)
        btn_search_contact.setOnClickListener {
            if (contactLookUp.isNotEmpty()) {
                val action = MainFragmentDirections.actionMainFragmentToContactResultFragment(contactLookUp)
                findNavController().navigate(action)
            }
        }
    }


}
