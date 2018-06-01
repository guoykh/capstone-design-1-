/*
   Copyright (c) 2015 Intel Corporation.  All rights reserved.

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

#include <CurieBLE.h>

BLEPeripheral blePeripheral; // BLE Peripheral Device (the board you're programming)
BLEService tapService("d6e6a169-1a81-4ff4-a2b6-66534e32bebe"); // BLE tap Service

/// BLE Switch Characteristic
BLEUnsignedCharCharacteristic switchCharacteristic("11591b7f-bce5-4e28-ac31-1e54c5c077b1", BLERead | BLEWrite | BLENotify);

void setup() {
  Serial.begin(9600);
   pinMode(7,OUTPUT);
  pinMode(11,OUTPUT);
  pinMode(2,INPUT_PULLUP);
  pinMode(12,INPUT_PULLUP);
  
  // set advertised local name and service UUID:
  blePeripheral.setLocalName("TapTap2");
  blePeripheral.setAdvertisedServiceUuid(tapService.uuid());
  
  // add service and characteristic:
  blePeripheral.addAttribute(tapService);
  blePeripheral.addAttribute(switchCharacteristic);
  
  // set the initial value for the characeristic:
  switchCharacteristic.setValue(0);
  
  // begin advertising BLE service:
  blePeripheral.begin();
  Serial.println("BLE Peripheral");
}

boolean pressed = false;
boolean pressed2 = false;
boolean pressed3 = false;
const float reference=3.3;
const int bat=0;

void loop() {
  // listen for BLE peripherals to connect:
  BLECentral central = blePeripheral.central();

  // if a central is connected to peripheral:
  if (central) {
    Serial.print("Connected to central: ");
    // print the central's MAC address:
    Serial.println(central.address());

    // while the central is still connected to peripheral:
    while (central.connected()) {  
      int back = digitalRead(2);
      int sw = digitalRead(12);

      if (switchCharacteristic.written()) {
            if (switchCharacteristic.value() == 13) {
              digitalWrite(11,10);
              delay(500);
              digitalWrite(11,LOW); 
              //switchCharacteristic.setValue(0);
            }
            if (switchCharacteristic.value() == 14) {
              digitalWrite(7,10);
              delay(500);
              digitalWrite(7,LOW); 
              //switchCharacteristic.setValue(0);
            }
            if (switchCharacteristic.value() == 21){
              digitalWrite(7,10);
              digitalWrite(11,10);
              delay(500);
              digitalWrite(7,LOW); 
              digitalWrite(11,LOW); 
              //switchCharacteristic.setValue(0);
              }

              if(switchCharacteristic.value() == 101) {
                Serial.print("NONONONONONONONO");
                int val=analogRead(bat);
                float volts = (reference/1023.0)*val;
                if (volts >= 3.1){
                  Serial.print("dkdkdkdkdkd");
                  switchCharacteristic.setValue(190);
                  }
                  else if (volts < 3.1 && volts > 2.6)
                  switchCharacteristic.setValue(180);
                  else if (volts < 2.6 && volts >= 1.65 )
                  switchCharacteristic.setValue(150);
                  else if (volts < 1.65 && volts >=0.66) 
                  switchCharacteristic.setValue(120);
                  else if (volts < 0.66)
                  switchCharacteristic.setValue(110);
                  Serial.print("vol=");
                Serial.println(volts);
              Serial.print("\n");
              delay(1000);
                }
      }

      
     if(sw==LOW && back==LOW){
        if (pressed==true && pressed2==true){
          switchCharacteristic.setValue(18);
          Serial.print("down");
           //switchCharacteristic.setValue(0);
        }
        pressed=false;
        pressed2=false;
      }


      
      
      if (sw==HIGH){
        if(pressed==false){
          //switchCharacteristic.setValue(3);
          Serial.print("up");
          pressed=true;
          //switchCharacteristic.setValue(0);
        }      
      }
      else if(sw==LOW){
        if (pressed==true){
          switchCharacteristic.setValue(3);
          Serial.print("down");
           //switchCharacteristic.setValue(0);
        }
        pressed=false;
      }

      if (back==HIGH){
        if(pressed2==false){
          //switchCharacteristic.setValue(1);
          Serial.print("up");
          pressed2=true;
          //switchCharacteristic.setValue(0);
        }      
      }
      else if(back==LOW){
        if (pressed2==true){
          switchCharacteristic.setValue(4);
          Serial.print("down");
           //switchCharacteristic.setValue(0);
        }
        pressed2=false;
      }
    }
    
    // when the central disconnects, print it out:
    Serial.print(F("Disconnected from central: "));
    Serial.println(central.address());
  }
}


