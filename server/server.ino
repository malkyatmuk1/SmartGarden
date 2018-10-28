#include <EEPROM.h>
#include <vector>
#include<Hash.h>
#include <ESP8266WiFi.h>

using namespace std;

 struct person 
{
  char username[10];
  char pass_hash[20];
  char salt[12];
  char perm;
};

#define user_start  78
#define user_step  sizeof(person)
#define passAP_start  68
#define passAP_stop  77 
#define permission_start 120 
#define wifi_step 32
#define wifipass_start 0
#define wifipass_stop 31
#define usercount 10

char ssid[64];
char password[64];
const char passAp[]="12345678";
const char ssidAp[]="espd";
const char passWiFi[]="IamSuperProgrammer";
const char ssidWiFi[]="JohnAndWillow";
vector<String> commands;
person list[10];
byte listofflags[10]; 

vector<String> splitString(String line, char c);
void writePassAp(String pass);
void writeWifi(String passWifi,String ssidWifi);
char* salt_random();
void readPerson(uint index, person* p);
void writePerson(int start, person* p);
char isHavingPermission(char* usernamePerson,String passwordPerson);
void printlist();
void printt(char* s,int n);
void GetExternalIP(String* s);
String readWifi();
double getTemp(int pin);

//create wifi server listen on a given port
WiFiServer server(3030);
//create Wifi client
WiFiClient client;

int pin= A0;


void setup() {
  
  ESP.eraseConfig();
  WiFi.mode(WIFI_AP_STA);
  Serial.begin(115200);
  WiFi.softAP(ssidAp,passAp);
  EEPROM.begin(512);
  pinMode(pin, INPUT); 
  
  for(int i=0;i<10;i++)
  {
    readPerson(count,&list[i]);
    if(strlen(list[i].username)==0)listofflags[i]=0;
    else listofflags[i]=1;
    count+=user_step;
  }
  for(int i=0;i<10;i++)
  {
    Serial.println(listofflags[i]);
  }
  printlist();
  
  int br=0;
  //connect to WiFi
  while(WiFi.begin(ssid, password)!=WL_CONNECTED && br<5)
  {
    Serial.print('.');
    br++;
  }
  if(br==5) Serial.println("connection failed");
  Serial.print("IP Address: "); Serial.println(WiFi.localIP());
  server.begin();
  
}

void loop() {
  client = server.available();
  if(client)
  {
      str=client.readStringUntil('\r\n');
      commands=splitString(str,' ');
      if(commands[0]=="getTemperature")
      {
        client.println(getTemp(pin));        
      } 
  }
}
vector<String> splitString(String line, char c)
{
    vector<String> str;
    
    String curr_string = "";
    
    for(int i = 0; i < line.length(); i++)
    {
      if(line[i] == c)
      {

        str.push_back(curr_string);
        curr_string = "";
      }
     
      else curr_string+=line[i];
      Serial.println(line[i]);
    }

    str.push_back(curr_string);
    return str;
}
double getTemp(int pin)
{
  int tmp;
  tmp = analogRead (A0);
  r = ((1023.0*R0)/(float)tmp)-R0;
  temperature = B/log(r/0.09919) - 273.15;
  return temperature;
}
void writePerson(int start, person* p)
{
  for ( int i = 0; i < sizeof(person); i++) 
  {
    EEPROM.write(start+i, *((byte*)p+i));
    Serial.print(char(EEPROM.read(start + i)));
  }
  EEPROM.commit();
}
void readPerson(int start, person* p)
{
   for ( int i = 0; i < sizeof(person); i++) *((byte*)p+i) = EEPROM.read(start + i);
}
void writeWifi(String passWifi,String ssidWifi)
{  
  for(int i=0;i<64;i++)  EEPROM.write(i,0);
  Serial.println("tuk sam");
  for(int i=0;i<passWifi.length();i++)
  {    
    EEPROM.write(i,passWifi[i]);
    
    Serial.print(char(EEPROM.read(i))); 
  }
  for(int i=0;i<ssidWifi.length();i++)
  {    
    EEPROM.write(i+32,ssidWifi[i]);
    Serial.print(char(EEPROM.read(i+32))); 
  }
    EEPROM.commit();
}

char* salt_random()
{
  
  int random_number;
  static char salt[12];
  for(int i=0;i<12;i++){
  random_number=random(33,126);
  salt[i]=char(random_number);
  }
  return salt;
}
String readWifi()
{
  char ssid[32],password[32];
  for(int i=wifipass_start;i<=wifipass_stop;i++)
  {
    password[i]=EEPROM.read(i);
    ssid[i]=EEPROM.read(i+wifi_step);
  }
  //Serial.println(strcat(strcat(ssid,"~"),password));
  return strcat(strcat(ssid," "),password);
}
void writePassAp(String pass,int start)
{
  int j=0;
   for(int i=start;i<pass.length()+start;i++)
   {
    
     //EEPROM.write(i,pass[j]);
     j++;
   }
}
vector<String> splitString(String line, char c)
{
    vector<String> str;
    
    String curr_string = "";
    
    for(int i = 0; i < line.length(); i++)
    {
      if(line[i] == c)
      {

        str.push_back(curr_string);
        curr_string = "";
      }
     
      else curr_string+=line[i];
      Serial.println(line[i]);
    }

    str.push_back(curr_string);
    return str;
}
char isHavingPermission(char* usernamePerson,String passwordPerson)
{
  Serial.println(usernamePerson);
  Serial.println(passwordPerson);
  char passh[20];
  char salt[12];
  char perm1;
  bool flag;
  for(int i=0;i<usercount;i++)
   {   
    //Serial.println(list[i].username);
    if(!strncmp(list[i].username,usernamePerson,10))
      {
        Serial.print(usernamePerson);
        Serial.println(".");
            flag=1;
            //Serial.println(flag);
            
            strncpy(salt,list[i].salt,12);
            perm1=list[i].salt[12];
            salt[12]='\0';     
            passwordPerson+=salt;
            Serial.println(passwordPerson);
            //we hash the password+salt
            sha1(passwordPerson).toCharArray(passh,20);
            Serial.println(passh);
            Serial.println(list[i].pass_hash);
            //check if the hashed pass form the client and the hashed password form the list are the same-> if is false the flag  become 0 
            if(strncmp(passh,list[i].pass_hash,20)) flag=0;
           Serial.println(flag);
            break;
          }
          else flag=0;
    }
  if(flag==1)return perm1;
  else return 'x';
       
}
void printlist()
{
  for(int i=0;i<10;i++)
  {
    if(list[i].username[0]!='\0'){
      Serial.print("User:");
      Serial.println(i);
      printt(list[i].username,10);
      printt(list[i].pass_hash,20);
      printt(list[i].salt,12);
      printt(&list[i].perm,1);
    }
  }
}
void printt(char* s,int n)
{
  for(int i=0;i<n;i++)
  {
    if(s[i]==0)break;
    Serial.print(s[i]);
  }
  Serial.println();
}
void GetExternalIP(String &ip)
{
WiFiClient client;
  
  int p;
  p=client.connect("api.ipify.org", 80);
  //Serial.println(p);
  if (p)
  {
    Serial.println("connectedtuk");
    client.println("GET / HTTP/1.1");
    client.println("Host: api.ipify.org");
    client.println();
    
    while(client)
    {
      String line =client.readStringUntil('\n');
      
      if(line.length()==1)
      {
        line=client.readStringUntil('\n');
        ip=line;
        break;
      }
    }  
  }
  else 
  {
    //Serial.println("connection failedpopop");
  }
}
