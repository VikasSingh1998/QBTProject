1. AdapterService.java
This is the main service class that manages the Bluetooth adapter lifecycle, profiles, bonding, and communication with native code.
Learning this first gives you the big picture of how everything is wired together.
-------------------------------------------------------------------------------------------------------------------------------------------
Overview of AdapterService.java
=================================
What is this file about?
  
Think of AdapterService.java as the Bluetooth adapter’s “manager” or “boss” inside Android.
==> It’s responsible for turning Bluetooth on and off, keeping track of Bluetooth’s current state (like ON, OFF, TURNING ON, etc.).
==> It communicates between Android’s Java layer and the lower-level Bluetooth native stack (which is written in C/C++).
==> It handles Bluetooth profiles (like A2DP for audio, HID for keyboards, etc.) by managing their life cycle.
==> It manages device bonding (pairing), keeping track of connected devices and their states.
==> It listens for events from the native Bluetooth stack and acts on those events (like a device connecting or disconnecting).
==> It broadcasts state changes and events to the rest of the Android system and apps, so they can react properly.
==> It also manages permissions, settings, and other policy rules around Bluetooth usage.

What main operations does it handle?
=====================================
Service lifecycle management: ==> Starts and stops the Bluetooth service cleanly.
Adapter state management: ==> Knows if Bluetooth is ON, OFF, or changing states.
Profile management: ==> Starts/stops Bluetooth profiles, manages profile connections.
Device bonding: ==> Manages pairing and unpairing devices securely.
Communication with native code: ==> Calls native methods to actually operate the Bluetooth hardware.
Event handling: ==> Receives and processes events from native Bluetooth (like connection status changes).
Broadcasting intents: ==> Sends system-wide broadcasts about Bluetooth events and state changes.

Why is this important?
Without this service, Android wouldn’t be able to manage Bluetooth devices, turn Bluetooth on/off, or even notify apps about Bluetooth events.
-------------------------------------------------------------------------------------------------------------------------------------------------
AdapterService.java — Line-by-Line Explanation
-----------------------------------------------
📦 1. Package Declaration
 package com.android.bluetooth.btservice;   ===> Declares the package this file belongs to.

btservice stands for "Bluetooth service" — this is the backend logic handling Bluetooth system-level operations.
-----------------------------------------------------------------------------------------------------------------
import android.app.Application;    ===> 
import android.app.Service;
import android.bluetooth.BluetoothAdapter;  ==> Main class to control the local Bluetooth adapter (e.g. enabling/disabling).
import android.bluetooth.BluetoothDevice;  ==> Represents a remote Bluetooth device (used for pairing, bonding, etc.).
import android.bluetooth.BluetoothProfile;  ==> Represents a Bluetooth profile (like A2DP, HFP) and its connection states.
import android.bluetooth.IBluetooth;
import android.bluetooth.IBluetoothCallback;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.IBluetoothManagerCallback;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;   ==> 
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import com.android.bluetooth.a2dp.A2dpService;
import com.android.bluetooth.hid.HidService;
import com.android.bluetooth.hfp.HeadsetService;
import com.android.bluetooth.hdp.HealthService;
import com.android.bluetooth.pan.PanService;
import com.android.bluetooth.R;
import com.android.bluetooth.Utils;
import com.android.bluetooth.btservice.RemoteDevices.DeviceProperties;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.List;
import android.content.pm.PackageManager;
import android.os.ServiceManager;

================================================================================================
onCreate() 
----------









