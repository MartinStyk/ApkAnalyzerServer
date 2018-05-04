# Apk Analyzer Server
Backend part for Android app [Apk Analyzer](https://github.com/MartinStyk/AndrodApkAnalyzer).
Provided functionality:
* Store Android APK's metadata
* Perform repackaged APK detection

## Routes                       

#### App database
Following section lists all available routes for management of app metadata database

##### /app_records/{id}
Metadata report about application with given ID.

##### /app_records/{id}/upload_records
List of all uploads of given APK. If one APK file is uploaded multiple times, only one metadata entry is saved. This path shows IDs of all Android clients with upload time and analysis mode.

##### /app_records/{id}/permissions
List of all permissions of given APK.

##### /app_records/{id}/filtered_drawables
List of all application specific image files with their hashes.

##### /app_records?package_name={package_name}&version_name={version_name}
Metadata report about applications matching specified filter criteria.

##### /app_records/names
List of most common app names in our APK metadata database

##### /app_records/names_versions
List of most common app versions in our APK metadata database

##### /app_records [POST]
Endpoint accepting APK metadata uploaded from Android clients.

##### /usage
Basic database usage info (number of apps, number of upload records, etc...)


#### Repackaged Detection

##### /repackaged_detection?app_hash={hash}
Endpoint accepting hash of application for detection. This method runs repackaged detection algorithm from Android client side.

##### /repackaged_detection/{id}
This method runs repackaged detection algorithm for app with given ID. Useful for testing. 

##### /repackaged_detection/{id}?cache=false
This method runs repackaged detection algorithm for app with given ID. Cached results are ignored and recalculated.

##### /repackaged_results/{id}
Displays results of repackaged detection with given ID. Contains detection results, number of similar apps, percentage of majority signature etc..

##### /repackaged_results/statistics
Number of detections and aggregate results of detections.

##### Technical spec

| Prefix        | Verb           | URI Pattern  | Controller#Action |           
| ------------- |:-------------|:-----|:-----|           
|            names_app_records |GET| /app_records/names(.:format)                                |app_records#names|
|   names_versions_app_records |GET|  /app_records/names_versions(.:format)                        |app_records#names_versions|
|    app_record_upload_records |GET|  /app_records/:app_record_id/upload_records(.:format)         |upload_records#index|
|     app_record_upload_record |GET|  /app_records/:app_record_id/upload_records/:id(.:format)     |upload_records#show|
|       app_record_permissions |GET|  /app_records/:app_record_id/permissions(.:format)            |permissions#index|
|        app_record_permission |GET|  /app_records/:app_record_id/permissions/:id(.:format)        |permissions#show|
|app_record_filtered_drawables |GET|  /app_records/:app_record_id/filtered_drawables(.:format)     |filtered_drawables#index|
| app_record_filtered_drawable |GET|  /app_records/:app_record_id/filtered_drawables/:id(.:format) |filtered_drawables#show|
|                  app_records |GET|  /app_records(.:format)                                       |app_records#index|
|                              |POST| /app_records(.:format)                                       |app_records#create|
|                   app_record |GET|  /app_records/:id(.:format)                                   |app_records#show|
|   repackaged_detection_index |GET|  /repackaged_detection(.:format)                              |repackaged_detection#index|
|         repackaged_detection |GET|  /repackaged_detection/:id(.:format)                          |repackaged_detection#show|
|statistics_repackaged_results |GET|  /repackaged_results/statistics(.:format)                     |repackaged_results#statistics|
|           repackaged_results |GET|  /repackaged_results(.:format)                                |repackaged_results#index|
|            repackaged_result |GET|  /repackaged_results/:id(.:format)                            |repackaged_results#show|
|                  usage_index |GET|  /usage(.:format)                                             |usage#index|
|              available_index |GET|  /available(.:format)                                         |available#index|


## Technologies
* Ruby on Rails
* PostgreSQL database
* Deployed on [Heroku] (http://apk-analyzer.herokuapp.com/)
