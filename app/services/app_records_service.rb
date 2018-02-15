class AppRecordsService


  def save_with_duplicate_check(app_record_params, upload_record_params,
                                permission_params, drawable_params)

    @app_record = get_if_exists app_record_params

    AppRecord.transaction do

      if @app_record.nil?
        @app_record = AppRecord.create! app_record_params

        @app_record.drawables.import drawable_params, validate: false
        @app_record.filtered_drawables.import filter_lib_drawables(drawable_params), validate: false
        @app_record.permissions.import permission_params, validate: false
      end

      if @app_record.upload_records.exists?(android_id: upload_record_params[:android_id])
        return nil
      end
      @app_record.upload_records.create!(upload_record_params)
    end

    @app_record
  end

  # Retrieve app record if exists. Hash based check.
  def get_if_exists(app_record_params)
    AppRecord.find_by_app_hash app_record_params[:app_hash]
  end

  # Filter library drawables from set of all drawables
  # If possible, update known library hashes
  def filter_lib_drawables(drawables)
    # get library drawable names
    library_file_names = LibraryDrawableNamesProviderService.excluded_file_names

    # filter app drawables and get only those included in common libraries
    app_library_drawables_by_name = drawables.select {|drawable| library_file_names.include? drawable[:file_name]}

    # hashes of drawables from common library included in current app
    app_library_drawables_hashes = app_library_drawables_by_name.map {|drawable| drawable[:file_hash]}

    # import hashes of known by name library files
    existing_drawable_hashes = LibraryDrawableHash.where(file_hash: app_library_drawables_hashes).pluck(:file_hash)
    (app_library_drawables_hashes - existing_drawable_hashes).each do |drawable_hash|
      LibraryDrawableHash.create(file_hash:drawable_hash)
    end

    # app specific files left after name based filtering
    app_specific_drawables_by_name = drawables - app_library_drawables_by_name

    # find hashes of app specific files known by name, which arepresent among hashes of common library files
    app_library_drawables_hashes = LibraryDrawableHash.where(file_hash: app_specific_drawables_by_name.map {|drawable| drawable[:file_hash]})
                                         .pluck(:file_hash)

    # return app specific files as an array of app specific files by name minus library files identified by hash
    app_specific_drawables_by_hash = app_specific_drawables_by_name.select{|drawable| not drawable.in? app_library_drawables_hashes }

    app_specific_drawables_by_hash
  end

end