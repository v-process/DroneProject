#include "main.h"

__Drone_Control Drone_Control;
__IO uint8_t  Wifi_Connect  = 0;
__IO uint8_t  Wifi_fly_Set  = 0;
__IO uint8_t  Wifi_Land_Set = 0;
__IO uint8_t  Send_Throttle = 0;
__IO int16_t  Send_Count    = 0;
__IO uint8_t  Send_Exit     = 0;
__IO uint8_t  Wifi_Send     = 0;


const uint16_t START_THRROTLE = 34700;

__IO int16_t  roll_TARGET_ANGLE = 0, pitch_TARGET_ANGLE = 0, yaw_TARGET_ANGLE = 0;

__IO int16_t  height_err = 0,  height_err_past = 0,  height_err_sum = 0;
__IO double  height_p_ctrl_val = 0, height_i_ctrl_val = 0, height_d_ctrl_val = 0;

void PID_Control();

void initailze_wifiSet() 
{
  NVIC_DisableIRQ(USART2_IRQn);
  
  printf("AT+RST\n"); // Reset for safety
  delay_ms(3000);
  printf("AT+CWMODE=3\n"); // Set server mode
  delay_ms(150);
  printf("AT+CIPMUX=1\n"); // It allows multi connection
  delay_ms(150);
  printf("AT+CWSAP=\"Banana\",\"0123456789\",11,0\n"); // Set SSID
  delay_ms(150);
  printf("AT+CIPSERVER=1,3002\n"); // Activation server, set port number
  delay_ms(150);
  printf("AT+CIFSR\n"); // Fetching IP, This is always '192.168.4.1'
  delay_ms(150);

  NVIC_EnableIRQ(USART2_IRQn);
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

  Drone_Control.axis[HEIGHT].KP = 0.282;
  Drone_Control.axis[HEIGHT].KI = 0.752;
  Drone_Control.axis[HEIGHT].KD = 0.012;
 
  Drone_Control.axis[HEIGHT].PID_value = 0;
  
  Drone_Control.axis[ROLL].Current = 100;
  Drone_Control.axis[PITCH].Current = 100;
  Drone_Control.axis[YAW].Current = 100;
  Drone_Control.axis[PITCH].Target = 150;
  Drone_Control.axis[ROLL].Target = 150;
  Drone_Control.Throttle = 1000; 
  Drone_Control.axis[HEIGHT].Target = 6000;
  
  Drone_Control.Height1 = 0;
  Drone_Control.Height2 = 0;

}

int main()
{
  RCC_Configuration();
  NVIC_Configuration();
  GPIO_Configuration();
 
  USART_Configuration(USART3, 230400); 
  USART_Configuration(USART2, 9600);
  USART_Configuration(UART4, 9600);
  
  TIM_Configuration();
  initailze_wifiSet();
  Initailze_value();
  
  GPIO_SetBits(GPIOB, GPIO_Pin_0);
  delay_ms(100);
       
  while(!Wifi_fly_Set)
 {
    if(Wifi_Connect)
    {
      printf("AT+CIPSEND=0,21\n");
      delay_ms(150);
      printf("Angle,NN\n");     
      delay_ms(350);
    }
 }
  delay_ms(150);
  Send_Count = 0;
  while(1) {     
   
      
      if((Send_Count%5) == 0)        Tx_Data16t(USART3, 0x5555);
      else if((Send_Count%5) == 1)   Tx_Data16t(USART3, Drone_Control.axis[ROLL].Target);
      else if((Send_Count%5) == 2)   Tx_Data16t(USART3, Drone_Control.axis[PITCH].Target);
      else if((Send_Count%5) == 3)   Tx_Data16t(USART3, Drone_Control.Throttle);
      else if((Send_Count%5) == 4)   Tx_Data16t(USART3, 0xFFFF);

      if(Send_Exit)
      {
        GPIO_ResetBits(GPIOB, GPIO_Pin_1);
        if(Send_Exit > 200)  {   Drone_Control.Throttle = 100; /*Send_Throttle = 0; */  }
        else                 {   Send_Exit++;   }
      }
      
            
      if(Wifi_Land_Set) {
        Send_Throttle = 0;
        if(Drone_Control.axis[HEIGHT].Current < 3000){ Send_Exit = 1;  Wifi_Land_Set = 0;}
        else Drone_Control.Throttle -= 2; 
      }  
      
      if(Send_Count == 4){
     
        GPIO_SetBits(GPIOA, GPIO_Pin_8);
        delay_us(10);
        GPIO_ResetBits(GPIOA, GPIO_Pin_8);
        if(Send_Throttle)  PID_Control();
        Send_Count++;
      }
      
      
      else if(Send_Count == 9)
      {
        GPIO_SetBits(GPIOA, GPIO_Pin_5);
        delay_us(10);
        GPIO_ResetBits(GPIOA, GPIO_Pin_5);
 
        Send_Count = 0;
        if(Send_Throttle)  PID_Control();
        
      }
      else  Send_Count++;

      delay_ms(1);
  }
}

void PID_Control()
{
      height_err  = Drone_Control.axis[HEIGHT].Target  - (Drone_Control.axis[HEIGHT].Current);
      
      height_p_ctrl_val  = (double)(Drone_Control.axis[HEIGHT].KP * height_err);
      height_i_ctrl_val  += (double)(Drone_Control.axis[HEIGHT].KI * height_err * dt);
      height_d_ctrl_val  = (double)(Drone_Control.axis[HEIGHT].KD * ( height_err - height_err_past )/dt);
   
      Drone_Control.axis[HEIGHT].PID_value  = (int16_t)(height_p_ctrl_val + height_i_ctrl_val + height_d_ctrl_val);
     
      height_err_past = height_err;
      if(START_THRROTLE - Drone_Control.axis[HEIGHT].PID_value < 40000) {
        Drone_Control.Throttle = START_THRROTLE + Drone_Control.axis[HEIGHT].PID_value;
      }
   
}