package io.codedigest.contacts.ui.main.adapter

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.codedigest.contacts.R
import io.codedigest.contacts.ui.main.model.Contact
import kotlinx.android.synthetic.main.item_contact.view.*


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private var contacts: List<Contact> = listOf()

    fun submitList(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(inflater.inflate(R.layout.item_contact, parent, false))
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) = with(itemView) {
            name.text = contact.name
            contactLookupKey.text = contact.lookupKey
            contactLookupKey.setOnClickListener {
                copyToClipboard()
            }
        }

        private fun copyToClipboard() = with(itemView) {
            val clipboard: ClipboardManager? =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText("ContactLookupKey", contactLookupKey.text)
            clipboard?.setPrimaryClip(clip)
            Snackbar.make(this, "contact lookup copied to clipboard", Snackbar.LENGTH_LONG).show()
        }
    }

}
