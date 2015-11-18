#include "main.h"

 __Drone_Control Drone_Control;
__IO uint8_t Wifi_Connect = 0;
__IO int16_t Send_Count = 0;
__IO uint8_t Send_Exit = 0;
__IO uint8_t Wifi_Send =0;

void initailze_wifiSet() 
{
  NVIC_DisableIRQ(UART4_IRQn);
  
  printf("AT+RST\n"); // Reset for safety
  delay_ms(3000);
  printf("AT+CWMODE=3\n"); // Set server mode
  delay_ms(130);
  printf("AT+CIPMUX=1\n"); // It allows multi connection
  delay_ms(130);
  printf("AT+CWSAP=\"Banana\",\"0123456789\",11,0\n"); // Set SSID
  delay_ms(130);
  printf("AT+CIPSERVER=1,3002\n"); // Activation server, set port number
  delay_ms(130);
  printf("AT+CIFSR\n"); // Fetching IP, This is always '192.168.4.1'
  delay_ms(130);

  NVIC_EnableIRQ(UART4_IRQn);
}

void Initailze_value()
{
  Drone_Control.axis[ROLL].KP = 0;
  Drone_Control.axis[ROLL].KI = 0;
  Drone_Control.axis[ROLL].KD = 0;
  
  Drone_Control.axis[PITCH].KP = 0;
  Drone_Control.axis[PITCH].KI = 0;
  Drone_Control.axis[PITCH].KD = 0;
  
  Drone_Control.axis[YAW].KP = 0;
  Drone_Control.axis[YAW].KI = 0;
  Drone_Control.axis[YAW].KD = 0;
  
  Drone_Control.axis[ROLL].angle = 100;
  Drone_Control.axis[PITCH].angle = 100;
  Drone_Control.axis[YAW].angle = 100;
  
  Drone_Control.Throttle = 1000; 
}
int main()
{
  RCC_Configuration();
  NVIC_Configuration();
  GPIO_Configuration();
 
  USART_Configuration(USART1, 115200);
  ///USART_Configuration(USART2, 9600);
  USART_Configuration(USART3, 115200); 
  USART_Configuration(UART4, 9600);
  TIM_Configuration();
  
  Initailze_value();
  initailze_wifiSet();
  
  GPIO_SetBits(GPIOB, GPIO_Pin_1);
  GPIO_SetBits(GPIOB, GPIO_Pin_2); 
  GPIO_SetBits(GPIOB, GPIO_Pin_0);
  delay_ms(5);
  Tx_String(USART1, "START\n");
  while(1) {    
     
      if(Send_Count == 0){
        if(Wifi_Connect){ 
          printf("AT+CIPSEND=0,50\n");
          delay_ms(10);
          printf(",Angle,%6.2f,%6.2f,%6.2f,%6d,NNNNNNNN\n",
                   Drone_Control.axis[ROLL].angle*0.01,
                   Drone_Control.axis[PITCH].angle*0.01,
                   Drone_Control.axis[YAW].angle*0.01,
                   Drone_Control.Height1);   
        }
      }
      if((Send_Count%6) == 0)        Tx_Data16t(USART3, 0x5555);
      else if((Send_Count%6) == 1)   Tx_Data16t(USART3, Drone_Control.axis[PITCH].KP);
      else if((Send_Count%6) == 2)   Tx_Data16t(USART3, Drone_Control.axis[PITCH].KI);
      else if((Send_Count%6) == 3)   Tx_Data16t(USART3, Drone_Control.axis[PITCH].KD);
      else if((Send_Count%6) == 4)   Tx_Data16t(USART3, Drone_Control.Throttle);
      else if((Send_Count%6) == 5)   Tx_Data16t(USART3, 0xFFFF);

       if(Send_Exit)
      {
        Send_Exit++;
        if(Send_Exit > 200)
        {
          Drone_Control.Throttle = 100;
          Send_Exit = 0;
        }
      }
      
      if(Send_Count == 5){
        GPIO_SetBits(GPIOA, GPIO_Pin_8);
        delay_us(10);
        GPIO_ResetBits(GPIOA, GPIO_Pin_8);
        Send_Count++;
      }
      else if(Send_Count == 11)
      {
        GPIO_SetBits(GPIOA, GPIO_Pin_5);
        delay_us(10);
        GPIO_ResetBits(GPIOA, GPIO_Pin_5);
        Send_Count = 0;
      }
      else  Send_Count++;

      
      delay_ms(2);
  }
}
