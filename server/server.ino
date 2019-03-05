#include <DHTesp.h>



#include <EEPROM.h>
#include <vector>
#include <Hash.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
using namespace std;

 struct person 
{
  char username[10];
  char pass_hash[20];
  char salt[12];
  char perm;
};

#define DHTTYPE DHT11
#define DHTPIN 16

#define user_start  78
#define pass_start 88
#define salt_start 108
#define user_step  sizeof(person)
#define passAP_start  68
#define passAP_stop  77 
#define permission_start 120 
#define wifi_step 32
#define wifipass_start 0
#define wifipass_stop 31
#define usercount 10
#define  B ((float)3435)

DHTesp dht;
unsigned int EchoPin = 2;           // connect Pin 2(Arduino digital io) to Echo at US-015
unsigned int TrigPin = 3;           // connect Pin 3(Arduino digital io) to Trig at US-015
unsigned long Time_Echo_us = 0;
unsigned long Len_mm  = 0;
//user-info
char ssid[32];
char password[32];
const char passAp[]="12345678";
const char ssidAp[]="espd";


vector<String> commands;
person list[10];
byte listofflags[10]; 
String str;
bool flagChangedWifi= true;
String ip="";

//all methods
vector<String> splitString(String line, char c);
void writePassAp(String pass);
void writeWifi(String passWifi,String ssidWifi);
char* salt_random();
void readPerson(uint index, person* p);
void writePerson(int start, person* p);
char isHavingPermission(char* usernamePerson,String passwordPerson);
int indexOfUser(char* usernamePerson,String passwordPerson);
void printlist();
void printt(char* s,int n);
void GetExternalIP(String* s);
String readWifi();
double getTemp();
void EEPROMNull();

//create wifi server listen on a given port
WiFiServer server(3030);
//create Wifi client
WiFiClient client;

int pin= A0;
//dht DHT;
uint32_t delayMS;


void setup() {
   
  ESP.eraseConfig();
  WiFi.mode(WIFI_AP_STA);
  Serial.begin(115200);
  WiFi.softAP(ssidAp,passAp);
  EEPROM.begin(512);
  pinMode(pin, INPUT); 
  pinMode(EchoPin, INPUT);                    //Set EchoPin as input, to receive measure result from US-015
  pinMode(TrigPin, OUTPUT);        

     
 
  int count=user_start;
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

  
    String ssidpass;
 ssidpass=readWifi();
  Serial.println(ssidpass);
 commands=splitString(ssidpass,' ');
 // //readWifi
 

 commands[1].toCharArray(ssid,32);
 commands[0].toCharArray(password,32);
 
Serial.println(ssid);
Serial.println(password);


  //connect to WiFi
  int br=0;
  Serial.println("here");
  WiFi.begin(ssid,password);

  while (WiFi.status() != WL_CONNECTED && br<15)
  {
    delay(500);
    Serial.print(".");
    br++;
  }
  Serial.print("IP Address: "); Serial.println(WiFi.localIP());
  server.begin();
   
}

