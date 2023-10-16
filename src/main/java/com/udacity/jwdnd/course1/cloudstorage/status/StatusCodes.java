package com.udacity.jwdnd.course1.cloudstorage.status;

import java.util.HashMap;
import java.util.Map;

public enum StatusCodes {
    UNKNOWN_ERROR(-1, "Unknown Error"),
    SUCCESS(0, "Operation successful"),
    FILE_CREATION_ERROR(1, "Error during file upload"),
    DUPLICATE_FILE(2, "This file name already exists."),
    UNAUTHORIZED(3, "File inaccessible"),
    NOTE_CREATION_ERROR(4, "Error during creating note."),
    NOTE_UPDATE_ERROR(5, "Error during note update."),
    NO_FILE_FOUND(6, "No file was selected"),
    FILE_DELETION_ERROR(7, "File deletion Error"),
    FILE_TOO_LARGE(8, "File size too large");

    private static final Map<Integer, StatusCodes> statusCodeByStatusNumber = new HashMap<>();

    static {
        for (StatusCodes statusCode : StatusCodes.values()) {
            statusCodeByStatusNumber.put(statusCode.getStatusCode(), statusCode);
        }
    }

    private final int statusCode;
    private final String message;

    StatusCodes(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static StatusCodes getStatusMessageFromNumber(Integer statusNumber) {
        return statusCodeByStatusNumber.get(statusNumber);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return message;
    }
}
