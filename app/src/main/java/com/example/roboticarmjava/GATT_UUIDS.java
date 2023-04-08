package com.example.roboticarmjava;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class GATT_UUIDS {
    private static List<List<UUID>> UUIDS = new ArrayList<>();
    UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    //Battery service UUID
    public static final UUID UUID_BATT_SERV                 = UUID.fromString("00001818-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_BATT_VALUE                = UUID.fromString("00002B00-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_DIRECT_CONTROL            = UUID.fromString("00002B20-0000-1000-8000-00805f9b34fb");
    //Temp service UUID
    public static final UUID UUID_TEMP_SERV                 = UUID.fromString("00001817-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_TEMP_VALUE                = UUID.fromString("00002B02-0000-1000-8000-00805f9b34fb");
    //Data download service UUID
    public static final UUID UUID_FLASH_SERV                = UUID.fromString("00001815-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_DOWNLOAD_VALUE            = UUID.fromString("00002B06-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_DEVICE_HOLDER_VALUE       = UUID.fromString("00002BA0-0000-1000-8000-00805f9b34fb");
    //Force calibration service UUID
    public static final UUID UUID_FORCE_CALIBRATION_SERV    = UUID.fromString("0000181A-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_FORCE_0N_CALIBRATION      = UUID.fromString("00002B07-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_FORCE_1N_CALIBRATION      = UUID.fromString("00002B08-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_FORCE_2N_CALIBRATION      = UUID.fromString("00002B09-0000-1000-8000-00805f9b34fb");
    //Long term service UUID
    public static final UUID UUID_LNG_TRM_SERV              = UUID.fromString("00001814-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_LNG_TRM_MODE              = UUID.fromString("00002B03-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_LNG_SAMPLING_RATE         = UUID.fromString("00002B04-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_LNG_TRM_DATE              = UUID.fromString("00002B05-0000-1000-8000-00805f9b34fb");
    //Force sensor service UUID
    public static final UUID UUID_FORCE_SENSOR_SERV         = UUID.fromString("00001816-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_FORCE_SENSOR_VALUE        = UUID.fromString("00002B01-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_READ_ANGLES               = UUID.fromString("00002BA1-0000-1000-8000-00805f9b34fb");
    //unit identifier UUID
    public static final UUID UUID_IDENTIFIER_SERV           = UUID.fromString("0000191E-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_IDENTIFIER_VAL            = UUID.fromString("00002B0D-0000-1000-8000-00805f9b34fb");
    //device info UUID
    public static final UUID UUID_DEVICEINFO_SERV           = UUID.fromString("0000181F-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_INFO_READ            = UUID.fromString("00002B0F-0000-1000-8000-00805f9b34fb");

}

