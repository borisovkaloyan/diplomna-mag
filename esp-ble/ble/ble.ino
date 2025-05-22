#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEBeacon.h>

#define TABLE_ID 7

void setup() {
  Serial.begin(115200);
  delay(1000);

  BLEDevice::init("Table_" + String(TABLE_ID));
  BLEServer *pServer = BLEDevice::createServer();

  BLEBeacon beacon = BLEBeacon();
  beacon.setManufacturerId(0x4C00); // Apple
  beacon.setProximityUUID(BLEUUID("12345678-1234-1234-1234-123456789abc"));
  beacon.setMajor(TABLE_ID);
  beacon.setMinor(0);

  BLEAdvertisementData adData;
  adData.setFlags(0x04); // BR_EDR_NOT_SUPPORTED

  // ✅ Must be manufacturer data for iBeacon
  adData.setManufacturerData(beacon.getData());

  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->setAdvertisementData(adData);
  pAdvertising->start();

  Serial.println("Beacon started");
}

void loop() {
}
