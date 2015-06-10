(ns cmr.metadata-db.data.providers
  "Defines a protocol for CRUD operations on providers.")

(def small-provider-id
  "Provider id of the small provider"
  "SMALL_PROV")

(defprotocol ProvidersStore
  "Functions for saving and retrieving providers"

  (save-provider
    [db provider]
    "Saves a provider and returns the provider id. If the provider already
    exists then an exception is thrown.")

  (get-providers
    [db]
    "Get a sequence of all the providers.")

  (get-provider
    [db provider-id]
    "Get the provider with given id.")

  (update-provider
    [db provider]
    "Updates an existing provider in the database based on the
    provider map's provider-id value.")

  (delete-provider
    [db provider-id]
    "Remove a provider from the database completely, including all of its concepts.")

  (reset-providers
    [db]
    "Delete all providers from the database including their concept tables.  USE WITH CAUTION."))

(defn provider-not-found-error
  "Error message indicating that the given provider-id does not exist."
  [provider-id]
  {:error :not-found :error-message (format "Provider [%s] does not exist." provider-id)})

(defn small-field-cannot-be-modified
  "Error message indicating that the small field of a provider cannot be modified."
  [provider-id]
  {:error :bad-request :error-message (format "Provider [%s] small field cannot be modified." provider-id)})