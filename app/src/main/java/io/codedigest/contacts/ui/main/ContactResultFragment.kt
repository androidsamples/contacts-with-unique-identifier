package io.codedigest.contacts.ui.main


import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import io.codedigest.contacts.R
import io.codedigest.contacts.ui.main.model.Contact
import kotlinx.android.synthetic.main.fragment_contact_result.*

class ContactResultFragment : Fragment() {

    private val contentResolver by lazy { requireContext().contentResolver }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let { bundle ->
            val args = ContactResultFragmentArgs.fromBundle(bundle)
            val contactLookupKey = args.contactLookupKey
            val contact = searchContactByLookupKey(contactLookupKey)
            contact?.let {
                name.text = it.name
                phone.text = it.phone
            }
        }
    }

    private fun searchContactByLookupKey(lookupKey: String): Contact? {
        if (lookupKey.isNotEmpty()) {
            val lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
            val uri = ContactsContract.Contacts.lookupContact(contentResolver, lookupUri)
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    val contact = Contact(id, lookupKey, name)
                    val hasPhoneNumber = (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
                    if (hasPhoneNumber) {
                        val whereContactIdIs = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
                        val phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, whereContactIdIs, arrayOf(id), null)
                        if (phoneCursor != null) {
                            while (phoneCursor.moveToNext()) {
                                val phone = phoneCursor.getString(phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER))
                                contact.phone = phone
                            }
                            phoneCursor.close()
                        }
                    }
                    return contact
                }
                cursor.close()
            }
        }
        return null
    }

}