void loop()
{ 
  client=server.available();
  if(ip.length()==0 || flagChangedWifi) {GetExternalIP(ip);flagChangedWifi=false;  Serial.println("here "+ip);}

  if(client)
  {
    Serial.println("connected to client");
    str=client.readStringUntil('\r\n');
    commands=splitString(str,' ');

    if(commands[0]=="ip") client.println(ip);
    else if(commands[0]=="signup")
    {

      person p;
      commands[1].toCharArray(p.username,sizeof(commands[1]));
      char salt[13];
      for(int i=0;i<12;i++)salt[i]=char(random(33,126));
      salt[12]=0;
      commands[2]+=salt;
      strncpy(p.salt,salt,12);
      sha1(commands[2]).toCharArray(p.pass_hash,20);
      bool haveUser=false;
      int br=0;
      //isHavingAUser
      for(int i=0;i<10;i++)
      {
        if(listofflags[i]==1)
        {
          br++;
          //return 0 if the two arrays are equal
          if(!strncmp(p.username,list[i].username,10)){haveUser=true;break;}
        }
      }
      if (br==0) p.perm='a';
      else p.perm='n';
      if(br==10) client.println("falsesignup.There is not free space.");
      else if(!haveUser)
      {
        for(int i=0;i<10;i++)
        {
          if(listofflags[i]==0)
          {
            strncpy(list[i].username,p.username,10);
            strncpy(list[i].pass_hash,p.pass_hash,20);
            strncpy(list[i].salt,salt,12);
            list[i].perm=p.perm;
            writePerson(user_start+i*user_step, &p);
            listofflags[i]=1;
            break;
          }
        }
      printlist();
      EEPROM.commit();
      client.println("truesignup");
      }
      else client.println("thereIsAPerson");   
    }
    else if(commands[0]=="signin")
    {
      char perm1;
      person p;
      char passh[20];
      commands[1].toCharArray(p.username,10);
      char* pass;
      char salt[12];
      commands[2].toCharArray(pass,commands[2].length());
      perm1=isHavingPermission(p.username,commands[2]);
      if(perm1!='i' && perm1!='n') client.println(perm1);
      else if(perm1=='i') client.println("IncorrectPass");
      else client.println("NoPermission");
    }
    else if(commands[0]=="setWifi")
    {
      //setWifi TN tanqnaid tanq a
      //setWifi JohnAndWillow IamSuperProgrammer tanq a
        int flag;
        char nameuser[10];
        char* passworduser;
        char perm1;
        
        commands[3].toCharArray(nameuser,10);
        perm1=isHavingPermission(nameuser,commands[4]);
        Serial.println(perm1);
        commands[1].toCharArray(ssid,32);
        commands[2].toCharArray(password,32);
        Serial.println(ssid);
        Serial.println(password);
        if(perm1=='a') 
        {
          Serial.println();          
          writeWifi(commands[1],commands[2]);
        }
        int br=0;
        client.stop();
        while(client.available());
        WiFi.begin(ssid,password);
       while (WiFi.status() != WL_CONNECTED && br<15)
        {
         delay(500);
         Serial.print(".");
         br++;
        }
         Serial.print("IP Address: "); Serial.println(WiFi.localIP());
         
        
      
    }
    else if(commands[0]=="setPermission")
    {
      char nameuser[10];
      String str;
      bool flag=0;
      person p;
      char passh[20];
      
      commands[1].toCharArray(nameuser,10);   
      commands[3].toCharArray(p.username,10);   
      
     
      String pass=commands[4];
      char salt[12];
      strncpy(salt,list[0].salt,12);
      //printt(salt,12);
      salt[12]='\0';
      pass+=salt;
      sha1(pass).toCharArray(passh,20); 
      //setPermission name perm nameadmin passadmin
      
      char username1[10];
      commands[1].toCharArray(username1,10);
      String permission=commands[2];
      char perm1=permission[0];
      if(!strncmp(passh,list[0].pass_hash,20)&&!strncmp(list[0].username,p.username,10)){flag=1;}
        else flag=0;
      for(int i=0;i<10;i++)
      { 
        if(!strncmp(list[i].username,username1,10))
        {         
          if(flag==1)
          {
            list[i].perm=perm1;
            EEPROM.write(permission_start+i*user_step,perm1);
            EEPROM.commit();
            Serial.println(EEPROM.read(permission_start+i*user_step));
            client.println(perm1);
            break;
          }
        }
      }
    }
    else if(commands[0]=="setPassWord")
    {
      char nameuser[10];
      String str;
      bool flag=0;
      person p;
      char passh[20];
      int index;
        
      commands[1].toCharArray(p.username,10);   
      index=indexOfUser(p.username, commands[2]);
      Serial.println("im here: "+index);
  
      if(index!=-1){
        
        char salt[13];
        for(int i=0;i<12;i++){
        salt[i]=char(random(33,126));
        }
        salt[12]=0;
        commands[3]+=salt;
        strncpy(p.salt,salt,12);
        sha1(commands[3]).toCharArray(p.pass_hash,20);
        
        strncpy(list[index].pass_hash,p.pass_hash,20);
        strncpy(list[index].salt,salt,12);
        for(int i=0;i<20;i++)
        {
          
          EEPROM.write(pass_start+index*user_step+i,p.pass_hash[i]);
          EEPROM.commit();
          
        }
        for(int i=0;i<12;i++){EEPROM.write(salt_start+index*user_step+i,p.salt[i]);EEPROM.commit();}
        client.println("ready!");
      
     }
     else client.println("Incorect Pass");
    }
    
    else if(commands[0]=="list")
    {
      char nameuser[10];
      String str;
      char perm1;
      bool flag=0;
      person p;
      char passh[20];
      
      commands[1].toCharArray(p.username,10);
      commands[1].toCharArray(nameuser,10);      
      
      Serial.println(nameuser);
      
      if(isHavingPermission(p.username,commands[2])=='a')flag=1;
      else flag=0; 
     //if is admin make the list 
     if(flag==1)
     {
      for(int i=0;i<10;i++)
      {
        //if the flag is 1 in the list with flags and the user is not an admin then we print username and permission
       if(listofflags[i]==1 && list[i].perm!='a')
       {
          str=str+list[i].username;
         str+=' ';
         str+=list[i].perm;
         Serial.println(str);
         client.println(str);
         str="";
       }
      }
     }
     else client.println("you are not admin");
      client.println("stop");
    }
    else if(commands[0]=="del")
    {
      char username1[10];
       bool flag=0;
      String str;
      char perm1;
      person p;
      char passh[20];

      commands[1].toCharArray(username1,10);    
      commands[2].toCharArray(p.username,10);
      
      Serial.println(commands[3]);
      String pass=commands[3];
      char salt[12];
      strncpy(salt,list[0].salt,12);
      //printt(salt,12);
       salt[12]='\0';
       pass+=salt;
       sha1(pass).toCharArray(passh,20); 
       
       //check if the user is admin
       
       if(!strncmp(passh,list[0].pass_hash,20)&&!strncmp(list[0].username,p.username,10)){ flag=1;}
       else flag=0;
             Serial.print(flag);
      for(int i=1;i<10;i++)
     {
         Serial.println(username1);
          Serial.println(list[i].username);
       if(!strncmp(list[i].username,username1,10)&& flag==1) 
       {
        int start_fordel=user_start+i*user_step;
        listofflags[i]=0;
        for(int j=0;j<user_step;j++)EEPROM.write(start_fordel+j,0);
        EEPROM.commit(); 
        int count=user_start;
        for(int i=0;i<10;i++)
        {
          readPerson(count, &list[i]);
          count+=user_step;
        }
        printlist();
        break;
      }
     }
    } 
    else if (commands[0]=="getTemp")
    {
      
      client.println(String(getTemp()));
    }
    else if(commands[0]=="Humidity")
    {
      if(commands[1]=="true")client.println(getHumidityOrTemp(true));
      else if(commands[1]=="false")client.println(getHumidityOrTemp(false));
    }
    Serial.println("here");
  }
/*

  
    digitalWrite(TrigPin, HIGH);              //begin to send a high pulse, then US-015 begin to measure the distance
    delayMicroseconds(50);                    //set this high pulse width as 50us (>10us)
    digitalWrite(TrigPin, LOW);               //end this high pulse
    
    Time_Echo_us = pulseIn(EchoPin, HIGH);               //calculate the pulse width at EchoPin, 
    if((Time_Echo_us < 60000) && (Time_Echo_us > 1))     //a valid pulse width should be between (1, 60000).
    {
      Len_mm = (Time_Echo_us*34/100)/2;      //calculate the distance by pulse width, Len_mm = (Time_Echo_us * 0.34mm/us) / 2 (mm)
      Serial.print("Present Distance is: ");  //output result to Serial monitor
      Serial.print(Len_mm, DEC);            //output result to Serial monitor
      Serial.println("mm");                 //output result to Serial monitor
    }
    delay(100);   
    */                         
}


