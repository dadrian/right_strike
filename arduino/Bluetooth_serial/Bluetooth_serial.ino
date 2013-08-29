#include <SoftwareSerial.h>

int bluetoothTx = 2;
int bluetoothRx = 3;
 
int fsrPinA = 0;     // the FSR and 10K pulldown are connected to a0
int fsrReadingA;     // the analog reading from the FSR resistor divider
int fsrPinZ = 1;     // the FSR and 10K pulldown are connected to a0
int fsrReadingZ;     // the analog reading from the FSR resistor divider
int difference;

SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);


void readPressure() {

  fsrReadingA = analogRead(fsrPinA);  
  fsrReadingZ = analogRead(fsrPinZ);  

  if(fsrReadingA > 800 || fsrReadingZ > 800) {
    difference = fsrReadingA - fsrReadingZ;
    if(difference > 0) {
      //bluetooth.println(" A Fore strike ");
      bluetooth.print('A');
      //Serial.println(" A Fore Strike ");
    } else if (difference <= 0) {
      bluetooth.print('Z');
      //bluetooth.println(" Z Heel strike ");
      //Serial.println(" Z Heel Strike ");
    }
    while(fsrReadingA > 500 || fsrReadingZ > 500) {
      delay(10); //Just for comment sake
      fsrReadingA = analogRead(fsrPinA);  
      fsrReadingZ = analogRead(fsrPinZ);  
    }
  }
}

void setup()
{
  //Setup usb serial connection to computer
  //Serial.begin(9600);

  //Setup Bluetooth serial connection to android
    bluetooth.begin(9600);
  bluetooth.print("$$$");
  delay(100);
  bluetooth.begin(115200);
  bluetooth.print("$$$");
  delay(100);
  bluetooth.println("U,9600,N");
  bluetooth.begin(9600);
}

void loop()
{

  if(bluetooth.available())
  {
    char toSend = (char)bluetooth.read();
    //Serial.print(toSend);
  }

  /*
  if(Serial.available())
  {
    char toSend = (char)Serial.read();
    bluetooth.print(toSend);
  }
  */
  readPressure();
}
