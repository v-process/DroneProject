#include "main.h"

void Delay(vu32 nCount)
{
  for(; nCount != 0; nCount--);
}

void delay_us(int cnt)
{
  int i;
  for(; cnt!=0; cnt--)
  {
    for(i=0;i<9;i++) asm("NOP"); 
  }
}

void delay_ms(int cnt)
{
  for(; cnt!=0; cnt--)
  {
    delay_us(1000);
  }
}

void initailze_motorSet(int value)
{ 
    TIM3->CCR1 = value;
    TIM3->CCR2 = value;

    TIM5->CCR1 = value;
    TIM5->CCR2 = value;
    TIM5->CCR3 = value;
    TIM5->CCR4 = value;
}

void initailze_wifiSet() 
{
  NVIC_DisableIRQ(USART3_IRQn);
  
  printf("AT+RST\n\r"); // Reset for safety
  delay_ms(3000);
  printf("AT+CWMODE=3\n\r"); // Set server mode
  delay_ms(100);
  printf("AT+CIPMUX=1\n\r"); // It allows multi connection
  delay_ms(100);
  printf("AT+CWSAP=\"Banana\",\"0123456789\",11,0\n\r"); // Set SSID
  delay_ms(100);
  printf("AT+CIPSERVER=1,3002\n\r"); // Activation server, set port number
  delay_ms(100);
  printf("AT+CIFSR\n\r"); // Fetching IP, This is always '192.168.4.1'
  delay_ms(100);

  NVIC_EnableIRQ(USART3_IRQn);
}

int main()
{
  RCC_Configuration();
  NVIC_Configuration();
  GPIO_Configuration();
 
  USART1_Configuration(); 
  USART3_Configuration(); 
  TIM_Configuration();
  PWM_Configuration();
  
  initailze_wifiSet();
  
  initailze_motorSet(18000); // 30%
  
  Delay(0x1FFFFF);
  initailze_motorSet(0); 
  Delay(0x1FFFFF);

  for(int nCount = 18000; nCount < 30000; nCount++) // 20000 -> X, 30000~ -> O, MAX : ~55000
  {
    initailze_motorSet(nCount); 
    Delay(0x1FF);
  } 
  
  while(1); 
}