double getHumidityOrTemp(boolean isHumidity)
{
  dht.setup(DHTPIN, DHTesp::DHT11);
  
  if(isHumidity)
  {
    return dht.getHumidity();
  }
  else return dht.getTemperature();
}

double getTemp()
{
  double tmp;
  double r,temperature,ut, Ri=4530.0, E=1.00;
  
  tmp = analogRead (A0);
  ut=double(tmp/1024);
  //Serial.println(tmp);
  r = (double)(ut*Ri)/(E-ut);
  //Serial.print("r= ");
 // Serial.println(r);
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
    Serial.println("minah");
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
  //incorrectpass
  else return 'i';
       
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
    Serial.print(char(s[i]));
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
int indexOfUser(char* usernamePerson,String passwordPerson)
{
  Serial.println(usernamePerson);
  Serial.println(passwordPerson);
  char passh[20];
  char salt[12];
  int index;
  char perm1;
  bool flag;
  for(int i=0;i<usercount;i++)
   {   
    //Serial.println(list[i].username);
    if(!strncmp(list[i].username,usernamePerson,10))
      {
        Serial.print(usernamePerson);
        Serial.println(".");
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
           
            index=i;
            if(strncmp(passh,list[i].pass_hash,20)) {index=-1;}
           Serial.println(flag);
            break;
          }
        else index=-1;
    }
  return index;
}
void EEPROMNull()
{
  for(int i=0;i<512;i++)EEPROM.write(i,0);
  EEPROM.commit();
}
//setPassWord test a b
// setPermission test u tanq a
