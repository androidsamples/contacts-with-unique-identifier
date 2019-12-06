package io.codedigest.contacts.ui.main


import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import io.codedigest.contacts.R
import io.codedigest.contacts.ui.main.adapter.ContactsAdapter
import io.codedigest.contacts.ui.main.model.Contact
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment() {

    private lateinit var contactsAdapter: ContactsAdapter
    private val contentResolver by lazy { requireContext().contentResolver }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        contactsAdapter.submitList(loadContacts())
    }

    private fun setupAdapter() {
        contactsAdapter = ContactsAdapter()
        contacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun loadContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentUri = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                val lookupKey =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                val hasPhoneNumber =
                    (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
                val contact = Contact(id, lookupKey, name)
                if (hasPhoneNumber) {
                    val whereContactIdIs =
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        whereContactIdIs,
                        arrayOf(id),
                        null
                    )
                    if (phoneCursor != null) {
                        while (phoneCursor.moveToNext()) {
                            val phone = phoneCursor.getString(
                                phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            contact.phone = phone
                        }
                        phoneCursor.close()
                    }
                }
                contacts.add(contact)
            }
            cursor.close()
        }
        return contacts
    }

}
