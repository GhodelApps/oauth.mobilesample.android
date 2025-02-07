package com.authsamples.basicmobileapp.views.companies

import com.authsamples.basicmobileapp.api.client.ApiClient
import com.authsamples.basicmobileapp.api.client.ApiRequestOptions
import com.authsamples.basicmobileapp.api.entities.Company
import com.authsamples.basicmobileapp.plumbing.errors.UIError
import com.authsamples.basicmobileapp.views.utilities.ApiViewEvents
import com.authsamples.basicmobileapp.views.utilities.Constants.VIEW_MAIN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * A simple view model class for the companies view
 */
class CompaniesViewModel(
    private val apiClient: ApiClient,
    private val apiViewEvents: ApiViewEvents
) {

    // Data once retrieved
    var companies: List<Company> = ArrayList()

    /*
     * A method to do the work of calling the API
     */
    fun callApi(
        options: ApiRequestOptions,
        onSuccess: () -> Unit,
        onError: (UIError) -> Unit
    ) {

        // Indicate a loading state
        this.apiViewEvents.onViewLoading(VIEW_MAIN)

        // Make the remote call on a background thread
        val that = this@CompaniesViewModel
        CoroutineScope(Dispatchers.IO).launch {

            try {
                // Make the API call
                that.companies = apiClient.getCompanyList(options).toList()

                // Return results on the main thread
                withContext(Dispatchers.Main) {
                    that.apiViewEvents.onViewLoaded(VIEW_MAIN)
                    onSuccess()
                }
            } catch (uiError: UIError) {

                // Return results on the main thread
                withContext(Dispatchers.Main) {
                    that.companies = ArrayList()
                    that.apiViewEvents.onViewLoadFailed(VIEW_MAIN, uiError)
                    onError(uiError)
                }
            }
        }
    }
}
