package org.example.orderservice.domain.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Interface for a file storage service.
 * Provides methods to save and read files from a storage location.
 */
public interface StorageService {

    /**
     * Saves a file to the specified location.
     *
     * @param fileName  the name of the file to be saved.
     * @param data      the data of the file to be saved as a ByteArrayOutputStream.
     * @param fileType  specifies the type of file e.g. ".png" or value of the HTTP header content-type e.g. "application/pdf"/
     * @return a String representing the path to the saved resource. For example, if the file is saved to a bucket,
     * the returned string will be the path inside the bucket.
     * @throws IOException if an I/O error occurs while saving the file.
     */
    String save(String fileName, String fileType, ByteArrayOutputStream data);

}
