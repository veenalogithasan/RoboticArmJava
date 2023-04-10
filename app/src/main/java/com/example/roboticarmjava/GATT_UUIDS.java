package com.example.roboticarmjava;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class GATT_UUIDS {
    private static List<List<UUID>> UUIDS = new ArrayList<>();
    UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");



    public static final UUID UUID_IDENTIFIER_SERV           = UUID.fromString("D973F2E0-B19E-11E2-9E96-0800200C9A66");
    public static final UUID UUID_tx_char = UUID.fromString("D973F2E1-B19E-11E2-9E96-0800200C9A66");
    public static final UUID UUID_rx_char            = UUID.fromString("D973F2E2-B19E-11E2-9E96-0800200C9A66");

}

